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

import java.util.Collection;
import java.util.Collections;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.JoinAction;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.NodeToken;

/**
 * Represents a complete join result, encapsulating the arc tokens
 * which completed the join.
 *
 * @author Paul Lorenz
 */
public final class CompleteJoinResult implements JoinResult
{
  private final Collection<ArcToken> tokens;

  public CompleteJoinResult (final ArcToken token)
  {
    this.tokens = Collections.singletonList( token );
  }

  public CompleteJoinResult (final Collection<ArcToken> tokens)
  {
    this.tokens = tokens;
  }

  /**
   * @see JoinResult#getArcTokensCompletingJoin()
   */
  @Override
  public Collection<ArcToken> getArcTokensCompletingJoin ()
  {
    return tokens;
  }

  /**
   * Always returns {@link JoinAction#Complete}
   *
   * @see com.googlecode.sarasvati.JoinResult#getJoinAction()
   */
  @Override
  public JoinAction getJoinAction ()
  {
    return JoinAction.Complete;
  }

  @Override
  public NodeToken getMergeTarget ()
  {
    throw new IllegalStateException( "getMergeTarget should never be called for a JoinResult with an action of Complete" );
  }
}
