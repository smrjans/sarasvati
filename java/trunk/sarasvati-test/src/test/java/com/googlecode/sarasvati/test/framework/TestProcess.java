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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;

/**
 *
 * Format:
 *   Each line should contain 1 token
 *   The arc tokens should be indented under the node token
 *   that spawned them.
 *
 *  Node token format
 *  [id nodeName status executionType ]
 *
 *  status is one of I = Incomplete or C = Complete
 *  executionType is one of F, B, FB, BB
 *
 *  Arc token format
 *  (status executionType childTokenId/nodeName)
 *
 *  status is one of P - Pending, I - incomplete, C - complete
 *  if status is P or I then the last element will be a node name
 *  if status is C then the last element will be a node token id
 *
 *  ex: [1 foo C F]
 *        (C F 2)
 *        (C F 3)
 *      [2 bar C F]
 *      [3 baz C F]
 *
 * @author Paul Lorenz
 */
public class TestProcess
{
  public static void validate (final GraphProcess p, final String state)
  {
    TestProcess testProcess = null;
    try
    {
      testProcess = new TestProcess( p.getGraph(), state );
      testProcess.compare( p );
    }
    finally
    {
      if ( testProcess != null )
      {
        System.out.println( "================================================================================" );
        ProcessPrinter.print( p );
        testProcess.dumpToStandardOut();
        System.out.println( "================================================================================" );
      }
    }
  }

  private final List<TestNodeToken> startTestTokens = new LinkedList<TestNodeToken>();
  private final Graph graph;

  private final Map<String, TestNodeToken> nodeTokens = new HashMap<String, TestNodeToken>();
  private final Map<String, List<TestArcToken>> arcTokens = new HashMap<String, List<TestArcToken>>();

  public TestProcess (final Graph graph, final String spec)
  {
    this.graph = graph;

    TestNodeToken current = null;

    int lineNumber = 1;

    StringBuilder buf = new StringBuilder( spec );
    int index = 0;

    while ( index < buf.length() )
    {
      if ( buf.charAt( index ) == '[' )
      {
        int endIndex = buf.indexOf( "]", index );
        if ( endIndex == -1 )
        {
          throw new IllegalArgumentException( "No closing ] found at line: " + lineNumber );
        }
        String tokenSpec = buf.substring( index + 1, endIndex );
        current = parseNodeToken( tokenSpec.trim(), lineNumber );
        nodeTokens.put( current.getId(), current );
        lineNumber++;
        index = endIndex + 1;
      }
      else if ( buf.charAt( index ) == '(' )
      {
        int endIndex = buf.indexOf( ")", index );
        if ( endIndex == -1 )
        {
          throw new IllegalArgumentException( "No closing ) found at line: " + lineNumber );
        }
        String tokenSpec = buf.substring( index + 1, endIndex );
        parseArcToken(tokenSpec, current, lineNumber );
        lineNumber++;
        index = endIndex + 1;
      }
      else
      {
        index++;
      }
    }

    for ( Entry<String,List<TestArcToken>> entry : arcTokens.entrySet() )
    {
      TestNodeToken child = nodeTokens.get( entry.getKey() );

      if ( child == null )
      {
        throw new RuntimeException( "No node found with id: " + entry.getKey() );
      }

      for ( TestArcToken token : entry.getValue() )
      {
        child.addParent( token );
        token.setChildToken( child );
      }
    }

    for ( TestNodeToken token : nodeTokens.values() )
    {
      if ( token.getParents().isEmpty() )
      {
        startTestTokens.add( token );
      }
    }
  }

  private TestNodeToken parseNodeToken (final String line, final int lineNumber)
  {
    String[] parts = line.split( " " );

    boolean complete = false;

    if ( "C".equals( parts[2] ) )
    {
      complete = true;
    }
    else if ( !"I".equals( parts[2] ) )
    {
      throw new RuntimeException( "Unreconized status marker on line number " + lineNumber + " in " + line );
    }

    String tokenSetName = null;
    int tokenSetIndex = 0;

    if ( parts.length == 6 )
    {
      tokenSetName = parts[4];
      tokenSetIndex = Integer.parseInt( parts[5] );
    }

    return new TestNodeToken( lineNumber,
                              parts[0],
                              getNode( parts[1] ),
                              complete,
                              stringToExecutionType( line, parts[3] ),
                              tokenSetName,
                              tokenSetIndex );
  }

