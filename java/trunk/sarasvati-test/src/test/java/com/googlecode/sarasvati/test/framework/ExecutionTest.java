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
package com.googlecode.sarasvati.test.framework;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.event.EventActions;
import com.googlecode.sarasvati.event.ExecutionEvent;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.event.ExecutionListener;
import com.googlecode.sarasvati.load.GraphLoader;
import com.googlecode.sarasvati.xml.XmlLoader;

public class ExecutionTest
{
  public static class DuplicateEventDetector implements ExecutionListener
  {
    private Set<NodeToken> backtrackedNodeTokens = new HashSet<NodeToken>();
    private Set<ArcToken> backtrackedArcTokens = new HashSet<ArcToken>();

    private Set<NodeToken> completedNodeTokens = new HashSet<NodeToken>();
    private Set<ArcToken> completedArcTokens = new HashSet<ArcToken>();


    @Override
    public EventActions notify (final ExecutionEvent event)
    {
      if ( event.getEventType() == ExecutionEventType.NODE_TOKEN_BACKTRACKED )
      {
        if ( backtrackedNodeTokens.contains( event.getNodeToken() ) )
        {
          throw new RuntimeException( "Node Token " + event.getNodeToken() + " notified of backtracking twice" );
        }
        backtrackedNodeTokens.add( event.getNodeToken() );
      }

      if ( event.getEventType() == ExecutionEventType.ARC_TOKEN_BACKTRACKED )
      {
        if ( backtrackedArcTokens.contains( event.getArcToken() ) )
        {
          throw new RuntimeException( "Arc Token " + event.getNodeToken() + " notified of backtracking twice" );
        }
        backtrackedArcTokens.add( event.getArcToken() );
      }

      if ( event.getEventType() == ExecutionEventType.NODE_TOKEN_COMPLETED )
      {
        if ( completedNodeTokens.contains( event.getNodeToken() ) )
        {
          throw new RuntimeException( "Node Token " + event.getNodeToken() + " notified of completion twice" );
        }
        completedNodeTokens.add( event.getNodeToken() );
      }

      if ( event.getEventType() == ExecutionEventType.ARC_TOKEN_COMPLETED )
      {
        if ( completedArcTokens.contains( event.getArcToken() ) )
        {
          throw new RuntimeException( "Arc Token " + event.getNodeToken() + " notified of completion twice" );
        }
        completedArcTokens.add( event.getArcToken() );
      }

      return null;
    }
  }

  private Engine engine;

  @Before
  public void setup ()
  {
    engine = TestEnv.getEngine();
    engine.addExecutionListener( DuplicateEventDetector.class, ExecutionEventType.values());
  }

  @After
  public void cleanup()
  {
    TestEnv.commitSession();
  }

  protected Graph ensureLoaded (final String name) throws Exception
  {
    final File basePath = new File( "common/unit-test/" );
    assert basePath.exists();
    final GraphLoader<? extends Graph> loader = engine.getLoader();

    if ( !loader.isLoaded( name ) )
    {
      loader.loadDefinition( new XmlLoader(), new File( basePath, name + ".wf.xml" ) );
    }
    engine = TestEnv.commitSession();
    return engine.getRepository().getLatestGraph( name );
  }

  public NodeToken getActiveToken(final GraphProcess p, final String nodeName)
  {
    return getActiveToken(p, new TokenOnNodePredicate(nodeName));
  }

  public NodeToken getActiveToken(final GraphProcess p, final TestPredicate<NodeToken> test)
  {
    for ( NodeToken token : p.getActiveNodeTokens() )
    {
      if ( test.matches(token) )
      {
        return token;
      }
    }

    Assert.assertTrue( "No node token found for predicate: " + test, false );
    return null;
  }

  public GraphProcess completeToken (final GraphProcess p, final String nodeName)
  {
    final NodeToken token = getActiveToken(p, new TokenOnNodePredicate(nodeName));
    System.out.println("Completing token: " + token + " on node " + token.getNode());
    completeToken(token);
    return p;
  }

  public GraphProcess completeToken (final GraphProcess p, final TestPredicate<NodeToken> test)
  {
    final NodeToken token = getActiveToken(p, test);
    System.out.println("Completing token: " + token + " on node " + token.getNode());
    completeToken(token);
    return p;
  }

  public void completeToken (final NodeToken token)
  {
    engine.complete(token, Arc.DEFAULT_ARC);
  }

  public void completeToken (final NodeToken token, final String arcName)
  {
    engine.complete(token, arcName );
  }

  public GraphProcess completeToken (final GraphProcess p, final String nodeName, final String arcName)
  {
    final NodeToken token = getActiveToken(p, new TokenOnNodePredicate(nodeName));
    engine.complete(token, arcName);
    return p;
  }

  public void backtrackToken(final NodeToken token)
  {
    engine.backtrack(token);
  }

  public void completeToken (final GraphProcess p,
                             final String nodeName,
                             final String tokenSetName,
                             final int tokenSetIdx)
  {
    final NodeToken token = getActiveToken(p, new MemberSetPredicate(nodeName, tokenSetName, tokenSetIdx));
    System.out.println("Completing token: " + token);
    engine.complete(token, Arc.DEFAULT_ARC);
  }

  public void completeToken (final GraphProcess p,
                             final String nodeName,
                             final String arcName,
                             final String tokenSetName,
                             final int tokenSetIdx)
  {
    final NodeToken token = getActiveToken(p, new MemberSetPredicate(nodeName, tokenSetName, tokenSetIdx));
    engine.complete(token, arcName);
  }

  public GraphProcess startProcess(final String graphName) throws Exception
  {
    ensureLoaded(graphName);
    final Graph graph = engine.getRepository().getLatestGraph( graphName );
    return startProcess(graph);
  }

  public GraphProcess startProcess(final Graph graph)
  {
    final GraphProcess p = engine.startProcess(graph);
    return p;
  }

  public void addGlobalCustomNodeType(final String typeName, final Class<? extends CustomNode> nodeClass)
  {
    engine.addGlobalCustomNodeType( typeName, nodeClass );
  }
}