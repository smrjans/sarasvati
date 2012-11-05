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

import org.junit.Test;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.rubric.env.DefaultRubricFunctionRepository;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;
import com.googlecode.sarasvati.test.framework.UnitTestPredicates;

public class LabelArcsRequiredRequiredTest extends ExecutionTest
{
  private static final String TEST_GRAPH_NAME = "joinlang-label-arcs-required";

  @Test public void testOne() throws Exception
  {
    DefaultRubricFunctionRepository rep = DefaultRubricFunctionRepository.getGlobalInstance();
    rep.registerPredicate( "DefaultRequired", UnitTestPredicates.ALWAYS_TRUE );
    rep.registerPredicate( "FooRequired", UnitTestPredicates.ALWAYS_FALSE );

    Graph g = reloadDefinition( TEST_GRAPH_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 nodeA I F]" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeA");

    state =
      "[1 nodeA C F]" +
      "  (I F nodeE)" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeB");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeC", "foo");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC C F]" +
      "  (C F 5)" +
      "[4 nodeD I F]" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeD", "foo");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC C F]" +
      "  (C F 5)" +
      "[4 nodeD C F]" +
      "  (C F 5)" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeE");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC C F]" +
      "  (C F 5)" +
      "[4 nodeD C F]" +
      "  (C F 5)" +
      "[5 nodeE C F]";
    TestProcess.validate( p, state );

    verifyComplete(p);
  }

  @Test public void testTwo() throws Exception
  {
    DefaultRubricFunctionRepository rep = DefaultRubricFunctionRepository.getGlobalInstance();
    rep.registerPredicate( "DefaultRequired", UnitTestPredicates.ALWAYS_TRUE );
    rep.registerPredicate( "FooRequired", UnitTestPredicates.ALWAYS_TRUE );

    Graph g = reloadDefinition( TEST_GRAPH_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 nodeA I F]" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeA");

    state =
      "[1 nodeA C F]" +
      "  (I F nodeE)" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeB");

    state =
      "[1 nodeA C F]" +
      "  (I F nodeE)" +
      "[2 nodeB C F]" +
      "  (I F nodeE)" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeC", "foo");

    state =
      "[1 nodeA C F]" +
      "  (I F nodeE)" +
      "[2 nodeB C F]" +
      "  (I F nodeE)" +
      "[3 nodeC C F]" +
      "  (I F nodeE)" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeD", "foo");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC C F]" +
      "  (C F 5)" +
      "[4 nodeD C F]" +
      "  (C F 5)" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeE");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC C F]" +
      "  (C F 5)" +
      "[4 nodeD C F]" +
      "  (C F 5)" +
      "[5 nodeE C F]";
    TestProcess.validate( p, state );

    verifyComplete(p);
  }

  @Test public void testThree() throws Exception
  {
    DefaultRubricFunctionRepository rep = DefaultRubricFunctionRepository.getGlobalInstance();
    rep.registerPredicate( "DefaultRequired", UnitTestPredicates.ALWAYS_TRUE );
    rep.registerPredicate( "FooRequired", UnitTestPredicates.ALWAYS_FALSE );

    Graph g = reloadDefinition( TEST_GRAPH_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 nodeA I F]" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeD", "foo");

    state =
      "[1 nodeA I F]" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD C F]" +
      "  (I F nodeE)";
    TestProcess.validate( p, state );

    completeToken( p, "nodeB");

    state =
      "[1 nodeA I F]" +
      "[2 nodeB C F]" +
      "  (I F nodeE)" +
      "[3 nodeC I F]" +
      "[4 nodeD C F]" +
      "  (I F nodeE)";
    TestProcess.validate( p, state );

    completeToken( p, "nodeC", "foo");

    state =
      "[1 nodeA I F]" +
      "[2 nodeB C F]" +
      "  (I F nodeE)" +
      "[3 nodeC C F]" +
      "  (I F nodeE)" +
      "[4 nodeD C F]" +
      "  (I F nodeE)";
    TestProcess.validate( p, state );

    completeToken( p, "nodeA");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC C F]" +
      "  (C F 5)" +
      "[4 nodeD C F]" +
      "  (C F 5)" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeE");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC C F]" +
      "  (C F 5)" +
      "[4 nodeD C F]" +
      "  (C F 5)" +
      "[5 nodeE C F]";
    TestProcess.validate( p, state );

    verifyComplete(p);
  }

  @Test public void testFour() throws Exception
  {
    DefaultRubricFunctionRepository rep = DefaultRubricFunctionRepository.getGlobalInstance();
    rep.registerPredicate( "DefaultRequired", UnitTestPredicates.ALWAYS_TRUE );
    rep.registerPredicate( "FooRequired", UnitTestPredicates.ALWAYS_FALSE );

    Graph g = reloadDefinition( TEST_GRAPH_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 nodeA I F]" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeD", "foo");

    state =
      "[1 nodeA I F]" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]" +
      "[4 nodeD C F]" +
      "  (I F nodeE)";
    TestProcess.validate( p, state );

    completeToken( p, "nodeB");

    state =
      "[1 nodeA I F]" +
      "[2 nodeB C F]" +
      "  (I F nodeE)" +
      "[3 nodeC I F]" +
      "[4 nodeD C F]" +
      "  (I F nodeE)";
    TestProcess.validate( p, state );

    completeToken( p, "nodeA");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC I F]" +
      "[4 nodeD C F]" +
      "  (C F 5)" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeC", "foo");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC C F]" +
      "  (C F 5)" +
      "[4 nodeD C F]" +
      "  (C F 5)" +
      "[5 nodeE I F]";
    TestProcess.validate( p, state );

    completeToken( p, "nodeE");

    state =
      "[1 nodeA C F]" +
      "  (C F 5)" +
      "[2 nodeB C F]" +
      "  (C F 5)" +
      "[3 nodeC C F]" +
      "  (C F 5)" +
      "[4 nodeD C F]" +
      "  (C F 5)" +
      "[5 nodeE C F]";
    TestProcess.validate( p, state );

    verifyComplete(p);
  }
}
