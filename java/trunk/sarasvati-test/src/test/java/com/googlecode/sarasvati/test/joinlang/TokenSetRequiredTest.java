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

package com.googlecode.sarasvati.test.joinlang;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;
import com.googlecode.sarasvati.test.framework.TokenWithTokenSetParentPredicate;

public class TokenSetRequiredTest extends ExecutionTest
{
  private static final String TEST_GRAPH1_NAME = "joinlang-tokenset-required1";
  private static final String TEST_GRAPH2_NAME = "joinlang-tokenset-required2";

  @Test public void testOne() throws Exception
  {
    Graph g = ensureLoaded( TEST_GRAPH1_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC I F ts1 0]" +
      "[3 nodeC I F ts1 1]" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD I F ts2 0]" +
      "[6 nodeD I F ts2 1]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeC", "ts1", 0 );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (I F nodeE)" +
      "[3 nodeC I F ts1 1]" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD I F ts2 0]" +
      "[6 nodeD I F ts2 1]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeC", "ts1", 1 );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (C F 7)" +
      "[3 nodeC C F ts1 1]" +
      "  (C F 7)" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD I F ts2 0]" +
      "[6 nodeD I F ts2 1]" +
      "[7 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeD", "ts2", 0 );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (C F 7)" +
      "[3 nodeC C F ts1 1]" +
      "  (C F 7)" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD C F ts2 0]" +
      "  (I F nodeE)" +
      "[6 nodeD I F ts2 1]" +
      "[7 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeD", "ts2", 1 );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (C F 7)" +
      "[3 nodeC C F ts1 1]" +
      "  (C F 7)" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD C F ts2 0]" +
      "  (C F 8)" +
      "[6 nodeD C F ts2 1]" +
      "  (C F 8)" +
      "[7 nodeE I F]" +
      "[8 nodeE I F]";
    TestProcess.validate( p, state );

    Assert.assertTrue( "Process should be executing", p.isExecuting() );

    completeToken(p, new TokenWithTokenSetParentPredicate("nodeE", "ts1"));

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (C F 7)" +
      "[3 nodeC C F ts1 1]" +
      "  (C F 7)" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD C F ts2 0]" +
      "  (C F 8)" +
      "[6 nodeD C F ts2 1]" +
      "  (C F 8)" +
      "[7 nodeE C F]" +
      "[8 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken(p, new TokenWithTokenSetParentPredicate("nodeE", "ts2"));

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (C F 7)" +
      "[3 nodeC C F ts1 1]" +
      "  (C F 7)" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD C F ts2 0]" +
      "  (C F 8)" +
      "[6 nodeD C F ts2 1]" +
      "  (C F 8)" +
      "[7 nodeE C F]" +
      "[8 nodeE C F]";
    TestProcess.validate( p, state );

    Assert.assertTrue( "Process should be complete", p.isComplete() );
  }

  @Ignore @Test public void testTwo() throws Exception
  {
    Graph g = ensureLoaded( TEST_GRAPH2_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC I F ts1 0]" +
      "[3 nodeC I F ts1 1]" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD I F ts2 0]" +
      "[6 nodeD I F ts2 1]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeC", "ts1", 0 );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (I F nodeE)" +
      "[3 nodeC I F ts1 1]" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD I F ts2 0]" +
      "[6 nodeD I F ts2 1]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeC", "ts1", 1 );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (C F 7)" +
      "[3 nodeC C F ts1 1]" +
      "  (C F 7)" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD I F ts2 0]" +
      "[6 nodeD I F ts2 1]" +
      "[7 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeD", "ts2", 0 );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (C F 7)" +
      "[3 nodeC C F ts1 1]" +
      "  (C F 7)" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD C F ts2 0]" +
      "  (I F nodeE)" +
      "[6 nodeD I F ts2 1]" +
      "[7 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeD", "ts2", 1 );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (C F 7)" +
      "[3 nodeC C F ts1 1]" +
      "  (C F 7)" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD C F ts2 0]" +
      "  (C F 7)" +
      "[6 nodeD C F ts2 1]" +
      "  (C F 7)" +
      "[7 nodeE I F]";
    TestProcess.validate( p, state );

    Assert.assertTrue( "Process should be executing", p.isExecuting() );

    completeToken( p, "nodeE" );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeC C F ts1 0]" +
      "  (C F 7)" +
      "[3 nodeC C F ts1 1]" +
      "  (C F 7)" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "  (C F 6)" +
      "[5 nodeD C F ts2 0]" +
      "  (C F 7)" +
      "[6 nodeD C F ts2 1]" +
      "  (C F 7)" +
      "[7 nodeE C F]";
    TestProcess.validate( p, state );

    Assert.assertTrue( "Process should be complete", p.isComplete() );
  }
}