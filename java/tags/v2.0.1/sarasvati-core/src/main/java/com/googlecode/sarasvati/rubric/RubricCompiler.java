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

package com.googlecode.sarasvati.rubric;

import java.util.Date;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.rubric.lang.ErrorReporter;
import com.googlecode.sarasvati.rubric.lang.ErrorReportingRubricLexer;
import com.googlecode.sarasvati.rubric.lang.ErrorReportingRubricParser;
import com.googlecode.sarasvati.rubric.lang.RubricStmt;
import com.googlecode.sarasvati.rubric.visitor.RubricVisitor;

/**
 * Rubric is a simple rules language. It has the following grammar:
 *
 * <pre>
 * STMT = 'if' EXPR 'then' STMT 'else' STMT
 *      | Accept | Discard | Skip | Skip ARCNAME
 *      | digit
 *      | "quoted string"
 *      | '(' DATEFUNCTION ')'
 *      | '(' NUMBER (hour|hours|day|days|week|weeks) (before|after) DATEFUNCTION ')'
 *
 * EXPR = PREDICATE
 *      | PREDICATE 'or' EXPR
 *      | PREDICATE 'and' EXPR
 *      | 'not' EXPR
 *      | '(' EXPR ')'
 *
 * PREDICATE = ID
 * DATEFUNCTION = ID
 * ID = (letter)(letter|digit|'.')*  // letter followed by 0 to many letters, digits or dots
 *
 * </pre>
 *
 * A predicate used in an expression is evaluated with the help of a
 * {@link RubricEnv}. Specifically, the method {@link RubricEnv#evalPredicate(String)}
 * is called to evaluated a predicate.
 *
 * <p>
 *
 * Date functions work much the same way, except they are evaluated used the
 * {@link RubricEnv#evalDateFunction(String)} method which returns a {@link Date}.
 *
 * <p>
 *
 * A Rubric statement used in a guard might look something like
 *
 * <pre>
 * if Order.isExpedite then Accept else Skip
 * </pre>
 *
 * A Rubric statement used in generating a task due date might look like
 *
 * <pre>
 *   if Order.isExpedite then (2 days before standardInterval) else (standardInterval)
 * </pre>
 *
 * @author Paul Lorenz
 */
public class RubricCompiler
{
  /**
   * Takes a Rubric program and returns a compiled version in the form of
   * an Abstract Syntax Tree (AST). The AST can be executed/evaluated with
   * a call to {@link RubricStmt#eval(RubricEnv)} or the AST can be traversed
   * using a {@link RubricVisitor} with call to
   * {@link RubricStmt#traverse(RubricVisitor)}.
   *
   * @param rubricStatement A String containing a Rubric program
   * @throws RubricException Thrown if the program is not valid.
   * @return A RubricStmt, which is the root of the compiled AST.
   */
  public static RubricStmt compile (final String rubricStatement) throws RubricException
  {
    try
    {
      ErrorReporter errorReporter = new ErrorReporter();
      ErrorReportingRubricLexer lexer = new ErrorReportingRubricLexer( new ANTLRStringStream( rubricStatement ), errorReporter );

      CommonTokenStream stream = new CommonTokenStream( lexer );
      ErrorReportingRubricParser parser = new ErrorReportingRubricParser( stream, errorReporter );

      RubricStmt stmt = parser.program().value;
      RubricException e = errorReporter.getError();
      if ( e != null )
      {
        throw e;
      }

      return stmt;
    }
    catch ( RecognitionException re )
    {
      // this should never happen, as the parser is configured to not throw exceptions.
      System.err.println( "Received unexpected recognition exception" );
      re.printStackTrace();
      return null;
    }
  }
}
