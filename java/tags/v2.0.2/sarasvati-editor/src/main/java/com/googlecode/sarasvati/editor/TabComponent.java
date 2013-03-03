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
package com.googlecode.sarasvati.editor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

public class TabComponent extends JPanel
{
  private static final long serialVersionUID = 1L;

  private final JLabel label;

  public TabComponent (final JTabbedPane pane, final String labelText)
  {
    setBorder( BorderFactory.createEmptyBorder( 2, 0, 0, 0 ) );
    label = new JLabel( labelText );
    label.setBorder( BorderFactory.createEmptyBorder( 0, 10, 0, 5 ) );

    setLayout( new BorderLayout() );
    setOpaque( true );
    add( label, BorderLayout.CENTER );

    JButton closeButton = new JButton ( "X" );
    closeButton.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) );
    closeButton.setFont( Font.decode( "sans bold 9" ) );

    closeButton.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed (final ActionEvent e)
      {
        GraphEditor.getInstance().closeTab( pane.indexOfTabComponent( TabComponent.this ) );
      }
    });

    add( closeButton, BorderLayout.EAST );
  }

  public void setLabelText (final String text)
  {
    label.setText( text );
  }

  @Override
  public void updateUI ()
  {
    // does nothing
  }
}