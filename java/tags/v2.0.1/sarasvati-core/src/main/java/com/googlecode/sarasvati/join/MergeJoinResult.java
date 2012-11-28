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
import java.util.List;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.JoinAction;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.TokenSet;

public final class MergeJoinResult implements JoinResult
{
  private final NodeToken mergeTarget;
  private final Collection<ArcToken> tokens;

  public MergeJoinResult (final ArcToken token, final NodeToken mergeTarget)
  {
    this.tokens = Collections.singletonList( token );
    this.mergeTarget = mergeTarget;
  }

  public MergeJoinResult (final Collection<ArcToken> tokens, final NodeToken mergeTarget)
  {
    this.tokens = tokens;
    this.mergeTarget = mergeTarget;
  }

  /**
   * @see com.googlecode.sarasvati.JoinResult#getArcTokensCompletingJoin()
   */
  @Override
  public Collection<ArcToken> getArcTokensCompletingJoin ()
  {
    return tokens;
  }

  /**
   * Always returns {@link JoinAction#Merge}
   *
   * @see com.googlecode.sarasvati.JoinResult#getJoinAction()
   */
  @Override
  public JoinAction getJoinAction ()
  {
    return JoinAction.Merge;
  }

  /**
   * @see JoinResult#getMergeTarget()
   */
  @Override
  public NodeToken getMergeTarget ()
  {
    return mergeTarget;
  }

  /**
   * Always throws an {@link IllegalStateException}
   *
   * @see com.googlecode.sarasvati.JoinResult#getTerminatingTokenSets()
   */
  @Override
  public List<TokenSet> getTerminatingTokenSets()
  {
    throw new IllegalStateException( "getTerminatingTokenSets should never be called for a JoinResult with an action of Merge" );
  }
}