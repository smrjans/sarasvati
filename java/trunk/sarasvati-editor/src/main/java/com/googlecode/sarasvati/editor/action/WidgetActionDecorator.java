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

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public class WidgetActionDecorator implements WidgetAction
{
  protected WidgetAction action;

  public WidgetActionDecorator (final WidgetAction action)
  {
    this.action = action;
  }

  public WidgetAction getAction ()
  {
    return action;
  }

  @Override
  public State dragEnter( final Widget arg0, final WidgetDropTargetDragEvent arg1 )
  {
    return action.dragEnter( arg0, arg1 );
  }

  @Override
  public State dragExit( final Widget arg0, final WidgetDropTargetEvent arg1 )
  {
    return action.dragExit( arg0, arg1 );
  }

  @Override
  public State dragOver( final Widget arg0, final WidgetDropTargetDragEvent arg1 )
  {
    return action.dragOver( arg0, arg1 );
  }

  @Override
  public State drop( final Widget arg0, final WidgetDropTargetDropEvent arg1 )
  {
    return action.drop( arg0, arg1 );
  }

  @Override
  public State dropActionChanged( final Widget arg0, final WidgetDropTargetDragEvent arg1 )
  {
    return action.dropActionChanged( arg0, arg1 );
  }

  @Override
  public State focusGained( final Widget arg0, final WidgetFocusEvent arg1 )
  {
    return action.focusGained( arg0, arg1 );
  }

  @Override
  public State focusLost( final Widget arg0, final WidgetFocusEvent arg1 )
  {
    return action.focusLost( arg0, arg1 );
  }

  @Override
  public State keyPressed( final Widget arg0, final WidgetKeyEvent arg1 )
  {
    return action.keyPressed( arg0, arg1 );
  }

  @Override
  public State keyReleased( final Widget arg0, final WidgetKeyEvent arg1 )
  {
    return action.keyReleased( arg0, arg1 );
  }

  @Override
  public State keyTyped( final Widget arg0, final WidgetKeyEvent arg1 )
  {
    return action.keyTyped( arg0, arg1 );
  }

  @Override
  public State mouseClicked( final Widget arg0, final WidgetMouseEvent arg1 )
  {
    return action.mouseClicked( arg0, arg1 );
  }

  @Override
  public State mouseDragged( final Widget arg0, final WidgetMouseEvent arg1 )
  {
    return action.mouseDragged( arg0, arg1 );
  }

  @Override
  public State mouseEntered( final Widget arg0, final WidgetMouseEvent arg1 )
  {
    return action.mouseEntered( arg0, arg1 );
  }

  @Override
  public State mouseExited( final Widget arg0, final WidgetMouseEvent arg1 )
  {
    return action.mouseExited( arg0, arg1 );
  }

  @Override
  public State mouseMoved( final Widget arg0, final WidgetMouseEvent arg1 )
  {
    return action.mouseMoved( arg0, arg1 );
  }

  @Override
  public State mousePressed( final Widget arg0, final WidgetMouseEvent arg1 )
  {
    return action.mousePressed( arg0, arg1 );
  }

  @Override
  public State mouseReleased( final Widget arg0, final WidgetMouseEvent arg1 )
  {
    return action.mouseReleased( arg0, arg1 );
  }

  @Override
  public State mouseWheelMoved( final Widget arg0, final WidgetMouseWheelEvent arg1 )
  {
    return action.mouseWheelMoved( arg0, arg1 );
  }
}
