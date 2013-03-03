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

import java.util.Date;


/**
 * A GuardResult is returned from {@link Node#guard(Engine, NodeToken)} to indicate
 * what should be done with an incoming {@link NodeToken}.
 *
 * Most of this is imparted by which {@link GuardAction} is returned. In the cases of
 * {@link GuardAction#AcceptToken} and {@link GuardAction#DiscardToken}, no further
 * information is required. In the case of {@link GuardAction#SkipNode}, the guard
 * may wish to select which arcs to exit, based on arc name.
 *
 * @author Paul Lorenz
 */
public interface GuardResult
{
  /**
   * Indicates which {@link GuardAction} has been selected.
   *
   * @return The {@link GuardAction} selected.
   */
  GuardAction getGuardAction ();

  /**
   * If {@link GuardAction#SkipNode} is returned from {@link Node#guard(Engine, NodeToken)},
   * it may indicate an arc name other than the default on which to exit. This can be useful
   * for decision logic.
   *
   * @return The name of the arc or arcs on which to exit this node.
   */
  String getExitArcForSkip ();

  /**
   * If {@value GuardAction#DelayUntil} is returned from {@link Node#guard(Engine, NodeToken)},
   * it must indicate when to reevaluate the guard.
   *
   * @return The date at which a delayed node guard should be reevaluated
   */
  Date getDelayTillTime();
}