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

    Copyright 2008-2009 Paul Lorenz
*/
package com.googlecode.sarasvati;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.googlecode.sarasvati.env.Env;

/**
 * Node tokens point to nodes in the graph. Unlike arc tokens,
 * they may have attributes associated with them.
 *
 * @author Paul Lorenz
 */
public interface NodeToken extends Token
{
  /**
   * A unique identifier for this node token. May
   * be globally unique, or may be unique for just
   * the containing GraphProcess.
   *
   * @return A unique identifier for this node token
   */
  Long getId ();

  /**
   * Returns the node that this token points to.
   *
   * @return The node associated node.
   */
  Node getNode ();

  /**
   * Returns the process that this node token belongs to.
   *
   * @return The associated process
   */
  GraphProcess getProcess ();

  /**
   * Returns the list of arc tokens which were involved in creating this NodeToken. If this
   * node was the (or one of the) initial tokens created on the start nodes, this list
   * will be empty.
   *
   * @return The list of arc tokens which were involved in creating this NodeToken.
   */
  List<ArcToken> getParentTokens ();

  /**
   * Returns the list of ArcTokens which have this token as their parent. If this NodeToken
   * has not yet been completed, or no arcs match the selected arc name when the node was
   * completed, the list will be empty.
   *
   * @return The list of ArcTokens which have this token as their parent
   */
  List<ArcToken> getChildTokens ();

  /**
   * Returns the full environment. This will include variables set on the
   * token as well as variables set on the process. If a variable with the
   * same name exists in both the token and process environments, the token
   * variable will be returned.
   *
   * NOTE: Changes can only be made at the token level. Updates or removals
   *       will only be applied to the token attributes.
   *
   * @return An {@link Env} containing variables from this token and from the {@link GraphProcess}
   * @see GraphProcess#getEnv()
   */
  Env getFullEnv ();

  /**
   * Returns the environment for just this token. Variables defined on the
   * process will not be found.
   *
   * @return An {@link Env} containing variables from just this token
   */
  Env getEnv ();

  /**
   * A NodeToken is evaluated by the {@link Node#guard(Engine, NodeToken)}
   * method. setGuardAction will be called by the engine to record the result
   * of the guard.
   *
   * @param action The {@link GuardAction} taken with this NodeToken.
   * @see Node#guard(Engine, NodeToken)
   */
  void setGuardAction (GuardAction action);

  /**
   * Returns the GuardAction that was returned from the Node guard
   * called on this token or null if the guard has not completed
   * or been called.
   *
   * @return The result of the Node guard called on this token, or
   *         null if the guard has not completed or been called.
   */
  GuardAction getGuardAction ();

  /**
   * Returns the date/time the node token was created.
   *
   * @return The date/time the node token was created.
   */
  Date getCreateDate ();

  /**
   * Returns the date/time the node token was completed
   *
   * @return The date/time the node token was completed.
   */
  Date getCompleteDate ();

  /**
   * Marks this token as being complete, in the sense that it no longer
   * represents an active part of the process. Once a token is marked
   * complete, it is generally only of historical interest.
   */
  void markComplete ();

  /**
   * Returns the first {@link TokenSet} of the given name that
   * this token is tied to, or null if the token is not associated
   * with a token of this name. As a general principal, tokens
   * should only belong to one token set with a given name.
   * <p>
   * This method must also remove the token from the active sets of
   * any token sets that it belongs to.

   * @param name The token set name
   *
   * @return the first {@link TokenSet} of the given name that
   *         this token is tied to, or null if the token is not associated
   *         with a token of this name.
   */
  TokenSet getTokenSet (String name);

  /**
   * Like {@link NodeToken#getTokenSet(String)}, except returns the link
   * rather than the the token set itself.
   *
   * @see NodeToken#getTokenSet(String)
   */
  NodeTokenSetMember getTokenSetMember (String name);

  /**
   * Returns the set members for each token set that this
   * token is tied to.
   */
  @Override
  Set<NodeTokenSetMember> getTokenSetMemberships ();
}