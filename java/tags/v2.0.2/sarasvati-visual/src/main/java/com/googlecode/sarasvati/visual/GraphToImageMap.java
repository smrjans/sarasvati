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

import javax.swing.Icon;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Node;

/**
 * Used to generate HTML Image maps of graphs. It provides
 * methods to generate hrefs and hovers for nodes and arcs.
 *
 * @author Paul Lorenz
 */
public interface GraphToImageMap
{
  /**
   * Controls drawing of arc labels
   * @param arc
   *
   * @return true if arc labels should be rendered, false otherwise
   */
  boolean drawArcLabels (Arc arc);

  /**
   * Each node may have a different {@link Icon} representing
   * it in the generated image map. Override this method
   * to specify which Icon to use for a given node.
   *
   * @param node The graph node needing an icon
   * @return An icon representing the given node.
   */
  Icon iconForNode (Node node);

  /**
   * Every node in a generated image of a graph may have
   * a link for when the node is clicked. The link may
   * be a regular http URL or a javascript: link.
   *
   * If null is returned, no link will be associated with the node.
   *
   * @param node The node to generate the HREF for
   * @return An HREF for when the node is clicked in the generated image.
   *         May be null, in which case no link is generated.
   */
  String hrefForNode (Node node);

  /**
   * Every node in a generated image of a graph may have
   * associated text which will pop-up when the node is
   * hovered over.
   *
   * If null is returned, no hover will be created for the node.
   *
   * @param node The node to generate the hover for.
   * @return An String to display in a hover when the node is moused over.
   *         May be null, in which case no hover will appear.
   */
  String hoverForNode (Node node);

  /**
   * Every arc in a generated image of a graph may have
   * a link for when the arc is clicked. The link may
   * be a regular http URL or a javascript: link.
   *
   * If null is returned, no link will be associated with the arc.
   *
   * @param arc The arc to generate the HREF for
   * @return An HREF for when the arc is clicked in the generated image.
   *         May be null, in which case no link is generated.
   */
  String hrefForArc (Arc arc);

  /**
   * Every arc in a generated image of a graph may have
   * associated text which will pop-up when the arc is
   * hovered over.
   *
   * If null is returned, no hover will be created for the arc.
   *
   * @param arc The arc to generate the hover for.
   * @return An String to display in a hover when the arc is moused over.
   *         May be null, in which case no hover will appear.
   */
  String hoverForArc (Arc arc);
}