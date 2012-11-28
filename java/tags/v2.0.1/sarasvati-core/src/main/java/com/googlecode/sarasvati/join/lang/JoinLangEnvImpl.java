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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.Token;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.impl.NodeTokenComparator;
import com.googlecode.sarasvati.rubric.env.PredicateEnv;
import com.googlecode.sarasvati.util.SvUtil;

public class JoinLangEnvImpl implements JoinLangEnv
{
  private final Engine engine;
  private final PredicateEnv predicateEnv;
  private final ArcToken initiatingToken;
  private List<ArcToken> availableTokens = null;
  private Map<String, TokenSet> tokenSets;

  private boolean mergeTokenInitialized = false;
  private NodeToken mergeToken = null;

  public JoinLangEnvImpl (final Engine engine, final ArcToken initiatingToken, final PredicateEnv predicateEnv)
  {
    this.engine = engine;
    this.initiatingToken = initiatingToken;
    this.predicateEnv = predicateEnv;
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.JoinLangEnv#getEngine()
   */
  @Override
  public Engine getEngine ()
  {
    return engine;
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.JoinLangEnv#getAvailableTokens()
   */
  @Override
  public List<ArcToken> getAvailableTokens ()
  {
    if ( availableTokens == null )
    {
      availableTokens = new ArrayList<ArcToken>();

      GraphProcess process = getInitiatingToken().getProcess();
      Node targetNode = getInitiatingToken().getArc().getEndNode();

      for ( ArcToken arcToken : process.getActiveArcTokens() )
      {
        if ( arcToken.getArc().getEndNode().equals( targetNode ) )
        {
          availableTokens.add( arcToken );
        }
      }
    }

    return availableTokens;
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.JoinLangEnv#getInitiatingToken()
   */
  @Override
  public ArcToken getInitiatingToken ()
  {
    return initiatingToken;
  }

  /**
   * @see com.googlecode.sarasvati.rubric.env.PredicateEnv#evalPredicate(java.lang.String)
   */
  @Override
  public boolean evalPredicate (final String predicate)
  {
    if ( "first".equals( predicate ) )
    {
      initializeMergeTokenIfNecesary();
      return mergeToken == null;
    }
    return predicateEnv.evalPredicate( predicate );
  }

  private void initializeMergeTokenIfNecesary ()
  {
    if ( !mergeTokenInitialized )
    {
      Node targetNode = initiatingToken.getArc().getEndNode();
      Collection<NodeToken> nodeTokens = initiatingToken.getProcess().getTokensOnNode( targetNode, engine );

      if ( !nodeTokens.isEmpty() )
      {
        NodeToken newestToken = Collections.max( nodeTokens, NodeTokenComparator.INSTANCE );

        if ( !newestToken.getExecutionType().isBacktracked() )
        {
          mergeToken = newestToken;
        }
      }
    }
  }

  @Override
  public NodeToken getMergeToken ()
  {
    initializeMergeTokenIfNecesary();
    return mergeToken;
  }

  @Override
  public TokenSet getTokenSet (final String tokenSetName)
  {
    if ( tokenSets == null )
    {
      tokenSets = new HashMap<String, TokenSet>();
    }

    if ( tokenSets.containsKey( tokenSetName ) )
    {
      return tokenSets.get( tokenSetName );
    }

    for ( Token token : getAvailableTokens() )
    {
      TokenSet tokenSet = SvUtil.getTokenSet( token, tokenSetName );
      if ( tokenSet != null )
      {
        tokenSets.put( tokenSetName, tokenSet );
        return tokenSet;
      }
    }

    tokenSets.put( tokenSetName, null );
    return null;
  }
}