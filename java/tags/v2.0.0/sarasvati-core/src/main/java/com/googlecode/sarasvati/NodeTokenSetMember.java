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

import com.googlecode.sarasvati.env.Env;

/**
 * Extends the TokenSetMember interface to narrow the Token
 * to a NodeToken.
 *
 * @author Paul Lorenz
 */
public interface NodeTokenSetMember extends TokenSetMember
{
  /**
   * @see com.googlecode.sarasvati.TokenSetMember#getToken()
   */
  @Override
  NodeToken getToken ();

  /**
   * Each TokenSet can set attributes specific to a given memberIndex. The
   * {@link Env} returned from a token set member will access only attributes
   * that are specific to the its member index.
   *
   * @return The environment for this token set member
   */
  Env getEnv ();
}
