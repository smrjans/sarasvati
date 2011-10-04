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

import org.junit.Test;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;
import com.googlecode.sarasvati.visitor.FindNodeNamedVisitor;

public class BacktrackCompletedTest extends ExecutionTest
{
  public static class BacktrackCompletedTestNode extends CustomNode
  {
    @Override
    public void execute (final Engine engine, final NodeToken token)
    {
      engine.complete( token, Arc.DEFAULT_ARC );
      NodeToken parentToken = FindNodeNamedVisitor.findFirstNamedParent( token, "nodeC" );
      engine.backtrack( parentToken );
    }
  }

  @Test public void testCompleted () throws Exception
  {
    addGlobalCustomNodeType( "backtrackCompletedTestNode", BacktrackCompletedTestNode.class );

    GraphProcess p = startProcess( "backtrack-completed" );

    String state = "[1 nodeA I F]";
    TestProcess.validate( p, state );

    p = completeToken( p, "nodeA" );

    state = "[1 nodeA C F]" +
            "  (C F 2)" +
            "  (C F 3)" +
            "[2 nodeB I F]" +
            "[3 nodeC I F]";
    TestProcess.validate( p, state );

    p = completeToken( p, "nodeC" );

    state = "[1 nodeA C F]" +
            "  (C F 2)" +
            "  (C F 3)" +
            "[2 nodeB I F]" +
            "[3 nodeC C F]" +
            "  (C F 4)" +
            "[4 nodeD I F]";

    TestProcess.validate( p, state );

    p = completeToken( p, "nodeD", "test" );

    state = "[1 nodeA C F]" +
            "  (C F 2)" +
            "  (C F 3)" +
            "[2 nodeB I F]" +
            "[3 nodeC C FB]" +
            "  (C FB 4)" +
            "[4 nodeD C FB]" +
            "  (C FB 5)" +
            "[5 nodeE C FB]" +
            "  (C B 6)" +
            "[6 nodeD C B]" +
            "  (C B 7)" +
            "[7 nodeC I F]";
    TestProcess.validate( p, state );
  }
}
