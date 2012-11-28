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
package com.googlecode.sarasvati.editor.command;

import com.googlecode.sarasvati.editor.model.ArcState;
import com.googlecode.sarasvati.editor.model.EditorArc;

public class EditArcCommand extends AbstractCommand
{
  private final EditorArc   arc;
  private final ArcState    newState;

  public EditArcCommand (final EditorArc arc,
                         final ArcState newState)
  {
    this.arc = arc;
    this.newState = newState;
  }

  @Override
  public void performAction ()
  {
    arc.pushState( newState );
  }

  @Override
  public void undoAction ()
  {
    arc.popState();
  }

  @Override
  public String getName ()
  {
    return "Edit Arc";
  }
}