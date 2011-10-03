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

    Copyright 2008 Paul Lorenz
*/

package com.googlecode.sarasvati;

/**
 * Specifies the different execution types. During normal
 * execution, only {@link ExecutionType#Forward} is used.
 * The other execution types are using when backtracking
 * (see {@link Engine#backtrack(NodeToken)}.
 *
 * @author Paul Lorenz
 */
public enum ExecutionType
{
  /**
   * Indicates that the associated token was
   * generated via normal execution and has
   * not been backtracked.
   */
  Forward,

  /**
   * Indicates that the associated token was
   * generated via normal execution but has
   * since been backtracked.
   */
  ForwardBacktracked,

  /**
   * Indicates that the associated token was
   * generated during a backtracking operation,
   * as part of the reverse execution.
   */
  Backtracked,

  /**
   * Indicates that the associated token was
   * generated during a backtracking operation,
   * on an arc token which was backtracked
   * but not as far as the previous node. Execution
   * on this arc therefore looks somewhat like a
   * u-turn, where entry and exit are both on the
   * exit side.
   */
  UTurn,

  /**
   * Indicates that the associated token was
   * generated during a backtracking operation,
   * as a U-turn and has now been backtracked
   * as part of a second backtrack operation.
   */
  UTurnBacktracked;

  /**
   * Returns the backtracked version of the execution type, or itself
   * if the execution type is already a backtracking type.
   *
   * @param isComplete Indicates if the associated token is complete
   *
   * @return The backtracked version of the execution type.
   */
  public ExecutionType getCorrespondingBacktracked (final boolean isComplete)
  {
    if ( this == Forward )
    {
      return ForwardBacktracked;
    }

    if ( this == UTurn )
    {
      return isComplete ? UTurnBacktracked : Backtracked;
    }

    return this;
  }

  /**
   * Returns true if the execution type indicates that the token
   * has been backtracked and false otherwise.

   * @return True if the execution type indicates that the token
   *         has been backtracked and false otherwise.
   */
  public boolean isBacktracked ()
  {
    return this == ForwardBacktracked ||
           this == Backtracked ||
           this == UTurnBacktracked;
  }
}