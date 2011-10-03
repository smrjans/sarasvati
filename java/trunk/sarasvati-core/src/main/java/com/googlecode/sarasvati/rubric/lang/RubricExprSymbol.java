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

package com.googlecode.sarasvati.rubric.lang;

import com.googlecode.sarasvati.rubric.env.PredicateEnv;
import com.googlecode.sarasvati.rubric.visitor.RubricVisitor;

public class RubricExprSymbol extends AbstractRubricExpr
{
  protected String symbol;

  public RubricExprSymbol (final String symbol)
  {
    this.symbol = symbol;
  }

  public String getSymbol ()
  {
    return symbol;
  }

  public void setSymbol (final String symbol)
  {
    this.symbol = symbol;
  }

  @Override
  public boolean eval (final PredicateEnv env)
  {
    return env.evalPredicate( symbol );
  }

  @Override
  public void traverse (final RubricVisitor visitor)
  {
    visitor.visit( this );
  }

  @Override
  public boolean isSymbol ()
  {
    return true;
  }

  @Override
  public RubricExprSymbol asSymbol ()
  {
    return this;
  }

  @Override
  public boolean isEqualTo (final RubricExpr expr)
  {
    return expr.isSymbol() && expr.asSymbol().getSymbol().equals( symbol );
  }

  @Override
  public String toString ()
  {
    return symbol;
  }
}