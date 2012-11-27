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

public class RubricExprNot extends AbstractRubricExpr
{
  protected RubricExpr expr;

  public RubricExprNot (final RubricExpr expr)
  {
    this.expr = expr;
  }

  public RubricExpr getExpr ()
  {
    return expr;
  }

  public void setExpr (final RubricExpr expr)
  {
    this.expr = expr;
  }

  @Override
  public boolean eval (final PredicateEnv env)
  {
    return !expr.eval( env );
  }

  @Override
  public void traverse (final RubricVisitor visitor)
  {
    visitor.visit( this );
    expr.traverse( visitor );
  }

  @Override
  public boolean isNot ()
  {
    return true;
  }

  @Override
  public RubricExprNot asNot ()
  {
    return this;
  }

  @Override
  public boolean isEqualTo (final RubricExpr e)
  {
    return e.isNot() && e.asNot().getExpr().isEqualTo( expr );
  }

  @Override
  public String toString ()
  {
    return "(not " + expr + ")";
  }
}
