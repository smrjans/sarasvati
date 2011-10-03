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

    Copyright 2008 Paul Lorenz
*/

package com.googlecode.sarasvati.mem;

import java.util.LinkedList;
import java.util.List;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.event.DefaultExecutionEventQueue;
import com.googlecode.sarasvati.event.ExecutionEventQueue;
import com.googlecode.sarasvati.impl.AbstractGraph;

public class MemGraph extends AbstractGraph
{
  protected String        name;
  protected String        customId;
  protected List<Node> nodes;
  protected List<Arc>  arcs;

  protected final ExecutionEventQueue eventQueue = DefaultExecutionEventQueue.newArrayListInstance();

  public MemGraph (final String name, final String customId)
  {
    this.name     = name;
    this.customId = customId;
    this.nodes    = new LinkedList<Node>();
    this.arcs     = new LinkedList<Arc>();
  }

  @Override
  public List<Node> getNodes ()
  {
    return nodes;
  }

  @Override
  public List<Arc> getArcs ()
  {
    return arcs;
  }

  @Override
  public String getName ()
  {
    return name;
  }

  @Override
  public int getVersion ()
  {
    return 1;
  }

  @Override
  public String getCustomId ()
  {
    return customId;
  }

  /**
   * @see com.googlecode.sarasvati.event.HasEventQueue#getEventQueue()
   */
  @Override
  public ExecutionEventQueue getEventQueue ()
  {
    return eventQueue;
  }
}
