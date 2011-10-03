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
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.join.lang.JoinLangEnvImpl;

/**
 * Class for JoinStrategies which wish to first attempt a merge, then
 * fall back to another join strategy if that fails.
 *
 * @author Paul Lorenz
 */
public class MergeJoinStrategy implements JoinStrategy
{
  private JoinStrategy fallbackJoinStrategy;

  public MergeJoinStrategy (final JoinStrategy fallbackJoinStrategy)
  {
    this.fallbackJoinStrategy = fallbackJoinStrategy;
  }

  @Override
  public JoinResult performJoin (final Engine engine, final ArcToken token)
  {
    JoinLangEnvImpl joinLangEnv = new JoinLangEnvImpl( engine, token, null );
    NodeToken mergeToken = joinLangEnv.getMergeToken();

    if ( mergeToken != null )
    {
      return new MergeJoinResult( token, mergeToken );
    }

    return fallbackJoinStrategy.performJoin( engine, token );
  }
}
