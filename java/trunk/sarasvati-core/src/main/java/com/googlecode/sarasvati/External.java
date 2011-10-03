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

    Copyright 2009 Paul Lorenz
*/
package com.googlecode.sarasvati;

import com.googlecode.sarasvati.env.ReadEnv;

/**
 * When an external is defined in a process definition, custom attributes
 * may be defined. {@link External} allows access to those custom attributes.
 *
 *
 * @author Paul Lorenz
 */
public interface External
{
  /**
   * Returns the name given to the external in the defining graph. External names must
   * be unique withing a given process definition.
   *
   * @return The name given to the external.
   */
  String getName ();

  /**
   * Returns the graph that this external is a member of.
   *
   * @return The containing Graph
   */
  Graph getGraph ();

  /**
   * Returns the graph that this external points to.
   *
   * @return The referenced Graph
   */
  Graph getExternalGraph ();

  /**
   * Returns the read-only state associated with this external.
   *
   * @return The read-only state associated with this external.
   */
  ReadEnv getEnv ();
}