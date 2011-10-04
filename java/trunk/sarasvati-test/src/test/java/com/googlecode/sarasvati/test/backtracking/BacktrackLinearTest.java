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

import java.util.Collection;

import org.junit.Test;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;

public class BacktrackLinearTest extends ExecutionTest
{
  @Test public void testLinear () throws Exception
  {
    Graph g = ensureLoaded( "backtrack-linear" );
    GraphProcess p = startProcess( g );
    Collection<NodeToken> tokens = p.getActiveNodeTokens();
    NodeToken tokenA = tokens.iterator().next();

    String state = "[1 nodeA I F]";
    TestProcess.validate( p, state );

    completeToken(tokenA);

    tokens = p.getActiveNodeTokens();

    state = "[1 nodeA C F]" +
            "  (C F 2)" +
            "[2 nodeB I F]";
    TestProcess.validate( p, state );

    backtrackToken(tokenA);

    state = "[1 nodeA C FB]" +
            "  (C FB 2)" +
            "[2 nodeB C FB]" +
            "  (C B 3)" +
            "[3 nodeA I F]";
    TestProcess.validate( p, state );

    tokens = p.getActiveNodeTokens();
    tokenA = tokens.iterator().next();

    completeToken(tokenA);

    state = "[1 nodeA C FB]" +
            "  (C FB 2)" +
            "[2 nodeB C FB]" +
            "  (C B 3)" +
            "[3 nodeA C F]" +
            "  (C F 4)" +
            "[4 nodeB I F]"
            ;

    TestProcess.validate( p, state );
    backtrackToken( tokenA );

    state = "[1 nodeA C FB]" +
            "  (C FB 2)" +
            "[2 nodeB C FB]" +
            "  (C B 3)" +
            "[3 nodeA C FB]" +
            "  (C FB 4)" +
            "[4 nodeB C FB]" +
            "  (C B 5)" +
            "[5 nodeA I F]"
            ;
    TestProcess.validate( p, state );
  }

  @Test public void testLinearTwice () throws Exception
  {
    Graph g = ensureLoaded( "backtrack-twice-linear" );
    GraphProcess p = startProcess( g );
    Collection<NodeToken> tokens = p.getActiveNodeTokens();

    String state = "[1 nodeA I F]";
    TestProcess.validate( p, state );

    NodeToken tokenA = tokens.iterator().next();
    completeToken( tokenA);

    tokens = p.getActiveNodeTokens();

    state = "[1 nodeA C F]\n" +
            "  (C F 2)\n" +
            "[2 nodeB I F]";
    TestProcess.validate( p, state );

    tokens = p.getActiveNodeTokens();
    NodeToken tokenB = tokens.iterator().next();

    completeToken(tokenB);

    state = "[1 nodeA C F]\n" +
            "  (C F 2)\n" +
            "[2 nodeB C F]\n" +
            "  (C F 3)\n" +
            "[3 nodeC I F]";
    TestProcess.validate( p, state );

    backtrackToken(tokenB);

    state = "[1 nodeA C F]\n" +
            "  (C F 2)\n" +
            "[2 nodeB C FB]\n" +
            "  (C FB 3)\n" +
            "[3 nodeC C FB]\n" +
            "  (C B 4)\n" +
            "[4 nodeB I F]\n";

    TestProcess.validate( p, state );
    backtrackToken( tokenA );

    state = "[1 nodeA C FB]\n" +
            "  (C FB 2)\n" +
            "[2 nodeB C FB]\n" +
            "  (C FB 3)\n" +
            "[3 nodeC C FB]\n" +
            "  (C B 4)\n" +
            "[4 nodeB C FB]\n" +
            "  (C B 5)\n" +
            "[5 nodeA I F]\n";

    TestProcess.validate( p, state );
  }
}
