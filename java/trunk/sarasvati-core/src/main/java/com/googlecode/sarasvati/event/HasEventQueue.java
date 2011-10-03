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

    Copyright 2009 Paul Lorenz
*/
package com.googlecode.sarasvati.event;

/**
 * Interface for types which have an event queue (in other words,
 * types which manage an associated set of listeners).
 *
 * @author Paul Lorenz
 */
public interface HasEventQueue
{
  /**
   * Returns the event queue which manages the set of listeners for the instance
   * and handles firing events to those listeners.
   *
   * @return The event queue for this instance.
   */
  ExecutionEventQueue getEventQueue ();
}
