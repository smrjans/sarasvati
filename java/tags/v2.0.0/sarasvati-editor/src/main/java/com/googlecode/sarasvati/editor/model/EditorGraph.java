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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.load.definition.NodeDefinition;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;
import com.googlecode.sarasvati.rubric.RubricCompiler;
import com.googlecode.sarasvati.rubric.lang.RubricStmt;
import com.googlecode.sarasvati.rubric.visitor.ExitArcNameCollector;
import com.googlecode.sarasvati.rubric.visitor.ResultTypeValidator;
import com.googlecode.sarasvati.util.SvUtil;

public class EditorGraph extends AbstractStateful<GraphState>
{
  protected File                 file;
  protected String               name;

  protected List<EditorNode>     nodes     = new ArrayList<EditorNode>();
  protected List<EditorExternal> externals = new ArrayList<EditorExternal>();
  protected List<EditorArc>      arcs      = new ArrayList<EditorArc>();

  protected Map<EditorGraphMember<?>, List<EditorArc>> outArcs = new HashMap<EditorGraphMember<?>, List<EditorArc>> ();

  public EditorGraph (final GraphState initialState)
  {
    super( initialState );
  }

  public String getName ()
  {
    return name;
  }

  public void setName (final String name)
  {
    this.name = name;
  }

  public File getFile ()
  {
    return file;
  }

  public void setFile (final File file)
  {
    this.file = file;
  }

  public List<EditorNode> getNodes()
  {
    return nodes;
  }

  public void setNodes (final List<EditorNode> nodes)
  {
    this.nodes = nodes;
  }

  public void addNode (final EditorNode node)
  {
    nodes.add( node );
    outArcs.put( node, new LinkedList<EditorArc>() );
  }

  public void removeNode (final EditorNode node)
  {
    nodes.remove( node );
    outArcs.remove( node );
  }

  public List<EditorExternal> getExternals()
  {
    return externals;
  }

  public void setExternals (final List<EditorExternal> externals )
  {
    this.externals = externals;
  }

  public void addExternal (final EditorExternal external)
  {
    externals.add( external );
    outArcs.put( external, new LinkedList<EditorArc>() );
  }

  public void removeExternal (final EditorExternal external)
  {
    externals.remove( external );
    outArcs.remove( external );
  }

  public List<EditorArc> getArcs()
  {
    return arcs;
  }

  public void setArcs (final List<EditorArc> arcs )
  {
    this.arcs = arcs;
  }

  public List<EditorArc> getOutArcs (final EditorGraphMember<?> member)
  {
    return outArcs.get( member );
  }

  public void addArc (final EditorArc arc)
  {
    arcs.add( arc );
    getOutArcs( arc.getStart() ).add( arc );
  }

  public void removeArc (final EditorArc arc)
  {
    arcs.remove( arc );
    getOutArcs( arc.getStart() ).remove( arc );
  }

  public Set<String> getUniqueNames (final Collection<? extends EditorGraphMember<?>> members)
  {
    Set<String> names = new HashSet<String> ();
    for ( EditorGraphMember<?> member : members )
    {
      names.add( member.getName() );
    }
    return names;
  }

  public Set<String> getCurrentNodeNames ()
  {
    return getUniqueNames( nodes );
  }

  public Set<String> getCurrentExternalNames ()
  {
    return getUniqueNames( externals );
  }

