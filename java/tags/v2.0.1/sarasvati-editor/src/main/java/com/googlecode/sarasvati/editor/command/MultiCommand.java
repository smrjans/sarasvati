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

import java.util.List;
import java.util.ListIterator;

import com.googlecode.sarasvati.editor.model.EditorScene;

public class MultiCommand extends AbstractCommand
{
  private final String name;
  private final EditorScene scene;
  private final List<Command> commands;

  public MultiCommand (final String name,
                       final EditorScene scene,
                       final List<Command> commands)
  {
    this.name = name;
    this.scene = scene;
    this.commands = commands;
  }

  @Override
  public void performAction ()
  {
    for ( Command command : commands )
    {
      command.performAction();
    }
  }

  @Override
  public void undoAction ()
  {
    ListIterator<Command> iter = commands.listIterator( commands.size() );
    while (iter.hasPrevious() )
    {
      iter.previous().undoAction();
    }
  }

  public EditorScene getScene ()
  {
    return scene;
  }

  @Override
  public String getName ()
  {
    return name;
  }
}