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

import java.util.List;

import com.googlecode.sarasvati.event.HasEventQueue;

/**
 * A Graph corresponds to a process definition. It has a set of
 * Nodes, which correspond to actions that need to be taken for the process
 * to complete. It also has a set of Arcs which define the relationships
 * between the Nodes.
 *
 * @author Paul Lorenz
 */
public interface Graph extends HasEventQueue
{
  /**
   * Returns the graph name, which should be unique. Different versions with the same
   * name may exist, but each of these should have a unique version number.
   *
   * @return The name of the graph
   */
  String getName ();

  /**
   * Returns the version number of the graph. Multiple versions of a graph may exists.
   * Newer versions of the graph should have higher version numbers than older versions.
   * A graph can be uniquely identified by name and version number.
   *
   * @return The version of the graph
   */
  int getVersion ();

  /**
   * Users may associate custom ids with a graph. This may be used to
   * aid in determining if a graph has changed. For example, it may store
   * a hash of the process definition file. Or it might be a version number
   * set and incremented by an editor.
   *
   * @return The custom id (if any) associated with the graph
   */
  String getCustomId ();

  /**
   * Returns a list of all arcs in the graph.
   *
   * @return A list of arcs
   */
  List<Arc> getArcs ();

  /**
   * Returns a list of arcs which have the given node as an end point. The
   * list may be empty, but will never be null.
   *
   * @param node A node belonging to this graph
   * @return A list of arcs
   */
  List<Arc> getInputArcs (Node node);

  /**
   * Returns a list of arcs which have the given node as an end point and
   * which have the given name. The list may be empty but will never be
   * null.
   *
   * @param node A node belonging to this graph
   * @return A list of arcs
   */
  List<Arc> getInputArcs (Node node, String arcName);

  /**
   * Returns a list of arcs which have the given node as a starting point.
   * The list may be empty but will never be null.
   *
   * @param node A node belonging to this graph
   * @return A list of arcs
   */
  List<Arc> getOutputArcs (Node node);

  /**
   * Returns a list of arcs which have the given node as a starting point
   * and which have the given name. The list may be empty but will never
   * be null.
   *
   * @param node A node belonging to this graph
   * @return A list of arcs
   */
  List<Arc> getOutputArcs (Node node, String arcName);

  /**
   * Returns a list of arcs which have the given node as a starting point
   * and which have the given name. The list may be empty but will never
   * be null.
   *
   * @param node A node belonging to this graph
   * @return A list of arcs
   */
  List<Arc> getOutputArcs (Node node, String...arcNames);

  /**
   * Returns a list of the nodes at which nodes should be placed to
   * start a workflow process.
   *
   * @return A list of nodes
   */
  List<Node> getStartNodes ();

  /**
   * Returns a list of all nodes in the graph.
   *
   * @return A list of nodes
   */
  List<Node> getNodes ();
}