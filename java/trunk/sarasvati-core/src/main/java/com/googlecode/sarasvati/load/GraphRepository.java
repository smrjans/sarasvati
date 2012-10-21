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
package com.googlecode.sarasvati.load;

import java.util.List;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.impl.NestedProcessNode;

/**
 * The graph repository is an engine specific way of accessing instances of
 * {@link Graph} which have been loaded. A database backed engine would
 * provide a repository which queried the database, while a memory backed
 * engine would use some memory cache. Other implementation are possible,
 * such as a file backed repository.
 *
 * @author Paul Lorenz
 */
public interface GraphRepository<T extends Graph>
{
  /**
   * Returns The newest {@link Graph} with the given name, or null if none is found.
   *
   * @param name The graph name
   *
   * @return The newest {@link Graph} with the given name, or null if none is found.
   */
  T getLatestGraph (String name);

  /**
   * Returns all graphs with the given name.
   *
   * @param name The name to match
   * @return All graphs with the given name
   */
  List<T> getGraphs (String name);

  /**
   * Returns all graphs accessible via this repository.
   *
   * @return All graphs accessible via this repository.
   */
  List<T> getGraphs ();

  /**
   * Adds a graph to the repository. Used by the {@link GraphLoader}.
   *
   * @param graph The graph to add to the repository.
   */
  void addGraph (T graph);

  /**
   * Returns any GraphProcesses that have been spawned by {@link NestedProcessNode} nodes.
   *
   * @param process The process whose nested children to find.
   *
   * @return A list of active nested processes
   */
  List<GraphProcess> getActiveNestedProcesses(final GraphProcess process);
}