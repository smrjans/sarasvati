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

    Copyright 2008-2009 Paul Lorenz
*/

package com.googlecode.sarasvati.unittest.rubric;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.impl.AcceptTokenGuardResult;
import com.googlecode.sarasvati.impl.DiscardTokenGuardResult;
import com.googlecode.sarasvati.impl.SkipNodeGuardResult;
import com.googlecode.sarasvati.rubric.RubricCompiler;
import com.googlecode.sarasvati.unittest.framework.TestRubricEnv;

public class RubricGuardResultTest
{
  @Test public void testAccept ()
  {
    GuardResult expected = AcceptTokenGuardResult.INSTANCE;
    String script="Accept";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testDiscard ()
  {
    GuardResult expected = DiscardTokenGuardResult.INSTANCE;
    String script="Discard";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testSkip ()
  {
    GuardResult expected = SkipNodeGuardResult.DEFAULT_ARC_SKIP_NODE_RESULT;
    String script="Skip";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testSkipWithArcName ()
  {
    GuardResult expected = new SkipNodeGuardResult( "testArc" );
    String script="Skip testArc";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testSkipWithArcNameQuoted ()
  {
    GuardResult expected = new SkipNodeGuardResult( "testArc" );
    String script="Skip \"testArc\"";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testSkipWithArcNameQuotedAndWithSpaces ()
  {
    GuardResult expected = new SkipNodeGuardResult( "test Arc" );
    String script="Skip \"test Arc\"";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testSkipWithArcNameQuotedWithSpacesAndQuotes ()
  {
    GuardResult expected = new SkipNodeGuardResult( "test \"Arc\"" );
    String script="Skip \"test \\\"Arc\\\"\"";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( TestRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }
}