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

package com.googlecode.sarasvati.hib;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.External;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.JoinStrategy;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.adapter.NodeAdapterManager;
import com.googlecode.sarasvati.env.ReadEnv;
import com.googlecode.sarasvati.impl.MapEnv;

@Entity
@Table (name="wf_node")
@Inheritance (strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula( "(select t.behaviour from wf_node_type t where t.id = type)" )
@DiscriminatorValue( "node" )
public class HibNode implements Node
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column( name="id", nullable=false)
  protected Long   id;

  @ForeignKey(name="FK_node_graph")
  @ManyToOne (fetch=FetchType.EAGER, targetEntity=HibGraph.class)
  @JoinColumn(name="graph_id", nullable=false)
  protected Graph graph;

  @Column( name="name", nullable=false)
  protected String name;

  @Column( name="type", nullable=false)
  protected String type;

  @Column (name="join_type", nullable=false)
  protected JoinType joinType;

  @Column (name="join_param", nullable=true)
  protected String joinParam;

  @Column (name="is_start")
  @Type (type="yes_no")
  protected boolean start;

  protected String guard;

  public HibNode () { /* Default constructor for Hibernate */ }

  @Override
  public Long getId ()
  {
    return id;
  }

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

  @Override
  public String getType ()
  {
    return type;
  }

  public void setType (final String type)
  {
    this.type = type;
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
  public JoinType getJoinType ()
  {
    return joinType;
  }

  public void setJoinType (final JoinType joinType)
  {
    this.joinType = joinType;
  }

  @Override
  public String getJoinParam ()
  {
    return joinParam;
  }

  public void setJoinParam (final String joinParam)
  {
    this.joinParam = joinParam;
  }

  @Override
  public JoinStrategy getJoinStrategy (final Arc arc)
  {
    return getJoinType().getJoinStrategy();
  }

  @Override
  public boolean isStart()
  {
    return start;
  }

  public void setStart( final boolean start )
  {
    this.start = start;
  }

  @Override
  public String getGuard()
  {
    return guard;
  }

  public void setGuard( final String guard )
  {
    this.guard = guard;
  }

  /**
   * This is meaningless for HibNode, since these are never directly
   * external. This maybe wrapped by some node refs which are external
   * and others which aren't.
   */
  @Override
  public boolean isImportedFromExternal()
  {
    return false;
  }

  @Override
  public External getExternal ()
  {
    return null;
  }

  @Override
  public ReadEnv getExternalEnv ()
  {
    return MapEnv.READONLY_EMPTY_INSTANCE;
  }

  @Override
  public Node getOriginatingExternalNode ()
  {
    return null;
  }

  /**
   * Does nothing by default. Can be overridden by subclasses.
   * @see Node#backtrack(Engine, NodeToken)
   */
  @Override
  public void backtrack (final Engine engine, final NodeToken token)
  {
    // does nothing by default. Can be overridden by subclasses.
  }

  /**
   * Returns true. Can be overridden by subclasses.
   *
   * @see Node#isBacktrackable(Engine,NodeToken)
   */
  @Override
  public boolean isBacktrackable (final Engine engine, final NodeToken token)
  {
    return true;
  }

  @Override
  public GuardResult guard (final Engine engine, final NodeToken token)
  {
    return engine.evaluateGuard( token, guard );
  }

  @Override
  public void execute (final Engine engine, final NodeToken token)
  {
    engine.complete( token, Arc.DEFAULT_ARC );
  }

  /**
   * Will use the {@link NodeAdapterManager} to produce an adapter.
   * Subclasses may override this behavior.
   *
   * @see Node#getAdaptor(Class)
   */
  @Override public <T> T getAdaptor (final Class<T> clazz)
  {
    return NodeAdapterManager.getAdaptor( this, clazz );
  }

  /**
   * Called when this node should be persisted to the database. May be overridden
   * by subclasses, but then the subclass must either call super.create(...) or
   * call session.save( this ).
   *
   * @param session The hibernate session to use
   */
  public void create (final Session session)
  {
    session.save( this );
  }

  @Override
  public String toString ()
  {
    return "[HibNode id=" + id + " name=" + name + " type=" + type + "]";
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
    if ( !( obj instanceof HibNode ) ) return false;
    final HibNode other = (HibNode)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }
}