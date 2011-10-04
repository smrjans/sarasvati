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
package com.googlecode.sarasvati.test.backtracking;

import java.util.Iterator;

import org.junit.Test;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;

public class BacktrackJoinTest extends ExecutionTest
{
  @Test public void testJoin() throws Exception
  {
    Graph g = ensureLoaded( "backtrack-join" );
    GraphProcess p = startProcess( g );

    Iterator<NodeToken> iter = p.getActiveNodeTokens().iterator();
    NodeToken tokenA = iter.next();
    NodeToken tokenB = iter.next();

    if ( "nodeB".equals( tokenA.getNode().getName() ) )
    {
      NodeToken tmp = tokenA;
      tokenA = tokenB;
      tokenB = tmp;
    }

    String state =
      "[1 nodeA I F]" +
      "[2 nodeB I F]";
    TestProcess.validate( p, state );

    completeToken(tokenA);

    state =
      "[1 nodeA C F]" +
      "  (I F nodeC)" +
      "[2 nodeB I F]";
    TestProcess.validate( p, state );

    completeToken(tokenB);

    state =
      "[1 nodeA C F]" +
      "  (C F 3)" +
      "[2 nodeB C F]" +
      "  (C F 3)" +
      "[3 nodeC I F]";
    TestProcess.validate( p, state );

    backtrackToken( tokenA );

    state =
      "[1 nodeA C FB]" +
      "  (C FB 3)" +
      "[2 nodeB C F]" +
      "  (C FB 3)" +
      "[3 nodeC C FB]" +
      "  (C B 4)" +
      "  (I U nodeC)" +
      "[4 nodeA I F]";
    TestProcess.validate( p, state );

    backtrackToken( tokenB );

    state =
      "[1 nodeA C FB]" +
      "  (C FB 3)" +
      "[2 nodeB C FB]" +
      "  (C FB 3)" +
      "[3 nodeC C FB]" +
      "  (C B 4)" +
      "  (C B 5)" +
      "[4 nodeA I F]" +
      "[5 nodeB I F]";
    TestProcess.validate( p, state );
  }

  @Test public void testJoin2() throws Exception
  {
    Graph g = ensureLoaded( "backtrack-join" );
    GraphProcess p = startProcess( g );

    Iterator<NodeToken> iter = p.getActiveNodeTokens().iterator();
    NodeToken tokenA = iter.next();
    NodeToken tokenB = iter.next();

    if ( "nodeB".equals( tokenA.getNode().getName() ) )
    {
      NodeToken tmp = tokenA;
      tokenA = tokenB;
      tokenB = tmp;
    }

    String state =
      "[1 nodeA I F]" +
      "[2 nodeB I F]";
    TestProcess.validate( p, state );

    completeToken(tokenA);

    state =
      "[1 nodeA C F]" +
      "  (I F nodeC)" +
      "[2 nodeB I F]";
    TestProcess.validate( p, state );

    completeToken(tokenB);

    state =
      "[1 nodeA C F]" +
      "  (C F 3)" +
      "[2 nodeB C F]" +
      "  (C F 3)" +
      "[3 nodeC I F]";
    TestProcess.validate( p, state );

    backtrackToken( tokenA );

    state =
      "[1 nodeA C FB]" +
      "  (C FB 3)" +
      "[2 nodeB C F]" +
      "  (C FB 3)" +
      "[3 nodeC C FB]" +
      "  (C B 4)" +
      "  (I U nodeC)" +
      "[4 nodeA I F]";
    TestProcess.validate( p, state );

    tokenA = p.getActiveNodeTokens().iterator().next();
    completeToken( tokenA, Arc.DEFAULT_ARC );

    state =
      "[1 nodeA C FB]" +
      "  (C FB 3)" +
      "[2 nodeB C F]" +
      "  (C FB 3)" +
      "[3 nodeC C FB]" +
      "  (C B 4)" +
      "  (C U 5)" +
      "[4 nodeA C F]" +
      "  (C F 5)" +
      "[5 nodeC I F]";
    TestProcess.validate( p, state );

    backtrackToken( tokenA );

    state =
      "[1 nodeA C FB]" +
      "  (C FB 3)" +
      "[2 nodeB C F]" +
      "  (C FB 3)" +
      "[3 nodeC C FB]" +
      "  (C B 4)" +
      "  (C UB 5)" +
      "[4 nodeA C FB]" +
      "  (C FB 5)" +
      "[5 nodeC C FB]" +
      "  (C B 6)" +
      "  (I U nodeC)" +
      "[6 nodeA I F]";
    TestProcess.validate( p, state );

    backtrackToken( tokenB );

    state =
      "[1 nodeA C FB]" +
      "  (C FB 3)" +
      "[2 nodeB C FB]" +
      "  (C FB 3)" +
      "[3 nodeC C FB]" +
      "  (C B 4)" +
      "  (C UB 5)" +
      "[4 nodeA C FB]" +
      "  (C FB 5)" +
      "[5 nodeC C FB]" +
      "  (C B 6)" +
      "  (C B 7)" +
      "[6 nodeA I F]" +
      "[7 nodeB I F]";
    TestProcess.validate( p, state );

  }
}