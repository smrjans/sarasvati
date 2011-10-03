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

/**
 * Represents the link between a {@link Token} and {@link TokenSet}.
 * This mainly exists as a separate entity in order to track the
 * member index.
 *
 * @author Paul Lorenz
 */
public interface TokenSetMember
{
  /**
   * Returns the token set
   *
   * @return The token set
   */
  TokenSet getTokenSet ();

  /**
   * Returns the token.
   *
   * @return The token
   */
  Token getToken ();

  /**
   * Returns the index assigned to the token within the token set.
   *
   * @return The index assigned to the token within the token set.
   */
  int getMemberIndex ();
}
