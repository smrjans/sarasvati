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
import com.googlecode.sarasvati.editor.model.EditorGraphMember;
import com.googlecode.sarasvati.editor.model.EditorScene;

public class ChangeArcCommand extends AbstractCommand
{
  private final EditorScene scene;
  private final EditorArc arc;

  private final boolean isSource;
  private final EditorGraphMember<?> oldNode;
  private final EditorGraphMember<?> newNode;

  public ChangeArcCommand (final EditorScene scene,
                           final EditorArc arc,
                           final boolean isSource,
                           final EditorGraphMember<?> newNode)
  {
    this.scene = scene;
    this.arc   = arc;
    this.isSource = isSource;
    this.oldNode = isSource ? arc.getStart() : arc.getEnd();
    this.newNode = newNode;
  }

  @Override
  public void performAction ()
  {
    if ( isSource )
    {
      arc.setStart( newNode );
      scene.setEdgeSource( arc, arc.getStart() );
    }
    else
    {
      arc.setEnd( newNode );
      scene.setEdgeTarget( arc, arc.getEnd() );
    }
  }

  @Override
  public void undoAction ()
  {
    if ( isSource )
    {
      arc.setStart( oldNode );
      scene.setEdgeSource( arc, arc.getStart() );
    }
    else
    {
      arc.setEnd( oldNode );
      scene.setEdgeTarget( arc, arc.getEnd() );
    }
  }

  @Override
  public String getName ()
  {
    return "Change Connection";
  }
}