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
package com.googlecode.sarasvati.visual.common;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.graph.Path;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.visual.util.ConvertUtil;
import com.googlecode.sarasvati.visual.util.SvGraphicsUtil;

public class PathTrackingConnectionWidget extends ConnectionWidget
{
  protected ShortestPathRouterAdapter router;

  protected Point start = null;
  protected Point end   = null;

  protected Path path;
  protected List<Point> route;

  public PathTrackingConnectionWidget (final ShortestPathRouterAdapter router,
                                       final GraphSceneImpl<?,?> scene)
  {
    super( scene );
    this.router = router;
  }

  public Rectangle getSourceBounds ()
  {
    final Widget widget = getSourceAnchor().getRelatedWidget();
    return widget == null ? null : SvGraphicsUtil.getBounds( widget );
  }

  public Rectangle getTargetBounds ()
  {
    final Widget widget = getTargetAnchor().getRelatedWidget();
    return widget == null ? null : SvGraphicsUtil.getBounds( widget );
  }

  public boolean ensurePathCurrent ()
  {
    final Anchor sourceAnchor = getSourceAnchor();
    final Anchor targetAnchor = getTargetAnchor();

    if ( sourceAnchor != null && targetAnchor != null )
    {
      Point newStart = null;
      Point newEnd = null;

      final boolean isSelfArc = sourceAnchor.getRelatedWidget() != null &&
                          SvUtil.equals( sourceAnchor.getRelatedWidget(), targetAnchor.getRelatedWidget() );
      if ( isSelfArc )
      {
        final Point origin = sourceAnchor.getRelatedWidget().getLocation();
        final Rectangle bounds = sourceAnchor.getRelatedWidget().getBounds();
        newStart = new Point( origin.x + (bounds.width >> 1 ), origin.y + bounds.height + 1 );
        newEnd = new Point( origin.x - 1, origin.y + (bounds.height >> 1 ) );
      }
      else
      {
        newStart = sourceAnchor.compute( getSourceAnchorEntry() ).getAnchorSceneLocation();
        newEnd = targetAnchor.compute( getTargetAnchorEntry() ).getAnchorSceneLocation();
        SvGraphicsUtil.movePointOutOfBounds( newStart, getSourceBounds() );
        SvGraphicsUtil.movePointOutOfBounds( newEnd, getTargetBounds() );
      }

      final boolean pathChange = path == null || !start.equals( newStart ) || !end.equals( newEnd );
      if ( pathChange )
      {
        if ( path != null )
        {
          router.removePath( path );
        }

        start = newStart;
        end   = newEnd;
        path = new Path( ConvertUtil.awtToSwt( start ), ConvertUtil.awtToSwt( end ) );
        path.data = this;
        router.addPath( path, isSelfArc );
      }
      return pathChange;
    }

    router.removePath( path );
    start = null;
    end   = null;
    path  = null;

    return true;
  }

  public Point getStart ()
  {
    return start;
  }

  public Point getEnd ()
  {
    return end;
  }

  public void updateRoute ()
  {
    final PointList pointList = path.getPoints();

    route = new ArrayList<Point>( pointList.size() );

    for ( int i = 0; i < pointList.size(); i++ )
    {
      final Point point = ConvertUtil.swtToAwt( pointList.getPoint( i ) );
      route.add( point );
    }
  }

  public List<Point> getRoute ()
  {
    return route;
  }
}