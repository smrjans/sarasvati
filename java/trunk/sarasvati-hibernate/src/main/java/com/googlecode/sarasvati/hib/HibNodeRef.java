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
/**
 * Created on Apr 25, 2008
 */
package com.googlecode.sarasvati.hib;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.JoinStrategy;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.env.ReadEnv;
import com.googlecode.sarasvati.impl.MapEnv;
import com.googlecode.sarasvati.impl.NestedReadEnv;

@Entity
@Table (name="wf_node_ref")
public class HibNodeRef implements Node
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected Long   id;

  @ForeignKey(name="FK_ref_node")
  @ManyToOne (fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
  @JoinColumn(name="node_id")
  protected HibNode node;

  @Index(name="wf_node_ref_graph_idx")
  @ForeignKey(name="FK_ref_graph")
  @ManyToOne (fetch=FetchType.EAGER, targetEntity=HibGraph.class)
  @JoinColumn (name="graph_id")
  protected Graph graph;

  @ForeignKey(name="FK_ref_orig")
  @ManyToOne (fetch=FetchType.LAZY)
  @JoinColumn (name="parent_id")
  protected HibNodeRef originatingExternalNode;

  @ForeignKey(name="FK_ref_ext")
  @ManyToOne (fetch=FetchType.EAGER)
  @JoinColumn (name="external_id")
  protected HibExternal external;

  @Transient
  protected ReadEnv externalEnv;

  protected HibNodeRef () { /* Default constructor for Hibernate */ }

  protected HibNodeRef (final Graph graph,
                        final HibNode node,
                        final HibNodeRef originatingExternalNode,
                        final HibExternal external )
  {
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

  public void setId (final Long id)
  {
    this.id = id;
  }

  public HibNode getNode ()
  {
    return node;
  }

  public void setNode (final HibNode node)
  {
    this.node = node;
  }

  @Override
  public Graph getGraph ()
  {
    return graph;
  }

  public void setGraph (final Graph graph)
  {
    this.graph = graph;
  }

  @Override
  public String getGuard ()
  {
    return node.getGuard();
  }

  @Override
  public HibNodeRef getOriginatingExternalNode ()
  {
    return originatingExternalNode;
  }

  public void setOriginatingExternalNode (final HibNodeRef originatingExternalNode)
  {
    this.originatingExternalNode = originatingExternalNode;
  }

  @Override
  public HibExternal getExternal ()
  {
    return external;
  }

  public void setExternal (final HibExternal external)
  {
    this.external = external;
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
    return node.getJoinStrategy( arc );
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
    return external != null;
  }

  @Override
  public <T> T getAdaptor (final Class<T> clazz)
  {
    return node.getAdaptor (clazz);
  }

  @Override
  public String toString ()
  {
    return "[HibNodeRef id=" + id + " node=" + node + "]";
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
    if ( !( obj instanceof HibNodeRef ) ) return false;
    final HibNodeRef other = (HibNodeRef)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }
}