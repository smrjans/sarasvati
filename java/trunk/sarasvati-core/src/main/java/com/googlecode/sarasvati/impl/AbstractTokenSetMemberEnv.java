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
/**
 * Created on Apr 25, 2008
 */
package com.googlecode.sarasvati.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.googlecode.sarasvati.env.AttributeConverters;
import com.googlecode.sarasvati.env.TokenSetMemberEnv;


public abstract class AbstractTokenSetMemberEnv implements TokenSetMemberEnv
{
  private final int maxMemberIndex;
  protected final Map<String, Object>[] transientAttrs;

  @SuppressWarnings("unchecked")
  public AbstractTokenSetMemberEnv (final int maxMemberIndex)
  {
    this.maxMemberIndex = maxMemberIndex;
    this.transientAttrs = new Map[ maxMemberIndex + 1 ];

    for ( int i = 0; i <= maxMemberIndex; i++ )
    {
      transientAttrs[i] = new HashMap<String, Object>();
    }
  }

  /**
   * @see com.googlecode.sarasvati.env.TokenSetMemberEnv#getAttribute(int, java.lang.String, java.lang.Class)
   */
  @Override
  public <T> T getAttribute (final int memberIndex,
                             final String name,
                             final Class<T> type)
  {
    String value = getAttribute( memberIndex, name );
    return AttributeConverters.stringToObject( value, type );
  }

  /**
   * @see com.googlecode.sarasvati.env.TokenSetMemberEnv#setAttribute(int, java.lang.String, java.lang.Object)
   */
  @Override
  public void setAttribute (final int memberIndex,
                            final String name,
                            final Object value)
  {
    setAttribute( memberIndex, name, AttributeConverters.objectToString( value ) );
  }

  /**
   * @see com.googlecode.sarasvati.env.TokenSetMemberEnv#setAttribute(java.lang.String, java.util.List)
   */
  @Override
  public void setAttribute (final String name,
                            final List<?> values)
  {
    int idx = 0;
    Iterator<?> iter = values.iterator();
    while ( iter.hasNext() && idx <= maxMemberIndex )
    {
      setAttribute( idx, name, iter.next() );
      idx++;
    }
  }

  /**
   * @see com.googlecode.sarasvati.env.TokenSetMemberEnv#removeAttribute(java.lang.String)
   */
  @Override
  public void removeAttribute (final String name)
  {
    for ( int index = 0; index <= maxMemberIndex; index++ )
    {
      removeAttribute( index, name );
    }
  }

  /**
   * @see com.googlecode.sarasvati.env.TokenSetMemberEnv#getTransientAttribute(int, java.lang.String)
   */
  @Override
  public Object getTransientAttribute (final int memberIndex,
                                       final String name)
  {
    if ( memberIndex < 0 || memberIndex >= transientAttrs.length )
    {
      return null;
    }

    return transientAttrs[ memberIndex ].get( name );
  }

  /**
   * @see com.googlecode.sarasvati.env.TokenSetMemberEnv#getTransientAttributeNames(int)
   */
  @Override
  public Iterable<String> getTransientAttributeNames (final int memberIndex)
  {
    if ( memberIndex < 0 || memberIndex >= transientAttrs.length )
    {
      return Collections.emptyList();
    }

    return Collections.unmodifiableSet( transientAttrs[memberIndex].keySet() );
  }


  /**
   * @see com.googlecode.sarasvati.env.TokenSetMemberEnv#hasTransientAttribute(int, java.lang.String)
   */
  @Override
  public boolean hasTransientAttribute (final int memberIndex,
                                        final String name)
  {
    if ( memberIndex < 0 || memberIndex >= transientAttrs.length )
    {
      return false;
    }

    return transientAttrs[memberIndex].containsKey( name );
  }

  /**
   * @see com.googlecode.sarasvati.env.TokenSetMemberEnv#removeTransientAttribute(int, java.lang.String)
   */
  @Override
  public void removeTransientAttribute (final int memberIndex,
                                        final String name)
  {
    if ( memberIndex < 0 || memberIndex >= transientAttrs.length )
    {
      throw new IllegalArgumentException( "Given memberIndex of " + memberIndex + " is out of valid range" );
    }

    transientAttrs[memberIndex].remove( name );
  }

  /**
   * @see com.googlecode.sarasvati.env.TokenSetMemberEnv#removeAttribute(java.lang.String)
   */
  @Override
  public void removeTransientAttribute (final String name)
  {
    for ( int index = 0; index <= maxMemberIndex; index++ )
    {
      transientAttrs[index].remove( name );
    }
  }

  /**
   * @see com.googlecode.sarasvati.env.TokenSetMemberEnv#setTransientAttribute(int, java.lang.String, java.lang.Object)
   */
  @Override
  public void setTransientAttribute (final int memberIndex,
                                     final String name,
                                     final Object value)
  {
    if ( memberIndex < 0 || memberIndex >= transientAttrs.length )
    {
      throw new IllegalArgumentException( "Given memberIndex of " + memberIndex + " is out of valid range" );
    }

    transientAttrs[memberIndex].put( name, value );
  }
}