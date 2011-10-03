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

package com.googlecode.sarasvati.unittest.joinlang;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.join.JoinLangCompilationException;
import com.googlecode.sarasvati.join.JoinLangCompiler;
import com.googlecode.sarasvati.join.lang.AllArcsRequired;
import com.googlecode.sarasvati.join.lang.AndJoinExpr;
import com.googlecode.sarasvati.join.lang.AtLeastArcsRequired;
import com.googlecode.sarasvati.join.lang.AtLeastLabelArcsRequired;
import com.googlecode.sarasvati.join.lang.JoinLangExpr;
import com.googlecode.sarasvati.join.lang.JoinRequirement;
import com.googlecode.sarasvati.join.lang.LabelArcsRequired;
import com.googlecode.sarasvati.join.lang.NodeRequired;
import com.googlecode.sarasvati.join.lang.OrJoinExpr;
import com.googlecode.sarasvati.join.lang.TokenSetRequired;
import com.googlecode.sarasvati.rubric.lang.RubricExprAnd;
import com.googlecode.sarasvati.rubric.lang.RubricExprNot;
import com.googlecode.sarasvati.rubric.lang.RubricExprOr;
import com.googlecode.sarasvati.rubric.lang.RubricExprSymbol;

public class JoinLangCompilerTest
{
  @Test public void testRequireNode ()
  {
    AndJoinExpr expr = new AndJoinExpr( new NodeRequired( "foo" ) );

    String script = "require node \"foo\"";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireNodeWithWhen ()
  {
    NodeRequired nodeRequired = new NodeRequired( "foo" );
    nodeRequired.setWhenExpr( new RubricExprSymbol( "Order.isExpedited" ) );
    AndJoinExpr expr = new AndJoinExpr( nodeRequired );

    String script = "require node \"foo\" when Order.isExpedited";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( "Result does not match expected", expr.isEqualTo( result ) );
  }

  @Test public void testMultiRequireNode ()
  {
    AndJoinExpr expr = new AndJoinExpr( new NodeRequired( "foo" ) );
    expr.add( new NodeRequired( "bar" ) );

    String script = "require node \"foo\"\n" +
                    "require node \"bar\"";

    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( "Result does not match expected", expr.isEqualTo( result ) );
  }

  @Test public void testMultiRequireNodeWithWhen ()
  {
    NodeRequired require1 = new NodeRequired( "foo" );
    require1.setWhenExpr( new RubricExprAnd( new RubricExprSymbol( "Order.isExpedited" ),
                                             new RubricExprSymbol( "Order.isNew" ) ) );
    AndJoinExpr expr = new AndJoinExpr( require1 );

    NodeRequired require2 = new NodeRequired( "bar" );
    require2.setWhenExpr( new RubricExprOr( new RubricExprNot( new RubricExprSymbol( "Order.isNew" ) ),
                                            new RubricExprSymbol( "Order.isExpedited" ) ) );
    expr.add( require2 );

    String script = "require node \"foo\" when Order.isExpedited and Order.isNew \n" +
                    "require node \"bar\" when not Order.isNew or Order.isExpedited";

    System.out.println( "SCRIPT:\n " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    System.out.println( "Expected:\n"+ expr );
    System.out.println( "Result:\n" + result );
    Assert.assertTrue( "Result does not match expected", expr.isEqualTo( result ) );
  }

  @Test public void testAmbiguousOr ()
  {
    NodeRequired require1 = new NodeRequired( "foo" );
    require1.setWhenExpr( new RubricExprOr( new RubricExprSymbol( "Order.isExpedited" ),
                                             new RubricExprSymbol( "Order.isNew" ) ) );
    AndJoinExpr expr1 = new AndJoinExpr( require1 );

    NodeRequired require2 = new NodeRequired( "bar" );
    require2.setWhenExpr( new RubricExprOr( new RubricExprNot( new RubricExprSymbol( "Order.isNew" ) ),
                                            new RubricExprSymbol( "Order.isExpedited" ) ) );
    AndJoinExpr expr2 = new AndJoinExpr( require2 );
    OrJoinExpr orExpr = new OrJoinExpr( expr1, expr2 );

    String script = "require node \"foo\" when Order.isExpedited or Order.isNew \n" +
                    "or\n" +
                    "require node \"bar\" when not Order.isNew or Order.isExpedited";

    System.out.println( "SCRIPT:\n " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    System.out.println( "Expected:\n"+ orExpr );
    System.out.println( "Result:\n" + result );
    Assert.assertTrue( "Result does not match expected", orExpr.isEqualTo( result ) );
  }

  @Test public void testRequireTokenSet ()
  {
    AndJoinExpr expr = new AndJoinExpr( new TokenSetRequired( "foo" ) );

    String script = "require tokenset \"foo\"";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireTokenSetWithWhen ()
  {
    JoinRequirement jr = new TokenSetRequired( "foo" );
    jr.setWhenExpr( new RubricExprSymbol( "Order.isExpedited" ) );
    AndJoinExpr expr = new AndJoinExpr( jr );

    String script = "require tokenset \"foo\" when Order.isExpedited";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireArcs ()
  {
    AndJoinExpr expr = new AndJoinExpr( new AllArcsRequired() );

    String script = "require all arcs";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireArcsWithWhen ()
  {
    JoinRequirement jr = new AllArcsRequired();
    jr.setWhenExpr( new RubricExprSymbol( "Order.isExpedited" ) );
    AndJoinExpr expr = new AndJoinExpr( jr );

    String script = "require all arcs when Order.isExpedited";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireLabelledArcs ()
  {
    AndJoinExpr expr = new AndJoinExpr( new LabelArcsRequired( "foo" ) );

    String script = "require all arcs labelled \"foo\"";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireLabelledArcsWithWhen ()
  {
    JoinRequirement jr = new LabelArcsRequired( "foo" );
    jr.setWhenExpr( new RubricExprSymbol( "Order.isExpedited" ) );
    AndJoinExpr expr = new AndJoinExpr( jr );

    String script = "require all arcs labelled \"foo\" when Order.isExpedited";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireLabelledDefaultArcs ()
  {
    AndJoinExpr expr = new AndJoinExpr( new LabelArcsRequired( null ) );

    String script = "require all arcs labelled default";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireLabelledDefaultArcsWithWhen ()
  {
    JoinRequirement jr = new LabelArcsRequired( null );
    jr.setWhenExpr( new RubricExprSymbol( "Order.isExpedited" ) );
    AndJoinExpr expr = new AndJoinExpr( jr );

    String script = "require all arcs labelled default when Order.isExpedited";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireAtLeastArcs ()
  {
    AndJoinExpr expr = new AndJoinExpr( new AtLeastArcsRequired( 3 ) );

    String script = "require at least 3 arcs";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test (expected=JoinLangCompilationException.class)
  public void testRequireAtLeastArcsWithZero ()
  {
    String script = "require at least 0 arcs";
    System.out.println( "SCRIPT: " + script );

    JoinLangCompiler.compile( script );
  }

  @Test (expected=JoinLangCompilationException.class)
  public void testRequireAtLeastArcsWithNegativeNumber ()
  {
    String script = "require at least -1 arcs";
    System.out.println( "SCRIPT: " + script );

    JoinLangCompiler.compile( script );
  }

  @Test public void testRequireAtLeastArcsWithWhen ()
  {
    JoinRequirement jr = new AtLeastArcsRequired( 4 );
    jr.setWhenExpr( new RubricExprSymbol( "Order.isExpedited" ) );
    AndJoinExpr expr = new AndJoinExpr( jr );

    String script = "require at least 4 arcs when Order.isExpedited";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireAtLeastLabelledArcs ()
  {
    AndJoinExpr expr = new AndJoinExpr( new AtLeastLabelArcsRequired( "foo", 5 ) );

    String script = "require at least 5 arcs labelled \"foo\"";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireAtLeastLabelledArcsWithWhen ()
  {
    JoinRequirement jr = new AtLeastLabelArcsRequired( "foo", 6 );
    jr.setWhenExpr( new RubricExprSymbol( "Order.isExpedited" ) );
    AndJoinExpr expr = new AndJoinExpr( jr );

    String script = "require at least 6 arcs labelled \"foo\" when Order.isExpedited";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireAtLeastDefaultLabelledArcs ()
  {
    AndJoinExpr expr = new AndJoinExpr( new AtLeastLabelArcsRequired( null, 20 ) );

    String script = "require at least 20 arcs labelled default";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }

  @Test public void testRequireAtLeastDefaultLabelledArcsWithWhen ()
  {
    JoinRequirement jr = new AtLeastLabelArcsRequired( null, 1 );
    jr.setWhenExpr( new RubricExprSymbol( "Order.isExpedited" ) );
    AndJoinExpr expr = new AndJoinExpr( jr );

    String script = "require at least 1 arcs labelled default when Order.isExpedited";
    System.out.println( "SCRIPT: " + script );

    JoinLangExpr result = JoinLangCompiler.compile( script );
    Assert.assertTrue( expr.isEqualTo( result ) );
  }


}