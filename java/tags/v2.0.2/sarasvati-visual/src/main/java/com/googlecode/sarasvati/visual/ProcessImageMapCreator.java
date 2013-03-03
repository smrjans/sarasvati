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
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.adapter.Function;
import com.googlecode.sarasvati.visual.process.SarasvatiProcessScene;
import com.googlecode.sarasvati.visual.process.VisualProcessNode;
import com.googlecode.sarasvati.visual.util.ProcessHoverFunctionAdapter;
import com.googlecode.sarasvati.visual.util.ProcessHrefFunctionAdapter;

/**
 * Class which generates HTML image maps for processes. This uses {@link SarasvatiProcessScene}
 * internally, which can be used directly if more control is required.
 * <br>
 * <br>
 * <b>Note:</b> If using {@link SarasvatiProcessScene}, be sure to call
 * {@link SarasvatiProcessScene#setupForExportOnHeadless()} before calling
 * {@link SarasvatiProcessScene#export(StringBuilder, Function, Function)}.
 * <br>
 *  Example usage in a JSP:
 *  <pre>
 *  &lt;%
 *    ProcessToImageMapAdapter helper = new ProcessToImageMapAdapter ()
 *    {
 *      public String hrefForNode (VisualProcessNode node)
 *      {
 *        return "javascript:alert( 'You have selected " + node.getNode().getName() + "' );";
 *      }
 *
 *      public String hoverForNode (VisualProcessNode node)
 *      {
 *        NodeToken token = node.getToken();
 *        if ( token == null )
 *        {
 *          return null;
 *        }
 *        return "Started: " + sdf.format( token.getCreateDate() ) +
 *               " Finished: " + (token.getCompleteDate() == null ? "Not yet finished" : sdf.format( token.getCompleteDate() ) ) ;
 *      }
 *    };
 *
 *    String basePath = config.getServletContext().getRealPath( "/" );
 *    ProcessImageMapCreator imageMapCreator = new ProcessImageMapCreator( process, helper );
 *    imageMapCreator.writeImageToFile( "gif", basepath + "/test-process.gif" );
 *  %&gt;
 *
 *  &lt;map name="processMap"&gt;
 *    &lt;%=imageMapCreator.getMapContents()%&gt;
 *  &lt;/map&gt;
 *
 *  &lt;div style="margin-left:10px; padding-top:10px"&gt;
 *    &lt;image style="border:2px black solid" src="&lt;%=request.getContextPath() + "/test-process.gif"%>" usemap="#processMap"/&gt;
 *  &lt/div&gt;
 *  </pre>
 *
 * @author Paul Lorenz
 */
public class ProcessImageMapCreator
{
  protected String mapContents;
  protected BufferedImage image;

  /**
   * Creates a new ProcessImageMapCreate using the given process and process to image map
   * helper. Will immediately generate the map contents and image.
   *
   * @param process The process to create an image map and image from.
   * @param processToImageMap Controls how the image and image map are constructed.
   */
  public ProcessImageMapCreator (final GraphProcess process,
                                 final ProcessToImageMap processToImageMap)
  {
    generateMapAndImage( process, processToImageMap );
  }

  protected void generateMapAndImage (final GraphProcess process,
                                      final ProcessToImageMap processToImageMap)
  {
    final ProcessLookAndFeel lookAndFeelAdapter = new ProcessLookAndFeel ()
    {
      @Override
      public boolean drawArcLabels (final Arc arc)
      {
        return processToImageMap.drawArcLabels( arc );
      }

      @Override
      public boolean drawSelfArcs (final Arc arc)
      {
        return false;
      }

      @Override
      public boolean isBackArc (final Arc arc, final boolean defaultValue)
      {
        return processToImageMap.isBackArc( arc, defaultValue );
      }

      @Override
      public Widget newWidget (final VisualProcessNode node, final SarasvatiProcessScene scene)
      {
        Icon icon = processToImageMap.iconForNode( node );
        JLabel label = new JLabel( icon );
        label.setSize( icon.getIconWidth(), icon.getIconHeight() );
        return new ComponentWidget( scene, label );
      }
    };

    SarasvatiProcessScene processScene = new SarasvatiProcessScene( process, lookAndFeelAdapter );
    processScene.setupForExportOnHeadless();

    StringBuilder buf = new StringBuilder( 1024 );

    Function<String,Widget> hrefMapper = new ProcessHrefFunctionAdapter( processToImageMap );
    Function<String,Widget> hoverMapper = new ProcessHoverFunctionAdapter( processToImageMap );
    image = processScene.export( buf, hrefMapper, hoverMapper );
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
   * Returns the generated process image. If you just want to write it to a file,
   * you can use {@link ProcessImageMapCreator#writeImageToFile(String, String)}.
   * @return The generated process image
   */
  public BufferedImage getImage ()
  {
    return image;
  }

  /**
   * Writes the generated process image to a file in the given format
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