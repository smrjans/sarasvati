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
 * Contains the valid states that a {@link GraphProcess} may have.
 *
 * @author Paul Lorenz
 */
public enum ProcessState
{
  /**
   * GraphProcess has been created, but has not yet been started. It contains no tokens.
   */
  Created,

  /**
   * GraphProcess has been started and contains active tokens, either node tokens or arc tokens
   * or both.
   */
  Executing,

  /**
   * GraphProcess has no active tokens and is in the process of being completed.
   */
  PendingCompletion,

  /**
   * GraphProcess has been completed. It has no active tokens.
   */
  Completed,

  /**
   * GraphProcess has been scheduled for cancellation. It may have active tokens, but no further
   * action will be taken with those tokens.
   */
  PendingCancel,

  /**
   * GraphProcess has been canceled. It may have active tokens but no further action will be taken
   * with those tokens.
   */
  Canceled
}
