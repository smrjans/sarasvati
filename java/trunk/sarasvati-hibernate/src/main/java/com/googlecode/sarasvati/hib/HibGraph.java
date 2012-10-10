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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.event.CachingExecutionEventQueue;
import com.googlecode.sarasvati.event.ExecutionEventQueue;
import com.googlecode.sarasvati.event.InitialExecutionEventQueue;
import com.googlecode.sarasvati.impl.AbstractGraph;

@Entity
@Table (name="wf_graph")
public class HibGraph extends AbstractGraph
{
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO, generator="sequence_generator")
  @SequenceGenerator(name="sequence_generator", sequenceName="wf_graph_seq")
  protected Long   id;
  protected String name;
  protected int    version;

  @Column (name="custom_id")
  protected String customId;

  @Column (name="create_date", updatable=false)
  @Temporal (TemporalType.TIMESTAMP)
  protected Date   createDate;

  @OneToMany (mappedBy="graph", fetch=FetchType.LAZY)
  @Cascade( { org.hibernate.annotations.CascadeType.LOCK, org.hibernate.annotations.CascadeType.DELETE } )
  protected List<HibGraphListener> listeners;

  @Transient
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

  protected HibGraph () { /* Default constructor for hibernate */ }

  protected HibGraph (final String name, final int version, final String customId)
  {
    this.name = name;
    this.version = version;
    this.customId = customId;
    this.createDate = new Date();
    this.nodes = new LinkedList<Node>();
    this.arcs = new LinkedList<Arc>();
    this.listeners = new LinkedList<HibGraphListener>();
  }

  @OneToMany (fetch=FetchType.EAGER, mappedBy="graph", cascade=CascadeType.REMOVE, targetEntity=HibNodeRef.class)
  protected List<Node> nodes;

  @OneToMany (fetch=FetchType.LAZY, mappedBy="graph", cascade=CascadeType.REMOVE, targetEntity=HibArc.class)
  protected List<Arc>     arcs;

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
  public int getVersion ()
  {
    return version;
  }

  public void setVersion (final int version)
  {
    this.version = version;
  }

  @Override
  public String getCustomId ()
  {
    return customId;
  }

  public void setCustomId (final String customId)
  {
    this.customId = customId;
  }

  public Date getCreateDate()
  {
    return createDate;
  }

  public void setCreateDate( final Date createDate )
  {
    this.createDate = createDate;
  }

  @Override
  public List<Node> getNodes ()
  {
    return nodes;
  }

  public void setNodes (final List<Node> nodeRefs)
  {
    this.nodes = nodeRefs;
  }

  @Override
  public List<Arc> getArcs ()
  {
    return arcs;
  }

  public void setArcs (final List<Arc> arcs)
  {
    this.arcs = arcs;
  }

  /**
   * @return the listeners
   */
  public List<HibGraphListener> getListeners ()
  {
    return listeners;
  }

  /**
   * @param listeners the listeners to set
   */
  public void setListeners (final List<HibGraphListener> listeners)
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
    result = prime * result + ( ( id == null )
        ? 0 : id.hashCode() );
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof HibGraph ) ) return false;
    final HibGraph other = (HibGraph)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }
}
