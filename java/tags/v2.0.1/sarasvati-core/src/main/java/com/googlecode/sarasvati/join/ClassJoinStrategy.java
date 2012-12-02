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
package com.googlecode.sarasvati.join;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.JoinStrategy;
import com.googlecode.sarasvati.SarasvatiException;
import com.googlecode.sarasvati.util.SvUtil;

/**
 * Implements a join strategy that assumes the join param
 * is the name of class which implements JoinStrategy.
 *
 * A instance of the specified class will be delegated to.
 *
 * @author Paul Lorenz
 */
public class ClassJoinStrategy implements JoinStrategy
{
  private static final Map<String, JoinStrategy> instanceMap = new ConcurrentHashMap<String, JoinStrategy>( 7, 0.75f, 2 );

  protected JoinStrategy getJoinStrategy (final String className)
  {
    JoinStrategy joinStrategy = instanceMap.get( className );
    if ( joinStrategy == null )
    {
      try
      {
        joinStrategy = (JoinStrategy) SvUtil.newInstanceOf( className, "JoinStrategy" );
        instanceMap.put( className, joinStrategy );
      }
      catch (Exception e)
      {
        throw new SarasvatiException( "Failed to instantiate new instance of JoinStrategy '" + className + "'", e );
      }
    }

    return joinStrategy;
  }

  @Override
  public JoinResult performJoin (final Engine engine, final ArcToken token)
  {
    final String className = token.getArc().getEndNode().getJoinParam();
    return getJoinStrategy( className ).performJoin( engine, token );
  }
}