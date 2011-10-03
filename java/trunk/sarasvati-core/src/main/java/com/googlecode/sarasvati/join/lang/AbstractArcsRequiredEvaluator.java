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

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.GraphProcess;

abstract class AbstractArcsRequiredEvaluator<T extends JoinRequirement> extends MultiTokenRequirementEvaluator<T>
{
  private boolean isSatisfied = false;

  public AbstractArcsRequiredEvaluator (final JoinLangEnv env, final T requirement)
  {
    super( env, requirement );
  }

  protected abstract List<Arc> getJoiningArcs (final GraphProcess process, final ArcToken token);

  protected int getNumberOfRequiredArcs (final int numArcs)
  {
    return numArcs;
  }

  @Override
  public void evaluate ()
  {
    ArcToken token = getEnv().getInitiatingToken();
    GraphProcess process = token.getProcess();
    List<Arc> joinArcs = getJoiningArcs( process, token );

    ArrayList<ArcToken> tokens = new ArrayList<ArcToken>( joinArcs.size() );

    for ( Arc arc : joinArcs )
    {
      for ( ArcToken arcToken : process.getActiveArcTokens() )
      {
        if ( arcToken.getArc().equals( arc ) )
        {
          tokens.add( arcToken );
          break;
        }
      }
    }

    includeTokens( tokens );

    if ( tokens.size() >= getNumberOfRequiredArcs( joinArcs.size() ) )
    {
      isSatisfied = true;
    }
  }

  /**
   * @see com.googlecode.sarasvati.join.lang.MultiTokenRequirementEvaluator#isSatisfied()
   */
  @Override
  public boolean isSatisfied ()
  {
    return isSatisfied;
  }
}