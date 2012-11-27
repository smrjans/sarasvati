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
package com.googlecode.sarasvati.jdbc;

import java.util.LinkedList;
import java.util.List;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.event.CachingExecutionEventQueue;
import com.googlecode.sarasvati.event.ExecutionEventQueue;
import com.googlecode.sarasvati.event.InitialExecutionEventQueue;
import com.googlecode.sarasvati.impl.AbstractGraph;

public class JdbcGraph extends AbstractGraph implements JdbcObject
{
  protected Long   id;
  protected String name;
  protected int    version;
  protected String customId;

  protected List<Node> nodes;
  protected List<Arc> arcs;

  protected List<JdbcGraphListener> listeners;

  protected ExecutionEventQueue eventQueue = new InitialExecutionEventQueue()
  {
    @Override
    protected ExecutionEventQueue init ()
    {
      CachingExecutionEventQueue newEventQueue = CachingExecutionEventQueue.newArrayListInstance();
      newEventQueue.initFromPersisted( getListeners() );
      eventQueue = newEventQueue;
      return eventQueue;
    }
  };

  public JdbcGraph (final String name,
                    final int version,
                    final String customId)
  {
    this( null, name, version, customId );
  }

  public JdbcGraph (final Long id,
                    final String name,
                    final int version,
                    final String customId)
  {
    this.id       = id;
    this.name     = name;
    this.version  = version;
    this.customId = customId;
    this.nodes    = new LinkedList<Node>();
    this.arcs     = new LinkedList<Arc>();
  }

  @Override
  public Long getId ()
  {
    return id;
  }

  @Override
  public void setId (final Long id)
  {
    this.id = id;
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
  public List<Node> getNodes ()
  {
    return nodes;
  }

  @Override
  public int getVersion ()
  {
    return version;
  }

  @Override
  public String getCustomId ()
  {
    return customId;
  }

  @Override
  public boolean isMutable ()
  {
    return false;
  }

  /**
   * @return the listeners
   */
  public List<JdbcGraphListener> getListeners()
  {
    return listeners;
  }

  /**
   * @param listeners the listeners to set
   */
  public void setListeners(final List<JdbcGraphListener> listeners)
  {
    this.listeners = listeners;
  }

  /**
   * @see com.googlecode.sarasvati.event.HasEventQueue#getEventQueue()
   */
  @Override
  public ExecutionEventQueue getEventQueue ()
  {
    return eventQueue;
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof JdbcGraph))
      return false;
    JdbcGraph other = (JdbcGraph) obj;
    if (id == null)
    {
      if (other.id != null)
        return false;
    } else if (!id.equals( other.id ))
      return false;
    return true;
  }
}
