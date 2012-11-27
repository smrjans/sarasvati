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
package com.googlecode.sarasvati.adapter;

/**
 * Interface for allowing optional extension. A class which implements Adaptable
 * may provide varying adapters. If an adaptor type is requested which is not
 * supported, the Adaptable may indicate this by returning.
 *
 * <br/>
 *
 * Implementors may choose to use a manager and/or factory to handle adaptor creation.
 * This may allows the some functionality to be moved outside of subclasses, which may
 * be useful in cases where the subclass can not be directly manipulated.
 *
 * @author Paul Lorenz
 */
public interface Adaptable
{
  /**
   * An adaptor of the given class is requested. The implementing
   * class may return an instance of the class, or null if the
   * adaptor type is not supported.
   *
   * @param clazz The type of adaptor requested
   * @return An instance of the requested class, or null if the adaptor type is not supported
   */
  <T> T getAdaptor (Class<T> clazz);
}
