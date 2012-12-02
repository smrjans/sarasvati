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

import com.googlecode.sarasvati.rubric.RubricCompiler;
import com.googlecode.sarasvati.rubric.lang.RubricStmt;
import com.googlecode.sarasvati.rubric.visitor.DropNotToAboveLeavesVisitor;

public class NotPushDownTest
{
  @Test public void testOrPushdown ()
  {
    String a = "if not (isThis or isThat) then Accept else Discard";
    String b = "if not isThis and not isThat then Accept else Discard";

    RubricStmt stmtA = RubricCompiler.compile( a );
    RubricStmt stmtB = RubricCompiler.compile( b );

    DropNotToAboveLeavesVisitor.process( stmtA );
    Assert.assertTrue( "stmts should match", stmtA.isEqualTo( stmtB ) );
  }

  @Test public void testAndPushdown ()
  {
    String a = "if not (isThis and isThat) then Accept else Discard";
    String b = "if not isThis or not isThat then Accept else Discard";

    RubricStmt stmtA = RubricCompiler.compile( a );
    RubricStmt stmtB = RubricCompiler.compile( b );

    DropNotToAboveLeavesVisitor.process( stmtA );
    Assert.assertTrue( "stmts should match", stmtA.isEqualTo( stmtB ) );
  }

  @Test public void testNestedOrPushdown ()
  {
    String a = "if foo and not (isThis or isThat) then Accept else Discard";
    String b = "if foo and (not isThis and not isThat) then Accept else Discard";

    RubricStmt stmtA = RubricCompiler.compile( a );
    RubricStmt stmtB = RubricCompiler.compile( b );

    DropNotToAboveLeavesVisitor.process( stmtA );
    Assert.assertTrue( "stmts should match", stmtA.isEqualTo( stmtB ) );
  }

  @Test public void testNestedAndPushdown ()
  {
    String a = "if foo and not (isThis and isThat) then Accept else Discard";
    String b = "if foo and (not isThis or not isThat) then Accept else Discard";

    RubricStmt stmtA = RubricCompiler.compile( a );
    RubricStmt stmtB = RubricCompiler.compile( b );

    DropNotToAboveLeavesVisitor.process( stmtA );
    Assert.assertTrue( "stmts should match", stmtA.isEqualTo( stmtB ) );
  }

  @Test public void testOrPushdownWithNot ()
  {
    String a = "if not (isThis or not isThat) then Accept else Discard";
    String b = "if not isThis and isThat then Accept else Discard";

    RubricStmt stmtA = RubricCompiler.compile( a );
    RubricStmt stmtB = RubricCompiler.compile( b );

    DropNotToAboveLeavesVisitor.process( stmtA );
    Assert.assertTrue( "stmts should match", stmtA.isEqualTo( stmtB ) );
  }

  @Test public void testAndPushdownWithNot ()
  {
    String a = "if not (isThis and not isThat) then Accept else Discard";
    String b = "if not isThis or isThat then Accept else Discard";

    RubricStmt stmtA = RubricCompiler.compile( a );
    RubricStmt stmtB = RubricCompiler.compile( b );

    DropNotToAboveLeavesVisitor.process( stmtA );
    Assert.assertTrue( "stmts should match", stmtA.isEqualTo( stmtB ) );
  }

  @Test public void testComplex()
  {
    String a = "if not ((not (not foo or not bar)) and not (bar and not baz)) then Accept else Discard";
    String b = "if (not foo or not bar) or (bar and not baz) then Accept else Discard";

    RubricStmt stmtA = RubricCompiler.compile( a );
    RubricStmt stmtB = RubricCompiler.compile( b );

    DropNotToAboveLeavesVisitor.process( stmtA );
    Assert.assertTrue( "stmts should match", stmtA.isEqualTo( stmtB ) );
  }
}
