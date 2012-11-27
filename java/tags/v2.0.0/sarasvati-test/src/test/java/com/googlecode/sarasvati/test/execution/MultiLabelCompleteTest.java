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

    Copyright 2012 Paul Lorenz
*/
package com.googlecode.sarasvati.test.execution;

import org.junit.Test;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;

public class MultiLabelCompleteTest extends ExecutionTest
{
  @Test
  public void testMultiArc() throws Exception
  {
    GraphProcess p = startProcess("multi-arc-complete");

    NodeToken token = getActiveToken( p, ":N1" );

    String state = "[1 :N1 I F]";
    TestProcess.validate( p, state );

    completeToken( token, "arc1", "arc2", Arc.DEFAULT_ARC );

    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "  (C F 4)" +
      "[2 N2 I F]" +
      "[3 N3 I F]" +
      "[4 N4 I F]"
      ;
    TestProcess.validate( p, state );

    // double check that completeing the same token twice doesn't have any effect
    completeToken( token, "arc1", "arc2", Arc.DEFAULT_ARC );

    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "  (C F 4)" +
      "[2 N2 I F]" +
      "[3 N3 I F]" +
      "[4 N4 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "N3" );

    completeToken( token, Arc.DEFAULT_ARC );
    state =
        "[1 :N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "[2 N2 I F]" +
        "[3 N3 C F]" +
        "  (I F N5)" +
        "[4 N4 I F]"
        ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "N4" );

    completeToken( token, Arc.DEFAULT_ARC );
    state =
        "[1 :N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "[2 N2 I F]" +
        "[3 N3 C F]" +
        "  (I F N5)" +
        "[4 N4 C F]" +
        "  (I F N5)"
        ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "N2" );

    completeToken( token, Arc.DEFAULT_ARC );
    state =
        "[1 :N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "[2 N2 C F]" +
        "  (C F 5)" +
        "[3 N3 C F]" +
        "  (C F 5)" +
        "[4 N4 C F]" +
        "  (C F 5)" +
        "[5 N5 I F]"
        ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "N5" );

    completeToken( token, Arc.DEFAULT_ARC );
    state =
        "[1 :N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "[2 N2 C F]" +
        "  (C F 5)" +
        "[3 N3 C F]" +
        "  (C F 5)" +
        "[4 N4 C F]" +
        "  (C F 5)" +
        "[5 N5 C F]"
        ;
    TestProcess.validate( p, state );

    verifyComplete(p);
  }

  @Test
  public void testMultiArc2() throws Exception
  {
    GraphProcess p = startProcess("multi-arc-complete2");

    NodeToken token = getActiveToken( p, ":N1" );

    String state = "[1 :N1 I F]";
    TestProcess.validate( p, state );

    completeToken( token, "arc1", "arc2");

    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 N2 I F]" +
      "[3 N3 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "N2" );

    completeToken( token, Arc.DEFAULT_ARC );
    state =
        "[1 :N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 I F]" +
        "[4 N5 I F]"
        ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "N5" );

    completeToken( token, Arc.DEFAULT_ARC );
    state =
        "[1 :N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 I F]" +
        "[4 N5 C F]"
        ;
    TestProcess.validate( p, state );

    verifyExecuting(p);

    token = getActiveToken( p, "N3" );

    completeToken( token, Arc.DEFAULT_ARC );
    state =
        "[1 :N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 C F]" +
        "  (C F 4)" +
        "[4 N5 C F]"
        ;
    TestProcess.validate( p, state );

    verifyComplete(p);
  }
}