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

import java.util.List;

/**
 * Similar to {@link Env}, except that attributes can be tied
 * to the specific member index of a token set member.
 *
 * @author Paul Lorenz
 */
public interface TokenSetMemberEnv
{
  /**
   * @see Env#getAttribute(String)
   */
  String getAttribute (final int memberIndex, final String name);

  /**
   * @see Env#getAttribute(String, Class)
   */
  <T> T getAttribute (final int memberIndex, final String name, final Class<T> type);

  /**
   * @see Env#getAttributeNames()
   */
  Iterable<String> getAttributeNames (final int memberIndex);

  /**
   * @see Env#hasAttribute(String)
   */
  boolean hasAttribute (final int memberIndex, final String name);

  /**
   * @see Env#setAttribute(String, String)
   */
  void setAttribute (final int memberIndex, final String name, final String value);

  /**
   * @see Env#setAttribute(String, Object)
   */
  void setAttribute (final int memberIndex, final String name, final Object value);

  /**
   * Sets the values for the given attribute name on each memberIndex. Values
   * in the list past the number of member indices will be ignored.
   *
   * @param name The attribute name
   * @param values The list of values to set, one per memberIndex
   */
  void setAttribute (final String name, final List<?> values);

  /**
   * @see Env#removeAttribute(String)
   */
  void removeAttribute (final int memberIndex, final String name);

  /**
   * Removes the given attribute from all member indices.
   *
   * @param name The attribute to remove
   */
  void removeAttribute (final String name);

  /**
   * @see Env#getTransientAttribute(String)
   */
  Object getTransientAttribute (final int memberIndex, final String name);

  /**
   * @see Env#getTransientAttributeNames()
   */
  Iterable<String> getTransientAttributeNames (final int memberIndex);

  /**
   * @see Env#hasTransientAttribute(String)
   */
  boolean hasTransientAttribute (final int memberIndex, final String name);

  /**
   * @see Env#removeTransientAttribute(String)
   */
  void removeTransientAttribute (final int memberIndex, final String name);

  /**
   * Removes the given transient attribute from all member indices.
   *
   * @param name The attribute to remove
   */
  void removeTransientAttribute (final String name);

  /**
   * @see Env#setTransientAttribute(String, Object)
   */
  void setTransientAttribute (final int memberIndex, final String name, final Object value);
}