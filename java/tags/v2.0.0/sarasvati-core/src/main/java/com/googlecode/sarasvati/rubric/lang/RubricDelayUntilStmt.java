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

    Copyright 2012 Paul Lorenz
*/
package com.googlecode.sarasvati.rubric.lang;

import java.util.Date;

import com.googlecode.sarasvati.impl.DelayUntilGuardResult;
import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.rubric.visitor.RubricVisitor;

public class RubricDelayUntilStmt extends AbstractRubricStmt
{
  private final RubricDateStmt dateStmt;

  public RubricDelayUntilStmt(final RubricDateStmt dateStmt)
  {
    this.dateStmt = dateStmt;
  }

  @Override
  public DelayUntilGuardResult eval(final RubricEnv env)
  {
    final Date delayTillDate = dateStmt.eval(env);
    return new DelayUntilGuardResult(delayTillDate);
  }

  /**
   * @see com.googlecode.sarasvati.rubric.lang.RubricStmt#asDelayUntilStmt()
   */
  @Override
  public RubricDelayUntilStmt asDelayUntilStmt()
  {
    return this;
  }

  @Override
  public void traverse(final RubricVisitor visitor)
  {
    visitor.visit(this);
  }

  /**
   * @see com.googlecode.sarasvati.rubric.lang.RubricStmt#isDelayUntil()
   */
  @Override
  public boolean isDelayUntil()
  {
    return true;
  }

  public RubricDateStmt getDateStmt()
  {
    return dateStmt;
  }

  @Override
  public boolean isEqualTo(final RubricStmt otherStmt)
  {
    if ( !otherStmt.isDelayUntil() )
    {
      return false;
    }

    final RubricDelayUntilStmt other = otherStmt.asDelayUntilStmt();
    return dateStmt.isEqualTo(other.getDateStmt());
  }
}