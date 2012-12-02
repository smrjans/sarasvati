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

/**
 * Listeners may be registered for events happening on a single {@link GraphProcess} or
 * all processes. Listeners may in some cases influence how execution proceeds by returning
 * an {@link EventActions} object.
 * <b>
 * Listeners may be stored a database by classname. They should therefore have a public
 * default (no-args) constructor.
 *
 * @see Engine#addExecutionListener(Class, ExecutionEventType...)
 * @see Engine#addExecutionListener(GraphProcess, Class, ExecutionEventType...)
 * @see EventActionType
 *
 * @author Paul Lorenz
 */
public interface ExecutionListener
{
  /**
   * Invoked by the {@link Engine} when an event of a type that this
   * listener has register for occurs. May return an {@link EventActions}
   * defining the actions to be performed or null.
   *
   * @param event The event which has just occurred.
   *
   * @return EventActions The set of requested {@link EventActionType}s or null.
   */
  EventActions notify (ExecutionEvent event);
}
