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
                        Cheong Chung Onn
*/
package com.googlecode.sarasvati.visual.common;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Path;
import org.eclipse.draw2d.graph.ShortestPathRouter;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.visual.util.ConvertUtil;
import com.googlecode.sarasvati.visual.util.SvGraphicsUtil;

public class ShortestPathRouterAdapter implements Router
{
  protected GraphSceneImpl<?, ?> scene;
  protected ShortestPathRouter router;

  protected Map<Widget, WidgetBoundsTracker> widgetMap = new HashMap<Widget, WidgetBoundsTracker>();

  public ShortestPathRouterAdapter (final GraphSceneImpl<?,?> scene, final int spacing)
  {
    this.scene = scene;
    this.router = new ShortestPathRouter();
    router.setSpacing( spacing );
  }

  public void setAdjacentLineSpacing (final int spacing)
  {
    router.setSpacing( spacing );
  }

  public void addNodeWidget (final Widget w)
  {
    widgetMap.put( w, new WidgetBoundsTracker( w ) );
  }

  public void removeNodeWidget (final Widget w)
  {
    WidgetBoundsTracker boundsTracker = widgetMap.remove( w );
    if ( boundsTracker != null )
    {
      boundsTracker.cleanup();
    }
  }

  public void sceneValidated ()
  {
    boolean updates = false;
    for ( WidgetBoundsTracker boundsTracker : widgetMap.values() )
    {
      updates |= boundsTracker.checkBounds();
    }

    if ( updates )
    {
      updateRoutes();
      scene.revalidate();
    }
  }

  public void addPath (final Path path, final boolean isSelfArc)
  {
    //Check for overlapping path, If there are overlapping paths
    //Force path to bend.
    for ( Widget w : scene.connLayer.getChildren() )
    {
      if ( path.data == w || !(w instanceof PathTrackingConnectionWidget ) )
      {
        continue;
      }

      PathTrackingConnectionWidget conn = (PathTrackingConnectionWidget)w;

      if ( conn.getStart() == null || conn.getEnd() == null )
      {
        continue;
      }

      List<Point> route = conn.getRoute();

      if ( route != null && route.size() == 2 &&
           ( ConvertUtil.awtToSwt( conn.getEnd() ).equals( path.getStartPoint() ) &&
             ConvertUtil.awtToSwt( conn.getStart() ).equals( path.getEndPoint() ) ) ||
           ( ConvertUtil.awtToSwt( conn.getStart() ).equals( path.getStartPoint() ) &&
             ConvertUtil.awtToSwt( conn.getEnd() ).equals( path.getEndPoint() ) ) )
      {
        SvGraphicsUtil.addOffsetBendToPath( path, router.getSpacing() );
        break;
      }
    }

    if ( isSelfArc )
    {
      int offset = 15;
      PointList bendPoints = new PointList();
      bendPoints.addPoint( path.getStartPoint().x, path.getStartPoint().y + offset );
      bendPoints.addPoint( path.getEndPoint().x - offset, path.getStartPoint().y + offset );
      bendPoints.addPoint( path.getEndPoint().x - offset, path.getEndPoint().y );
      path.setBendPoints( bendPoints );
    }

    router.addPath( path );
  }

  public void removePath (final Path path)
  {
    router.removePath( path );
  }

  @SuppressWarnings("unchecked")
  public void updateRoutes ()
  {
    List<Path> paths = router.solve();
    for ( Path p : paths )
    {
      PathTrackingConnectionWidget pathTrackingCW = (PathTrackingConnectionWidget)p.data;
      pathTrackingCW.updateRoute();
      pathTrackingCW.revalidate();
    }
  }

  @Override
  public List<Point> routeConnection (final ConnectionWidget conn)
  {
    PathTrackingConnectionWidget pathTrackingCW = (PathTrackingConnectionWidget)conn;
    if ( pathTrackingCW.ensurePathCurrent() )
    {
      updateRoutes();
    }

    return pathTrackingCW.getRoute();
  }

  public class WidgetBoundsTracker
  {
    private Widget widget;
    private Rectangle bounds;

    public WidgetBoundsTracker (final Widget widget)
    {
      this.widget = widget;
    }

    public Rectangle getNewBounds ()
    {
      java.awt.Rectangle newBounds = widget.getBounds();
      if (newBounds == null )
      {
        return null;
      }
      newBounds.setLocation( widget.getLocation() );
      return ConvertUtil.awtToSwt( newBounds );
    }

    public boolean checkBounds ()
    {
      Rectangle newBounds = getNewBounds();

      if ( SvUtil.equals( bounds, newBounds ) )
      {
        return false;
      }

      if ( bounds == null )
      {
        if ( newBounds != null )
        {
          router.addObstacle( newBounds );
        }
      }
      else if ( newBounds == null )
      {
        router.removeObstacle( bounds );
      }
      else
      {
        router.updateObstacle( bounds, newBounds );
      }

      bounds = newBounds;
      return true;
    }

    public void cleanup ()
    {
      if ( bounds != null )
      {
        router.removeObstacle( bounds );
      }
    }
  }
}