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
package com.googlecode.sarasvati.unittest;

import org.junit.Test;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.unittest.framework.ExecutionTest;
import com.googlecode.sarasvati.unittest.framework.ProcessPrinter;
import com.googlecode.sarasvati.unittest.framework.TestProcess;

public class BacktrackUnevenTreesTest extends ExecutionTest
{
  @Test public void testReject() throws Exception
  {
    Graph g = ensureLoaded( "backtrack-uneven-trees" );
    GraphProcess p = startProcess( g );

    NodeToken tokenA = getActiveToken( p, "nodeA" );

    String state = "[1 nodeA I F]";
    TestProcess.validate( p, state );

    completeToken(tokenA);

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeB I F]" +
      "[3 nodeC I F]"
      ;
    TestProcess.validate( p, state );

    NodeToken tokenB = getActiveToken( p, "nodeB" );
    completeToken( tokenB, "reject" );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeB C F]" +
      "  (C F 4)" +
      "[3 nodeC I F]" +
      "[4 nodeB I F]"
      ;

    ProcessPrinter.print( p );
    TestProcess.validate( p, state );

    tokenB = getActiveToken( p, "nodeB" );
    completeToken( tokenB, "reject" );

    state =
      "[1 nodeA C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 nodeB C F]" +
      "  (C F 4)" +
      "[3 nodeC C F]" +
      "[4 nodeB C F]" +
      "  (C F 5)" +
      "[5 nodeB I F]"
      ;

    backtrackToken( tokenA );

    state =
      "[1 nodeA C FB]" +
      "  (C FB 2)" +
      "  (C FB 3)" +
      "[2 nodeB C FB]" +
      "  (C FB 4)" +
      "[3 nodeC C FB]" +
      "  (C B 8)" +
      "[4 nodeB C FB]" +
      "  (C FB 5)" +
      "[5 nodeB C FB]" +
      "  (C B 6)" +
      "[6 nodeB C B]" +
      "  (C B 7)" +
      "[7 nodeB C B]" +
      "  (C B 8)" +
      "[8 nodeA I F]"
      ;

    ProcessPrinter.print( p );
    TestProcess.validate( p, state );
  }
}