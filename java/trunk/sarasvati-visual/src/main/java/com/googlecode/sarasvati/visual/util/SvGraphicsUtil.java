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

    Copyright 2009 Paul Lorenz
*/

package com.googlecode.sarasvati.visual.util;

import java.awt.Point;
import java.awt.Rectangle;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.graph.Path;
import org.netbeans.api.visual.widget.Widget;

public class SvGraphicsUtil
{
  public static void addOffsetBendToPath (final Path path, final int spacing)
  {
    PointList bendPoints = new PointList();
    int deltaX = path.getEndPoint().x - path.getStartPoint().x;
    int y = ((path.getEndPoint().y + path.getStartPoint().y) >> 1 ) + Math.min( Math.abs( deltaX ), spacing );

    int deltaY = path.getEndPoint().y - path.getStartPoint().y;
    int offset = Math.min( Math.abs( deltaY ), spacing );
    if ( ( deltaX > 0 && deltaY > 0 ) || (deltaX < 0 && deltaY < 0 ) )
    {
      offset = -offset;
    }

    int x = ((path.getEndPoint().x + path.getStartPoint().x) >> 1 ) + offset;

    bendPoints.addPoint( x, y );
    //Add a new obstacle
    path.setBendPoints( bendPoints  );
  }

  public static Rectangle getBounds (final Widget widget)
  {
    Rectangle bounds = widget.getBounds();
    bounds.setLocation( widget.getLocation() );
    return bounds;
  }

  public static void movePointOutOfBounds (final Point point, final Rectangle bounds)
  {
    if ( bounds != null )
    {
      if ( point.x == bounds.x )
      {
        point.x--;
      }
      else if ( point.x == ( bounds.x + bounds.width ) )
      {
        point.x++;
      }

      if ( point.y == bounds.y )
      {
        point.y--;
      }
      else if ( point.y == ( bounds.y + bounds.height ) )
      {
        point.y++;
      }
    }
  }
}
