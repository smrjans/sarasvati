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
package com.googlecode.sarasvati.hib;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.impl.AbstractTokenSetMemberEnv;


public class HibTokenSetMemberEnv extends AbstractTokenSetMemberEnv
{
  protected final HibTokenSet tokenSet;
  protected final Map<String,HibTokenSetMemberAttribute>[] attrs;

  @SuppressWarnings("unchecked")
  public HibTokenSetMemberEnv (final HibTokenSet tokenSet)
  {
    super( tokenSet.getMaxMemberIndex() );
    this.tokenSet = tokenSet;
    this.attrs = new Map[ tokenSet.getMaxMemberIndex() ];

    for ( int i = 0; i< tokenSet.getMaxMemberIndex(); i++ )
    {
      attrs[i] = new HashMap<String, HibTokenSetMemberAttribute>();
    }

    for ( final HibTokenSetMemberAttribute property : tokenSet.getMemberAttributes() )
    {
      attrs[ property.memberIndex ].put( property.getName(), property );
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

    final HibTokenSetMemberAttribute property = attrs[ memberIndex ].get( name );
    return property == null ? null : property.getValue();
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

    final Map<String, HibTokenSetMemberAttribute> map = attrs[memberIndex];
    HibTokenSetMemberAttribute property = map.get( name );

    if ( property == null )
    {
      property = new HibTokenSetMemberAttribute( tokenSet, memberIndex, name, value );
      map.put( name, property );
      tokenSet.getMemberAttributes().add( property );
    }
    else
    {
      property.setValue( value );
    }
  }

  @Override
  public void removeAttribute (final int memberIndex,
                               final String name)
  {
    if ( memberIndex < 0 || memberIndex >= attrs.length )
    {
      throw new IllegalArgumentException( "Given memberIndex of " + memberIndex + " is out of valid range" );
    }

    final Map<String, HibTokenSetMemberAttribute> map = attrs[memberIndex];
    final HibTokenSetMemberAttribute property = map.get( name );

    if ( property != null )
    {
      map.remove( name );
      tokenSet.getMemberAttributes().remove( property );
    }
  }
}