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

abstract class AbstractJoinRequirementEvaluator<T extends JoinRequirement> implements JoinRequirementEvaluator
{
  private final JoinLangEnv env;
  private final T requirement;

  private Boolean applicable;

  public AbstractJoinRequirementEvaluator (final JoinLangEnv env, final T requirement)
  {
    this.env = env;
    this.requirement = requirement;
  }

  protected T getRequirement ()
  {
    return requirement;
  }

  protected JoinLangEnv getEnv ()
  {
    return env;
  }

  @Override
  public boolean isApplicable ()
  {
    if ( applicable == null )
    {
      RubricExpr expr = getRequirement().getWhenExpr();
      applicable = expr == null ? true : expr.eval( env );
    }
    return applicable;
  }
}
