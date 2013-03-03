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
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.googlecode.sarasvati.editor.GraphEditor;
import com.googlecode.sarasvati.editor.model.EditorPreferences;

public class ExportPreferencesAction extends AbstractAction
{
  private static final long serialVersionUID = 1L;

  public ExportPreferencesAction ()
  {
    super( "Export Preferences" );
    putValue( Action.MNEMONIC_KEY, KeyEvent.VK_E );
  }

  @Override
  public void actionPerformed (final ActionEvent event)
  {
    GraphEditor editor = GraphEditor.getInstance();

    JFileChooser fileChooser = new JFileChooser();

    fileChooser.setFileFilter( new FileFilter()
    {
      @Override
      public String getDescription ()
      {
        return "XML Files";
      }

      @Override
      public boolean accept (final File f)
      {
        return f.isDirectory() || f.getName().endsWith( ".xml" );
      }
    });

    int retVal = fileChooser.showSaveDialog( editor.getMainWindow() );

    if ( retVal == JFileChooser.APPROVE_OPTION )
    {
      EditorPreferences.getInstance().exportPreferences( fileChooser.getSelectedFile() );
    }
  }
}