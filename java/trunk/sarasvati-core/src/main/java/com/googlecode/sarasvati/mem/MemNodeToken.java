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

    Copyright 2008-2009 Paul Lorenz
*/

package com.googlecode.sarasvati.mem;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.GuardAction;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.NodeTokenSetMember;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.impl.MapEnv;
import com.googlecode.sarasvati.impl.NestedEnv;
import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.visitor.TokenVisitor;

public class MemNodeToken implements NodeToken
{
  protected long id;
  protected Node node;
  protected GraphProcess process;
  protected GuardAction guardAction;
  protected List<ArcToken> parentTokens;
  protected List<ArcToken> childTokens;
  protected Date createDate;
  protected Date completeDate;
  protected ExecutionType executionType;
  protected Date delayUntilTime;
  protected int delayCount;

  protected Set<NodeTokenSetMember> tokenSetMemberships = null;

  protected Map<String, String> attributes = new HashMap<String, String>();

  protected Env env = new MapEnv();
  protected Env fullEnv = null;

  protected MemGraphProcess nestedProcess;

  public MemNodeToken (final long id, final Node node, final GraphProcess process, final ExecutionType executionType, final List<ArcToken> parentTokens)
  {
    this.id = id;
    this.node = node;
    this.process = process;
    this.parentTokens = parentTokens;
    this.executionType = executionType;
    this.childTokens = new LinkedList<ArcToken>();
    this.createDate = new Date();
    tokenSetMemberships = new HashSet<NodeTokenSetMember>();
  }

  @Override
  public Long getId ()
  {
    return id;
  }

  @Override
  public Node getNode ()
  {
    return node;
  }

  @Override
  public GraphProcess getProcess ()
  {
    return process;
  }

  @Override
  public GuardAction getGuardAction ()
  {
    return guardAction;
  }

  @Override
  public List<ArcToken> getParentTokens()
  {
    return parentTokens;
  }

  @Override
  public List<ArcToken> getChildTokens()
  {
    return childTokens;
  }

  @Override
  public Date getCreateDate()
  {
    return createDate;
  }

  @Override
  public void setGuardAction (final GuardAction action)
  {
    this.guardAction = action;
  }

  @Override
  public boolean isComplete ()
  {
    return completeDate != null;
  }

  @Override
  public void markComplete ()
  {
    completeDate = new Date();
  }

  @Override
  public Date getCompleteDate ()
  {
    return completeDate;
  }

  @Override
  public void accept (final TokenVisitor visitor)
  {
    visitor.visit( this );
  }

  @Override
  public Env getFullEnv()
  {
    if ( fullEnv == null )
    {
      fullEnv = new NestedEnv( env, process.getEnv() );
    }
    return fullEnv;
  }

  @Override
  public Env getEnv()
  {
    return env;
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
  public TokenSet getTokenSet (final String name)
  {
    return SvUtil.getTokenSet( this, name );
  }

  @Override
  public NodeTokenSetMember getTokenSetMember (final String name)
  {
    return (NodeTokenSetMember)SvUtil.getTokenSetMember( this, name );
  }

  @Override
  public Set<NodeTokenSetMember> getTokenSetMemberships ()
  {
    return tokenSetMemberships;
  }

  /**
   * @return the nestedProcess
   */
  public MemGraphProcess getNestedProcess()
  {
    return nestedProcess;
  }

  /**
   * @param childProcess the childProcess to set
   */
  public void setNestedProcess(final MemGraphProcess nestedProcess)
  {
    this.nestedProcess = nestedProcess;
  }

  /**
   * @see com.googlecode.sarasvati.NodeToken#getDelayUntilTime()
   */
  @Override
  public Date getDelayUntilTime()
  {
    return delayUntilTime;
  }

  /**
   * @see com.googlecode.sarasvati.NodeToken#getDelayCount()
   */
  @Override
  public int getDelayCount()
  {
    return delayCount;
  }

  /**
   * @see com.googlecode.sarasvati.NodeToken#markDelayed(java.util.Date)
   */
  @Override
  public void markDelayed(final Date newDelayUntilTime)
  {
    this.delayUntilTime = newDelayUntilTime;
    this.delayCount++;
  }

  @Override
  public String toString()
  {
    return "[MemNodeToken id=" + id + " node=" + (node == null ? null : node.getName()) + " execType=" + executionType + " hashCode=" + hashCode() + "]";
  }
}