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
package com.googlecode.sarasvati.visual.process;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

import org.netbeans.api.visual.layout.LayoutFactory.ConnectionWidgetLayoutAlignment;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.visual.ProcessLookAndFeel;
import com.googlecode.sarasvati.visual.common.GraphSceneImpl;
import com.googlecode.sarasvati.visual.common.NodeDrawConfig;

public class SarasvatiProcessScene extends GraphSceneImpl<ProcessTreeNode, ProcessTreeArc>
{
  protected static final Font ARC_LABEL_FONT = Font.decode( "serif bold 11" );
  protected ProcessLookAndFeel lookAndFeel;

  public SarasvatiProcessScene (final GraphProcess process, final ProcessLookAndFeel lookAndFeel)
  {
    this.lookAndFeel = lookAndFeel;

    if ( process != null )
    {
      ProcessTree processTree = new ProcessTree( process, lookAndFeel );
      Iterable<ProcessTreeNode> nodes = processTree.getProcessTreeNodes();

      for ( ProcessTreeNode node : nodes )
      {
        Widget widget = addNode( node );

        Rectangle bounds = widget.getPreferredBounds();
        Point origin = new Point( node.getOriginX( bounds.width ), node.getOriginY( bounds.height) );

        widget.setPreferredLocation( origin );
        widget.resolveBounds( origin, null );
      }

      for ( ProcessTreeNode node : nodes )
      {
        for ( ProcessTreeArc ptArc : node.getChildren() )
        {
          Arc arc = ptArc.getArc();
          if ( lookAndFeel.drawSelfArcs(arc) || !ptArc.getParent().equals( ptArc.getChild() ) )
          {
            ConnectionWidget w = (ConnectionWidget)addEdge( ptArc );
            setEdgeSource( ptArc, ptArc.getParent() );
            setEdgeTarget( ptArc, ptArc.getChild() );

            ArcToken token = ptArc.getToken();
            if ( token != null )
            {
              w.setStroke( new BasicStroke( 3 ) );
              if ( token.getExecutionType().isBacktracked() )
              {
                w.setLineColor( NodeDrawConfig.NODE_BG_BACKTRACKED );
              }
              else if ( token.isComplete() )
              {
                w.setLineColor( NodeDrawConfig.NODE_BG_COMPLETED );
              }
              else
              {
                w.setLineColor( NodeDrawConfig.NODE_BG_ACTIVE );
              }
            }

            w.resolveBounds( null, null );


            if ( arc.getName() != null && lookAndFeel.drawArcLabels(arc) )
            {
              LabelWidget arcLabel = new LabelWidget( this, arc.getName() );
              arcLabel.setFont( ARC_LABEL_FONT );
              arcLabel.setForeground( Color.BLUE );
              arcLabel.setOpaque( true );
              w.addChild( arcLabel );
              w.setConstraint( arcLabel, ConnectionWidgetLayoutAlignment.CENTER, 30 );
            }
          }
        }
      }
    }

    revalidate();
  }

  @Override
  protected Widget widgetForNode (final ProcessTreeNode node)
  {
    return lookAndFeel.newWidget( node, this );
  }
}