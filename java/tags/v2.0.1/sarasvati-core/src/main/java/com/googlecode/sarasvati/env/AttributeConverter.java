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
package com.googlecode.sarasvati.env;

/**
 * Interface for converting to and from objects and strings. Used for
 * loading custom data from XML process definitions into nodes as well
 * as transforming process/token/token set {@link Env} attributes to
 * and from database friendly strings.
 * <p>
 * AttributeConverters should be registered with and used from the
 * {@link AttributeConverters} class.
 *
 * @author Paul Lorenz
 */
public interface AttributeConverter
{
  /**
   * Converts the given object into string format.
   *
   * @param object The object to converter
   *
   * @return The object in string format
   */
  String objectToString (Object object);

  /**
   * Converts the given string into an object of the given type.
   *
   * @param string The string to be converted into an object
   * @param type The type to transform the string into
   *
   * @return The object produced from the string
   */
  Object stringToObject (String string, Class<?> type);
}