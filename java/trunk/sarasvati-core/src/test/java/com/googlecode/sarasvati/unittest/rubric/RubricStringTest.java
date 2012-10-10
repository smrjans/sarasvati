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

package com.googlecode.sarasvati.unittest.rubric;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.rubric.RubricCompiler;
import com.googlecode.sarasvati.rubric.RubricException;

public class RubricStringTest
{
  @Test public void testSimple ()
  {
    String expected = "foo";
    String script="\"foo\"";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( MockRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testWithQuotes ()
  {
    String expected = "foo\"bar\"";
    String script="\"foo\\\"bar\\\"\"";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( MockRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test (expected=RubricException.class)
  public void testUnbalancedQuoteFailure ()
  {
    String script="\"foo";
    System.out.println( "SCRIPT: " + script );
    RubricCompiler.compile( script ).eval( MockRubricEnv.INSTANCE );
  }

  @Test public void testWithSpaces ()
  {
    String expected = "This is a long statement, include a \"quote with stuff\"";
    String script="\"This is a long statement, include a \\\"quote with stuff\\\"\"";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( MockRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }
}
