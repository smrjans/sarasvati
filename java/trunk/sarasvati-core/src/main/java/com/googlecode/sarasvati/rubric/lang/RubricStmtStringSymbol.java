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

import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.rubric.visitor.RubricVisitor;
import com.googlecode.sarasvati.util.SvUtil;

public class RubricStmtStringSymbol extends AbstractRubricStmt
{
  protected String symbol;

  public RubricStmtStringSymbol (final String symbol)
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
  public String eval (final RubricEnv env)
  {
    return env.evalStringFunction( symbol );
  }

  @Override
  public void traverse (final RubricVisitor visitor)
  {
    visitor.visit( this );
  }

  @Override
  public boolean isEqualTo (final RubricStmt stmt)
  {
    return stmt.isStringSymbol() && SvUtil.equals( symbol, stmt.asStringSymbol().getSymbol() );
  }

  @Override
  public RubricStmtStringSymbol asStringSymbol ()
  {
    return this;
  }

  @Override
  public boolean isStringSymbol ()
  {
    return true;
  }
}
