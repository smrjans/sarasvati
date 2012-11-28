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

package com.googlecode.sarasvati.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ArcTokenSetMember;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.GuardAction;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.NodeTokenSetMember;
import com.googlecode.sarasvati.SarasvatiException;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.event.ArcTokenEvent;
import com.googlecode.sarasvati.event.NodeTokenEvent;
import com.googlecode.sarasvati.util.NodeTokenIdComparator;

public class BacktrackTokenVisitor implements TokenVisitor
{
  protected Engine engine;
  protected NodeToken destinationToken;

  protected SortedSet<NodeToken> queue = new TreeSet<NodeToken>( new NodeTokenIdComparator( false ) );
  protected Set<ArcToken> arcTokenLeaves = new HashSet<ArcToken>();

  protected Set<NodeToken> visited = new HashSet<NodeToken>();
  protected Map<ArcToken,ArcToken> arcTokenMap = new HashMap<ArcToken, ArcToken>();

  protected BacktrackMirrors backtrackMirror = new BacktrackMirrors();
  protected Map<NodeToken,NodeToken> parentMap = new HashMap<NodeToken, NodeToken>();

  protected Set<TokenSet> tokenSets = new HashSet<TokenSet>();

  public BacktrackTokenVisitor (final Engine engine, final NodeToken destinationToken)
  {
    this.engine = engine;
    this.destinationToken = destinationToken;
  }

  @Override
  public void visit (final NodeToken token)
  {
    if ( !token.getNode().isBacktrackable( engine, token ) )
    {
      throw new SarasvatiException( "Can not backtrack node name: " +
                                   token.getNode().getName()  +
                                   "id: " + token.getNode().getId() );
    }

    if ( token.getChildTokens().isEmpty() )
    {
      queue.add( token );
    }
    visited.add( token );
  }

  @Override
  public void visit (final ArcToken token)
  {
    if ( token.getExecutionType() != ExecutionType.Forward  )
    {
      backtrackMirror.addVisited( token );
    }

    if ( token.getChildToken() == null )
    {
      if ( token.getExecutionType() == ExecutionType.UTurn )
      {
        ArcToken mirror = backtrackMirror.getMirror( token );
        arcTokenMap.put( mirror, token );
        queue.add( mirror.getParentToken() );
      }
      else
      {
        arcTokenLeaves.add( token );
      }
    }

    if (token.getExecutionType() == ExecutionType.Backtracked)
    {
      ArcToken mirror = backtrackMirror.getMirror( token );
      parentMap.put( token.getChildToken(), mirror.getParentToken() );
    }
  }

  @Override
  public boolean follow (final ArcToken child)
  {
    if ( child.getExecutionType() != ExecutionType.Forward )
    {
      backtrackMirror.findMirror( child );
    }

    if ( child.getExecutionType() == ExecutionType.Backtracked ||
         child.getExecutionType() == ExecutionType.UTurn ||
         child.getExecutionType() == ExecutionType.UTurnBacktracked )
    {
      return backtrackMirror.hasMirror( child );
    }

    return true;
  }

  private void backtrackLeafArcTokens ()
  {
    for ( ArcToken token : arcTokenLeaves )
    {
      arcTokenMap.put( token, token );

      token.markBacktracked();
      ArcTokenEvent.fireBacktrackedEvent( engine, token );

      queue.add( token.getParentToken() );
      token.getProcess().removeActiveArcToken( token );
    }
  }

  public NodeToken backtrack ()
  {
    backtrackLeafArcTokens();

    NodeToken resultToken = null;

    while ( !queue.isEmpty() )
    {
      NodeToken token = queue.first();
      queue.remove( token );
      if (token.getExecutionType().isBacktracked() )
      {
        continue;
      }

      token.getNode().backtrack( engine, token );

      boolean isDestination = token.equals( destinationToken );

      if ( isDestination )
      {
        resultToken = backtrackCompletedToken( token, ExecutionType.Forward );
      }
      else
      {
        NodeToken backtrackToken = backtrackToken( token );

        if ( backtrackToken != token )
        {
          backtrackToken.markBacktracked();
          NodeTokenEvent.fireBacktrackedEvent( engine, backtrackToken );

          backtrackToken.markComplete();
          NodeTokenEvent.fireCompletedEvent( engine, backtrackToken );
        }
      }
    }

    return resultToken;
  }

  private NodeToken backtrackCompletedToken (final NodeToken token, final ExecutionType executionType)
  {
    token.markBacktracked();
    NodeTokenEvent.fireBacktrackedEvent( engine, token );

    List<ArcToken> parents = new ArrayList<ArcToken>( token.getChildTokens().size() );
    for ( ArcToken childToken : token.getChildTokens() )
    {
      ArcToken parent = arcTokenMap.get( childToken );
      if ( parent == null )
      {
        throw new RuntimeException( "No backtrack found for: " + childToken );
      }
      parents.add( parent );
    }

    NodeToken backtrackToken =
      engine.getFactory().newNodeToken( token.getProcess(),
                                        token.getNode(),
                                        executionType,
                                        parents,
                                        token );
    NodeTokenEvent.fireCreatedEvent( engine, backtrackToken );
    token.getProcess().addNodeToken( backtrackToken );
    shareTokenSets( backtrackToken, token );

    for ( ArcToken parent : parents )
    {
      token.getProcess().removeActiveArcToken( parent );

      parent.markBacktracked();
      ArcTokenEvent.fireBacktrackedEvent( engine, parent );

      parent.markComplete( backtrackToken );
      ArcTokenEvent.fireCompletedEvent( engine, parent );
    }

    return backtrackToken;
  }

