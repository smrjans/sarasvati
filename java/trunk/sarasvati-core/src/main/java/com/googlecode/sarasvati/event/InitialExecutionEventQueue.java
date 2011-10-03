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

import com.googlecode.sarasvati.Engine;

/**
 * Class used to initialize the actual execution event queue.
 *
 * @author Paul Lorenz
 */
public abstract class InitialExecutionEventQueue implements ExecutionEventQueue
{

  @Override
  public void addListener (final Engine engine,
                           final Class<? extends ExecutionListener> listenerClass,
                           final ExecutionEventType... eventTypes)
  {
    init().addListener( engine, listenerClass, eventTypes );
  }

  @Override
  public EventActions fireEvent (final ExecutionEvent event)
  {
    return init().fireEvent( event );
  }

  @Override
  public void removeListener (final Engine engine,
                              final Class<? extends ExecutionListener> listenerClass,
                              final ExecutionEventType... eventTypes)
  {
    init().removeListener( engine, listenerClass, eventTypes );
  }

  protected abstract ExecutionEventQueue init ();
}
