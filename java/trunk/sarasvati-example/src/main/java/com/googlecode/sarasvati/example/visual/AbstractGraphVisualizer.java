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
package com.googlecode.sarasvati.example.visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.hibernate.Session;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.adapter.Function;
import com.googlecode.sarasvati.adapter.NodeAdapterManager;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.visual.DefaultGraphLookAndFeel;
import com.googlecode.sarasvati.visual.GraphLookAndFeel;
import com.googlecode.sarasvati.visual.graph.SarasvatiGraphScene;
import com.googlecode.sarasvati.visual.icon.OvalNodeIcon;
import com.googlecode.sarasvati.visual.icon.RectangularNodeIcon;
import com.googlecode.sarasvati.visual.icon.SmallCircleNodeIcon;

/**
 * Base class for a standalone graph visualizer. Will show a list of
 * loaded graphs in the left pane, and a visualization of the selected
 * graph in the main pane.
 *
 * <br>
 *
 * To manage database connectivity, users should override
 * {@link AbstractGraphVisualizer#init()} and
 * {@link AbstractGraphVisualizer#getSession()}.
 *
 * <br>
 * For custom rendering of nodes, subclasses may also override
 * {@link AbstractGraphVisualizer#getWidgetFactory()}.

 * @author Paul Lorenz
 */
public abstract class AbstractGraphVisualizer
{
  protected Graph currentGraph = null;
  protected SarasvatiGraphScene scene = new SarasvatiGraphScene( null, getWidgetFactory() );

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

  public GraphLookAndFeel getWidgetFactory ()
  {
    return DefaultGraphLookAndFeel.INSTANCE;
  }

  public void run () throws Exception
  {
    init ();

    NodeAdapterManager.registerFactory( Component.class,
        new Function<Component, Node>()
        {
          @Override public Component apply (final Node node)
          {
            if( "end".equalsIgnoreCase( node.getType() )){
              return new JLabel( new SmallCircleNodeIcon());
            }
            return "task".equalsIgnoreCase( node.getType() ) ?
              new JLabel( new RectangularNodeIcon( node, null ) ) :
              new JLabel( new OvalNodeIcon( node, null ) );
          }
        });

    NodeAdapterManager.registerFactory( String.class,
        new Function<String,Node>()
        {
          @Override public String apply (final Node node)
          {
            return node.getName();
          }
        });

    Session session = getSession();
    HibEngine engine = new HibEngine( session );

    JFrame frame = new JFrame( "Workflow Visualizer" );
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    frame.setMinimumSize(  new Dimension( 800, 600 ) );

    JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
    frame.getContentPane().add( splitPane );

    DefaultListModel<Graph> listModel = new DefaultListModel<Graph>();
    for ( Graph g : engine.getRepository().getGraphs() )
    {
      listModel.addElement( g );
    }

    ListCellRenderer<Object> cellRenderer = new DefaultListCellRenderer()
    {
      private static final long serialVersionUID = 1L;

      @Override
      public Component getListCellRendererComponent( final JList<?> list, final Object value,
                                                     final int index, final boolean isSelected,
                                                     final boolean cellHasFocus )
      {
        super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );

        Graph g = (Graph)value;

        setText( g.getName() + "." + g.getVersion() + "  " );
        return this;
      }
    };

    final JList<Graph> graphList = new JList<Graph>( listModel );
    graphList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    graphList.setCellRenderer( cellRenderer );

    JScrollPane listScrollPane = new JScrollPane(graphList );
    listScrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
    listScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

    splitPane.add( listScrollPane );

    final JScrollPane scrollPane = new JScrollPane();
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

        final Graph g = graphList.getSelectedValue();

        if ( g != null && g.equals( currentGraph ) )
        {
          return;
        }

        currentGraph = g;
        scene = new SarasvatiGraphScene( currentGraph, getWidgetFactory() );

        scrollPane.setViewportView( scene.createView() );
        scrollPane.repaint();
      }
    });

    frame.setVisible( true );
  }
}