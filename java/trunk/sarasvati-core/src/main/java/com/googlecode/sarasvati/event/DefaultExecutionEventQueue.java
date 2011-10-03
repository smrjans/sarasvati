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
package com.googlecode.sarasvati.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.SarasvatiException;

public class DefaultExecutionEventQueue implements ExecutionEventQueue
{
  private List<RegisteredExecutionListener> listeners;

  public static DefaultExecutionEventQueue newArrayListInstance ()
  {
    return new DefaultExecutionEventQueue( new ArrayList<RegisteredExecutionListener>() );
  }

  public static DefaultExecutionEventQueue newCopyOnWriteListInstance ()
  {
    return new DefaultExecutionEventQueue( new CopyOnWriteArrayList<RegisteredExecutionListener>() );
  }

  DefaultExecutionEventQueue(final List<RegisteredExecutionListener> listeners)
  {
    this.listeners = listeners;
  }

  /**
   * @see ExecutionEventQueue#addListener(Engine, Class, ExecutionEventType...)
   */
  @Override
  public synchronized void addListener (final Engine engine,
                                        final Class<? extends ExecutionListener> listenerClass,
                                        final ExecutionEventType...eventTypes)
  {
    if ( listenerClass == null)
    {
      return;
    }

    ExecutionListener listener = null;

    try
    {
      listener = listenerClass.newInstance();
    }
    catch ( InstantiationException e )
    {
      throw new SarasvatiException( "ExecutionListeners must have a default public constructor. " +
                                    "They may not be non-static inner classes. " +
                                    "In other words, you must be able create new ones using listenerClass.newInstance()",
                                    e );
    }
    catch ( IllegalAccessException e )
    {
      throw new SarasvatiException( "ExecutionListeners must have a default public constructor. " +
                                    "They may not be non-static inner classes. " +
                                    "In other words, you must be able create new ones using listenerClass.newInstance()",
                                    e );
    }
    addListener( listener, eventTypes );
  }

  public synchronized void addListener (final ExecutionListener listener,
                                        final ExecutionEventType...eventTypes)
  {
    if ( listener == null )
    {
      return;
    }

    addListener( listener, ExecutionEventType.toMask( eventTypes ) );
  }

  public synchronized void addListener (final ExecutionListener listener,
                                        final int eventTypeMask)
  {
    if ( eventTypeMask == 0 || listener == null )
    {
      return;
    }

    // If we already have a visitor of the given type, reuse it rather than creating a new one
    for ( RegisteredExecutionListener registeredListener : listeners )
    {
      if ( registeredListener.getListener().getClass() == listener.getClass() )
      {
        registeredListener.addEventTypesMask( eventTypeMask );
        return; // INNER RETURN
      }
    }

    listeners.add( new RegisteredExecutionListener( listener, eventTypeMask ) );
  }

  /**
   * @see ExecutionEventQueue#removeListener(Engine, Class, ExecutionEventType...)
   */
  @Override
  public synchronized void removeListener (final Engine engine,
                                           final Class<? extends ExecutionListener> listener,
                                           final ExecutionEventType... eventTypes)
  {
    if ( listener == null )
    {
      return;
    }

    List<RegisteredExecutionListener> toRemove = new ArrayList<RegisteredExecutionListener>();

    for ( RegisteredExecutionListener wrapper : listeners )
    {
      if ( listener == wrapper.listener.getClass() )
      {
        if ( eventTypes == null || eventTypes.length == 0 )
        {
          toRemove.add( wrapper );
        }
        else
        {
          for ( ExecutionEventType eventType : eventTypes )
          {
            wrapper.removeEventType( eventType );
          }

          if ( wrapper.isUnregisteredForAllEventTypes() )
          {
            toRemove.add( wrapper );
          }
        }
      }
    }

    listeners.removeAll( toRemove );
  }

  /**
   * @see com.googlecode.sarasvati.event.ExecutionEventQueue#fireEvent(com.googlecode.sarasvati.event.ExecutionEvent)
   */
  @Override
  public EventActions fireEvent (final ExecutionEvent event)
  {
    EventActions eventActions = new EventActions();
    for (RegisteredExecutionListener wrapper : listeners )
    {
      if ( wrapper.isRegisteredForEventType( event.getEventType() ) )
      {
        eventActions.compose( wrapper.notify( event ) );
      }
    }
    return eventActions;
  }

  public static class RegisteredExecutionListener implements ExecutionListener
  {
    protected int eventTypeMask;
    protected ExecutionListener listener;

    public RegisteredExecutionListener (final ExecutionListener listener, final int eventTypeMask)
    {
      this.listener = listener;
      this.eventTypeMask = eventTypeMask;
    }

    public void addEventTypesMask (final int mask)
    {
      eventTypeMask |= mask;
    }

    public void removeEventType (final ExecutionEventType eventType)
    {
      if ( eventType != null )
      {
        eventTypeMask &= ExecutionEventType.invertMask( eventType.getEventType() );
      }
    }

    public boolean isRegisteredForEventType (final ExecutionEventType eventType)
    {
      return (eventType.getEventType() & eventTypeMask) != 0;
    }

    public boolean isUnregisteredForAllEventTypes ()
    {
      return eventTypeMask == 0;
    }

    public ExecutionListener getListener ()
    {
      return listener;
    }

    @Override
    public EventActions notify (final ExecutionEvent event)
    {
      return listener.notify( event );
    }
  }
}