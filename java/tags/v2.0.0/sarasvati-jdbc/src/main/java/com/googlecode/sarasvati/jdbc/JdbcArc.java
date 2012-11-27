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

import com.googlecode.sarasvati.Arc;

public class JdbcArc implements Arc, JdbcObject
{
  protected Long id;
  protected String name;
  protected JdbcGraph graph;

  protected JdbcNodeRef startNode;

  protected JdbcNodeRef endNode;

  public JdbcArc (final JdbcGraph graph, final JdbcNodeRef startNode, final JdbcNodeRef endNode, final String name)
  {
    this ( null, graph, startNode, endNode, name );
  }

  public JdbcArc (final Long id, final JdbcGraph graph, final JdbcNodeRef startNode, final JdbcNodeRef endNode, final String name)
  {
    this.id        = id;
    this.graph     = graph;
    this.startNode = startNode;
    this.endNode   = endNode;
    this.name      = name;
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
  public String getName ()
  {
    return name;
  }

  public void setName (final String name)
  {
    this.name = name;
  }

  public JdbcGraph getGraph ()
  {
    return graph;
  }

  public void setGraph (final JdbcGraph graph)
  {
    this.graph = graph;
  }

  @Override
  public JdbcNodeRef getStartNode ()
  {
    return startNode;
  }

  public void setStartNode (final JdbcNodeRef startNode)
  {
    this.startNode = startNode;
  }

  @Override
  public JdbcNodeRef getEndNode ()
  {
    return endNode;
  }

  public void setEndNode (final JdbcNodeRef endNode)
  {
    this.endNode = endNode;
  }

  @Override
  public boolean isSelfArc ()
  {
    return startNode.equals( endNode );
  }

  @Override
  public boolean isMutable ()
  {
    return false;
  }

  @Override
  public String toString ()
  {
    return "[Arc id=" + id + " name=" + name + "]";
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( id == null )
        ? 0 : id.hashCode() );
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof JdbcArc ) ) return false;
    final JdbcArc other = (JdbcArc)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }
}