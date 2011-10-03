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
package com.googlecode.sarasvati.editor.command;

import java.awt.Point;

import com.googlecode.sarasvati.editor.model.EditorExternal;
import com.googlecode.sarasvati.editor.model.EditorScene;

public class DeleteExternalCommand extends AbstractCommand
{
  private final String action;
  private final EditorScene scene;
  private final Point location;
  private final EditorExternal external;

  public DeleteExternalCommand (final String action,
                                final EditorScene scene,
                                final EditorExternal external)
  {
    this.action = action;
    this.scene = scene;
    this.location = external.getOrigin();
    this.external = external;
  }

  @Override
  public void performAction ()
  {
    scene.removeNode( external );
    scene.getGraph().removeExternal( external );
  }

  @Override
  public void undoAction ()
  {
    external.setOrigin( new Point( location ) );
    scene.addNode( external );
    scene.getGraph().addExternal( external );
  }

  @Override
  public int getUndoOrder ()
  {
    return 10;
  }

  @Override
  public String getName ()
  {
    return action + " External";
  }
}