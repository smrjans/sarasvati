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

    Copyright 2008-2009 Paul Lorenz
*/
package com.googlecode.sarasvati.env;


/**
 * A  ReadEnv is a container for attributes that can be read but not changed.
 * The attributes are likely all stored as strings, to allow simple database storage,
 * but they may be retrieved as a specified type, if an {@link AttributeConverter}
 * for that type has been registered (via
 * {@link AttributeConverters#setConverterForType(Class, AttributeConverter)}).
 *
 * @author Paul Lorenz
 */
public interface ReadEnv
{
  /**
   * Gets an attribute as a String. If there is no value set for
   * the attribute, null will be returned.
   *
   * @param name The name of the attribute to get
   * @return The value of attribute or null if no value is set for the attribute.
   *
   */
  String getAttribute (String name);

  /**
   * Returns the given attribute, transformed into the given type.
   *
   * @param <T> The type which the attribute should be returned as.
   *
   * @param name The name of the attribute to get.
   * @param type The class type which the attribute should be transformed to.
   *
   * @return The attribute value, or null if no value is set for the attribute.
   */
  <T> T getAttribute (String name, Class<T> type);

  /**
   * Returns the given attribute, transformed into the given type. If no value
   * is set, return the given default.
   *
   * @param <T> The type which the attribute should be returned as.
   *
   * @param name The name of the attribute to get.
   * @param type The class type which the attribute should be transformed to.
   * @param defaultValue The value to return if no value is set.
   *
   * @return The attribute value, or null if no value is set for the attribute.
   */
  <T> T getAttribute (String name, Class<T> type, T defaultValue);

  /**
   * Checks if the given attribute is present.
   *
   * @param name Name of the attribute to check for
   * @return True if the given attribute is present, false otherwise.
   */
  boolean hasAttribute (String name);

  /**
   * Returns an Iterable of attribute names.
   *
   * @return Iterable of attribute names
   */
  Iterable<String> getAttributeNames ();
}