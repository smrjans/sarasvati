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
package com.googlecode.sarasvati.jdbc;

/**
 * An interface for database types with generated ids.
 *
 * @author Paul Lorenz
 */
public interface JdbcObject
{
  /**
   * Returns the object id
   *
   * @return The object id
   */
  Long getId ();

  /**
   * Sets the generated id
   *
   * @param id The generated ID
   */
  void setId (Long id);

  /**
   * True if the object is mutated after creation, false otherwise. Mutable
   * objects are instances of process and tokens. Immutable objects are
   * instances of graph, node and arc.
   *
   * @return True if the object is mutated after creation, false otherwise.
   */
  boolean isMutable ();
}
