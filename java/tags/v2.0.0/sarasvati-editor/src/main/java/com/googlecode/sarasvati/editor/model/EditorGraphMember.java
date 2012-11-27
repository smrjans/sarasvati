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
package com.googlecode.sarasvati.editor.model;

import java.awt.Point;

public abstract class EditorGraphMember<T extends GraphMemberState> extends AbstractStateful<T>
{
  protected Point  origin = new Point();
  protected boolean selected = false;

  public EditorGraphMember (final T initialState)
  {
    super( initialState );
  }

  public String getName ()
  {
    return getState().getName();
  }

  public int getX ()
  {
    return origin.x;
  }

  public void setX (final int x)
  {
    this.origin.x = x;
  }

  public int getY ()
  {
    return origin.y;
  }

  public void setY (final int y)
  {
    this.origin.y = y;
  }

  public Point getOrigin ()
  {
    return origin;
  }

  public void setOrigin (final Point origin)
  {
    this.origin = origin;
  }

  public boolean isExternal ()
  {
    return false;
  }

  public boolean isSelected ()
  {
    return selected;
  }

  public void setSelected (final boolean selected)
  {
    this.selected = selected;
    stateChanged();
  }

  public EditorExternal asExternal ()
  {
    return null;
  }

  public EditorNode asNode ()
  {
    return null;
  }

  @Override
  public String toString ()
  {
    return "[" + getClass().getSimpleName() + ": " + getName() + "]";
  }
}