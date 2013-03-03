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

import java.util.Collection;
import java.util.Set;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.TokenSet;

class TokenSetRequiredEvaluator extends MultiTokenRequirementEvaluator<TokenSetRequired>
{
  public TokenSetRequiredEvaluator (final JoinLangEnv env, final TokenSetRequired requirement)
  {
    super( env, requirement );
  }

  @Override
  public void evaluate ()
  {
    Engine engine = getEnv().getEngine();
    TokenSet tokenSet = getEnv().getTokenSet( getRequirement().getTokenSetName() );

    if ( tokenSet == null || !tokenSet.getActiveNodeTokens( engine ).isEmpty() )
    {
      return;
    }

    Node targetNode = getEnv().getInitiatingToken().getArc().getEndNode();
    Collection<ArcToken> activeMembers = tokenSet.getActiveArcTokens( engine );

    for ( ArcToken setMember : activeMembers )
    {
      if ( !setMember.getArc().getEndNode().equals( targetNode ) )
      {
        return;
      }
    }

    // The token set should only be included if it's complete.
    includeTokens( activeMembers );
  }

  @Override
  public void completeJoinAndContributeTokens (final Set<ArcToken> tokens)
  {
    if ( isSatisfied() )
    {
      super.completeJoinAndContributeTokens(tokens);
    }
  }
}