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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import com.googlecode.sarasvati.editor.GraphEditor;
import com.googlecode.sarasvati.editor.dialog.DialogFactory;
import com.googlecode.sarasvati.editor.model.Library;

public class ConvertLibraryAction extends AbstractAction
{
  private static final long serialVersionUID = 1L;

  public ConvertLibraryAction ()
  {
    super( "Convert Library to Newest Format" );
    putValue( Action.MNEMONIC_KEY, KeyEvent.VK_C );
  }

  @Override
  public void actionPerformed (final ActionEvent event)
  {
    if ( JOptionPane.OK_OPTION !=
         JOptionPane.showConfirmDialog( GraphEditor.getInstance().getMainWindow(),
                                        "This will attempt to convert all the process definitions in " +
                                        "your library to the newest version.\n" +
                                        "This process is not guaranteed to be successful or leave your process definitions in a usable state.\n" +
                                        "It is recommended that you run this on process definitions that can be reverted back to a source controlled copy." +
                                        "\nDo you wish to continue?",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE ) )
    {
      return;
    }

    try
    {
      Library.getInstance().saveAllEntriesInLibraryInNewestFormat();
      DialogFactory.showInfo( "Converted " + Library.getInstance().getEntries().size() + " process definitions." );
    }
    catch ( Exception e )
    {
      e.printStackTrace();
      DialogFactory.showError( "Error during conversion process: " + e.getMessage() );
    }
  }
}