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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.export.WidgetPolygonalCoordinates;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.export.Scene2Image;

import com.googlecode.sarasvati.adapter.Function;
import com.googlecode.sarasvati.visual.util.ConvertUtil;

public abstract class GraphSceneImpl<N,E> extends GraphScene<N, E>
{
  protected LayerWidget mainLayer = new LayerWidget( this );
  protected LayerWidget intrLayer = new LayerWidget( this );
  protected LayerWidget connLayer = new LayerWidget( this );

  protected ShortestPathRouterAdapter router;
  protected Map<N,Anchor> anchorMap = new HashMap<N,Anchor>();

  public GraphSceneImpl()
  {
    addChild( mainLayer );
    addChild( intrLayer );
    addChild( connLayer );

    router = new ShortestPathRouterAdapter( this, 10 );

    addSceneListener( new SceneListener()
    {
      @Override
      public void sceneValidating () { /* does nothing */ }

      @Override
      public void sceneValidated () { /* does nothing */ }

      @Override
      public void sceneRepaint ()
      {
        router.sceneValidated();
      }
    });
  }

  public void setAdjacentLineSpacing (final int spacing)
  {
    router.setAdjacentLineSpacing( spacing );
  }

  public LayerWidget getConnectionLayer ()
  {
    return connLayer;
  }

  @Override
  protected void attachEdgeSourceAnchor(final E edge, final N oldSourceNode, final N sourceNode)
  {
    ConnectionWidget edgeWidget = (ConnectionWidget) findWidget( edge );
    edgeWidget.setSourceAnchor( anchorMap.get( sourceNode ) );
  }

  @Override
  protected void attachEdgeTargetAnchor(final E edge, final N oldTargetNode, final N targetNode)
  {
    ConnectionWidget edgeWidget = (ConnectionWidget) findWidget( edge );
    edgeWidget.setTargetAnchor( anchorMap.get( targetNode ) );
  }

  @Override
  protected PathTrackingConnectionWidget attachEdgeWidget(final E edge)
  {
    PathTrackingConnectionWidget conn = new PathTrackingConnectionWidget( router, this );
    conn.setRouter( router );
    conn.setTargetAnchorShape( AnchorShape.TRIANGLE_FILLED );
    connLayer.addChild( conn );
    return conn;
  }

  @Override
  protected Widget attachNodeWidget (final N node)
  {
    Widget widget = widgetForNode( node );
    mainLayer.addChild( widget );
    anchorMap.put( node, AnchorFactory.createRectangularAnchor( widget ) );
    router.addNodeWidget( widget );
    return widget;
  }

  protected abstract Widget widgetForNode (N node);

  @Override
  protected void detachNodeWidget (final N node, final Widget widget)
  {
    super.detachNodeWidget( node, widget );
    router.removeNodeWidget( widget );
    anchorMap.remove( node );
  }

  public void setupForExportOnHeadless ()
  {
    mainLayer.resolveBounds( new Point( 0, 0 ), null );
    connLayer.resolveBounds( new Point( 0, 0 ), null );
    intrLayer.resolveBounds( new Point( 0, 0 ), null );
  }

  public BufferedImage export (final StringBuilder buf, final Function<String, Widget> hrefMapper, final Function<String, Widget> titleMapper )
  {
    Rectangle bounds = getPreferredBounds();
    BufferedImage image = new BufferedImage( bounds.width + NodeDrawConfig.getHorizontalNodeSpacing(),
                                             bounds.height + 20,
                                             BufferedImage.TYPE_4BYTE_ABGR );

    Graphics2D g = image.createGraphics();
    g.setColor( Color.white );
    g.fillRect( 0, 0, image.getWidth(), image.getHeight() );
    g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

    validate( g );
    paint( g );

    Scene2Image s = new Scene2Image( this, null );
    s.setScale( 1 );

    List<WidgetPolygonalCoordinates> coords = s.getSceneImageMapCoordinates(  0 );

    for ( WidgetPolygonalCoordinates coord : coords )
    {
      buf.append( "<area shape=\"poly\" coords=\"" );
      ConvertUtil.appendPolygon( coord.getPolygon(), buf );
      buf.append( "\" " );
      String result = hrefMapper.apply( coord.getWidget() );

      if ( result != null && result.length() != 0 )
      {
        buf.append( " href=\"" );
        buf.append( result );
        buf.append( "\" " );
      }
      else
      {
        buf.append( " nohref " );
      }

      result = titleMapper.apply( coord.getWidget() );

      if ( result != null && result.length() != 0 )
      {
        buf.append( "title=\"");
        buf.append( titleMapper.apply( coord.getWidget() ) );
        buf.append( "\"" );
      }

      buf.append( ">\n" );
    }

    return image;
  }
}