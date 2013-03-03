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

public class RubricStmtResult extends AbstractRubricStmt
{
  protected Object result;

  public RubricStmtResult (final Object result)
  {
    this.result = result;
  }

  public Object getResult ()
  {
    return result;
  }

  public void setResult (final Object result)
  {
    this.result = result;
  }

  @Override
  public Object eval (final RubricEnv env)
  {
    return result;
  }

  @Override
  public void traverse (final RubricVisitor visitor)
  {
    visitor.visit( this );
  }

  @Override
  public boolean isEqualTo (final RubricStmt stmt)
  {
    return stmt.isResult() && stmt.asResult().getResult().equals( result );
  }

  @Override
  public RubricStmtResult asResult ()
  {
    return this;
  }

  @Override
  public boolean isResult ()
  {
    return true;
  }
}