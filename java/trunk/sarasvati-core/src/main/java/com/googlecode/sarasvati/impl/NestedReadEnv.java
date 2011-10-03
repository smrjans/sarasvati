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
package com.googlecode.sarasvati.impl;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.sarasvati.env.ReadEnv;

public class NestedReadEnv implements ReadEnv
{
  private final ReadEnv outerEnv;
  private final ReadEnv innerEnv;

  public NestedReadEnv (final ReadEnv outerEnv,
                        final ReadEnv innerEnv)
  {
    this.outerEnv = outerEnv;
    this.innerEnv = innerEnv;
  }

  @Override
  public Iterable<String> getAttributeNames ()
  {
    Set<String> names = new HashSet<String>();

    for ( String name : outerEnv.getAttributeNames() )
    {
      names.add( name );
    }

    for ( String name : innerEnv.getAttributeNames() )
    {
      names.add( name );
    }

    return names;
  }

  @Override
  public String getAttribute (final String name)
  {
    return outerEnv.hasAttribute( name ) ? outerEnv.getAttribute( name ) :
                                           innerEnv.getAttribute( name );
  }

  @Override
  public <T> T getAttribute (final String name,
                             final Class<T> type)
  {
    return outerEnv.hasAttribute( name ) ? outerEnv.getAttribute( name, type ) :
                                           innerEnv.getAttribute( name, type );
  }

  @Override
  public <T> T getAttribute (final String name,
                             final Class<T> type,
                             final T defaultValue)
  {
    return outerEnv.hasAttribute( name ) ? outerEnv.getAttribute( name, type, defaultValue ) :
                                           innerEnv.getAttribute( name, type, defaultValue );
  }

  @Override
  public boolean hasAttribute (final String name)
  {
    return outerEnv.hasAttribute( name ) || innerEnv.hasAttribute( name );
  }
}