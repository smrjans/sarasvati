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

import java.util.Set;

import com.googlecode.sarasvati.visitor.TokenVisitor;

/**
 * The set of tokens in a process represent the current state
 * of the workflow. There are two types of tokens, node tokens
 * and arc tokens. Node tokens point at nodes and arc tokens
 * point to arcs. Tokens are never moved. They may be marked
 * as complete and new tokens will be created in the areas of
 * the workflow now in progress.
 *
 * @author Paul Lorenz
 */
public interface Token
{
  /**
   * Returns true if this token has been completed, false otherwise.
   *
   * @return True if this token has been completed
   */
  boolean isComplete ();

  /**
   * Returns the {@link ExecutionType} of the token. For a normal
   * token, which occurs as part of graph execution, the
   * ExecutionType will be {@link ExecutionType#Forward}.
   *
   * @return The token execution type
   */
  ExecutionType getExecutionType ();

  /**
   * Marks the token as backtracked by changing the execution
   * type. If the execution type is {@link ExecutionType#Forward}
   * it gets changed to {@link ExecutionType#ForwardBacktracked}.
   * If the execution type is {@link ExecutionType#UTurn} it
   * gets changed to {@link ExecutionType#UTurnBacktracked}.
   * If the execution type is {@link ExecutionType#Backtracked} it
   * stays the same.
   *
   * Any other type of change should not occur.
   */
  void markBacktracked ();

  /**
   * Should call the appropriate visit method on the given
   * visitor.
   *
   * @param visitor The visitor which is traversing the
   *                execution history.
   */
  void accept (TokenVisitor visitor);

  /**
   * Returns the set of TokenSetMembers tying this token to a token set.
   *
   * @return The set of TokenSetMembers tying this token to a token set.
   */
  Set<? extends TokenSetMember> getTokenSetMemberships ();
}