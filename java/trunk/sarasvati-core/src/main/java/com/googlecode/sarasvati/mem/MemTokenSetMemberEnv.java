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
package com.googlecode.sarasvati.mem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.impl.AbstractTokenSetMemberEnv;


public class MemTokenSetMemberEnv extends AbstractTokenSetMemberEnv
{
  protected final Map<String,String>[] attrs;

  @SuppressWarnings("unchecked")
  public MemTokenSetMemberEnv (final MemTokenSet tokenSet)
  {
    super( tokenSet.getMaxMemberIndex() );
    this.attrs = new Map[ tokenSet.getMaxMemberIndex() + 1 ];

    for ( int i = 0; i <= tokenSet.getMaxMemberIndex(); i++ )
    {
      attrs[i] = new HashMap<String, String>();
    }
  }

  @Override
  public String getAttribute (final int memberIndex,
                              final String name)
  {
    if ( memberIndex < 0 || memberIndex >= attrs.length )
    {
      return null;
    }

    return attrs[ memberIndex ].get( name );
  }

  @Override
  public Iterable<String> getAttributeNames (final int memberIndex)
  {
    if ( memberIndex < 0 || memberIndex >= attrs.length )
    {
      return Collections.emptyList();
    }

    return Collections.unmodifiableSet( attrs[memberIndex].keySet() );
  }

  @Override
  public boolean hasAttribute (final int memberIndex,
                               final String name)
  {
    if ( memberIndex < 0 || memberIndex >= attrs.length )
    {
      return false;
    }

    return attrs[memberIndex].containsKey( name );
  }

  @Override
  public void setAttribute (final int memberIndex,
                            final String name,
                            final String value)
  {
    if ( memberIndex < 0 || memberIndex >= attrs.length )
    {
      throw new IllegalArgumentException( "Given memberIndex of " + memberIndex + " is out of valid range" );
    }

    attrs[memberIndex].put( name, value );
  }

  @Override
  public void removeAttribute (final int memberIndex,
                               final String name)
  {
    if ( memberIndex < 0 || memberIndex >= attrs.length )
    {
      throw new IllegalArgumentException( "Given memberIndex of " + memberIndex + " is out of valid range" );
    }

    attrs[memberIndex].remove( name );
  }
}