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
package com.googlecode.sarasvati.jdbc;

import java.util.Date;
import java.util.Set;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ArcTokenSetMember;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.visitor.TokenVisitor;

public class JdbcArcToken implements ArcToken, JdbcObject
{
  protected Long    id;
  protected JdbcGraphProcess process;
  protected JdbcArc     arc;
  protected JdbcNodeToken parentToken;
  protected JdbcNodeToken childToken;

  protected Date    createDate;
  protected Date    completeDate;
  protected boolean pending;
  protected ExecutionType executionType;

  protected boolean tokenSetMember;

  public JdbcArcToken (final JdbcGraphProcess process,
                       final JdbcArc arc,
                       final ExecutionType executionType,
                       final JdbcNodeToken parentToken,
                       final boolean tokenSetMember)
  {
    this.process        = process;
    this.arc            = arc;
    this.executionType  = executionType;
    this.parentToken    = parentToken;
    this.createDate     = new Date();
    this.pending        = true;
    this.tokenSetMember = tokenSetMember;
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
  public JdbcGraphProcess getProcess ()
  {
    return process;
  }

  @Override
  public JdbcArc getArc ()
  {
    return arc;
  }

  @Override
  public NodeToken getParentToken ()
  {
    return parentToken;
  }

  public void setParentToken (final JdbcNodeToken parentToken)
  {
    this.parentToken = parentToken;
  }

  @Override
  public JdbcNodeToken getChildToken()
  {
    return childToken;
  }

  public void setChildToken(final JdbcNodeToken childToken)
  {
    this.childToken = childToken;
  }

  public Date getCreateDate ()
  {
    return createDate;
  }

  public Date getCompleteDate ()
  {
    return completeDate;
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
    this.childToken = (JdbcNodeToken)token;
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
  public boolean isMutable ()
  {
    return true;
  }

  @Override
  public Set<ArcTokenSetMember> getTokenSetMemberships ()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see com.googlecode.sarasvati.ArcToken#isTokenSetMember()
   */
  @Override
  public boolean isTokenSetMember()
  {
    return false;
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
    if ( !( obj instanceof JdbcArcToken ) ) return false;
    final JdbcArcToken other = (JdbcArcToken)obj;
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
    return "[JdbcArcToken id=" + id + " pending? " + pending + " execType=" + executionType + " complete? " + isComplete() + " arc=" + arc + "]";
  }
}