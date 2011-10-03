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

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.JoinStrategy;

/**
 * Implements a join strategy where a node will complete a join
 * whenever any arc token arrives.
 *
 * @author Paul Lorenz
 */
public class OrJoinStrategy implements JoinStrategy
{
  @Override
  public JoinResult performJoin (final Engine engine, final ArcToken token)
  {
    return new CompleteJoinResult( token );
  }
}
