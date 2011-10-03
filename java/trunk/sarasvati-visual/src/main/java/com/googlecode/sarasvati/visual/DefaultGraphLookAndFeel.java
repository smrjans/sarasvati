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

import java.awt.Component;

import org.netbeans.api.visual.widget.ComponentWidget;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.visual.graph.SarasvatiGraphScene;

/**
 * Generates widgets for nodes using the {@link Node#getAdaptor(Class)} method,
 * passing in the {@link Component} class. It will wrap the returned component
 * in a {@link ComponentWidget} instance.
 *
 * @author Paul Lorenz
 */
public class DefaultGraphLookAndFeel implements GraphLookAndFeel
{
  public static final DefaultGraphLookAndFeel INSTANCE = new DefaultGraphLookAndFeel( false, false );

  protected boolean drawSelfArcs;
  protected boolean drawArcLabels;

  public DefaultGraphLookAndFeel (final boolean drawSelfArcs, final boolean drawArcLabels)
  {
    this.drawSelfArcs = drawSelfArcs;
    this.drawArcLabels = drawArcLabels;
  }

  @Override
  public boolean drawArcLabels (final Arc arc)
  {
    return drawArcLabels;
  }

  @Override
  public boolean drawSelfArcs (final Arc arc)
  {
    return drawSelfArcs;
  }

  @Override
  public Widget newWidget (final Node node, final SarasvatiGraphScene scene)
  {
    Component c = node.getAdaptor( Component.class );

    if ( c == null )
    {
      throw new IllegalArgumentException( "No component found for node: " + node +
                                          ". You should configure one with NodeAdapterManager.registerFactory " +
                                          "or by subclassing your Node implemenations." );
    }

    return new ComponentWidget( scene, c );
  }

}
