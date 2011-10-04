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

import org.junit.Test;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;

public class AtLeastLabelArcsRequiredRequiredTest extends ExecutionTest
{
  private static final String TEST_GRAPH_NAME = "joinlang-at-least-label-arcs-required";

  @Test public void testOne() throws Exception
  {
    Graph g = ensureLoaded( TEST_GRAPH_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 nodeA I F]" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeA");

    state =
      "[1 nodeA C F]" +
      "  (I F joinNode)" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeB");

    state =
      "[1 nodeA C F]" +
      "  (I F joinNode)" +
      "[2 nodeB C F]" +
      "  (I F joinNode)" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeD", "foo");

    state =
      "[1 nodeA C F]" +
      "  (C F 6)" +
      "[2 nodeB C F]" +
      "  (C F 6)" +
      "[3 nodeC I F]" +
      "[4 nodeD C F]" +
      "  (C F 6)" +
      "[5 nodeE I F]" +
      "[6 joinNode I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeC");

    state =
      "[1 nodeA C F]" +
      "  (C F 6)" +
      "[2 nodeB C F]" +
      "  (C F 6)" +
      "[3 nodeC C F]" +
      "  (C F 6)" +
      "[4 nodeD C F]" +
      "  (C F 6)" +
      "[5 nodeE I F]" +
      "[6 joinNode I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeE", "foo");

    state =
      "[1 nodeA C F]" +
      "  (C F 6)" +
      "[2 nodeB C F]" +
      "  (C F 6)" +
      "[3 nodeC C F]" +
      "  (C F 6)" +
      "[4 nodeD C F]" +
      "  (C F 6)" +
      "[5 nodeE C F]" +
      "  (C F 6)" +
      "[6 joinNode I F]";
    TestProcess.validate( p, state );

    completeToken( p, "joinNode");

    state =
      "[1 nodeA C F]" +
      "  (C F 6)" +
      "[2 nodeB C F]" +
      "  (C F 6)" +
      "[3 nodeC C F]" +
      "  (C F 6)" +
      "[4 nodeD C F]" +
      "  (C F 6)" +
      "[5 nodeE C F]" +
      "  (C F 6)" +
      "[6 joinNode C F]";
    TestProcess.validate( p, state );

    Assert.assertTrue( "Process should be complete", p.isComplete() );
  }

  @Test public void testTwo() throws Exception
  {
    Graph g = ensureLoaded( TEST_GRAPH_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 nodeA I F]" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeA");

    state =
      "[1 nodeA C F]" +
      "  (I F joinNode)" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeB");

    state =
      "[1 nodeA C F]" +
      "  (I F joinNode)" +
      "[2 nodeB C F]" +
      "  (I F joinNode)" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeC");

    state =
      "[1 nodeA C F]" +
      "  (I F joinNode)" +
      "[2 nodeB C F]" +
      "  (I F joinNode)" +
      "[3 nodeC C F]" +
      "  (I F joinNode)" +
      "[4 nodeD I F]" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeD", "foo");

    state =
      "[1 nodeA C F]" +
      "  (C F 6)" +
      "[2 nodeB C F]" +
      "  (C F 6)" +
      "[3 nodeC C F]" +
      "  (C F 6)" +
      "[4 nodeD C F]" +
      "  (C F 6)" +
      "[5 nodeE I F]" +
      "[6 joinNode I F]";
    TestProcess.validate( p, state );

    completeToken( p, "joinNode");

    state =
      "[1 nodeA C F]" +
      "  (C F 6)" +
      "[2 nodeB C F]" +
      "  (C F 6)" +
      "[3 nodeC C F]" +
      "  (C F 6)" +
      "[4 nodeD C F]" +
      "  (C F 6)" +
      "[5 nodeE I F]" +
      "[6 joinNode C F]";
    TestProcess.validate( p, state );

    Assert.assertFalse( "Process should not be complete", p.isComplete() );

    completeToken( p, "nodeE", "foo");

    state =
      "[1 nodeA C F]" +
      "  (C F 6)" +
      "[2 nodeB C F]" +
      "  (C F 6)" +
      "[3 nodeC C F]" +
      "  (C F 6)" +
      "[4 nodeD C F]" +
      "  (C F 6)" +
      "[5 nodeE C F]" +
      "  (C F 6)" +
      "[6 joinNode C F]";
    TestProcess.validate( p, state );

    Assert.assertTrue( "Process should be complete", p.isComplete() );
  }
}
