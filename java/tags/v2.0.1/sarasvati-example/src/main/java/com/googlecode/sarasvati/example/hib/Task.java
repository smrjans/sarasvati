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
package com.googlecode.sarasvati.example.hib;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.example.TaskState;
import com.googlecode.sarasvati.hib.HibNodeToken;

@Entity
@Table(name = "wf_task")
public class Task
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long      id;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity=HibNodeToken.class)
  @JoinColumn(name = "node_token_id")
  protected NodeToken nodeToken;

  protected String    name;

  protected String    description;

  @Enumerated(EnumType.ORDINAL)
  protected TaskState state;

  public Task() { /* Default constructor for Hibernate */ }

  public Task( final NodeToken nodeToken, final String name, final String description, final TaskState state )
  {
    this.nodeToken = nodeToken;
    this.name = name;
    this.description = description;
    this.state = state;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( final Long id )
  {
    this.id = id;
  }

  public NodeToken getNodeToken()
  {
    return nodeToken;
  }

  public void setNodeToken( final HibNodeToken nodeToken )
  {
    this.nodeToken = nodeToken;
  }

  public String getName()
  {
    return name;
  }

  public void setName( final String name )
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( final String description )
  {
    this.description = description;
  }

  public TaskState getState()
  {
    return state;
  }

  public void setState( final TaskState state )
  {
    this.state = state;
  }

  public boolean isRejectable ()
  {
    Node nodeRef = getNodeToken().getNode();
    return !nodeRef.getGraph().getOutputArcs( nodeRef, "reject" ).isEmpty();
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof Task))
      return false;
    Task other = (Task) obj;
    if (id == null)
    {
      if (other.getId() != null)
        return false;
    } else if (!id.equals(other.getId()))
      return false;
    return true;
  }
}