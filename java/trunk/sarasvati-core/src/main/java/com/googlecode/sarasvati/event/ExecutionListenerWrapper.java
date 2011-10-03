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

import com.googlecode.sarasvati.Node;

/**
 * Useful for avoiding hibernate proxy behavior. If a node implements
 * ExecutionListener, it can return new ExecutionListenerWrapper( this )
 * from {@link Node#getAdaptor(Class)}, and the proxy will not break.
 *
 * @author Paul Lorenz
 */
public class ExecutionListenerWrapper implements ExecutionListener
{
  protected ExecutionListener listener;

  public ExecutionListenerWrapper (final ExecutionListener listener)
  {
    this.listener = listener;
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