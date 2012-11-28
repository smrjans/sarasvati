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
package com.googlecode.sarasvati.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.env.AttributeConverters;
import com.googlecode.sarasvati.env.Env;

/**
 * Implements {@link Env} using a Map
 *
 * @author Paul Lorenz
 */
public class MapEnv implements Env
{
  public static final MapEnv READONLY_EMPTY_INSTANCE =
    new MapEnv( Collections.unmodifiableMap( new HashMap<String, String>() ) );

  protected Map<String,String> attributes;
  protected Map<String,Object> transientAttributes;

  /**
   * Create a MapEnv which uses a {@link HashMap} for storage.
   */
  public MapEnv ()
  {
    this.attributes = new HashMap<String, String>();
    this.transientAttributes = new HashMap<String,Object>();
  }

  public MapEnv (final Map<String,String> attributes)
  {
    this.attributes = attributes;
    this.transientAttributes = new HashMap<String,Object>();
  }

  @Override
  public String getAttribute (final String name)
  {
    return attributes.get( name );
  }

  @Override
  public <T> T getAttribute (final String name,
                             final Class<T> type)
  {
    String value = getAttribute( name );
    return AttributeConverters.stringToObject( value, type );
  }

  @Override
  public <T> T getAttribute (final String name,
                             final Class<T> type,
                             final T defaultValue)
  {
    String value = getAttribute( name );
    return AttributeConverters.stringToObject( value, type, defaultValue );
  }

  @Override
  public boolean hasAttribute (final String name)
  {
    return attributes.containsKey( name );
  }

  @Override
  public void removeAttribute (final String name)
  {
    attributes.remove( name );
  }

  @Override
  public void setAttribute (final String name,
                            final String value)
  {
    attributes.put( name, value );
  }

  @Override
  public void setAttribute (final String name,
                            final Object value)
  {
    setAttribute( name, AttributeConverters.objectToString( value ) );
  }

  @Override
  public Iterable<String> getAttributeNames ()
  {
    return attributes.keySet();
  }

  @Override
  public void setTransientAttribute (final String name,
                                     final Object value)
  {
    transientAttributes.put( name, value );
  }

  @Override
  public boolean hasTransientAttribute (final String name)
  {
    return transientAttributes.containsKey( name );
  }

  @Override
  public Object getTransientAttribute (final String name)
  {
    return transientAttributes.get( name );
  }

  @Override
  public void removeTransientAttribute (final String name)
  {
    transientAttributes.remove( name );
  }

  @Override
  public Iterable<String> getTransientAttributeNames()
  {
    return transientAttributes.keySet();
  }

  @Override
  public void importEnv (final Env env)
  {
    for ( String name : env.getAttributeNames() )
    {
      setAttribute( name, env.getAttribute( name ) );
    }

    for ( String name : env.getTransientAttributeNames() )
    {
      setTransientAttribute( name, env.getTransientAttribute( name ) );
    }
  }
}