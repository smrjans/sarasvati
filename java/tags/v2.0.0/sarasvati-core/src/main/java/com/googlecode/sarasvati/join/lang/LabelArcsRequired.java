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

import com.googlecode.sarasvati.util.SvUtil;


public class LabelArcsRequired extends AbstractJoinRequirement
{
  private final String label;

  public LabelArcsRequired (final String label)
  {
    this.label = label;
  }

  public String getLabel ()
  {
    return label;
  }

  @Override
  public JoinRequirementEvaluator newEvaluator (final JoinLangEnv env)
  {
    return new LabelArcsRequiredEvaluator( env, this );
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

    if ( !( expr instanceof LabelArcsRequired) )
    {
      return false;
    }

    LabelArcsRequired other = (LabelArcsRequired)expr;
    return SvUtil.equals( label, other.label );
  }

  @Override
  public String toString ()
  {
    return "require all arcs labelled " +
           ( label == null ? "default" : label ) +
           (getWhenExpr() == null ? "" : " when (" + getWhenExpr() +")" );
  }
}