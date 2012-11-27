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
 */
package com.googlecode.sarasvati.editor.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;

import com.googlecode.sarasvati.editor.GraphEditor;
import com.googlecode.sarasvati.editor.xml.EditorXmlLoader;
import com.googlecode.sarasvati.editor.xml.XmlEditorProperties;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;
import com.googlecode.sarasvati.util.FileVisitor;
import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.xml.XmlLoader;

public class Library
{
  private static final Library INSTANCE = new Library ();

  public static Library getInstance ()
  {
    return INSTANCE;
  }

  private Map<String, LibraryEntry> entries;
  private File basePath;

  public Library ()
  {
    emptyLibrary();
  }

  public void loadFromPath (final String path,
                            final boolean recurse)
  {
    entries = new TreeMap<String, LibraryEntry>();
    basePath = new File( path );

    FileVisitor visitor = new FileVisitor()
    {

      @Override
      public boolean accept (final File dir, final String name)
      {
        return name.endsWith( ".wf.xml" );
      }

      @SuppressWarnings("synthetic-access")
      @Override
      public void accept (final File file)
      {
        String name = file.getName();
        name = name.substring( 0, name.length() - 7 );
        entries.put( name, new LibraryEntry( name, file ) );
      }
    };

    SvUtil.visitRecursive( basePath, visitor, recurse );
  }

  public void emptyLibrary ()
  {
    entries = new TreeMap<String, LibraryEntry>();
    basePath = new File( ".") ;
  }

  public Collection<LibraryEntry> getEntries ()
  {
    return entries.values();
  }

  public Collection<String> getNames ()
  {
    return entries.keySet();
  }

  public LibraryEntry getEntry (final String name)
  {
    return entries.get( name );
  }

  /**
   * @return the basePath
   */
  public File getBasePath ()
  {
    return basePath;
  }

  public ProcessDefinition getProcessDefinition (final String name)
  {
    LibraryEntry entry = getEntry( name );
    return entry == null ? null :entry.getProcessDefinition();
  }

  public void updateIfNotExists(final ProcessDefinition processDefinition,
                                final XmlEditorProperties editorProperties,
                                final File path)
  {
    if (!entries.containsKey(processDefinition.getName()))
    {
      update(processDefinition, editorProperties, path);
    }
  }

  public void update (final ProcessDefinition processDefinition,
                      final XmlEditorProperties editorProperties,
                      final File path)
  {
    entries.put( processDefinition.getName(), new LibraryEntry( processDefinition, editorProperties, path ) );
  }

  public void saveAllEntriesInLibraryInNewestFormat () throws IOException, JAXBException
  {
    GraphEditor editor = GraphEditor.getInstance();
    XmlLoader xmlLoader = editor.getXmlLoader();
    EditorXmlLoader editorXmlLoader = editor.getEditorXmlLoader();

    for ( LibraryEntry entry : getEntries() )
    {
      File saveFile = entry.getPath();
      File editorPropsFile = new File( saveFile.getParentFile(), entry.getName() + ".editor.xml" );
      XmlEditorProperties xmlEditorProps = null;
      if ( editorPropsFile.exists() && editorPropsFile.canRead() )
      {
        xmlEditorProps = editorXmlLoader.loadEditorProperties( editorPropsFile );
      }
      EditorGraph graph = EditorGraphFactory.loadFromXml( entry.getProcessDefinition(), xmlEditorProps );

      EditorGraphFactory.XmlSaveData saveData = EditorGraphFactory.exportToXml( graph );
      xmlLoader.saveProcessDefinition( saveData.getXmlProcDef(), saveFile );
      editorXmlLoader.saveEditorProperties( saveData.getXmlEditorProps(), new File( saveFile.getParentFile(), entry.getName() + ".editor.xml" ) );
      update( saveData.getXmlProcDef(), saveData.getXmlEditorProps(), saveFile );
    }
  }
}