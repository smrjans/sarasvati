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

import java.util.List;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.GraphProcess;

class AtLeastLabelArcsRequiredEvaluator extends AbstractArcsRequiredEvaluator<AtLeastLabelArcsRequired>
{
  public AtLeastLabelArcsRequiredEvaluator (final JoinLangEnv env, final AtLeastLabelArcsRequired requirement)
  {
    super( env, requirement );
  }

  @Override
  protected List<Arc> getJoiningArcs (final GraphProcess process, final ArcToken token)
  {
    return process.getGraph().getInputArcs( token.getArc().getEndNode(), getRequirement().getLabel() );
  }

  @Override
  protected int getNumberOfRequiredArcs (final int numArcs)
  {
    return Math.min( getRequirement().getArcsRequired(), numArcs );
  }
}