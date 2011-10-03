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
package com.googlecode.sarasvati.visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.hibernate.Session;

import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.hib.HibGraphProcess;
import com.googlecode.sarasvati.visual.process.SarasvatiProcessScene;

/**
 * Base class for a standalone process visualizer. Will show a list of
 * available processes in the left pane, and a visualization of the selected
 * process in the main pane.
 * <br>
 * To manage database connectivity, users should override
 * {@link AbstractProcessVisualizer#init()} and
 * {@link AbstractProcessVisualizer#getSession()}.
 * <br>
 * For custom rendering of nodes, subclasses may also override
 * {@link AbstractProcessVisualizer#getWidgetFactory()}.
 *
 * @author Paul Lorenz
 */
public abstract class AbstractProcessVisualizer
{
  protected Session               session        = null;
  protected HibGraphProcess       currentProcess = null;
  protected SarasvatiProcessScene scene = new SarasvatiProcessScene( null, getWidgetFactory() );

  final JScrollPane scrollPane = new JScrollPane();

  /**
   * Place to perform hibernate initialization
   *
   * @throws Exception
   */
  public abstract void init () throws Exception;

  /**
   * Should return an open hibernate session.
   *
   * @return Open hibernate session
   */
  public abstract Session getSession ();

  public ProcessLookAndFeel getWidgetFactory ()
  {
    return DefaultProcessLookAndFeel.INSTANCE;
  }

  @SuppressWarnings("unchecked")
  public synchronized void run () throws Exception
  {
    init ();

    session = getSession();
    final HibEngine engine = new HibEngine( session );

    List<GraphProcess> process = engine.getSession().createQuery( "from HibGraphProcess order by graph, createDate" ).list();

    JFrame frame = new JFrame( "Workflow Visualizer" );
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    frame.setMinimumSize(  new Dimension( 800, 600 ) );

    JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
    frame.getContentPane().add( splitPane );

    final DefaultListModel listModel = new DefaultListModel();
    for ( GraphProcess p : process )
    {
      listModel.addElement( p );
    }

    ListCellRenderer cellRenderer = new DefaultListCellRenderer()
    {
      private static final long serialVersionUID = 1L;

      @Override
      public Component getListCellRendererComponent( final JList list, final Object value,
                                                     final int index, final boolean isSelected,
                                                     final boolean cellHasFocus )
      {
        super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );

        HibGraphProcess p = (HibGraphProcess)value;

        setText( p.getGraph().getName() + "-" + p.getId() + "  " );
        return this;
      }
    };

    final JList graphList = new JList( listModel );
    graphList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    graphList.setCellRenderer( cellRenderer );

    JScrollPane listScrollPane = new JScrollPane(graphList );
    listScrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
    listScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

    splitPane.add( listScrollPane );

    scrollPane.setViewportView( scene.createView() );
    scrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
    scrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );

    splitPane.add( scrollPane );
    scrollPane.setBackground( Color.white );

    graphList.addListSelectionListener( new ListSelectionListener()
    {
      @Override
      public void valueChanged( final ListSelectionEvent e )
      {
        if ( e.getValueIsAdjusting() )
        {
          return;
        }

        final HibGraphProcess graphProcess = (HibGraphProcess)graphList.getSelectedValue();

        if ( graphProcess != null && graphProcess.equals( currentProcess ) )
        {
          return;
        }

        if ( graphProcess != null )
        {
          session.clear();
          session.refresh( graphProcess );
        }

        setProcess( graphProcess );
      }
    } );

    frame.setVisible( true );

    new Thread()
    {
      @Override public void run ()
      {
        while ( true )
        {
          try
          {
            synchronized( this )
            {
              wait( 1000 );
            }

            SwingUtilities.invokeLater( new Runnable()
            {
              @Override public void run()
              {
                if ( currentProcess != null )
                {
                  Integer currentVersion = currentProcess.getVersion();
                  session.clear();
                  session.refresh( currentProcess );

                  if ( currentVersion != null &&
                       currentProcess.getVersion() != null &&
                       currentVersion.intValue() !=  currentProcess.getVersion().intValue() )
                  {
                    setProcess( currentProcess );
                  }
                }

                List<GraphProcess> processList = engine.getSession().createQuery( "from HibGraphProcess order by graph, createDate" ).list();
                for ( GraphProcess p : processList )
                {
                  if ( !listModel.contains( p ) )
                  {
                    listModel.addElement( p );
                  }
                }
              }
            });
          }
          catch( InterruptedException ie )
          {
            return;
          }
        }
      }
    }.start();
  }

  public synchronized void setProcess (final HibGraphProcess graphProcess)
  {
    currentProcess = graphProcess;
    scene = new SarasvatiProcessScene( currentProcess, getWidgetFactory() );

    scrollPane.setViewportView( scene.createView() );
    scene.repaint();
  }
}