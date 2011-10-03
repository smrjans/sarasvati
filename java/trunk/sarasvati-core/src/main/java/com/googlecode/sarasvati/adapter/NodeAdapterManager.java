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

import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.Node;

public class NodeAdapterManager
{
  private static final Map<Class<?>, Function<?, Node>> map = new HashMap<Class<?>, Function<?, Node>>();

  public static <T> void registerFactory (final Class<T> clazz, final Function<T, Node> factory)
  {
    map.put( clazz, factory );
  }

  public static <T> void unregisterFactory (final Class<T> clazz)
  {
    map.remove( clazz );
  }

  @SuppressWarnings("unchecked")
  public static <T> T getAdaptor (final Node node, final Class<T> clazz)
  {
    Function<T, Node> function = (Function<T, Node>) map.get( clazz );
    return function == null ? null : function.apply( node );
  }
}
