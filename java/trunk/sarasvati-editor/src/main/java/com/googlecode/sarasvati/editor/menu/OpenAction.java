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
package com.googlecode.sarasvati.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import com.googlecode.sarasvati.editor.GraphEditor;
import com.googlecode.sarasvati.editor.model.Library;

public class OpenAction extends AbstractAction
{
  private static final long serialVersionUID = 1L;

  public OpenAction ()
  {
    super( "Open" );

    putValue( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK ) );
    putValue( Action.MNEMONIC_KEY, KeyEvent.VK_O );
  }

  @Override
  public void actionPerformed (final ActionEvent e)
  {
    GraphEditor editor = GraphEditor.getInstance();

    File path = Library.getInstance().getBasePath();

    if ( path == null )
    {
      path = editor.getLastFile();
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory( path );
    fileChooser.setFileFilter( new FileFilter()
    {
      @Override
      public String getDescription ()
      {
        return "Process Definitions";
      }

      @Override
      public boolean accept (final File f)
      {
        return f.isDirectory() || f.getName().endsWith( ".wf.xml" );
      }
    });

    int retVal = fileChooser.showOpenDialog( editor.getMainWindow() );

    if ( retVal == JFileChooser.APPROVE_OPTION )
    {
      editor.setLastFile( fileChooser.getSelectedFile() );
      GraphEditor.getInstance().openProcessDefinition( fileChooser.getSelectedFile() );
    }
  }
}