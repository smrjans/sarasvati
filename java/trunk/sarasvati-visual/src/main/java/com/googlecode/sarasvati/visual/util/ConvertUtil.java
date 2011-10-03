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
package com.googlecode.sarasvati.visual.util;

import java.awt.Polygon;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class ConvertUtil
{
  public static Rectangle awtToSwt (final java.awt.Rectangle r)
  {
    if ( r == null )
    {
      return null;
    }

    Rectangle swtRect = new Rectangle();
    swtRect.x = r.x;
    swtRect.y = r.y;
    swtRect.width = r.width;
    swtRect.height = r.height;

    return swtRect;
  }

  public static Point awtToSwt (final java.awt.Point point)
  {
    Point swtPoint = new org.eclipse.draw2d.geometry.Point();
    swtPoint.x = point.x;
    swtPoint.y = point.y;
    return swtPoint;
  }

  public static java.awt.Rectangle swtToAwt (final Rectangle r)
  {
    return new java.awt.Rectangle( r.x, r.y, r.width, r.height );
  }

  public static java.awt.Point swtToAwt (final org.eclipse.draw2d.geometry.Point point)
  {
    return new java.awt.Point( point.x, point.y );
  }

  public static void appendPolygon (final Polygon poly, final StringBuilder buf)
  {
    if ( poly.npoints > 0 )
    {
      buf.append( poly.xpoints[0] );
      buf.append( "," );
      buf.append( poly.ypoints[1] );
    }

    for ( int i = 0; i < poly.npoints; i++ )
    {
      buf.append( ", " );
      buf.append( poly.xpoints[i] );
      buf.append( "," );
      buf.append( poly.ypoints[i] );
    }
  }
}
