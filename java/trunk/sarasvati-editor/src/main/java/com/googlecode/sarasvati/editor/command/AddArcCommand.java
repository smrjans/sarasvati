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

import com.googlecode.sarasvati.editor.model.EditorArc;
import com.googlecode.sarasvati.editor.model.EditorScene;

public class AddArcCommand extends AbstractCommand
{
  private EditorScene scene;
  private EditorArc arc;

  public AddArcCommand (final EditorScene scene, final EditorArc arc)
  {
    this.scene = scene;
    this.arc   = arc;
  }

  @Override
  public void performAction ()
  {
    scene.getGraph().addArc( arc );
    scene.addEdge( arc );
    scene.setEdgeSource( arc, arc.getStart() );
    scene.setEdgeTarget( arc, arc.getEnd() );
  }

  @Override
  public void undoAction ()
  {
    scene.getGraph().removeArc( arc );
    scene.removeEdge( arc );
  }

  @Override
  public String getName ()
  {
    return "Add Connection";
  }
}