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
import com.googlecode.sarasvati.rubric.env.DefaultRubricFunctionRepository;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.ProcessPrinter;
import com.googlecode.sarasvati.test.framework.TestProcess;
import com.googlecode.sarasvati.test.framework.UnitTestPredicates;

public class NodeRequiredTest extends ExecutionTest
{
  private static final String TEST_GRAPH_NAME = "joinlang-node-required";
  @Test public void testJoin() throws Exception
  {
    DefaultRubricFunctionRepository rep = DefaultRubricFunctionRepository.getGlobalInstance();
    rep.registerPredicate( "B.isNeeded", UnitTestPredicates.ALWAYS_TRUE );
    rep.registerPredicate( "C.isNeeded", UnitTestPredicates.ALWAYS_FALSE );

    Graph g = ensureLoaded( TEST_GRAPH_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 A I F]" +
      "[2 B I F]" +
      "[3 C I F]";
    TestProcess.validate( p, state );

    completeToken( p, "B" );

    state =
      "[1 A I F]" +
      "[2 B C F]" +
      "  (I F D)" +
      "[3 C I F]";
    TestProcess.validate( p, state );

    completeToken( p, "C" );

    state =
      "[1 A I F]" +
      "[2 B C F]" +
      "  (I F D)" +
      "[3 C C F]" +
      "  (I F D)";
    TestProcess.validate( p, state );

    completeToken( p, "A" );

    state =
      "[1 A C F]" +
      "  (C F 4)" +
      "[2 B C F]" +
      "  (C F 4)" +
      "[3 C C F]" +
      "  (C F 4)" +
      "[4 D I F]";
    TestProcess.validate( p, state );

    completeToken( p, "D" );

    state =
      "[1 A C F]" +
      "  (C F 4)" +
      "[2 B C F]" +
      "  (C F 4)" +
      "[3 C C F]" +
      "  (C F 4)" +
      "[4 D C F]";
    TestProcess.validate( p, state );

    verifyComplete(p);
  }

  @Test public void testJoinWithMerge() throws Exception
  {
    DefaultRubricFunctionRepository rep = DefaultRubricFunctionRepository.getGlobalInstance();
    rep.registerPredicate( "B.isNeeded", UnitTestPredicates.ALWAYS_TRUE );
    rep.registerPredicate( "C.isNeeded", UnitTestPredicates.ALWAYS_FALSE );

    Graph g = ensureLoaded( TEST_GRAPH_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 A I F]" +
      "[2 B I F]" +
      "[3 C I F]";
    TestProcess.validate( p, state );

    completeToken( p, "B" );

    state =
      "[1 A I F]" +
      "[2 B C F]" +
      "  (I F D)" +
      "[3 C I F]";
    TestProcess.validate( p, state );

    completeToken( p, "A" );

    state =
      "[1 A C F]" +
      "  (C F 4)" +
      "[2 B C F]" +
      "  (C F 4)" +
      "[3 C I F]" +
      "[4 D I F]";
    ProcessPrinter.print( p );
    TestProcess.validate( p, state );

    completeToken( p, "C" );

    state =
      "[1 A C F]" +
      "  (C F 4)" +
      "[2 B C F]" +
      "  (C F 4)" +
      "[3 C C F]" +
      "  (C F 4)" +
      "[4 D I F]";
    TestProcess.validate( p, state );

    completeToken( p, "D" );

    state =
      "[1 A C F]" +
      "  (C F 4)" +
      "[2 B C F]" +
      "  (C F 4)" +
      "[3 C C F]" +
      "  (C F 4)" +
      "[4 D C F]";
    TestProcess.validate( p, state );

    verifyComplete(p);
  }

  @Test public void testJoinWithMerge2() throws Exception
  {
    DefaultRubricFunctionRepository rep = DefaultRubricFunctionRepository.getGlobalInstance();
    rep.registerPredicate( "B.isNeeded", UnitTestPredicates.ALWAYS_TRUE );
    rep.registerPredicate( "C.isNeeded", UnitTestPredicates.ALWAYS_FALSE );

    Graph g = ensureLoaded( TEST_GRAPH_NAME );
    GraphProcess p = startProcess( g );

    String state =
      "[1 A I F]" +
      "[2 B I F]" +
      "[3 C I F]";
    TestProcess.validate( p, state );

    completeToken( p, "B" );

    state =
      "[1 A I F]" +
      "[2 B C F]" +
      "  (I F D)" +
      "[3 C I F]";
    TestProcess.validate( p, state );

    completeToken( p, "A" );

    state =
      "[1 A C F]" +
      "  (C F 4)" +
      "[2 B C F]" +
      "  (C F 4)" +
      "[3 C I F]" +
      "[4 D I F]";
    TestProcess.validate( p, state );

    completeToken( p, "D" );

    state =
      "[1 A C F]" +
      "  (C F 4)" +
      "[2 B C F]" +
      "  (C F 4)" +
      "[3 C I F]" +
      "[4 D C F]";
    TestProcess.validate( p, state );

    Assert.assertFalse( "Process should not be complete yet", p.isComplete() );

    completeToken( p, "C" );

    state =
      "[1 A C F]" +
      "  (C F 4)" +
      "[2 B C F]" +
      "  (C F 4)" +
      "[3 C C F]" +
      "  (C F 4)" +
      "[4 D C F]";
    TestProcess.validate( p, state );

    verifyComplete(p);
  }
}
