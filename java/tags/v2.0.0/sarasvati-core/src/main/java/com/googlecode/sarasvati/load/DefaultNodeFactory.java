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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.load.properties.DOMToObjectLoadHelper;

public class DefaultNodeFactory implements NodeFactory
{
  private static final Map<String, Class<? extends CustomNode>> globalCustomTypeMap = new HashMap<String, Class<? extends CustomNode>>();

  public static void addGlobalCustomType (final String type,
                                          final Class<? extends CustomNode> clazz)
  {
    globalCustomTypeMap.put( type, clazz );
  }

  protected final Map<String, Class<? extends Node>> typeMap = new HashMap<String, Class<? extends Node>>();
  protected final Class<? extends Node> defaultClass;

  public DefaultNodeFactory (final Class<? extends Node> defaultClass)
  {
    this.defaultClass = defaultClass;
  }

  public void addType (final String type, final Class<? extends Node> clazz)
  {
    typeMap.put( type, clazz );
  }

  @Override
  public Map<String, String> loadCustom (final Node node, final Object custom)
  {
    if ( custom == null )
    {
      return Collections.emptyMap();
    }

    if ( custom instanceof Element )
    {
      return DOMToObjectLoadHelper.setBeanValues( node, (Element)custom );
    }

    throw new SarasvatiLoadException( "Expected DOM Element, but got instance of " + custom.getClass().getName() +
                                      " while loading node of type " + node.getType() );
  }

  @Override
  public Node newNode (final String type)
  {
    Class<? extends Node> clazz = typeMap.get( type );

    if ( clazz == null )
    {
      clazz = globalCustomTypeMap.get( type );
    }

    if ( clazz == null )
    {
      clazz = defaultClass;
    }

    try
    {
      return clazz.newInstance();
    }
    catch ( final InstantiationException e )
    {
      throw new SarasvatiLoadException( "Unable to create new instance for type '" + type + "'", e );
    }
    catch ( final IllegalAccessException e )
    {
      throw new SarasvatiLoadException( "Unable to create new instance for type '" + type + "'", e );
    }
  }
}