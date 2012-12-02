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

import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.editor.model.EditorGraphMember;
import com.googlecode.sarasvati.editor.model.EditorScene;

public class MoveGraphMemberCommand extends AbstractCommand
{
  private EditorScene scene;
  private EditorGraphMember<?> member;
  private Point startLocation;
  private Point endLocation;

  public MoveGraphMemberCommand (final EditorScene scene,
                                 final EditorGraphMember<?> member,
                                 final Point startLocation,
                                 final Point endLocation)
  {
    this.scene = scene;
    this.member = member;
    this.startLocation = new Point( startLocation );
    this.endLocation = new Point( endLocation );
  }

  @Override
  public void performAction ()
  {
    member.setOrigin( new Point( endLocation ) );
    Widget widget = scene.findWidget( member );
    widget.setPreferredLocation( new Point( endLocation ) );
    widget.revalidate();
  }

  @Override
  public void undoAction ()
  {
    member.setOrigin( new Point( startLocation ) );
    Widget widget = scene.findWidget( member );
    widget.setPreferredLocation( new Point( startLocation ) );
    widget.revalidate();
  }

  @Override
  public String getName ()
  {
    return "Move " + (member.isExternal() ? "External" : "Node");
  }
}