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

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;

public abstract class ExecutionEvent
{
  private Engine             engine;
  private ExecutionEventType eventType;

  public ExecutionEvent (final Engine engine, final ExecutionEventType eventType)
  {
    this.engine    = engine;
    this.eventType = eventType;
  }

  /**
   * Returns the engine which generated this event
   *
   * @return The engine which generated this event
   */
  public Engine getEngine ()
  {
    return engine;
  }

  /**
   * Returns what type of event has occurred.
   *
   * @return The type of event
   */
  public ExecutionEventType getEventType ()
  {
    return eventType;
  }

  /**
   * Returns the process the event has occurred on.
   *
   * @return The process the event has occurred on.
   */
  public abstract GraphProcess getProcess ();

  /**
   * If this is a node token related event, this returns the related node token and null otherwise.
   *
   * @return If this is a node token related event, this returns the related node token and null otherwise.
   */
  public NodeToken getNodeToken ()
  {
    return null;
  }

  /**
   * If this is an arc token related event, this returns the related arc token and null otherwise.
   *
   * @return If this is an arc token related event, this returns the related arc token and null otherwise.
   */
  public ArcToken getArcToken ()
  {
    return null;
  }

  /**
   * If this is either a node token event for either {@link ExecutionEventType#NODE_TOKEN_SKIPPED} or
   * {@link ExecutionEventType#NODE_TOKEN_COMPLETED}, this will return the name of the arc (or arcs)
   * on which execution will be proceeding. If execution proceeded on multiple arcs with different
   * labels, this will return the first label.
   *
   * @return The arc name on which execution will proceed if this event is related
   *         to a node token skipped or node token completing event.
   */
  public String getExitArcsName ()
  {
    return null;
  }

  /**
   * If this is either a node token event for either {@link ExecutionEventType#NODE_TOKEN_SKIPPED} or
   * {@link ExecutionEventType#NODE_TOKEN_COMPLETED}, this will return the names of the arcs
   * on which execution will be proceeding.
   *
   * @return The arc names on which execution will proceed if this event is related
   *         to a node token skipped or node token completing event.
   */
  public String[] getExitArcsNames ()
  {
    return null;
  }

  public boolean isProcessEvent ()
  {
    return eventType.isProcessEvent();
  }

  public boolean isNodeTokenEvent ()
  {
    return eventType.isNodeTokenEvent();
  }

  public boolean isArcTokenEvent ()
  {
    return eventType.isArcTokenEvent();
  }
}
