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
package com.googlecode.sarasvati.join.lang;

import java.util.List;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.rubric.env.PredicateEnv;

public interface JoinLangEnv extends PredicateEnv
{
  /**
   * Returns the engine instance performing the join
   *
   * @return The engine instance performing the join
   */
  Engine getEngine ();

  /**
   * Returns the arc token which initiated this join operation.
   *
   * @return the arc token which initiated this join operation.
   */
  ArcToken getInitiatingToken();

  /**
   * Returns the arc tokens which are present at the node being joined. Other
   * arc tokens may be involved in the join. These however are those most
   * likely to be used, and are therefore easily accessible and should be
   * cached for performance reasons.
   *
   * @return the arc tokens which are present at the node being joined.
   */
  List<ArcToken> getAvailableTokens ();

  /**
   * Looks for a token set with the given name on
   * the tokens which are arriving at the joining node.
   * If none is found, returns null.
   *
   * @param tokenSetName the token set name
   *
   * @return The token set, or null if no token set with that name is found.
   */
  TokenSet getTokenSet (String tokenSetName);

  /**
   * Returns the newest token on the join node, as long as that token
   * is not backtracked. If appropriate token exist, null is returned.
   *
   * @return The newest token on the join node, as long as that token
   *         is not backtracked. If appropriate token exist, null is returned.
   */
  NodeToken getMergeToken();
}
