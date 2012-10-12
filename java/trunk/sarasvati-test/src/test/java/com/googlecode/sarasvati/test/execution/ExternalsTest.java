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

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;

public class ExternalsTest extends ExecutionTest
{
  @Test public void testExternal() throws Exception
  {
    ensureLoaded( "external" );
    Graph g = ensureLoaded( "external-user" );
    GraphProcess p = startProcess( g );

    NodeToken tokenA = getActiveToken( p, ":N1" );

    String state = "[1 :N1 I F]";
    TestProcess.validate( p, state );

    completeToken( tokenA, Arc.DEFAULT_ARC );

    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 I F]" +
      "[3 E2:N1 I F]"
      ;
    TestProcess.validate( p, state );

    NodeToken token = getActiveToken( p, "E1:N1" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 I F]" +
      "[4 E1:N2 I F]" +
      "[5 E1:N3 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E1:N3" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 I F]" +
      "[4 E1:N2 I F]" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E2:N1" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 I F]" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 I F]" +
      "[7 E2:N3 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E1:N2" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 I F]" +
      "[7 E2:N3 I F]" +
      "[8 E1:N4 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E2:N2" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 I F]" +
      "[8 E1:N4 I F]" +
      "[9 E2:N4 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E2:N4" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 I F]" +
      "[8 E1:N4 I F]" +
      "[9 E2:N4 C F]" +
      "  (I F E2:N5)"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E2:N3" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 C F]" +
      "  (C F 10)" +
      "[8 E1:N4 I F]" +
      "[9 E2:N4 C F]" +
      "  (C F 10)" +
      "[10 E2:N5 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E2:N5" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 C F]" +
      "  (C F 10)" +
      "[8 E1:N4 I F]" +
      "[9 E2:N4 C F]" +
      "  (C F 10)" +
      "[10 E2:N5 C F]" +
      "  (I F :N2)"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E1:N4" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (C F 11)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 C F]" +
      "  (C F 10)" +
      "[8 E1:N4 C F]" +
      "  (C F 11)" +
      "[9 E2:N4 C F]" +
      "  (C F 10)" +
      "[10 E2:N5 C F]" +
      "  (I F :N2)" +
      "[11 E1:N5 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E1:N5" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (C F 11)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 C F]" +
      "  (C F 10)" +
      "[8 E1:N4 C F]" +
      "  (C F 11)" +
      "[9 E2:N4 C F]" +
      "  (C F 10)" +
      "[10 E2:N5 C F]" +
      "  (C F 12)" +
      "[11 E1:N5 C F]" +
      "  (C F 12)" +
      "[12 :N2 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, ":N2" );
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (C F 11)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 C F]" +
      "  (C F 10)" +
      "[8 E1:N4 C F]" +
      "  (C F 11)" +
      "[9 E2:N4 C F]" +
      "  (C F 10)" +
      "[10 E2:N5 C F]" +
      "  (C F 12)" +
      "[11 E1:N5 C F]" +
      "  (C F 12)" +
      "[12 :N2 C F]"
      ;
    TestProcess.validate( p, state );

    Assert.assertTrue(p.isComplete());
  }
}