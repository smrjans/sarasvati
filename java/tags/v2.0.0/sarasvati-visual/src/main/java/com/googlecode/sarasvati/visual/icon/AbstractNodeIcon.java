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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

import com.googlecode.sarasvati.visual.common.NodeDrawConfig;

public abstract class AbstractNodeIcon implements Icon
{
  public static final int WIDTH  = 100;
  public static final int HEIGHT = NodeDrawConfig.getMaxNodeRadius() << 1;

  protected BufferedImage image;

  @Override
  public int getIconHeight ()
  {
    return HEIGHT;
  }

  @Override
  public int getIconWidth ()
  {
    return WIDTH;
  }

  @Override
  public void paintIcon(final Component c,
                        final Graphics g,
                        final int x,
                        final int y)
  {
    g.drawImage( image, x, y, c );
  }

  public void redrawImage ()
  {
    image = new BufferedImage( getIconWidth(), getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR );
    Graphics2D g = image.createGraphics();
    redrawImage( g );
    g.dispose();
  }

  public abstract void redrawImage (final Graphics2D g);
}
