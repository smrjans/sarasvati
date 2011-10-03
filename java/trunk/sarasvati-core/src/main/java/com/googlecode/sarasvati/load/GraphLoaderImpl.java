/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2008-2009 Paul Lorenz
                        Vincent Kirsch
*/

package com.googlecode.sarasvati.load;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.load.definition.ExternalDefinition;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;
import com.googlecode.sarasvati.util.FileVisitor;
import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.xml.XmlLoader;
import com.googlecode.sarasvati.xml.XmlProcessDefinition;

/**
 * Given a {@link GraphFactory} to construct the {@link Graph} parts,
 * a {@link GraphRepository} and a {@link ProcessDefinitionTranslator} to translate the
 * "raw" definition (for example an XML file) into a ProcessDefinition it can understand,
 * the GraphLoader will load process definitions into that repository.
 *
 * This class is *not* thread safe
 *
 * @author Paul Lorenz
 */
public class GraphLoaderImpl<G extends Graph> extends AbstractGraphLoader<G>
{
  public GraphLoaderImpl (final GraphFactory factory,
                          final GraphRepository<G> repository,
                          final GraphValidator validator)
  {
    super( factory, repository, validator );
  }

  @Override
  public <T> void loadDefinition (final ProcessDefinitionTranslator<T> translator, final T source)
    throws SarasvatiLoadException
  {
    loadDefinition( translator.translate( source ) );
  }

  @Override
  public <T> void loadDefinition (final ProcessDefinitionTranslator<T> translator,
                                  final T source,
                                  final String customId)
    throws SarasvatiLoadException
  {
    loadDefinition( translator.translate( source ), customId );
  }

  @Override
  public void loadDefinition (final ProcessDefinition procDef)
    throws SarasvatiLoadException
  {
    loadDefinition( procDef, procDef.getMessageDigest() );
  }

  @Override
  public List<LoadResult> loadWithDependencies (final String name, final ProcessDefinitionResolver resolver)
    throws SarasvatiLoadException
  {
    ProcessDefinition procDef = resolver.resolve( name );
    List<LoadResult> results = loadDependenciesIfNeeded( procDef, resolver, new Stack<String>(), new HashMap<String, ProcessDefinition>() );
    LoadResult result = loadIfNeeded( procDef, results );
    if ( result != null )
    {
      results.add( result );
    }
    return results;
  }

  private List<LoadResult> loadDependenciesIfNeeded (final ProcessDefinition procDef,
                                                     final ProcessDefinitionResolver resolver,
                                                     final Stack<String> stack,
                                                     final Map<String, ProcessDefinition> loaded)
  {
    List<LoadResult> results = new LinkedList<LoadResult>();
    stack.push( procDef.getName() );

    for ( ExternalDefinition external : procDef.getExternals() )
    {
      String extName = external.getProcessDefinition();
      if ( stack.contains( extName ) )
      {
        throw new SarasvatiLoadException( "Process definition '" + procDef.getName() +
                                          "' contains an illegal recursive reference to '" +
                                          extName + "'" );
      }

      ProcessDefinition externalDef = loaded.get( extName );
      if ( externalDef == null )
      {
        externalDef = resolver.resolve( extName );
        loaded.put( extName, externalDef );
      }

      List<LoadResult> childResults = loadDependenciesIfNeeded( externalDef, resolver, stack, loaded );
      results.addAll( childResults );
      LoadResult result = loadIfNeeded( externalDef, childResults );
      if ( result != null )
      {
        results.add( result );
      }
    }

    stack.pop();
    return results;
  }

  private LoadResult loadIfNeeded (final ProcessDefinition procDef,
                                   final List<LoadResult> childResults )
  {
    Graph latest = repository.getLatestGraph( procDef.getName() );
    String digest = procDef.getMessageDigest();

    if ( latest == null )
    {
      loadDefinition( procDef );
      return LoadResult.newGraph( procDef.getName() );
    }

    if ( !digest.equals( latest.getCustomId() ) )
    {
      loadDefinition( procDef );
      return LoadResult.updatedGraph( procDef.getName() );
    }
    else if ( !childResults.isEmpty() )
    {
      loadDefinition( procDef );
      return LoadResult.updatedGraph( procDef.getName(), childResults.get( 0 ).getName() );
    }
    return null;
  }

  @Override
  public boolean isLoaded (final String name)
  {
    return null != repository.getLatestGraph( name );
  }

  @Override
  public List<LoadResult> loadNewAndChanged (final File basePath)
    throws SarasvatiLoadException
  {
    return loadNewAndChanged( basePath, null );
  }

  @Override
  public List<LoadResult> loadNewAndChanged (final File basePath,
                                             final FilenameFilter filenameFilter)
    throws SarasvatiLoadException
  {
    final XmlLoader xmlLoader = new XmlLoader();
    final Map<String, ProcessDefinition> processDefs = new HashMap<String, ProcessDefinition>();

    final FilenameFilter filter =
      filenameFilter != null ?
          filenameFilter :
          new FilenameFilter()
          {
            @Override
            public boolean accept (final File dir, final String name)
            {
              return name.endsWith( ".wf.xml" );
            }
          };

    FileVisitor visitor = new FileVisitor()
    {
      @Override
      public boolean accept (final File dir, final String name)
      {
        return filter.accept( dir, name );
      }

      @Override
      public void accept (final File file)
      {
        XmlProcessDefinition pd = xmlLoader.translate( file );
        processDefs.put( pd.getName(), pd );
      }
    };

    Set<String> updated = new HashSet<String>();

    SvUtil.visitRecursive( basePath, visitor, true );

    List<LoadResult> loadResults = new LinkedList<LoadResult>();

    // Find all process definitions that are new or have changed
    for ( ProcessDefinition processDefinition : processDefs.values() )
    {
      String name   = processDefinition.getName();
      String digest = processDefinition.getMessageDigest();

      Graph latest = repository.getLatestGraph( name );
      if ( latest == null )
      {
        updated.add( name );
        loadResults.add( LoadResult.newGraph( name ) );
      }
      else if ( !digest.equals( latest.getCustomId() ) )
      {
        updated.add( name );
        loadResults.add( LoadResult.updatedGraph( name ) );
      }
    }

    // Collection is sorted so that externals are before all places that they are used.
    Collection<ProcessDefinition> sorted = SvUtil.getSorted( processDefs );

    // Find all process definition which depend on those that have been updated
    for ( ProcessDefinition processDefinition : sorted )
    {
      String name = processDefinition.getName();
      // If we already know it needs to updated, skip checking this one
      if ( updated.contains( name ) )
      {
        continue;
      }

      for ( ExternalDefinition ext : processDefinition.getExternals() )
      {
        if ( updated.contains( ext.getProcessDefinition() ) )
        {
          updated.add( name );
          loadResults.add( LoadResult.updatedGraph( name, ext.getProcessDefinition() ) );
          break;
        }
      }
    }

    // Load all updated/new process definitions in order
    for ( ProcessDefinition processDefinition : sorted )
    {
      if ( updated.contains( processDefinition.getName() ) )
      {
        loadDefinition( processDefinition, processDefinition.getMessageDigest() );
      }
    }

    return loadResults;
  }

  /**
   * Loads the given XML process definition file.
   *
   * @param file The xml file to load
   */
  @Override
  public void load (final File file)
  {
    loadDefinition( new XmlLoader(), file );
  }
}