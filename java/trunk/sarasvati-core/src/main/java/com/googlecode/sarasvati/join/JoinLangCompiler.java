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

package com.googlecode.sarasvati.join;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import com.googlecode.sarasvati.join.lang.ErrorReporter;
import com.googlecode.sarasvati.join.lang.ErrorReportingJoinLangLexer;
import com.googlecode.sarasvati.join.lang.ErrorReportingJoinLangParser;
import com.googlecode.sarasvati.join.lang.JoinLangExpr;
import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.rubric.lang.RubricStmt;
import com.googlecode.sarasvati.rubric.visitor.RubricVisitor;

/**
 * JoinLang is a simple language for defining join requirements
 *
 * <pre>
 *   PROGRAM = (REQUIRESET) ('or' REQUIRESET)*
 *   REQUIRESET = REQUIREMENT (REQUIREMENT)*
 *   REQUIREMENT = 'require' 'node' <node name> WHEN?
 *               | 'require' 'tokenset' <token set name> WHEN?
 *
 *   WHEN = 'when' EXPR
 * </pre>
 *
 * A JoinLang program is made up of requirements, grouped in require sets.
 * Require sets are separated by 'or's. The requirements in a require set
 * are implicitly anded.
 *
 * Rules of evaluation:
 *
 * <ol>
 *   <li>
 *     Requirement sets will be evaluated from first to last. Evaluation
 *     will stop with the first requirement set that returns either a join
 *     completion or join merge.
 *   </li>
 *   <li>
 *     Each requirement will be marked as applicable or non-applicable.
 *     A requirement with either no <code>when</when> clause or a
 *     <code>when</code> clause that evaluates to true will marked
 *     as applicable, otherwise it will be marked non-applicable.
 *   </li>
 *   <li>
 *     Each requirement will be evaluated to see if it is satisfied.
 *     As part of this evaluation, which tokens are involved in
 *     meeting the requirement will be tracked.
 *   </li>
 *   <li>
 *     If all applicable requirements in the requirement set are
 *     satisfied, then the join will be completed. All tokens which
 *     were involved in the join (for applicable and non-applicable
 *     requirement) will be completed in the join.
 *   </li>
 *   <li>
 *     Otherwise, when all applicable requirements in the requirement set are not
 *     satisfied a merge will be attempted. If there exist any optional tokens
 *     (those affected by non-applicable requirements) and there exist any
 *     non-backtracked node tokens on the joining node, the optional tokens will be
 *     merged into the newest, non-backtracked node token.
 *   <li>
 *   <li>
 *     If a merge is not possible, because there are no optional tokens, or no
 *     valid node tokens on the join node, the next requirement set will be
 *     evaluated. If this is the last requirement set, an incomplete join
 *     action will be returned.
 *   </li>
 * </ol>
 *
 * @author Paul Lorenz
 */
public class JoinLangCompiler
{
  /**
   * Takes a JoinLang program and returns a compiled version in the form of
   * an Abstract Syntax Tree (AST). The AST can be executed/evaluated with
   * a call to {@link RubricStmt#eval(RubricEnv)} or the AST can be traversed
   * using a {@link RubricVisitor} with call to
   * {@link RubricStmt#traverse(RubricVisitor)}.
   *
   * @param joinSpecStmt A String containing a Rubric program
   *
   * @throws JoinLangException Thrown if the program is not valid.
   *
   * @return A RubricStmt, which is the root of the compiled AST.
   */
  public static JoinLangExpr compile (final String joinSpecStmt) throws JoinLangException
  {
    try
    {
      ErrorReporter errorReporter = new ErrorReporter();
      ErrorReportingJoinLangLexer lexer = new ErrorReportingJoinLangLexer( new ANTLRStringStream( joinSpecStmt ), errorReporter );

      CommonTokenStream stream = new CommonTokenStream( lexer );
      ErrorReportingJoinLangParser parser = new ErrorReportingJoinLangParser( stream, errorReporter );

      JoinLangExpr expr = parser.program().value;
      JoinLangException e = errorReporter.getError();
      if ( e != null )
      {
        throw e;
      }

      return expr;
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