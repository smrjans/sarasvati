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
package com.googlecode.sarasvati.join;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.JoinStrategy;

/**
 * Implements a join strategy in which nodes will wait for arc tokens to be
 * present on all incoming arcs before completing the join.
 *
 * @author Paul Lorenz
 */
public class AndJoinStrategy implements JoinStrategy
{
  protected List<Arc> getJoiningArcs (final GraphProcess process, final ArcToken token)
  {
    return process.getGraph().getInputArcs( token.getArc().getEndNode() );
  }

  @Override
  public JoinResult performJoin (final Engine engine, final ArcToken token)
  {
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

    return joinArcs.size() == tokens.size() ? new CompleteJoinResult( tokens ) :
                                              IncompleteJoinResult.INSTANCE;
  }
}
