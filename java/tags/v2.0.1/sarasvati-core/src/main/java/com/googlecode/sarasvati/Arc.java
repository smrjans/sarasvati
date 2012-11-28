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

package com.googlecode.sarasvati;

/**
 * Arcs describe the relationships between Nodes. Arcs
 * are directed links between Nodes. During the execution
 * of a {@link GraphProcess}, arcs will determine in what order
 * tokens are generated in Nodes.
 *
 * Arc may be labeled with a name. The name can be used
 * for flow control.
 *
 * @author Paul Lorenz
 */
public interface Arc
{
  /**
   * If no name is specified for an Arc, it will be
   * given the default name, which is null.
   */
  static String DEFAULT_ARC = null;

  /**
   * Returns the {@link Node} at the start of the arc.
   *
   * @return The {@link Node} at the start of the arc
   */
  Node getStartNode ();

  /**
   * Returns the {@link Node} at the end of the arc.
   *
   * @return The {@link Node} at the end of the arc
   */
  Node getEndNode ();

  /**
   * Returns the arc name. Nulls and blanks are allowed,
   * but care should be taken as some databases have
   * issues with blanks and treat them either as nulls
   * or as single spaces.
   *
   * @return The arc name.
   */
  String getName ();

  /**
   * Returns true if the start node is the same as the end node, false otherwise.
   *
   * @return true if the start node is the same as the end node, false otherwise.
   */
  boolean isSelfArc ();
}