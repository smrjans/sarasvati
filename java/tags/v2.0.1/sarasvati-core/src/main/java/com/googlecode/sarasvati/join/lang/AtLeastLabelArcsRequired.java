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


public class AtLeastLabelArcsRequired extends AbstractJoinRequirement
{
  private final String label;
  private final int arcsRequired;

  public AtLeastLabelArcsRequired (final String label, final int arcsRequired)
  {
    this.label = label;
    this.arcsRequired = arcsRequired;
  }

  public int getArcsRequired ()
  {
    return arcsRequired;
  }

  public String getLabel ()
  {
    return label;
  }

  @Override
  public JoinRequirementEvaluator newEvaluator (final JoinLangEnv env)
  {
    return new AtLeastLabelArcsRequiredEvaluator( env, this );
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.AbstractJoinRequirement#isEqualTo(com.googlecode.sarasvati.join.lang.JoinRequirement)
   */
  @Override
  public boolean isEqualTo (final JoinRequirement req)
  {
    if ( !super.isEqualTo( req ) )
    {
      return false;
    }

    if ( !( req instanceof AtLeastLabelArcsRequired) )
    {
      return false;
    }

    AtLeastLabelArcsRequired other = (AtLeastLabelArcsRequired)req;
    if ( arcsRequired != other.arcsRequired )
    {
      return false;
    }

    return SvUtil.equals( label, other.label );
  }

  @Override
  public String toString ()
  {
    return "require at least " + arcsRequired + " arcs labelled " +
           (label == null ? "default" : label ) +
           (getWhenExpr() == null ? "" : " when (" + getWhenExpr() +")" );
  }
}