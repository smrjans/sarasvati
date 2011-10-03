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

/**
 * Enumerates the requested action types from execution event listeners.
 *
 * @author Paul Lorenz
 */
public enum EventActionType
{
  /**
   * Request that node execution be done by some external logic at a later point.
   * Only has any effect if returned from the {@link ExecutionEventType#NODE_TOKEN_ACCEPTED}
   * event type.
   */
  DELAY_NODE_EXECUTION( 1 ),

  /**
   * Request that {@link Engine#finalizeComplete(com.googlecode.sarasvati.GraphProcess)} be done by some external
   * logic at a later point. Only has any effect if returned from the {@link ExecutionEventType#PROCESS_PENDING_COMPLETE}
   * event type.
   */
  DELAY_PROCESS_FINALIZE_COMPLETE( 2 ),

  /**
   * Request that {@link Engine#finalizeCancel(com.googlecode.sarasvati.GraphProcess)} be done by some external
   * logic at a later point. Only has any effect if returned from the {@link ExecutionEventType#PROCESS_PENDING_CANCEL}
   * event type.
   */
  DELAY_PROCESS_FINALIZE_CANCEL( 4 );

  private int mask;

  private EventActionType (final int mask)
  {
    this.mask = mask;
  }

  int getMask ()
  {
    return mask;
  }
}
