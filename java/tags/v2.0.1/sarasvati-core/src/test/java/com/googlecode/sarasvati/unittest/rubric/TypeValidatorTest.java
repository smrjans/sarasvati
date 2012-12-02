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

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.rubric.RubricCompiler;
import com.googlecode.sarasvati.rubric.lang.RubricStmt;
import com.googlecode.sarasvati.rubric.visitor.ResultTypeValidator;

public class TypeValidatorTest
{
  @Test public void testStringCheckOnString ()
  {
    String program = "\"foo\"";
    RubricStmt stmt = RubricCompiler.compile( program );
    Assert.assertTrue( "All result type should be string", ResultTypeValidator.isResultOfType( stmt, String.class ) );

    program = "if a or b then \"foo\" else \"bar\"";
    stmt = RubricCompiler.compile( program );
    Assert.assertTrue( "All result type should be string", ResultTypeValidator.isResultOfType( stmt, String.class ) );
  }

  @Test public void testStringCheckOnNumber ()
  {
    String program = "1";
    RubricStmt stmt = RubricCompiler.compile( program );
    ResultTypeValidator validator = new ResultTypeValidator( String.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect int", validator.isAllResultsMatchType() );

    program = "if a or b then \"foo\" else 2";
    stmt = RubricCompiler.compile( program );
    validator = new ResultTypeValidator( String.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect int", validator.isAllResultsMatchType() );
  }

  @Test public void testStringCheckOnDate ()
  {
    String program = "(now)";
    RubricStmt stmt = RubricCompiler.compile( program );
    ResultTypeValidator validator = new ResultTypeValidator( String.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect date", validator.isAllResultsMatchType() );

    program = "if a or b then \"foo\" else (1 day before now)";
    stmt = RubricCompiler.compile( program );
    validator = new ResultTypeValidator( String.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect date", validator.isAllResultsMatchType() );
  }

  @Test public void testStringCheckOnGuardResult ()
  {
    String program = "Accept";
    RubricStmt stmt = RubricCompiler.compile( program );
    ResultTypeValidator validator = new ResultTypeValidator( String.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect guard response", validator.isAllResultsMatchType() );

    program = "if a or b then \"foo\" else Discard";
    stmt = RubricCompiler.compile( program );
    validator = new ResultTypeValidator( String.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect guard result", validator.isAllResultsMatchType() );
  }

  @Test public void testGuardResultCheckOnGuardResult ()
  {
    String program = "Accept";
    RubricStmt stmt = RubricCompiler.compile( program );
    ResultTypeValidator validator = new ResultTypeValidator( GuardResult.class );
    stmt.traverse( validator );
    Assert.assertTrue( "All result type should be guard result", validator.isAllResultsMatchType() );

    program = "if a or b then Discard else Accept";
    stmt = RubricCompiler.compile( program );
    validator = new ResultTypeValidator( GuardResult.class );
    stmt.traverse( validator );
    Assert.assertTrue( "All result type should be guard result", validator.isAllResultsMatchType() );
  }

  @Test public void testGuardResultCheckOnNumber ()
  {
    String program = "1";
    RubricStmt stmt = RubricCompiler.compile( program );
    ResultTypeValidator validator = new ResultTypeValidator( GuardResult.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect int", validator.isAllResultsMatchType() );

    program = "if a or b then Accept else 2";
    stmt = RubricCompiler.compile( program );
    validator = new ResultTypeValidator( GuardResult.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect int", validator.isAllResultsMatchType() );
  }

  @Test public void testGuardResultCheckOnDate ()
  {
    String program = "(now)";
    RubricStmt stmt = RubricCompiler.compile( program );
    ResultTypeValidator validator = new ResultTypeValidator( GuardResult.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect date", validator.isAllResultsMatchType() );

    program = "if a or b then Accept else (1 day before now)";
    stmt = RubricCompiler.compile( program );
    validator = new ResultTypeValidator( GuardResult.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect date", validator.isAllResultsMatchType() );
  }

  @Test public void testGuardResultCheckOnString ()
  {
    String program = "\"foo\"";
    RubricStmt stmt = RubricCompiler.compile( program );
    ResultTypeValidator validator = new ResultTypeValidator( GuardResult.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect guard result", validator.isAllResultsMatchType() );

    program = "if a or b then \"foo\" else Discard";
    stmt = RubricCompiler.compile( program );
    validator = new ResultTypeValidator( GuardResult.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect guard result", validator.isAllResultsMatchType() );
  }

  @Test public void testDateCheckOnDate ()
  {
    String program = "(now)";
    RubricStmt stmt = RubricCompiler.compile( program );
    Assert.assertTrue( "All result type should be date", ResultTypeValidator.isResultOfType( stmt, Date.class ) );

    program = "if a or b then (now) else (1 day after now)";
    stmt = RubricCompiler.compile( program );
    Assert.assertTrue( "All result type should be date", ResultTypeValidator.isResultOfType( stmt, Date.class ) );

    program = "if A.b and C.d and E.f then (immediately) else if not G.h or J.k then (5 business days after immediately) else (immediately)";
    stmt = RubricCompiler.compile( program );
    Assert.assertTrue( "All result type should be date", ResultTypeValidator.isResultOfType( stmt, Date.class ) );
  }

  @Test public void testDateCheckOnNumber ()
  {
    String program = "1";
    RubricStmt stmt = RubricCompiler.compile( program );
    ResultTypeValidator validator = new ResultTypeValidator( Date.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect int", validator.isAllResultsMatchType() );

    program = "if a or b then (now) else 2";
    stmt = RubricCompiler.compile( program );
    validator = new ResultTypeValidator( Date.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect int", validator.isAllResultsMatchType() );
  }

  @Test public void testDateCheckOnGuardResult ()
  {
    String program = "Accept";
    RubricStmt stmt = RubricCompiler.compile( program );
    ResultTypeValidator validator = new ResultTypeValidator( Date.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect date", validator.isAllResultsMatchType() );

    program = "if a or b then Accept else (1 day before now)";
    stmt = RubricCompiler.compile( program );
    validator = new ResultTypeValidator( Date.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect date", validator.isAllResultsMatchType() );
  }

  @Test public void testDateCheckOnString ()
  {
    String program = "\"foo\"";
    RubricStmt stmt = RubricCompiler.compile( program );
    ResultTypeValidator validator = new ResultTypeValidator( Date.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect guard response", validator.isAllResultsMatchType() );

    program = "if a or b then \"foo\" else (now)";
    stmt = RubricCompiler.compile( program );
    validator = new ResultTypeValidator( Date.class );
    stmt.traverse( validator );
    Assert.assertFalse( "Should detect guard response", validator.isAllResultsMatchType() );
  }
}