  private void parseArcToken (final String line,
                              final TestNodeToken parent,
                              final int lineNumber)
  {
    String[] parts = line.split( " " );

    if ( parent == null )
    {
      throw new RuntimeException( "Can't define arc token before node token" );
    }

    boolean pending = false;
    boolean complete = false;

    if ( "P".equals( parts[0] ) )
    {
      pending = true;
    }
    else if ( "I".equals( parts[0] ) )
    {
      pending = false;
      complete = false;
    }
    else if ( "C".equals( parts[0] ) )
    {
      pending = false;
      complete = true;
    }
    else
    {
      throw new RuntimeException( "Unreconized status marker '" + parts[0] + "' on line number " + lineNumber + " in " + line );
    }

    TestArcToken token = new TestArcToken( lineNumber, parent, pending, complete, stringToExecutionType( line, parts[1] ) );

    parent.addChild( token );

    if ( complete )
    {
      String childId = parts[2];
      List<TestArcToken> list = arcTokens.get( childId );
      if ( list == null )
      {
        list = new LinkedList<TestArcToken>();
        arcTokens.put( childId, list );
      }
      list.add( token );
    }
    else
    {
      token.setChildNode( getNode( parts[2] ) );
    }
  }

  private static ExecutionType stringToExecutionType (final String line, final String type)
  {
    ExecutionType executionType = executionTypeMap.get( type );

    if (executionType != null )
    {
      return executionType;
    }

    throw new RuntimeException( "Unrecognized execution type '" + type + "' on line: " + line );
  }

  private Node getNode (final String name)
  {
    List<Node> nodes = new ArrayList<Node>();
    for ( Node node : graph.getNodes() )
    {
      if ( name.equals( node.getName() ) )
      {
        nodes.add( node );
      }
    }

    if ( nodes.isEmpty() )
    {
      throw new RuntimeException( "Found no nodes named " + name );
    }

    if ( nodes.size() > 1 )
    {
      StringBuilder buf = new StringBuilder();
      for ( Node node : nodes )
      {
        buf.append( "Node id=" );
        buf.append( node.getId() );
        buf.append( " name=" );
        buf.append( node.getName() );
        buf.append( " type=" );
        buf.append( node.getType() );
        buf.append(" external=" );
        buf.append( node.isImportedFromExternal() );
        buf.append( " joinType=" );
        buf.append( node.getJoinType() );
        buf.append( " start=" );
        buf.append(  node.isStart() );
        buf.append( "\n" );
      }

      throw new RuntimeException( "Found too many nodes named " + name + "\n" + buf.toString() );
    }

    return nodes.get( 0 );
  }

  private LinkedList<NodeToken> getStartTokens (final GraphProcess p)
  {
    LinkedList<NodeToken> startTokens = new LinkedList<NodeToken>();

    for ( NodeToken token : p.getNodeTokens() )
    {
      if ( token.getParentTokens().isEmpty() && token.getNode().isStart() )
      {
        startTokens.add( token );
      }
    }

    return startTokens;
  }

  public void compare (final GraphProcess p)
  {
    LinkedList<NodeToken> startNodeTokens = getStartTokens( p );

    while ( !startNodeTokens.isEmpty() )
    {
      NodeToken t = startNodeTokens.removeFirst();
      boolean found = true;
      for ( TestNodeToken tt : startTestTokens )
      {
        if ( tt.matchesToken( t ) )
        {
          tt.setToken( t );
          found = true;
          break;
        }
      }

      Assert.assertTrue( "No test node token found for node token: " + t, found );
    }

    for ( TestNodeToken token : startTestTokens )
    {
      Assert.assertNotNull( "No real token found for test token " + token.getId(),  token.getToken() );
    }

    associateTokens( p );
    validate();

    for ( ArcToken t : p.getActiveArcTokens() )
    {
      Assert.assertFalse( "Active arc token should not be complete", t.isComplete() );
    }

    for ( NodeToken t : p.getActiveNodeTokens() )
    {
      Assert.assertFalse( "Active node token should not be complete", t.isComplete() );
    }
  }

