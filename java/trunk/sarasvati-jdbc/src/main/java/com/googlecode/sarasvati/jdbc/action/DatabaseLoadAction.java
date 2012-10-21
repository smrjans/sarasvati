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
package com.googlecode.sarasvati.jdbc.action;

import java.util.List;

/**
 * An interface for database actions which load 1 or more objects
 * from the database.
 *
 * @author Paul Lorenz
 *
 * @param <T> The type of object being loaded from the database.
 */
public interface DatabaseLoadAction<T> extends DatabaseAction
{
  /**
   * Returns the loaded objects. The list may be empty, but will
   * never be null.
   *
   * @return The load objects
   */
  List<T> getResult ();

  /**
   * Returns the first loaded object, or null, if no objects were loaded.
   * @return The first loaded object, or null, if no objects were loaded.
   */
  T getFirstResult ();
}
