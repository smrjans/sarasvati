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

import java.util.List;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.JoinAction;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.NodeToken;

public final class IncompleteJoinResult implements JoinResult
{
  public static final JoinResult INSTANCE = new IncompleteJoinResult();

  private IncompleteJoinResult ()
  {
    /* Don't allow this class to be used other than via the INSTANCE */
  }

  /**
   * Always returns {@link JoinAction#Nothing}
   *
   * @see com.googlecode.sarasvati.JoinResult#getJoinAction()
   */
  @Override
  public JoinAction getJoinAction ()
  {
    return JoinAction.Nothing;
  }

  /**
   * Always throws an {@link IllegalStateException}
   * @see JoinResult#getArcTokensCompletingJoin()
   */
  @Override
  public List<ArcToken> getArcTokensCompletingJoin ()
  {
    throw new IllegalStateException( "getArcTokensCompletingJoin should never be called for a JoinResult with an action of Nothing" );
  }

  @Override
  public NodeToken getMergeTarget ()
  {
    throw new IllegalStateException( "getMergeTarget should never be called for a JoinResult with an action of Nothing" );
  }
}