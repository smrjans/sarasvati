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
package com.googlecode.sarasvati.editor.model;

import java.util.LinkedList;

public abstract class AbstractStateful<T>
{
  private final LinkedList<T> stateStack = new LinkedList<T>();
  private final Notifier<AbstractStateful<?>> notifier = new Notifier<AbstractStateful<?>>();

  public AbstractStateful (final T initialState)
  {
    pushState( initialState );
  }

  public void pushState (final T memberState)
  {
    stateStack.push( memberState );
    stateChanged();
  }

  public void popState ()
  {
    stateStack.pop();
    stateChanged();
  }

  public T getState ()
  {
    return stateStack.getFirst();
  }

  public void addListener (final ModelListener<? extends AbstractStateful<?>> nodeListener )
  {
    notifier.addListener( nodeListener );
  }

  protected void stateChanged ()
  {
    notifier.notify( this );
  }
}
