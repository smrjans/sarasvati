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
package com.googlecode.sarasvati.editor.action;

import java.awt.event.MouseEvent;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.editor.command.CommandStack;
import com.googlecode.sarasvati.editor.model.EditorScene;

public class SceneAddNodeAction extends WidgetAction.Adapter
{
  public static final SceneAddNodeAction INSTANCE = new SceneAddNodeAction();

  private static boolean enabled = false;

  public static void setEnabled (final boolean enabled)
  {
    SceneAddNodeAction.enabled = enabled;
  }

  @Override
  public State mousePressed (final Widget widget, final WidgetMouseEvent event)
  {
    if( !enabled )
    {
      return State.REJECTED;
    }

    if ( event.getButton() == MouseEvent.BUTTON1 )
    {
      CommandStack.addNode( (EditorScene)widget.getScene(), event.getPoint() );
      return State.CONSUMED;
    }

    return State.REJECTED;
  }
}
