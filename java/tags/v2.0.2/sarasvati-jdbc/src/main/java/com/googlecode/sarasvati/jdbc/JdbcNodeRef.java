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
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.JoinStrategy;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.env.ReadEnv;
import com.googlecode.sarasvati.impl.MapEnv;
import com.googlecode.sarasvati.impl.NestedReadEnv;

public class JdbcNodeRef implements Node,JdbcObject
{
  protected Long   id;

  protected JdbcNode node;
  protected JdbcGraph graph;
  protected JdbcNodeRef originatingExternalNode;
  protected JdbcExternal external;

  protected ReadEnv externalEnv;

  public JdbcNodeRef (final JdbcGraph graph,
                      final JdbcNode node,
                      final JdbcNodeRef originatingExternalNode,
                      final JdbcExternal external)
  {
    this( null, graph, node, originatingExternalNode, external );
  }

  public JdbcNodeRef (final Long id,
                      final JdbcGraph graph,
                      final JdbcNode node,
                      final JdbcNodeRef originatingExternalNode,
                      final JdbcExternal external )
  {
    this.id       = id;
    this.graph    = graph;
    this.node     = node;
    this.originatingExternalNode = originatingExternalNode;
    this.external = external;
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

  public JdbcNode getNode ()
  {
    return node;
  }

  public void setNode (final JdbcNode node)
  {
    this.node = node;
  }

  @Override
  public JdbcGraph getGraph ()
  {
    return graph;
  }

  public void setGraph (final JdbcGraph graph)
  {
    this.graph = graph;
  }

  @Override
  public String getGuard ()
  {
    return node.getGuard();
  }

  @Override
  public JdbcNodeRef getOriginatingExternalNode ()
  {
    return originatingExternalNode;
  }

  public void setOriginatingExternalNode (final JdbcNodeRef originatingExternalNode)
  {
    this.originatingExternalNode = originatingExternalNode;
  }

  @Override
  public JdbcExternal getExternal ()
  {
    return external;
  }

  public void setExternal (final JdbcExternal external)
  {
    this.external = external;
  }

  @Override
  public String getName ()
  {
    return node.getName();
  }

  @Override
  public String getType ()
  {
    return node.getType();
  }

  @Override
  public JoinType getJoinType ()
  {
    return node.getJoinType();
  }

  @Override
  public String getJoinParam ()
  {
    return node.getJoinParam();
  }

  @Override
  public JoinStrategy getJoinStrategy (final Arc arc)
  {
    return node.getJoinStrategy(arc);
  }

  @Override
  public ReadEnv getExternalEnv ()
  {
    if ( external == null )
    {
      return MapEnv.READONLY_EMPTY_INSTANCE;
    }

    if ( externalEnv == null )
    {
      if ( originatingExternalNode == null )
      {
        externalEnv = external.getEnv();
      }
      else
      {
        externalEnv = new NestedReadEnv( external.getEnv(), originatingExternalNode.getExternalEnv() );
      }
    }

    return externalEnv;
  }

  @Override
  public boolean isStart ()
  {
    return node.isStart() && getGraph().equals( node.getGraph() );
  }

  @Override
  public void backtrack (final Engine engine, final NodeToken token)
  {
    node.backtrack( engine, token );
  }

  @Override
  public boolean isBacktrackable(final Engine engine, final NodeToken token)
  {
    return node.isBacktrackable( engine, token );
  }

  @Override
  public GuardResult guard (final Engine engine, final NodeToken token)
  {
    return node.guard( engine, token );
  }

  @Override
  public void execute (final Engine engine, final NodeToken token)
  {
    node.execute( engine, token );
  }

  @Override
  public boolean isImportedFromExternal ()
  {
    return !graph.equals( getNode().getGraph() );
  }

  @Override
  public <T> T getAdaptor (final Class<T> clazz)
  {
    return node.getAdaptor (clazz);
  }

  @Override
  public boolean isMutable ()
  {
    return false;
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
    if ( !( obj instanceof JdbcNodeRef ) ) return false;
    final JdbcNodeRef other = (JdbcNodeRef)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }
}