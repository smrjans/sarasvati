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

/**
 * Arc tokens point to arcs in the graph. Arc tokens may be active
 * only briefly, if the node at the end of the arc is ready to
 * execute. However, if the node is a join node, and not all incoming
 * arcs have tokens, an arc token may be active for some time.
 *
 * @author Paul Lorenz
 */
public interface ArcToken extends Token
{
  /**
   * Returns the arc that this arc token points to.
   *
   * @return The associated arc
   */
  Arc getArc ();

  /**
   * Returns the process that this node token belongs to.
   *
   * @return The associated process
   */
  GraphProcess getProcess ();

  /**
   * Returns the node token which directly preceded this
   * arc token.
   *
   * @return The parent NodeToken
   */
  NodeToken getParentToken ();

  /**
   * Returns the node token which was generated from
   * this arc token.
   *
   * @return The child NodeToken
   */
  NodeToken getChildToken ();

  /**
   * Return true if this arc token requires processing, false otherwise.
   * Multiple arc tokens will be created at once, in the case where a
   * node has multiple outgoing arcs. When first created, this method
   * will return true. After the arc token has been executed, this method
   * will return false. An executed arc token may not yet be completed. It
   * may be waiting for other node tokens to arrive at the target node.
   *
   * <br/>
   *
   * Whether an ArcToken has been processed may be important in the case where
   * an earlier arc token generates a NodeToken, which while executing a Node
   * generates and exception and processing is halted. In this case, the
   * unprocessed arc tokens can be found and processed.
   *
   * @return True if this arc token has not yet been processed.
   */
  boolean isPending ();

  /**
   * Marks this arc token as processed.
   *
   * @see ArcToken#isPending()
   */
  void markProcessed ();

  /**
   * Marks this token as being complete, in the sense that it no longer
   * represents an active part of the process. Once a token is marked
   * complete, it is generally only of historical interest.
   *
   * @param child The node token generated from this arc token
   */
  void markComplete (NodeToken child);

  /**
   * Returns the set members for each token set that this
   * token is tied to.
   *
   * @return the set members for each token set that this
   *         token is tied to.
   */
  @Override
  Set<ArcTokenSetMember> getTokenSetMemberships ();
}
