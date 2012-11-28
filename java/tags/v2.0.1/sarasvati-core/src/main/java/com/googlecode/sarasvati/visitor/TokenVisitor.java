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

package com.googlecode.sarasvati.visitor;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.NodeToken;

public interface TokenVisitor
{
  /**
   * Visits the given {@link NodeToken}.
   *
   * @param token The {@link NodeToken} being visited.
   */
  void visit (NodeToken token);

  /**
   * Visits the given {@link ArcToken}.
   *
   * @param token The {@link ArcToken} being visited.
   */
  void visit (ArcToken token);

  /**
   * Returns true if the arc token should be followed.
   *
   * @param child The child arc token in question
   * @return True if the arc token should be followed, false otherwise
   */
  boolean follow (ArcToken child);
}