  private NodeToken backtrackToken (final NodeToken token)
  {
    NodeToken backtrackToken = token;

    if ( !token.isComplete() )
    {
      token.markComplete();
      NodeTokenEvent.fireCompletedEvent( engine, backtrackToken );

      token.markBacktracked();
      NodeTokenEvent.fireBacktrackedEvent( engine, token );

      token.getProcess().removeActiveNodeToken( token );
    }
    else if ( !token.getExecutionType().isBacktracked() )
    {
      if ( token.getChildTokens().isEmpty() )
      {
        token.markBacktracked();
        NodeTokenEvent.fireBacktrackedEvent( engine, token );
      }
      else
      {
        backtrackToken = backtrackCompletedToken( token, ExecutionType.Backtracked );
        backtrackToken.setGuardAction( GuardAction.SkipNode );
        NodeTokenEvent.fireSkippedEvent( engine, backtrackToken, null );
      }
    }

    for ( ArcToken parent : getParents( token ) )
    {
      boolean backtrackParent = visited.contains( parent.getParentToken() );

      token.getProcess().removeActiveArcToken( parent );
      parent.markBacktracked();
      ArcTokenEvent.fireBacktrackedEvent( engine, parent );

      ArcToken backtrackArcToken =
        engine.getFactory().newArcToken( token.getProcess(),
                                         parent.getArc(),
                                         backtrackParent ? ExecutionType.Backtracked : ExecutionType.UTurn,
                                         backtrackToken,
                                         parent.isTokenSetMember());
      ArcTokenEvent.fireCreatedEvent( engine, backtrackArcToken );
      backtrackToken.getChildTokens().add( backtrackArcToken );
      shareTokenSets( backtrackArcToken, parent );

      if ( backtrackParent && parent.getExecutionType() != ExecutionType.Forward )
      {
        ArcToken mirror = backtrackMirror.getMirror( parent );
        arcTokenMap.put( mirror, backtrackArcToken );
        finishArcTokenBacktrack( backtrackArcToken, mirror );
      }
      else
      {
        finishArcTokenBacktrack( backtrackArcToken, parent );
      }
    }

    return backtrackToken;
  }

  protected void finishArcTokenBacktrack (final ArcToken backtrackArcToken, final ArcToken parent)
  {
    arcTokenMap.put( parent, backtrackArcToken );
    NodeToken nodeTokenParent = parent.getParentToken();
    if ( visited.contains( nodeTokenParent ) )
    {
      queue.add( nodeTokenParent );
      backtrackArcToken.markProcessed();
      ArcTokenEvent.fireProcessedEvent( engine, backtrackArcToken );
    }
    else
    {
      parent.getProcess().enqueueArcTokenForExecution( backtrackArcToken );
    }
  }

  private List<ArcToken> getParents (final NodeToken token)
  {
    NodeToken related = parentMap.get( token );
    return (related == null ? token : related).getParentTokens();
  }

  public NodeToken backtrackDeadEnd (final NodeToken token)
  {
    token.markBacktracked();
    NodeTokenEvent.fireBacktrackedEvent( engine, token );

    List<ArcToken> parents = new ArrayList<ArcToken>( token.getParentTokens().size() );
    for (ArcToken parent : token.getParentTokens() )
    {
      parent.markBacktracked();
      ArcTokenEvent.fireBacktrackedEvent( engine, parent );

      ArcToken backtrackArcToken =
        engine.getFactory().newArcToken( token.getProcess(),
                                         parent.getArc(),
                                         ExecutionType.UTurn,
                                         token,
                                         parent.isTokenSetMember() );
      ArcTokenEvent.fireCreatedEvent( engine, backtrackArcToken );
      token.getChildTokens().add( backtrackArcToken );
      parents.add( backtrackArcToken );
      shareTokenSets( backtrackArcToken, parent );
    }

    NodeToken backtrackToken =
      engine.getFactory().newNodeToken( token.getProcess(),
                                        token.getNode(),
                                        ExecutionType.Forward,
                                        parents,
                                        token );
    NodeTokenEvent.fireCreatedEvent( engine, backtrackToken );
    token.getProcess().addNodeToken( backtrackToken );
    shareTokenSets( backtrackToken, token );

    for ( ArcToken parent : parents )
    {
      parent.markProcessed();
      ArcTokenEvent.fireProcessedEvent( engine, parent );

      parent.markComplete( backtrackToken );
      ArcTokenEvent.fireCompletedEvent( engine, parent );
    }

    return backtrackToken;
  }

  private void shareTokenSets (final NodeToken newToken, final NodeToken origToken)
  {
    for ( NodeTokenSetMember setMember : origToken.getTokenSetMemberships() )
    {
      TokenSet tokenSet = setMember.getTokenSet();
      NodeTokenSetMember newSetMember =
        engine.getFactory().newNodeTokenSetMember( tokenSet, newToken, setMember.getMemberIndex() );
      newToken.getTokenSetMemberships().add( newSetMember );
      tokenSet.getActiveNodeTokens( engine ).add( newToken );
      tokenSets.add( tokenSet );
    }
  }

  private void shareTokenSets (final ArcToken newToken, final ArcToken origToken)
  {
    for ( ArcTokenSetMember setMember : origToken.getTokenSetMemberships() )
    {
      TokenSet tokenSet = setMember.getTokenSet();
      ArcTokenSetMember newSetMember =
        engine.getFactory().newArcTokenSetMember( tokenSet, newToken, setMember.getMemberIndex() );
      newToken.getTokenSetMemberships().add( newSetMember );
      tokenSet.getActiveArcTokens( engine ).add( newToken );
      tokenSets.add( tokenSet );
    }
  }
}