  private void associateTokens (final GraphProcess p)
  {
    LinkedList<TestNodeToken> queue = new LinkedList<TestNodeToken>();
    queue.addAll( startTestTokens );

    while ( !queue.isEmpty() )
    {
      TestNodeToken testNodeToken = queue.removeFirst();
      NodeToken nodeToken = testNodeToken.getToken();

      Assert.assertTrue( "Node token should be tracked by process", nodeToken.getProcess().getNodeTokens().contains( nodeToken ) );

      for ( ArcToken arcToken : nodeToken.getChildTokens() )
      {
        boolean found = false;
        if ( arcToken.isComplete() )
        {
          NodeToken child = arcToken.getChildToken();
          for ( TestArcToken testArcToken : testNodeToken.getChildren() )
          {
            if ( testArcToken.isComplete() &&
                 testArcToken.getChildToken().matchesToken( child ) &&
                 testArcToken.getToken() == null )
            {
              TestNodeToken testChild = testArcToken.getChildToken();
              testArcToken.setToken( arcToken );

              // If this test node token has already been processed by some incoming arc, don't re-queue
              if ( testChild.getToken() == null )
              {
                testChild.setToken( child );
                System.out.println("Tying " + testChild + " to " + ProcessPrinter.toString(child));
                queue.add( testChild );
              }

              found = true;
              break;
            }
          }
        }
        else
        {
          boolean backwards = arcToken.getExecutionType() == ExecutionType.Backtracked;

          Node endNode = backwards ? arcToken.getArc().getStartNode() : arcToken.getArc().getEndNode();
          for ( TestArcToken testArcToken : testNodeToken.getChildren() )
          {
            if ( !testArcToken.isComplete() && endNode.equals( testArcToken.getChildNode() ) )
            {
              testArcToken.setToken( arcToken );
              found = true;
              break;
            }
          }
        }

        Assert.assertTrue( "No corresponding test arc token found for arc token: " + arcToken, found );
      }

      for ( TestArcToken testArcToken : testNodeToken.getChildren() )
      {
        Assert.assertNotNull( "No arc token found for token " + testArcToken, testArcToken.getToken() );
      }
    }

    Set<NodeToken> actualNodeTokens = new HashSet<NodeToken>( p.getNodeTokens() );

    for ( TestNodeToken token : nodeTokens.values() )
    {
      Assert.assertNotNull( "No arc token found for token " + token, token.getToken() );
      actualNodeTokens.remove( token.getToken() );
    }

    Assert.assertTrue( "Not all node tokens covered by test tokens. Uncovered values: " + actualNodeTokens, actualNodeTokens.isEmpty() );
  }

  private void validate ()
  {
    LinkedList<TestNodeToken> queue = new LinkedList<TestNodeToken>();
    queue.addAll( startTestTokens );

    while ( !queue.isEmpty() )
    {
      TestNodeToken testNodeToken = queue.removeFirst();
      testNodeToken.validate();

      for ( TestArcToken testArcToken : testNodeToken.getChildren() )
      {
        testArcToken.validate();
        if ( testArcToken.getChildToken() != null && !testArcToken.getChildToken().isValidated() )
        {
          queue.add( testArcToken.getChildToken() );
        }
      }
    }
  }

  public void dumpToStandardOut ()
  {
    System.out.println( "\nTestProcess: " );
    Set<TestNodeToken> p = new HashSet<TestNodeToken>();

    LinkedList<TestNodeToken> queue = new LinkedList<TestNodeToken>();
    queue.addAll( startTestTokens );

    while ( !queue.isEmpty() )
    {
      TestNodeToken token = queue.removeFirst();
      System.out.println( "TestToken" +
                          " testId=" + token.getId() +
                          " node=" + token.getNode().getName() +
                          " testComplete=" + token.isComplete() +
                          " testExecutionType=" + token.getExecutionType() +
                          ( token.getToken() == null ?
                              " token=null" :
                              " tokenId=" + token.getToken().getId() +
                              " complete=" + token.getToken().isComplete() +
                              " executionType=" + token.getToken().getExecutionType() ) );

      for ( TestArcToken child : token.getChildren() )
      {
        System.out.println( "\tTestArcToken" +
                            " testExecutionType=" + child.getExecutionType() +
                            " testComplete=" + child.isComplete() +
                            ( child.getToken() == null ?
                                " token=null" :
                                " tokenArc=" + child.getToken().getArc() ) );
        if ( child.getChildToken() != null && !p.contains( child.getChildToken() ) )
        {
          p.add( child.getChildToken() );
          queue.add( child.getChildToken() );
        }
      }
    }
  }

  private static final Map<String, ExecutionType> executionTypeMap = new HashMap<String, ExecutionType>();

  static
  {
    executionTypeMap.put( "F", ExecutionType.Forward );
    executionTypeMap.put( "FB", ExecutionType.ForwardBacktracked );
    executionTypeMap.put( "B", ExecutionType.Backtracked );
    executionTypeMap.put( "U", ExecutionType.UTurn );
    executionTypeMap.put( "UB", ExecutionType.UTurnBacktracked );
  }
}
