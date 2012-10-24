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
package com.googlecode.sarasvati.join;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.util.SvUtil;

/**
 * Implements a join strategy in which nodes will wait for arc tokens from
 * the same token set index to be present on all incoming arcs before completing
 * the join. If the token does not belong to a token set, it will wait for
 * token with no token set to be present on all incoming arcs before completing
 * the join.
 *
 * @author Paul Lorenz
 */
public class TokenSetAndJoinStrategy extends AndJoinStrategy
{
  /**
   * @see com.googlecode.sarasvati.join.AndJoinStrategy#getActiveTokens(Engine, ArcToken)
   */
  @Override
  protected Collection<ArcToken> getActiveTokens(final Engine engine, final ArcToken arcToken)
  {
    final TokenSet ts = SvUtil.getTokenSet(arcToken);
    if (ts != null)
    {
      int index = SvUtil.getTokenSetMember(arcToken, ts.getName()).getMemberIndex();
      Collection<ArcToken> tokens = ts.getActiveArcTokens(engine);
      List<ArcToken> result = new ArrayList<ArcToken>(tokens.size());
      for (final ArcToken t : tokens)
      {
        if (SvUtil.getTokenSetMember(t, ts.getName()).getMemberIndex() == index)
        {
          result.add(t);
        }
      }
      return result;
    }
    Collection<ArcToken> tokens = arcToken.getProcess().getActiveArcTokens();
    List<ArcToken> result = new ArrayList<ArcToken>(tokens.size());
    for (final ArcToken t : tokens)
    {
      if (t.getTokenSetMemberships().isEmpty())
      {
        result.add(t);
      }
    }
    return result;
  }

  /* (non-Javadoc)
   * @see com.googlecode.sarasvati.join.AndJoinStrategy#performJoin(com.googlecode.sarasvati.Engine, com.googlecode.sarasvati.ArcToken)
   */
  @Override
  public JoinResult performJoin(final Engine engine, final ArcToken token)
  {
    // TODO Auto-generated method stub
    return super.performJoin(engine, token);
  }


}
