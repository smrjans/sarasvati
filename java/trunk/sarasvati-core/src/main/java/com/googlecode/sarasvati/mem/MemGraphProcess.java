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

package com.googlecode.sarasvati.mem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.ProcessState;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.event.DefaultExecutionEventQueue;
import com.googlecode.sarasvati.event.ExecutionEventQueue;
import com.googlecode.sarasvati.impl.MapEnv;

public class MemGraphProcess implements GraphProcess
{
  protected long tokenCounter = 0;

  protected Graph graph;

  protected List<ArcToken> arcTokens = new LinkedList<ArcToken>();
  protected List<NodeToken> nodeTokens = new LinkedList<NodeToken>();

  protected List<ArcToken> activeArcTokens = new LinkedList<ArcToken>();
  protected List<NodeToken> activeNodeTokens = new LinkedList<NodeToken>();

  protected ProcessState state;

  protected Queue<ArcToken> executionQueue = new LinkedList<ArcToken>();

  protected Env env = new MapEnv();

  protected NodeToken parentToken;

  protected ExecutionEventQueue eventQueue = DefaultExecutionEventQueue.newArrayListInstance();

  public MemGraphProcess (final Graph graph)
  {
    this.graph = graph;
    this.state = ProcessState.Created;
  }

  @Override
  public void addActiveArcToken (final ArcToken token)
  {
    activeArcTokens.add( token );
  }

  @Override
  public void addActiveNodeToken (final NodeToken token)
  {
    activeNodeTokens.add( token );
  }

  @Override
  public List<NodeToken> getNodeTokens()
  {
    return nodeTokens;
  }

  @Override
  public List<ArcToken> getActiveArcTokens ()
  {
    return activeArcTokens;
  }

  @Override
  public List<NodeToken> getActiveNodeTokens ()
  {
    return activeNodeTokens;
  }

  @Override
  public void addNodeToken(final NodeToken token)
  {
    nodeTokens.add( token );
  }

  @Override
  public Env getEnv ()
  {
    return env;
  }

  @Override
  public Graph getGraph ()
  {
    return graph;
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
  public void removeActiveArcToken (final ArcToken token)
  {
    activeArcTokens.remove( token );
  }

  @Override
  public void removeActiveNodeToken (final NodeToken token)
  {
    activeNodeTokens.remove( token );
  }

  @Override
  public ArcToken dequeueArcTokenForExecution()
  {
    return executionQueue.remove();
  }

  @Override
  public void enqueueArcTokenForExecution(final ArcToken token)
  {
    executionQueue.add( token );
  }

  @Override
  public boolean isArcTokenQueueEmpty()
  {
    return executionQueue.isEmpty();
  }

  @Override
  public ProcessState getState ()
  {
    return state;
  }

  @Override
  public void setState (final ProcessState state)
  {
    this.state = state;
  }

  @Override
  public boolean isCanceled()
  {
    return state == ProcessState.PendingCancel || state == ProcessState.Canceled;
  }

  @Override
  public boolean isComplete()
  {
    return state == ProcessState.PendingCompletion || state == ProcessState.Completed;
  }

  @Override
  public boolean isExecuting ()
  {
    return state == ProcessState.Executing;
  }

  @Override
  public boolean hasActiveTokens ()
  {
    return !activeArcTokens.isEmpty() || !activeNodeTokens.isEmpty();
  }

  public synchronized long nextTokenId ()
  {
    return tokenCounter++;
  }

  @Override
  public ExecutionEventQueue getEventQueue ()
  {
    return eventQueue;
  }

  @Override
  public List<NodeToken> getTokensOnNode (final Node node, final Engine engine)
  {
    List<NodeToken> result = new ArrayList<NodeToken>();

    for ( NodeToken token : getNodeTokens() )
    {
      if ( token.getNode().equals( node ) )
      {
        result.add( token );
      }
    }

    return result;
  }
}