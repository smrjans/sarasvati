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
package com.googlecode.sarasvati.unittest.rubric;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.impl.AcceptTokenGuardResult;
import com.googlecode.sarasvati.impl.DiscardTokenGuardResult;
import com.googlecode.sarasvati.impl.SkipNodeGuardResult;
import com.googlecode.sarasvati.rubric.RubricCompilationException;
import com.googlecode.sarasvati.rubric.RubricCompiler;
import com.googlecode.sarasvati.rubric.lang.RubricStmt;
import com.googlecode.sarasvati.rubric.lang.RubricStmtRelativeDate;
import com.googlecode.sarasvati.rubric.lang.RubricStmtResult;

public class RubricCompilerTest
{
  @Test public void testAccept ()
  {
    RubricStmt expected = new RubricStmtResult( AcceptTokenGuardResult.INSTANCE );

    String script = "Accept";
    System.out.println( "SCRIPT: " + script );

    RubricStmt result = RubricCompiler.compile( script );
    Assert.assertTrue( expected.isEqualTo( result ) );
  }

  @Test public void testDiscard ()
  {
    RubricStmt expected = new RubricStmtResult( DiscardTokenGuardResult.INSTANCE );

    String script = "Discard";
    System.out.println( "SCRIPT: " + script );

    RubricStmt result = RubricCompiler.compile( script );
    Assert.assertTrue( expected.isEqualTo( result ) );
  }

  @Test public void testSkip ()
  {
    RubricStmt expected = new RubricStmtResult( SkipNodeGuardResult.DEFAULT_ARC_SKIP_NODE_RESULT );

    String script = "Skip";
    System.out.println( "SCRIPT: " + script );

    RubricStmt result = RubricCompiler.compile( script );
    Assert.assertTrue( expected.isEqualTo( result ) );
  }

  @Test public void testSkipOnArc ()
  {
    RubricStmt expected = new RubricStmtResult( new SkipNodeGuardResult( "foo" ) );

    String script = "Skip foo";
    System.out.println( "SCRIPT: " + script );

    RubricStmt result = RubricCompiler.compile( script );
    Assert.assertTrue( expected.isEqualTo( result ) );
  }

  @Test public void testSkipOnStringArc ()
  {
    RubricStmt expected = new RubricStmtResult( new SkipNodeGuardResult( "something with spaces" ) );

    String script = "Skip \"something with spaces\"";
    System.out.println( "SCRIPT: " + script );

    RubricStmt result = RubricCompiler.compile( script );
    Assert.assertTrue( expected.isEqualTo( result ) );
  }

  @Test public void testDate ()
  {
    RubricStmt expected = new RubricStmtRelativeDate( 1, false, "day", "before", "DueDate" );

    String script = "(1 day before DueDate)";
    System.out.println( "SCRIPT: " + script );

    RubricStmt result = RubricCompiler.compile( script );
    Assert.assertTrue( expected.isEqualTo( result ) );
  }

  @Test(expected=RubricCompilationException.class)
  public void testDateMissingOffset ()
  {
    String script = "(day before DueDate)";
    System.out.println( "SCRIPT: " + script );
    RubricCompiler.compile( script );
  }

  @Test(expected=RubricCompilationException.class)
  public void testDateBadOffset ()
  {
    String script = "(1f day before DueDate)";
    System.out.println( "SCRIPT: " + script );
    RubricCompiler.compile( script );
  }

  @Test(expected=RubricCompilationException.class)
  public void testDateBadOffset2 ()
  {
    String script = "(+1 day before DueDate)";
    System.out.println( "SCRIPT: " + script );
    RubricCompiler.compile( script );
  }

}
