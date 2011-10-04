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
package com.googlecode.sarasvati.test.traversal;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.visitor.TokenTraversals;
import com.googlecode.sarasvati.visitor.TokenVisitorAdaptor;

public class TraversalTest extends ExecutionTest
{
  public static class TestVisitor extends TokenVisitorAdaptor
  {
    StringBuilder buf = new StringBuilder();

    @Override
    public void visit (final NodeToken token)
    {
      String name = token.getNode().getName();
      buf.append( name.substring( 4 ) );
    }
  }

  public NodeToken executeTraversal () throws Exception
  {
    Graph g = ensureLoaded( "traversal" );
    GraphProcess p = startProcess( g );

    NodeToken tokenA = null;

    for (NodeToken t : p.getNodeTokens() )
    {
      if ( t.getNode().isStart() )
      {
        tokenA = t;
        break;
      }
    }

    Assert.assertNotNull(  "No token found for start node", tokenA );

    return tokenA;
  }

  @Test public void testBreadthFirst () throws Exception
  {
    NodeToken tokenA = executeTraversal();

    TestVisitor visitor = new TestVisitor();
    TokenTraversals.traverseChildrenBreadthFirst( tokenA, visitor );

    String compare = "ABCDEFGHIJ";
    Assert.assertEquals( compare, visitor.buf.toString() );
  }

  @Test public void testDepthFirst () throws Exception
  {
    NodeToken tokenA = executeTraversal();

    TestVisitor visitor = new TestVisitor();
    TokenTraversals.traverseChildrenDepthFirst( tokenA, visitor );

    String compare = "ABDJEFCGHI";
    Assert.assertEquals( compare, visitor.buf.toString() );
  }
}