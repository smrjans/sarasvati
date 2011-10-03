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
package com.googlecode.sarasvati.hib;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.event.PersistedExecutionListener;

@Entity
@Table (name="wf_graph_listener")
public class HibGraphListener implements PersistedExecutionListener
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected Long id;

  protected String type;

  @Column (name="event_type_mask")
  protected int eventTypeMask;

  @ForeignKey(name="FK_listener_graph")
  @ManyToOne(fetch = FetchType.LAZY, targetEntity=HibGraph.class)
  @JoinColumn(name = "graph_id")
  protected Graph graph;

  protected HibGraphListener () { /* Default constructor for Hibernate */ }

  public HibGraphListener (final String type, final int eventTypeMask, final Graph graph)
  {
    this.eventTypeMask = eventTypeMask;
    this.type = type;
    this.graph = graph;
  }

  public Long getId ()
  {
    return id;
  }

  public void setId (final Long id)
  {
    this.id = id;
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
  public int getEventTypeMask ()
  {
    return eventTypeMask;
  }

  public void setEventTypeMask (final int eventTypeMask)
  {
    this.eventTypeMask = eventTypeMask;
  }

  /**
   * @return the graph
   */
  public Graph getGraph ()
  {
    return graph;
  }

  /**
   * @param graph the graph to set
   */
  public void setGraph (final Graph graph)
  {
    this.graph = graph;
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
    if (!(obj instanceof HibGraphListener))
      return false;
    HibGraphListener other = (HibGraphListener) obj;
    if (id == null)
    {
      if (other.getId() != null)
        return false;
    } else if (!id.equals(other.getId()))
      return false;
    return true;
  }
}
