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

import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.env.TokenSetMemberEnv;

/**
 * A token set is way of associating tokens together and allowing them to have
 * state relative to one another. This takes two forms:
 *
 * <ul>
 *   <li>Each token has an index in the token set. This allows a token in the
 *       set to be distinguished from other (useful for template style nodes).
 *   <li>The token set has it's own environment. The token set environment may
 *       contain attributes visible to all tokens in the set, or just tokens
 *       with a specific index.
 * </ul>
 *
 * @author Paul Lorenz
 */
public interface TokenSet
{
  /**
   * Returns the process that this token set belongs to.
   *
   * @return The process that this token set belongs to.
   */
  GraphProcess getProcess ();

  /**
   * Returns the name given to this token set.
   *
   * @return The name given to this token set.
   */
  String getName ();

  /**
   * Returns the list of active (non-completed) arc tokens which
   * are members of this token set.
   *
   * @param engine The engine executing the current process. May
   *               be required in order to load the token list.
   *
   * @return the collection of active (non-completed) arc tokens which
   *         are members of this token set.
   */
  Collection<ArcToken> getActiveArcTokens (Engine engine);

  /**
   * Returns the list of active (non-completed) node tokens which
   * are members of this token set.
   *
   * @param engine The engine executing the current process. May
   *               be required in order to load the token list.
   *
   * @return the list of active (non-completed) node tokens which
   *         are members of this token set.
   */
  Collection<NodeToken> getActiveNodeTokens (Engine engine);

  /**
   * Returns the maximum member index that a member of this token
   * set may have.
   *
   * @return the maximum member index that a member of this token
   *         set may have
   */
  int getMaxMemberIndex ();

  /**
   * Returns true if the token set has been join on and contains no more
   * active tokens, and false otherwise.
   *
   * @return True if the token set has been join on and contains no more
   *         active tokens, and false otherwise.
   */
  boolean isComplete ();

  /**
   * Marks this token set as being complete, in the sense that a
   * token set join has been performed on the token set.
   */
  void markComplete (Engine engine);

  /**
   * If a node or arc token in a previously completed token set is backtracked
   * into, the token set should be marked as incomplete again.
   *
   * @param engine The engine currently executing
   */
  void reactivateForBacktrack (Engine engine);

  /**
   * A token set provides an environment into which attributes can be stored.
   * This environment is shared across all tokens which are members of the
   * environment.
   *
   * @return The token set environment
   */
  Env getEnv ();

  /**
   * In addition to provide an environment which is shared across all members
   * of the token set (see {@link TokenSet#getEnv()}), the token set also
   * provides an environment which allows setting attributes per member index.
   * <p>
   * The member environment also allows setting attributes per index via list.
   *
   * @return The token set member environment.
   */
  TokenSetMemberEnv getMemberEnv ();
}