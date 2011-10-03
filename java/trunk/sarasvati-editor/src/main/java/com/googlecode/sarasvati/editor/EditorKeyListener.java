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
package com.googlecode.sarasvati.editor;

import java.awt.event.KeyEvent;

public class EditorKeyListener
{
  private EditorMode previousMode = null;

  public void keyPressed (final KeyEvent event)
  {
    EditorMode mode = null;

    if ( event.getKeyChar() == 'a' )
    {
      mode = EditorMode.Move;
    }
    else if ( event.getKeyChar() == 's' )
    {
      mode = EditorMode.EditArcs;
    }
    else if ( event.getKeyChar() == 'd' )
    {
      mode = EditorMode.AddNode;
    }
    else if ( event.getKeyChar() == 'f' )
    {
      mode = EditorMode.AddExternal;
    }

    if ( mode != null )
    {
      GraphEditor editor = GraphEditor.getInstance();

      if ( previousMode == null )
      {
        previousMode = editor.getMode();
      }

      editor.setMode( mode );
    }
  }

  public void keyReleased (final KeyEvent event)
  {
    if ( previousMode != null )
    {
      GraphEditor.getInstance().setMode( previousMode );
      previousMode = null;
      event.consume();
    }
  }
}