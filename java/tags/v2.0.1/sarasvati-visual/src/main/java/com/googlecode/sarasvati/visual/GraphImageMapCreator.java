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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JLabel;

import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.adapter.Function;
import com.googlecode.sarasvati.visual.graph.SarasvatiGraphScene;
import com.googlecode.sarasvati.visual.util.HoverFunctionAdapter;
import com.googlecode.sarasvati.visual.util.HrefFunctionAdapter;

/**
 * Class which generates HTML image maps for graphs. This uses {@link SarasvatiGraphScene}
 * internally, which can be used directly if more control is required.
 * <br>
 * <br>
 * <b>Note:</b> If using {@link SarasvatiGraphScene}, be sure to call
 * {@link SarasvatiGraphScene#setupForExportOnHeadless()} before calling
 * {@link SarasvatiGraphScene#export(StringBuilder, Function, Function)}.
 * <br>
 *  Example usage in a JSP:
 *  <pre>
 *  &lt;%
 *    GraphToImageMapAdapter helper = new GraphToImageMapAdapter ()
 *    {
 *      public String hrefForNode (Node node)
 *      {
 *         return "javascript:alert( 'You have selected " + node.getName() + "' );";
 *       }
 *
 *       public String hoverForNode (Node node)
 *       {
 *         return "Name: " + node.getName() + ",  Type: " + node.getType() +
 *                ",  Guard: " + node.getGuard() + ",  Is start: " + node.isStart() +
 *                ",  Is join: " + node.isJoin();
 *       }
 *    };
 *
 *    String basePath = config.getServletContext().getRealPath( "/" );
 *    GraphImageMapCreator imageMapCreator = new GraphImageMapCreator( graph, helper );
 *    imageMapCreator.writeImageToFile( "gif", basepath + "/test-graph.gif" );
 *  %&gt;
 *
 *  &lt;map name="graphMap"&gt;
 *    &lt;%=imageMapCreator.getMapContents()%&gt;
 *  &lt;/map&gt;
 *
 *  &lt;div style="margin-left:10px; padding-top:10px"&gt;
 *    &lt;image style="border:2px black solid" src="&lt;%=request.getContextPath() + "/test-graph.gif"%>" usemap="#graphMap"/&gt;
 *  &lt/div&gt;
 *  </pre>
 *
 * @author Paul Lorenz
 */
public class GraphImageMapCreator
{
  protected String mapContents;
  protected BufferedImage image;

  /**
   * Creates a new GraphImageMapCreate using the given graph and graph to image map
   * helper. Will immediately generate the map contents and image.
   *
   * @param graph The graph to create an image map and image from.
   * @param graphToImageMap Controls how the image and image map are constructed.
   */
  public GraphImageMapCreator (final Graph graph, final GraphToImageMap graphToImageMap)
  {
    generateMapAndImage( graph, graphToImageMap );
  }

  protected void generateMapAndImage (final Graph graph, final GraphToImageMap graphToImageMap)
  {
    final GraphLookAndFeel lookAndFeelAdapter = new GraphLookAndFeel ()
    {
      @Override
      public boolean drawArcLabels (final Arc arc)
      {
        return graphToImageMap.drawArcLabels(arc);
      }

      @Override
      public boolean drawSelfArcs (final Arc arc)
      {
        return false;
      }

      @Override
      public Widget newWidget (final Node node, final SarasvatiGraphScene scene)
      {
        Icon icon = graphToImageMap.iconForNode( node );
        JLabel label = new JLabel( icon );
        label.setSize( icon.getIconWidth(), icon.getIconHeight() );
        return new ComponentWidget( scene, label );
      }
    };

    SarasvatiGraphScene graphScene = new SarasvatiGraphScene( graph, lookAndFeelAdapter );
    graphScene.setupForExportOnHeadless();

    StringBuilder buf = new StringBuilder( 1024 );

    Function<String,Widget> hrefMapper = new HrefFunctionAdapter( graphToImageMap );
    Function<String,Widget> hoverMapper = new HoverFunctionAdapter( graphToImageMap );
    image = graphScene.export( buf, hrefMapper, hoverMapper );
    mapContents = buf.toString();
  }

  /**
   * Returns what should placed in a map tag.
   * @return The contents of the image map, i.e. what should be placed between the map start and end tags
   */
  public String getMapContents ()
  {
    return mapContents;
  }

  /**
   * Returns the generated graph image. If you just want to write it to a file,
   * you can use {@link GraphImageMapCreator#writeImageToFile(String, String)}.
   * @return The generated graph image
   */
  public BufferedImage getImage ()
  {
    return image;
  }

  /**
   * Writes the generate graph image to a file in the given format
   *
   * @param imageFormat The informal name of the format to write the file in,
   *                    as understood by {@link ImageIO}.
   * @param imageFileName The name of the file to write the image to
   * @throws IOException If an error occurs writing the image to disc
   */
  public void writeImageToFile (final String imageFormat, final String imageFileName)
    throws IOException
  {
    ImageIO.write( image, imageFormat, new File( imageFileName ) );
    image.flush();
  }
}