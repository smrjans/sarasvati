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
package com.googlecode.sarasvati.editor.dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.border.BevelBorder;

import com.googlecode.sarasvati.editor.panel.ContentAssistPanel;

public class ContentAssistDialog extends JDialog
{
  private static final long serialVersionUID = 1L;

  public ContentAssistDialog (final JFrame frame, final ContentAssistPanel panel)
  {
    super( frame, "Content Assist", false );

    setUndecorated( true );

    panel.setup( this );
    panel.setBorder( new BevelBorder( BevelBorder.RAISED ) );
    getContentPane().add( panel );
    //setResizable( false );
    pack();
  }
}
