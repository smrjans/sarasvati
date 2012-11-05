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
package com.googlecode.sarasvati;

import java.util.Collection;
import java.util.List;


/**
 * Encapsulates the results of a join attempt. Includes whether
 * or not the join is complete. If the join is complete, also
 * includes the list of arc tokens which completed the join.
 *
 * @author Paul Lorenz
 */
public interface JoinResult
{
  /**
   * Returns the action that the join strategy wants to take.
   *
   * @return the action that the join strategy wants to take.
   */
  JoinAction getJoinAction();

  /**
   * Returns the ArcTokens which were required to complete this join. In the case
   * of a join action of {@link JoinAction#Complete} these tokens will will be
   * the parents of the new {@link NodeToken}. In the case of a join action of
   * {@link JoinAction#Merge}, these tokens will be added as parents to the most
   * recent non-backtracked node token created on this node.
   *
   * @return The ArcTokens which were required to complete this join, and will be considered
   *         the parents of the new {@link NodeToken}.
   *
   * @throws IllegalStateException If this is invoked for an action of {@link JoinAction#Nothing}.
   */
  Collection<ArcToken> getArcTokensCompletingJoin();

  /**
   * If the join action is {@link JoinAction#Merge}, then the arc token in question will
   * be made into a parent of the given NodeToken.
   *
   * @return The node token into whose history the arc token will be merged into.
   * @throws IllegalStateException If this is invoked for a non-merge join action
   */
  NodeToken getMergeTarget();

  /**
   * @return The list of tokens which should not be propagated from this point
   */
  List<TokenSet> getTerminatingTokenSets();
}
