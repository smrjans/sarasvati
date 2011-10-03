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
package com.googlecode.sarasvati.visual.icon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.visual.common.NodeDrawConfig;
import com.googlecode.sarasvati.visual.util.FontUtil;

public class OvalNodeIcon extends AbstractNodeIcon
{
  protected String label;
  protected final Color color;
  protected final boolean isJoin;
  protected final boolean isSelected;

  public OvalNodeIcon (final Node node, final NodeToken token)
  {
    this.label = node.getAdaptor( String.class );
    if ( label == null )
    {
      label = node.getName();
    }
    this.isJoin = node.getJoinType() != JoinType.OR;
    this.color = NodeDrawConfig.getColor( token );
    this.isSelected = false;
    redrawImage();
  }

  public OvalNodeIcon (final String label,
                       final Color color,
                       final boolean isJoin,
                       final boolean isSelected)
  {
    this.label = label;
    this.color = color;
    this.isJoin = isJoin;
    this.isSelected = isSelected;
    redrawImage();
  }

  @Override
  public void redrawImage (final Graphics2D g)
  {
    g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

    g.setColor( color );
    g.fillOval( 0, 0, WIDTH - 1, HEIGHT - 1 );

    g.setColor( isSelected ? NodeDrawConfig.NODE_BORDER_SELECTED : NodeDrawConfig.NODE_BORDER );

    float[] dashes = isJoin ? new float[] { 10, 5 } : null;

    int offset = 1;

    BasicStroke stroke = new BasicStroke( 3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, dashes, 0) ;
    g.setStroke( stroke );

    int width = WIDTH - ((offset << 1) + 1);
    int height = HEIGHT - ((offset<< 1) + 1);

    g.drawOval( offset, offset, width, height);
    offset += 3;

    g.setColor( Color.white );

    int padding = 2 + offset;
    int startX = padding;

    int maxWidth = getIconWidth() - (padding << 1);

    g.setFont( g.getFont().deriveFont( Font.BOLD ) );
    FontUtil.setSizedFont( g, label, 11, maxWidth );
    int strWidth = (int)Math.ceil( g.getFontMetrics().getStringBounds( label, g ).getWidth() );
    int strHeight = g.getFontMetrics().getAscent();
    int left = startX + ((maxWidth - strWidth) >> 1);
    int top = ((getIconHeight() + strHeight) >> 1);
    g.drawString( label, left, top );
  }
}