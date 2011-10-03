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

import java.util.ArrayList;
import java.util.List;

import com.googlecode.sarasvati.JoinResult;

public class AndJoinExpr extends AbstractJoinLangExpr
{
  protected List<JoinRequirement> requirements = new ArrayList<JoinRequirement>();

  public AndJoinExpr (final JoinRequirement requirement)
  {
    requirements.add( requirement );
  }

  public void add (final JoinRequirement requirement)
  {
    requirements.add( requirement );
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.JoinLangExpr#performJoin(com.googlecode.sarasvati.join.lang.JoinLangEnv)
   */
  @Override
  public JoinResult performJoin (final JoinLangEnv joinEnv)
  {
    AndJoinExprEvaluator evaluator = new AndJoinExprEvaluator( joinEnv, requirements );
    return evaluator.evaluate();
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.JoinLangExpr#asAnd()
   */
  @Override
  public AndJoinExpr asAnd ()
  {
    return this;
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.JoinLangExpr#isAnd()
   */
  @Override
  public boolean isAnd ()
  {
    return true;
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.JoinLangExpr#isEqualTo(com.googlecode.sarasvati.join.lang.JoinLangExpr)
   */
  @Override
  public boolean isEqualTo (final JoinLangExpr expr)
  {
    if ( !expr.isAnd() )
    {
      return false;
    }

    AndJoinExpr other = expr.asAnd();

    if ( requirements.size() != other.requirements.size() )
    {
      return false;
    }

    for ( int i = 0; i < requirements.size(); i++ )
    {
      if ( !requirements.get( i ).isEqualTo( other.requirements.get( i ) ) )
      {
        return false;
      }
    }

    return true;
  }

  @Override
  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    buf.append( requirements.get( 0 ) );
    for ( int i = 1; i < requirements.size(); i++ )
    {
      buf.append( "\n" );
      buf.append( requirements.get( i ) );
    }
    return buf.toString();
  }
}