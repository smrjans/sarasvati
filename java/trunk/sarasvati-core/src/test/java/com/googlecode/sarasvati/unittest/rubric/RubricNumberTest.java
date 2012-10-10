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

public class RubricNumberTest
{
  @Test public void testSimple ()
  {
    Integer expected = 1;
    String script="1";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( MockRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }

  @Test public void testNegative ()
  {
    Integer expected = -10000;
    String script="-10000";
    System.out.println( "SCRIPT: " + script );

    Object result = RubricCompiler.compile( script ).eval( MockRubricEnv.INSTANCE );

    Assert.assertEquals( expected, result );
  }
}
