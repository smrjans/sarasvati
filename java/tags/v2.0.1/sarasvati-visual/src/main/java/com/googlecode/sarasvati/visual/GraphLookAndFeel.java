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

import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.visual.graph.SarasvatiGraphScene;

/**
 * Controls how a graph will be drawn, including generating an appropriate {@link Widget}
 * for each {@link Node}.
 *
 * @author Paul Lorenz
 */
public interface GraphLookAndFeel
{
  /**
   * Controls if self arcs should be drawn.
   * @param arc
   *
   * @return True if self arcs should be drawn, false otherwise
   */
  boolean drawSelfArcs (Arc arc);

  /**
   * Controls if arc labels should be drawn.
   * @param arc
   *
   * @return True if arc labels should be drawn, false otherwise
   */
  boolean drawArcLabels (Arc arc);

  /**
   * Returns a new Widget to represent the given node.
   *
   * @param node The node we need a widget for
   * @param scene The scene the widget is created in
   * @return A new widget for the given node
   */
  Widget newWidget (Node node, SarasvatiGraphScene scene);
}