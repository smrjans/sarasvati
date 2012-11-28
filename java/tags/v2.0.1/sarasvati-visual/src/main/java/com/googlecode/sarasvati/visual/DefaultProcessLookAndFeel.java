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

import javax.swing.Icon;
import javax.swing.JLabel;

import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.visual.icon.OvalNodeIcon;
import com.googlecode.sarasvati.visual.icon.RectangularNodeIcon;
import com.googlecode.sarasvati.visual.icon.SmallCircleNodeIcon;
import com.googlecode.sarasvati.visual.process.SarasvatiProcessScene;
import com.googlecode.sarasvati.visual.process.VisualProcessNode;

/**
 * Generates widgets using a TaskIcon for nodes of type 'task', and DefaultNodeIcon
 * for all other node types.
 *
 * @author Paul Lorenz
 */
public class DefaultProcessLookAndFeel implements ProcessLookAndFeel
{
  public static final DefaultProcessLookAndFeel INSTANCE = new DefaultProcessLookAndFeel( false, true );

  protected boolean drawSelfArcs;
  protected boolean drawArcLabels;

  public DefaultProcessLookAndFeel (final boolean drawSelfArcs,
                                    final boolean drawArcLabels)
  {
    this.drawSelfArcs = drawSelfArcs;
    this.drawArcLabels = drawArcLabels;
  }

  @Override
  public boolean drawArcLabels (final Arc arc)
  {
    return drawArcLabels;
  }

  @Override
  public boolean drawSelfArcs (final Arc arc)
  {
    return drawSelfArcs;
  }

  public Icon newIconForNode (final VisualProcessNode node)
  {
    String nodeType = node.getNode().getType();
    if ( nodeType.equalsIgnoreCase( getTaskType() ) )
    {
      return new RectangularNodeIcon( node.getNode(), node.getToken() );
    }

    else if( nodeType.equalsIgnoreCase( "end" ) )
    {
      return new SmallCircleNodeIcon();
    }

    return new OvalNodeIcon( node.getNode(), node.getToken() );
  }

  @Override
  public Widget newWidget (final VisualProcessNode node,
                           final SarasvatiProcessScene scene)
  {
    Icon icon = newIconForNode( node );
    JLabel label = new JLabel( icon );
    label.setSize( icon.getIconWidth(), icon.getIconHeight() );
    return new ComponentWidget( scene, label );
  }

  protected String getTaskType ()
  {
    return "task";
  }

  @Override
  public boolean isBackArc (final Arc arc,
                            final boolean defaultValue)
  {
    return defaultValue;
  }
}