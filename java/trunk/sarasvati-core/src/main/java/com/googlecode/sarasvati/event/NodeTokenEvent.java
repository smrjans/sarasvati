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
package com.googlecode.sarasvati.event;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;

public class NodeTokenEvent extends ExecutionEvent
{
  protected NodeToken nodeToken;
  protected String[]    exitArcsNames;

  public static final EventActions fireCreatedEvent (final Engine engine, final NodeToken nodeToken)
  {
    return engine.fireEvent( new NodeTokenEvent( engine, ExecutionEventType.NODE_TOKEN_CREATED, nodeToken) );
  }

  public static final EventActions fireAcceptedEvent (final Engine engine, final NodeToken nodeToken)
  {
    return engine.fireEvent( new NodeTokenEvent( engine, ExecutionEventType.NODE_TOKEN_ACCEPTED, nodeToken) );
  }

  public static final EventActions fireExecutedEvent (final Engine engine, final NodeToken nodeToken)
  {
    return engine.fireEvent( new NodeTokenEvent( engine, ExecutionEventType.NODE_TOKEN_EXECUTED, nodeToken) );
  }

  public static final EventActions fireDiscardedEvent (final Engine engine, final NodeToken nodeToken)
  {
    return engine.fireEvent( new NodeTokenEvent( engine, ExecutionEventType.NODE_TOKEN_DISCARDED, nodeToken) );
  }

  public static final EventActions fireSkippedEvent (final Engine engine, final NodeToken nodeToken, final String exitArcsName)
  {
    return engine.fireEvent( new NodeTokenEvent( engine, ExecutionEventType.NODE_TOKEN_SKIPPED, nodeToken, exitArcsName ) );
  }

  public static final EventActions fireCompletedEvent (final Engine engine, final NodeToken nodeToken, final String...exitArcsNames)
  {
    return engine.fireEvent( new NodeTokenEvent( engine, ExecutionEventType.NODE_TOKEN_COMPLETED, nodeToken, exitArcsNames) );
  }

  public static final EventActions fireBacktrackedEvent (final Engine engine, final NodeToken nodeToken)
  {
    return engine.fireEvent( new NodeTokenEvent( engine, ExecutionEventType.NODE_TOKEN_BACKTRACKED, nodeToken) );
  }

  private NodeTokenEvent (final Engine engine, final ExecutionEventType eventType, final NodeToken nodeToken, final String...exitArcsName)
  {
    super( engine, eventType );
    this.nodeToken = nodeToken;
    this.exitArcsNames = exitArcsName;
  }

  @Override
  public NodeToken getNodeToken()
  {
    return nodeToken;
  }

  @Override
  public String getExitArcsName()
  {
    return exitArcsNames == null || exitArcsNames.length < 1 ? null : exitArcsNames[0];
  }

  @Override
  public String[] getExitArcsNames()
  {
    return exitArcsNames;
  }

  @Override
  public GraphProcess getProcess()
  {
    return nodeToken.getProcess();
  }
}