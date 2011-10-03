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

package com.googlecode.sarasvati.visual.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.Node;

public class GraphLayoutTree extends AbstractLayoutTree<Node>
{
  private final Graph graph;

  public GraphLayoutTree (final Graph graph)
  {
    this.graph = graph;
    init();
  }

  @Override
  protected Collection<Node> getNodes ()
  {
    return graph.getNodes();
  }

  @Override
  protected Collection<Node> getStartNodes ()
  {
    return graph.getStartNodes();
  }

  @Override
  protected Collection<Node> getOutputs (final Node node)
  {
    List<Arc> arcs = graph.getOutputArcs( node );

    List<Node> outputs = new ArrayList<Node>( arcs.size() );

    for ( Arc arc : arcs )
    {
      outputs.add( arc.getEndNode() );
    }

    return outputs;
  }

  @Override
  protected Collection<Node> getInputs (final Node node)
  {
    List<Arc> arcs = graph.getInputArcs( node );

    List<Node> inputs = new ArrayList<Node>( arcs.size() );

    for ( Arc arc : arcs )
    {
      inputs.add( arc.getStartNode() );
    }

    return inputs;
  }

  @Override
  protected boolean hasNoInputs (final Node node)
  {
    return graph.getInputArcs( node ).isEmpty();
  }
}
