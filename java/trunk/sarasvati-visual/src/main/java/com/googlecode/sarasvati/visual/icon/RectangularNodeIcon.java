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
package com.googlecode.sarasvati.visual.icon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.visual.common.NodeDrawConfig;
import com.googlecode.sarasvati.visual.util.FontUtil;

public class RectangularNodeIcon extends AbstractNodeIcon
{
  protected boolean isJoin;
  protected String label;
  protected Color color;
  protected boolean isSelected;

  public RectangularNodeIcon (final Node node, final NodeToken token)
  {
    this.label = node.getAdaptor( String.class );
    this.isJoin = node.getJoinType() != JoinType.OR;
    this.color = NodeDrawConfig.getColor( token );
    redrawImage();
  }

  public RectangularNodeIcon (final String label,
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
    g.setFont( g.getFont().deriveFont( Font.BOLD ) );
    g.setColor( color );
    g.fillOval( 0, 0, WIDTH - 1, HEIGHT - 1 );
    g.fillRoundRect( 0, 0, WIDTH - 1, HEIGHT - 1, 10, 10 );

    float[] dashes = isJoin ? new float[] { 10, 5 } : null;

    int offset = 1;

    BasicStroke stroke = new BasicStroke( 3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, dashes, 0) ;
    g.setStroke( stroke );

    g.setColor( isSelected ? NodeDrawConfig.NODE_BORDER_SELECTED : NodeDrawConfig.NODE_BORDER );

    int width = WIDTH - ((offset << 1) + 1);
    int height = HEIGHT - ((offset<< 1) + 1);

    g.drawRoundRect( offset, offset, width, height, 10, 10 );

    g.setColor( Color.white );

    String[] lines = FontUtil.split( label );

    int padding = 2 + offset;
    int startX = padding;

    int maxWidth = getIconWidth() - (padding << 1);

    if ( lines.length == 1 )
    {
      FontUtil.setSizedFont( g, label, 11, maxWidth );
      int strWidth = (int)Math.ceil( g.getFontMetrics().getStringBounds( lines[0], g ).getWidth() );
      int left = startX + ((maxWidth - strWidth) >> 1);
      g.drawString( label, left, (getIconHeight() >> 1) );
    }
    else if ( lines.length == 2 )
    {
      FontUtil.setSizedFont( g, lines[0], 11, maxWidth );

      int strWidth = (int)Math.ceil( g.getFontMetrics().getStringBounds( lines[0], g ).getWidth() );
      int left = startX + ((maxWidth - strWidth) >> 1);
      g.drawString( lines[0], left, (getIconHeight() - offset >> 1) );

      FontUtil.setSizedFont( g, lines[1], 11, maxWidth );

      int strHeight = (int)Math.ceil( g.getFontMetrics().getStringBounds( lines[1], g ).getHeight() );
      strWidth = (int)Math.ceil( g.getFontMetrics().getStringBounds( lines[1], g ).getWidth() );
      left = startX + ((maxWidth - strWidth) >> 1);

      g.drawString( lines[1], left, (getIconHeight() - offset >> 1) + (strHeight + 1) );
    }
  }
}