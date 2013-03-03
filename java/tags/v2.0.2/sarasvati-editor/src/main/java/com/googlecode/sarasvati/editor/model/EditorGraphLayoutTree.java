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

    Copyright 2009 Paul Lorenz
*/

package com.googlecode.sarasvati.editor.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.googlecode.sarasvati.visual.graph.AbstractLayoutTree;

public class EditorGraphLayoutTree extends AbstractLayoutTree<EditorGraphMember<?>>
{
  private final List<EditorGraphMember<?>> nodes = new LinkedList<EditorGraphMember<?>>();
  private final List<EditorGraphMember<?>> startNodes = new LinkedList<EditorGraphMember<?>> ();
  private final Map<EditorGraphMember<?>,List<EditorGraphMember<?>>> inputMap = new HashMap<EditorGraphMember<?>, List<EditorGraphMember<?>>>();
  private final Map<EditorGraphMember<?>,List<EditorGraphMember<?>>> outputMap = new HashMap<EditorGraphMember<?>, List<EditorGraphMember<?>>>();

  public EditorGraphLayoutTree (final EditorGraph graph)
  {
    for ( EditorNode node : graph.getNodes() )
    {
      if ( node.getState().isStart() )
      {
        startNodes.add( node );
      }

      outputMap.put( node, new LinkedList<EditorGraphMember<?>>() );
      inputMap.put( node, new LinkedList<EditorGraphMember<?>>() );
    }

    for ( EditorExternal external : graph.getExternals() )
    {
      outputMap.put( external, new LinkedList<EditorGraphMember<?>>() );
      inputMap.put( external, new LinkedList<EditorGraphMember<?>>() );
    }

    for ( EditorArc arc : graph.getArcs() )
    {
      inputMap.get( arc.getEnd() ).add( arc.getStart() );
      outputMap.get( arc.getStart() ).add( arc.getEnd() );
    }

    for ( EditorNode node : graph.getNodes() )
    {
      if ( !startNodes.contains( node ) && hasNoInputs( node ) )
      {
        startNodes.add( node );
      }
    }

    init();
  }

  @Override
  protected Collection<EditorGraphMember<?>> getNodes ()
  {
    return nodes;
  }

  @Override
  protected Collection<EditorGraphMember<?>> getOutputs (final EditorGraphMember<?> node)
  {
    return outputMap.get( node );
  }

  @Override
  protected Collection<EditorGraphMember<?>> getInputs (final EditorGraphMember<?> node)
  {
    return inputMap.get( node );
  }

  @Override
  protected Collection<EditorGraphMember<?>> getStartNodes ()
  {
    return startNodes;
  }

  @Override
  protected boolean hasNoInputs (final EditorGraphMember<?> node)
  {
    return inputMap.get( node ).isEmpty();
  }
}