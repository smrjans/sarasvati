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

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.join.OrJoinStrategy;
import com.googlecode.sarasvati.join.TokenSetJoinStrategy;

/**
 * Implements a join strategy in which nodes will wait for for all
 * active arc tokens in a given token set to arrive at that node,
 * and for there to be no active node tokens in the token set.
 * <p>
 * A specific token set may be specified by name using the joinParam.
 * If none is specified, the first incomplete token set that the
 * token is a member of will be selected.
 *
 * If the incoming arc token does not belong to a token set, it falls back
 * to using {@link OrJoinStrategy}.
 *
 * @author Paul Lorenz
 */
public class TokenSetOrJoinStrategy extends TokenSetJoinStrategy
{
  /**
   * Called if the node token is not a member of a suitable token set.
   * Falls back to {@link OrJoinStrategy}.
   *
   * @param engine The engine performing the join
   * @param process The currently executing process.
   * @param token The token being join on.
   *
   * @return The result of the {@link OrJoinStrategy} used as a fall back.
   */
  @Override
  public JoinResult performFallbackJoin (final Engine engine, final GraphProcess process, final ArcToken token)
  {
    return JoinType.OR.getJoinStrategy().performJoin( engine, token );
  }
}