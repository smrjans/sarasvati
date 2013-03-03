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

import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;

/**
 * Env extends {@link ReadEnv} to allow add/changed/removing attributes. It also
 * allows getting/setting transient attributes.
 *
 * @author Paul Lorenz
 */
public interface Env extends ReadEnv
{
  /**
   * Sets the attribute of the given name to the given string value.
   *
   * @param name The name of the attribute to set.
   * @param value The value to set the attribute to
   */
  void setAttribute (String name, String value);

  /**
   * Sets the attribute of the given name to the given value.
   *
   * @param name The name of the attribute to set.
   * @param value The value to set the attribute to
   */
  void setAttribute (String name, Object value);

  /**
   * Unsets any attribute with the given name.
   *
   * @param name The name of the attribute to remove
   */
  void removeAttribute (String name);

  /**
   * Set a transient attribute of the environment. The attribute will
   * only be in the environment as long as the associated {@link GraphProcess} or
   * {@link NodeToken} instance is in memory. In other words, these
   * attributes will not be persisted to whatever the backing store
   * is.
   *
   * <br/>
   * If a transient value already existed for the given name, the value
   * will be overwritten
   * <br/>
   *
   * In the case of a memory back implementation, these transient
   * attributes will have the same lifetime as other attributes.
   *
   * @param name The name of the attribute
   * @param value The value of the attribute
   */
  void setTransientAttribute (String name, Object value);

  /**
   * Returns true if this transient attribute exists in the environment
   * and false otherwise.
   *
   * @param name The transient attribute name
   * @return True if the given transient attribute exists in this environment
   */
  boolean hasTransientAttribute (String name);

  /**
   * Returns the value associated with the given named transient attribute.
   *
   * @param name The attribute name
   * @return The attribute value, or null if a value has not been set.
   */
  Object getTransientAttribute (String name);

  /**
   * Remove the given attribute from the environment.
   *
   * @param name The attribute name
   */
  void removeTransientAttribute (String name);

  /**
   * Returns an {@link Iterable} of transient attribute names.
   *
   * @return {@link Iterable} of transient attribute names
   */
  Iterable<String> getTransientAttributeNames ();

  /**
   * Copy all attributes from given env, including transient attributes.
   *
   * @param env The environment to copy
   */
  void importEnv (Env env);
}