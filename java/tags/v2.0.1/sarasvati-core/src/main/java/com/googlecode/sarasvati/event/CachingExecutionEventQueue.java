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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.googlecode.sarasvati.Engine;

public class CachingExecutionEventQueue extends DefaultExecutionEventQueue
{
  public static CachingExecutionEventQueue newArrayListInstance ()
  {
    return new CachingExecutionEventQueue( new ArrayList<DefaultExecutionEventQueue.RegisteredExecutionListener>() );
  }

  public static CachingExecutionEventQueue newCopyOnWriteListInstance ()
  {
    return new CachingExecutionEventQueue( new CopyOnWriteArrayList<DefaultExecutionEventQueue.RegisteredExecutionListener>() );
  }

  protected static final ListenerCache  listenerCache = new ListenerCache();

  CachingExecutionEventQueue (final List<DefaultExecutionEventQueue.RegisteredExecutionListener> listeners)
  {
    super( listeners );
  }

  @Override
  public synchronized void addListener (final Engine engine,
                                        final Class<? extends ExecutionListener> listenerClass,
                                        final ExecutionEventType... eventTypes)
  {
    ExecutionListener listener = listenerCache.getListener( listenerClass );
    addListener( listener, eventTypes );
  }

  public static ListenerCache getListenerCache ()
  {
    return listenerCache;
  }

  public void initFromPersisted (List<? extends PersistedExecutionListener> listeners)
  {
    if ( listeners != null )
    {
      for ( PersistedExecutionListener listener : listeners )
      {
        addListener( listenerCache.getListener( listener.getType() ), listener.getEventTypeMask() );
      }
    }
  }
}