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

import java.util.concurrent.ConcurrentHashMap;

import com.googlecode.sarasvati.util.SvUtil;

public class ListenerCache
{
  protected ConcurrentHashMap<String, ExecutionListener> listenerCache = new ConcurrentHashMap<String, ExecutionListener>();

  public ExecutionListener getListener (final String type)
  {
    ExecutionListener listener = listenerCache.get( type );

    if ( listener == null )
    {
      listener = (ExecutionListener)SvUtil.newInstanceOf( type, "ExecutionListener" );
      listenerCache.put( type, listener );
    }

    return listener;
  }

  public ExecutionListener getListener (final Class<? extends ExecutionListener> listenerClass)
  {
    ExecutionListener listener = listenerCache.get( listenerClass.getName() );

    if ( listener == null )
    {
      listener = SvUtil.newInstanceOf( listenerClass, "ExecutionListener" );
      listenerCache.put( listenerClass.getName(), listener );
    }

    return listener;
  }
}