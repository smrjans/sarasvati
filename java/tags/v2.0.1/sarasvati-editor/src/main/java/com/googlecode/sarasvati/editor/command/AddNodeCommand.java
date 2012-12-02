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

import com.googlecode.sarasvati.editor.model.EditorNode;
import com.googlecode.sarasvati.editor.model.EditorScene;

public class AddNodeCommand extends AbstractCommand
{
  private final EditorScene scene;
  private final Point location;
  private final EditorNode node;

  public AddNodeCommand (final EditorScene scene,
                         final Point location,
                         final EditorNode node)
  {
    this.scene = scene;
    this.location = location;
    this.node = node;
  }

  @Override
  public void performAction ()
  {
    node.setOrigin( new Point( location ) );
    scene.addNode( node );
    scene.getGraph().addNode( node );
  }

  @Override
  public void undoAction ()
  {
    scene.removeNode( node );
    scene.getGraph().removeNode( node );
  }

  @Override
  public String getName ()
  {
    return "Add Node";
  }
}