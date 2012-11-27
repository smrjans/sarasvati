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

    Copyright 2009 Paul Lorenz
*/
package com.googlecode.sarasvati.join.lang;

import com.googlecode.sarasvati.rubric.lang.RubricExpr;

public abstract class AbstractJoinRequirement implements JoinRequirement
{
  private RubricExpr whenExpr;

  /**
   * @return the whenExpr
   */
  @Override
  public RubricExpr getWhenExpr ()
  {
    return whenExpr;
  }

  /**
   * @param whenExpr the whenExpr to set
   */
  @Override
  public void setWhenExpr (final RubricExpr whenExpr)
  {
    this.whenExpr = whenExpr;
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.JoinLangExpr#isEqualTo(com.googlecode.sarasvati.join.lang.JoinLangExpr)
   */
  @Override
  public boolean isEqualTo (final JoinRequirement expr)
  {
    if ( whenExpr == null )
    {
      return expr.getWhenExpr() == null;
    }

    return whenExpr.isEqualTo( expr.getWhenExpr() );
  }
}
