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

    Copyright 20082-2009 Paul Lorenz
*/
package com.googlecode.sarasvati.event;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;

public class ArcTokenEvent extends ExecutionEvent
{
  public static final EventActions fireCreatedEvent (final Engine engine, final ArcToken arcToken)
  {
    return engine.fireEvent( new ArcTokenEvent( engine, ExecutionEventType.ARC_TOKEN_CREATED, arcToken ) );
  }

  public static final EventActions fireProcessedEvent (final Engine engine, final ArcToken arcToken)
  {
    return engine.fireEvent( new ArcTokenEvent( engine, ExecutionEventType.ARC_TOKEN_PROCESSED, arcToken ) );
  }

  public static final EventActions fireCompletedEvent (final Engine engine, final ArcToken arcToken)
  {
    return engine.fireEvent( new ArcTokenEvent( engine, ExecutionEventType.ARC_TOKEN_COMPLETED, arcToken ) );
  }

  public static final EventActions fireBacktrackedEvent (final Engine engine, final ArcToken arcToken)
  {
    return engine.fireEvent( new ArcTokenEvent( engine, ExecutionEventType.ARC_TOKEN_BACKTRACKED, arcToken ) );
  }

  public static final EventActions fireIncompleteJoinEvent (final Engine engine, final ArcToken arcToken)
  {
    return engine.fireEvent( new ArcTokenEvent( engine, ExecutionEventType.ARC_TOKEN_INCOMPLETE_JOIN, arcToken ) );
  }

  public static final EventActions fireMergedEvent (final Engine engine, final ArcToken arcToken)
  {
    return engine.fireEvent( new ArcTokenEvent( engine, ExecutionEventType.ARC_TOKEN_MERGED, arcToken ) );
  }

  protected ArcToken arcToken;

  private ArcTokenEvent (final Engine engine, final ExecutionEventType eventType, final ArcToken arcToken)
  {
    super( engine, eventType );
    this.arcToken  = arcToken;
  }

  @Override
  public ArcToken getArcToken()
  {
    return arcToken;
  }

  @Override
  public GraphProcess getProcess()
  {
    return arcToken.getProcess();
  }
}
