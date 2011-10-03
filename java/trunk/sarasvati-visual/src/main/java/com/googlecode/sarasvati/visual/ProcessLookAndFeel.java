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
import com.googlecode.sarasvati.visual.process.SarasvatiProcessScene;
import com.googlecode.sarasvati.visual.process.VisualProcessNode;

/**
 * Controls how a graph will be drawn, including generating an appropriate {@link Widget}
 * for each {@link VisualProcessNode}.
 *
 * @author Paul Lorenz
 */
public interface ProcessLookAndFeel
{
  /**
   * Controls if self arcs should be drawn to the given arc.
   *
   * @param arc An arc in a process definition
   *
   * @return True if self arcs should be drawn, false otherwise
   */
  boolean drawSelfArcs (Arc arc);

  /**
   * Controls if arc labels should be drawn.
   *
   * @param arc An arc in a process definition
   *
   * @return True if arc labels should be drawn, false otherwise
   */
  boolean drawArcLabels (Arc arc);

  /**
   * Returns a new Widget to represent the given {@link VisualProcessNode}.
   *
   * @param node The node we need a widget for
   * @param scene The scene the widget is created in
   * @return A new widget for the given node
   */
  Widget newWidget (VisualProcessNode node, SarasvatiProcessScene scene);

  /**
   * Controls if this arc should point backwards or forwards.
   *
   * @param arc An arc in a process definition
   * @param defaultValue The calculated value based on the relative positions in a GraphTree
   *
   * @return True if the the arc is generally traversed going back to a previous point in the workflow
   */
  boolean isBackArc (Arc arc, boolean defaultValue);
}
