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


public class TokenSetRequired extends AbstractJoinRequirement
{
  private final String tokenSetName;

  public TokenSetRequired (final String tokenSetName)
  {
    this.tokenSetName = tokenSetName;
  }

  public String getTokenSetName ()
  {
    return tokenSetName;
  }

  @Override
  public JoinRequirementEvaluator newEvaluator (final JoinLangEnv env)
  {
    return new TokenSetRequiredEvaluator( env, this );
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.AbstractJoinRequirement#isEqualTo(JoinRequirement)
   */
  @Override
  public boolean isEqualTo (final JoinRequirement expr)
  {
    if ( !super.isEqualTo( expr ) )
    {
      return false;
    }

    if ( !( expr instanceof TokenSetRequired) )
    {
      return false;
    }

    TokenSetRequired other = (TokenSetRequired)expr;
    return tokenSetName.equals( other.tokenSetName );
  }

  @Override
  public String toString ()
  {
    return "require tokenset " + tokenSetName + (getWhenExpr() == null ? "" : " when (" + getWhenExpr() +")" );
  }
}