  public ValidationResults validateGraph ()
  {
    ValidationResults results = new ValidationResults();

    Set<String> ids = new HashSet<String> ();

    if ( nodes.isEmpty() )
    {
      results.error( "A graph must contain at least one node" );
    }

    boolean hasStartNode = false;

    for ( EditorNode node : nodes )
    {
      if ( node.getState().isStart() )
      {
        hasStartNode = true;
      }

      String nodeName = node.getState().getName();
      if ( ids.contains( nodeName ) )
      {
        results.error( "Node name '" + nodeName + "' is used more than once. " +
                       "Each node must have a unique name." );
      }
      ids.add( nodeName );

      String guard = node.getState().getGuard();
      if ( !SvUtil.isBlankOrNull( guard ) )
      {
        try
        {
          RubricStmt guardStmt = RubricCompiler.compile( guard );

          if ( !ResultTypeValidator.isResultOfType( guardStmt, GuardResult.class ) )
          {
            results.error( "The guard in node " + nodeName + " may return something other than guard response." );
          }

          ExitArcNameCollector exitArcCollector = new ExitArcNameCollector();
          guardStmt.traverse( exitArcCollector );

          Set<String> actualOutArcNames = new HashSet<String>();

          for ( EditorArc arc : getOutArcs( node ) )
          {
            actualOutArcNames.add( arc.getState().getLabel() );
          }

          for ( String guardExitArc : exitArcCollector.getExitArcNames() )
          {
            if ( !actualOutArcNames.contains( guardExitArc ) && guardExitArc != null )
            {
              results.warning( "The guard in node " + nodeName + " uses the arc name '" +
                               guardExitArc + "' but no arc with that name exits node" );
            }
          }
        }
        catch ( RuntimeException re )
        {
          results.warning( "The guard in node " + nodeName + " failed to compile with following error: " + re.getMessage() );
        }
      }
    }

    if ( !hasStartNode )
    {
      results.warning( "The process definition does not include a start node" );
    }

    String defaultIn = getState().getDefaultNodeForIncomingArcs();
    if ( !SvUtil.isBlankOrNull( defaultIn ) )
    {
      if ( !ids.contains( defaultIn ) )
      {
        results.error( "The node selected as the default target for incoming external arcs '" +
                       defaultIn + "' does not exist anymore. Please fix." );
      }
    }

    String defaultOut = getState().getDefaultNodeForOutgoingArcs();
    if ( !SvUtil.isBlankOrNull( defaultOut ) )
    {
      if ( !ids.contains( defaultOut ) )
      {
        results.error( "The node selected as the default source for outgoing arcs '" +
                       defaultOut + "' does not exist anymore. Please fix." );
      }
    }

    ids = new HashSet<String> ();

    for ( EditorExternal external : externals )
    {
      String externalName = external.getState().getName();
      if ( ids.contains( externalName ) )
      {
        results.error( "External name '" + externalName + "' is used more than once. " +
                       "Each external must have a unique name." );
      }
      ids.add( externalName );
    }

    if ( externals.isEmpty() )
    {
      return results;
    }

    for ( EditorArc arc : arcs )
    {
      if ( arc.isExternalInArc() )
      {
        if ( SvUtil.isBlankOrNull( arc.getState().getExternalStart() ) )
        {
          results.error( "The arc from external " +
                         "'" + arc.getStart().getName() + "' to '" + arc.getEnd().getName() +
                         "' does not have a start node selected." );
        }
      }

      if ( arc.isExternalOutArc() )
      {
        if ( SvUtil.isBlankOrNull( arc.getState().getExternalEnd() ) )
        {
          results.error( "The arc from " +
                         "'" + arc.getStart().getName() + "' to external '" + arc.getEnd().getName() +
                         "' does not have an end node selected." );
        }
      }
    }


    Library lib = Library.getInstance();
    if ( lib.getEntries().isEmpty() )
    {
      results.info( "Process Definition Library is empty. Externals can not be validated." );
      return results;
    }

    for ( EditorExternal external : externals )
    {
      String externalName = external.getState().getName();
      String pdName = external.getState().getGraphName();
      if ( lib.getEntry( pdName ) == null )
      {
        results.warning( "External with name '" + externalName + "' " +
                         "references process definition '" + pdName + "' " +
                         "which does not exist in the library." );
      }
    }


    for ( EditorArc arc : arcs )
    {
      if ( arc.isExternalInArc() )
      {
        String nodeName = arc.getState().getExternalStart();
        if ( !SvUtil.isBlankOrNull( nodeName ) )
        {
          String pdName = arc.getStart().asExternal().getState().getGraphName();
          LibraryEntry entry = lib.getEntry( pdName );
          if ( entry != null )
          {
            ProcessDefinition pd = entry.getProcessDefinition();
            boolean found = false;
            for ( NodeDefinition nodeDef : pd.getNodes() )
            {
              if ( nodeName.equals( nodeDef.getName() ) )
              {
                found = true;
                break;
              }
            }

            if ( !found  )
            {
              results.warning( "Arc from external " + "'" + arc.getStart().getName() +
                               "' to '" + arc.getEnd().getName() +
                               "' has node '" + nodeName + "' selected at start, which does not appear" +
                               " in the library version of graph '" + pdName + "'" );
            }
          }
        }
      }
      if ( arc.isExternalOutArc() )
      {
        String nodeName = arc.getState().getExternalEnd();
        if ( !SvUtil.isBlankOrNull( nodeName ) )
        {
          String pdName = arc.getEnd().asExternal().getState().getGraphName();
          LibraryEntry entry = lib.getEntry( pdName );
          if ( entry != null )
          {
            ProcessDefinition pd = entry.getProcessDefinition();
            boolean found = false;
            for ( NodeDefinition nodeDef : pd.getNodes() )
            {
              if ( nodeName.equals( nodeDef.getName() ) )
              {
                found = true;
                break;
              }
            }

            if ( !found  )
            {
              results.warning( "Arc from " + "'" + arc.getStart().getName() +
                               "' to external '" + arc.getEnd().getName() +
                               "' has node '" + nodeName + "' selected at end, which does not appear" +
                               " in the library version of graph '" + pdName + "'" );
            }
          }
        }
      }

    }

    return results;
  }
}