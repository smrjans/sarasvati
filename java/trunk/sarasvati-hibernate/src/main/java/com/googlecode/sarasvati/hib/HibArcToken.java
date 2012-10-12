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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ArcTokenSetMember;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.visitor.TokenVisitor;

@Entity
@Table (name="wf_arc_token")
@org.hibernate.annotations.Table( appliesTo="wf_arc_token", indexes={@Index(name="wf_arc_token_idx", columnNames={"process_id", "complete_date", "pending"} )} )
public class HibArcToken implements ArcToken
{
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO, generator="sequence_generator")
  @SequenceGenerator(name="sequence_generator", sequenceName="wf_arc_token_seq")
  protected Long    id;

  @ForeignKey( name="FK_arctok_process" )
  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "process_id")
  protected HibGraphProcess process;

  @ForeignKey( name="FK_arctok_arc" )
  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn (name = "arc_id")
  protected HibArc     arc;

  @ForeignKey( name="FK_arctok_parent" )
  @ManyToOne(fetch = FetchType.LAZY, targetEntity=HibNodeToken.class, cascade=CascadeType.REMOVE)
  @JoinColumn (name = "parent_token_id", nullable=false)
  protected NodeToken parentToken;

  @ManyToOne( fetch=FetchType.LAZY, targetEntity=HibNodeToken.class, cascade=CascadeType.ALL)
  @JoinTable( name = "wf_node_token_parent",
              joinColumns = @JoinColumn(name = "arc_token_id"),
              inverseJoinColumns = @JoinColumn(name = "node_token_id") )
  protected NodeToken childToken;

  @Temporal(TemporalType.TIMESTAMP)
  @Column (name="create_date", updatable = false)
  protected Date    createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column (name="complete_date")
  protected Date    completeDate;

  @Type (type="yes_no")
  @Column( name="pending")
  protected boolean pending;

  @Column (name="execution_type")
  @Enumerated( EnumType.ORDINAL )
  protected ExecutionType executionType;

  @OneToMany (mappedBy="token", targetEntity=HibArcTokenSetMember.class, fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
  @Cascade( org.hibernate.annotations.CascadeType.LOCK )
  protected Set<ArcTokenSetMember> tokenSetMemberships;

  public HibArcToken () { /* Default constructor for hibernate */ }

  public HibArcToken (final HibGraphProcess process, final HibArc arc, final ExecutionType executionType, final HibNodeToken parentToken)
  {
    this.process       = process;
    this.arc           = arc;
    this.executionType = executionType;
    this.parentToken   = parentToken;
    this.createDate    = new Date();
    this.pending       = true;
    this.tokenSetMemberships = new HashSet<ArcTokenSetMember>();
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

  @Override
  public HibGraphProcess getProcess ()
  {
    return process;
  }

  public void setProcess (final HibGraphProcess process)
  {
    this.process = process;
  }

  @Override
  public HibArc getArc ()
  {
    return arc;
  }

  public void setArc (final HibArc arc)
  {
    this.arc = arc;
  }

  @Override
  public NodeToken getParentToken ()
  {
    return parentToken;
  }

  public void setParentToken (final NodeToken parentToken)
  {
    this.parentToken = parentToken;
  }

  @Override
  public NodeToken getChildToken()
  {
    return childToken;
  }

  public void setChildToken(final NodeToken childToken)
  {
    this.childToken = childToken;
  }

  public Date getCreateDate ()
  {
    return createDate;
  }

  public void setCreateDate (final Date createDate)
  {
    this.createDate = createDate;
  }

  public Date getCompleteDate ()
  {
    return completeDate;
  }

  public void setCompleteDate (final Date completeDate)
  {
    this.completeDate = completeDate;
  }

  @Override
  public boolean isComplete ()
  {
    return completeDate != null;
  }

  @Override
  public void markComplete (final NodeToken token)
  {
    this.completeDate = new Date();
    this.childToken = token;
  }

  @Override
  public boolean isPending ()
  {
    return pending;
  }

  @Override
  public void markProcessed ()
  {
    pending = false;
  }

  @Override
  public ExecutionType getExecutionType ()
  {
    return executionType;
  }

  @Override
  public void markBacktracked ()
  {
    executionType = executionType.getCorrespondingBacktracked( isComplete() );
  }

  @Override
  public void accept (final TokenVisitor visitor)
  {
    visitor.visit( this );
  }

  @Override
  public Set<ArcTokenSetMember> getTokenSetMemberships ()
  {
    return tokenSetMemberships;
  }

  public void setTokenSetMembers (final Set<ArcTokenSetMember> tokenSetMemberships)
  {
    this.tokenSetMemberships = tokenSetMemberships;
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof HibArcToken ) ) return false;
    final HibArcToken other = (HibArcToken)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }

  @Override
  public String toString ()
  {
    return "[HibArcToken id=" + id + " pending? " + pending + " execType=" + executionType + " complete? " + isComplete() + " arc=" + arc + "]";
  }
}