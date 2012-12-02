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

package com.googlecode.sarasvati.rubric.visitor;

import com.googlecode.sarasvati.rubric.lang.RubricDelayUntilStmt;
import com.googlecode.sarasvati.rubric.lang.RubricExprAnd;
import com.googlecode.sarasvati.rubric.lang.RubricExprNot;
import com.googlecode.sarasvati.rubric.lang.RubricExprOr;
import com.googlecode.sarasvati.rubric.lang.RubricExprSymbol;
import com.googlecode.sarasvati.rubric.lang.RubricStmtDateSymbol;
import com.googlecode.sarasvati.rubric.lang.RubricStmtIf;
import com.googlecode.sarasvati.rubric.lang.RubricStmtRelativeDate;
import com.googlecode.sarasvati.rubric.lang.RubricStmtResult;
import com.googlecode.sarasvati.rubric.lang.RubricStmtStringSymbol;

public class RubricVisitorAdaptor implements RubricVisitor
{
  @Override
  public void visit (final RubricStmtIf ifStmt)
  {
    // does nothing by default
  }

  @Override
  public void visit (final RubricStmtDateSymbol dateSymbolStmt)
  {
    // does nothing by default
  }

  @Override
  public void visit (final RubricStmtStringSymbol stringSymbolStmt)
  {
    // does nothing by default
  }

  @Override
  public void visit (final RubricStmtRelativeDate relativeDateStmt)
  {
    // does nothing by default
  }

  @Override
  public void visit (final RubricStmtResult resultStmt)
  {
    // does nothing by default
  }

  @Override
  public void visit (final RubricExprAnd andExpr)
  {
    // does nothing by default
  }

  @Override
  public void visit (final RubricExprNot notExpr)
  {
    // does nothing by default
  }

  @Override
  public void visit (final RubricExprOr orExpr)
  {
    // does nothing by default
  }

  @Override
  public void visit (final RubricExprSymbol symbolExpr)
  {
    // does nothing by default
  }

  /**
   * @see com.googlecode.sarasvati.rubric.visitor.RubricVisitor#visit(com.googlecode.sarasvati.rubric.lang.RubricDelayUntilStmt)
   */
  @Override
  public void visit(final RubricDelayUntilStmt delayUntilStmt)
  {
    // does nothing by default
  }
}