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

import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.join.IncompleteJoinResult;


public class OrJoinExpr extends AbstractJoinLangExpr
{
  protected JoinLangExpr left;
  protected JoinLangExpr right;

  public OrJoinExpr (final JoinLangExpr left, final JoinLangExpr right)
  {
    this.left = left;
    this.right = right;
  }

  /**
   * @see AbstractJoinLangExpr#performJoin(JoinLangEnv)
   */
  @Override
  public JoinResult performJoin (final JoinLangEnv joinEnv)
  {
    JoinResult result = left.performJoin( joinEnv );
    if ( result != IncompleteJoinResult.INSTANCE )
    {
      return result;
    }

    return right.performJoin( joinEnv );
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.AbstractJoinLangExpr#asOr()
   */
  @Override
  public OrJoinExpr asOr ()
  {
    return this;
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.AbstractJoinLangExpr#isOr()
   */
  @Override
  public boolean isOr ()
  {
    return true;
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.JoinLangExpr#isEqualTo(com.googlecode.sarasvati.join.lang.JoinLangExpr)
   */
  @Override
  public boolean isEqualTo (final JoinLangExpr expr)
  {
    if ( !expr.isOr() )
    {
      return false;
    }

    OrJoinExpr other = expr.asOr();
    return left.isEqualTo( other.left ) && right.isEqualTo( other.right );
  }

  @Override
  public String toString()
  {
    return left + "\n" + "or" + "\n" + right;
  }
}
