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
/**
 * Created on Apr 25, 2008
 */
package com.googlecode.sarasvati;

/**
 * A GuardAction is part of the response from a Node guard. It indicates
 * how an incoming {@link NodeToken} should be handled.
 *
 * @author Paul Lorenz
 * @see Node#guard(Engine, NodeToken)
 */
public enum GuardAction
{
  /**
   * Indicates that an incoming {@link NodeToken} should be accepted
   * and that {@link Node#execute(Engine, NodeToken)} should be invoked.
   */
  AcceptToken,

  /**
   * Indicates that an incoming {@link NodeToken} should be marked as discarded.
   * No further execution will take place with that token.
   */
  DiscardToken,

  /**
   * Indicates that in incoming {@link NodeToken} should skip execution of the
   * current node. Execution will continue on the selected arcs.
   *
   * @see GuardResult#getExitArcForSkip()
   */
  SkipNode;
}
