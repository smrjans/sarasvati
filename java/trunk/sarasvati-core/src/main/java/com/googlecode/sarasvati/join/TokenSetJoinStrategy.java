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

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.JoinStrategy;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.util.SvUtil;

/**
 * Implements a join strategy in which nodes will wait for for all
 * active arc tokens in a given token set to arrive at that node,
 * and for there to be no active node tokens in the token set.
 * <p>
 * A specific token set may be specified by name using the joinParam.
 * If none is specified, the first incomplete token set that the
 * token is a member of will be selected.
 *
 * If the incoming arc token does not belong to a token set, an {@link IllegalStateException}
 * will be thrown. This behavior could be changed in a subclass by overriding
 * {@link TokenSetJoinStrategy#performFallbackJoin(Engine, GraphProcess, ArcToken)}.
 *
 * @author Paul Lorenz
 */
public class TokenSetJoinStrategy implements JoinStrategy
{
  public TokenSet getTokenSet (final ArcToken token)
  {
    return SvUtil.getTokenSet(token);
  }

  /**
   * Called if the node token is not a member of a suitable token set.
   * Throws {@link IllegalStateException} by default, but this behavior
   * may be overridden in a subclass.
   *
   * @param engine The engine performing the join
   * @param process The currently executing process.
   * @param token The token being join on.
   *
   * @return The result of the fallback join strategy. The default implementation
   *         does return anything, rather it throws an {@link IllegalStateException}.
   */
  public JoinResult performFallbackJoin (final Engine engine, final GraphProcess process, final ArcToken token)
  {
    throw new IllegalStateException( "Token " + token + " does not belong to the appropriate token set" );
  }

  @Override
  public JoinResult performJoin (final Engine engine, final ArcToken token)
  {
    TokenSet tokenSet = getTokenSet( token );

    if ( tokenSet == null )
    {
      return performFallbackJoin( engine, token.getProcess(), token );
    }

    Node targetNode = token.getArc().getEndNode();

    Collection<ArcToken> activeMembers = tokenSet.getActiveArcTokens( engine );

    for ( ArcToken setMember : activeMembers )
    {
      if ( !setMember.getArc().getEndNode().equals( targetNode ) )
      {
        return IncompleteJoinResult.INSTANCE;
      }
    }

    ArrayList<ArcToken> result = new ArrayList<ArcToken>( activeMembers );
    return new CompleteJoinResult( result, tokenSet.getName() );
  }
}