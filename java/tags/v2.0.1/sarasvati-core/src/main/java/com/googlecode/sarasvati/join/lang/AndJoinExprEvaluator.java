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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.join.CompleteJoinResult;
import com.googlecode.sarasvati.join.IncompleteJoinResult;
import com.googlecode.sarasvati.join.MergeJoinResult;

class AndJoinExprEvaluator
{
  private final JoinLangEnv env;
  private final List<JoinRequirementEvaluator> evaluators;

  private boolean isSatisfied               = false;
  private boolean isInitiatingTokenRequired = false;
  private boolean isInitiatingTokenOptional = false;

  public AndJoinExprEvaluator (final JoinLangEnv env, final List<JoinRequirement> requirements)
  {
    this.env = env;
    this.evaluators = new ArrayList<JoinRequirementEvaluator>( requirements.size() );

    for ( JoinRequirement requirement : requirements )
    {
      evaluators.add( requirement.newEvaluator( env ) );
    }
  }

  public JoinResult evaluate ()
  {
    for ( JoinRequirementEvaluator evaluator : evaluators )
    {
      evaluator.evaluate();
    }

    calculateInitiatingTokenCoverage();

    // If the initiating token isn't covered by any of the require
    // statements, the join shouldn't be satisfied, even if the
    // requirements are otherwise met.
    if ( !isInitiatingTokenRequired && !isInitiatingTokenOptional )
    {
      return IncompleteJoinResult.INSTANCE;
    }

    if ( isInitiatingTokenRequired && isSatisfied )
    {
      Set<ArcToken> completeSet = new HashSet<ArcToken>();

      for ( JoinRequirementEvaluator evaluator : evaluators )
      {
        evaluator.completeJoinAndContributeTokens( completeSet );
      }

      return new CompleteJoinResult( new ArrayList<ArcToken>( completeSet ) );
    }

    NodeToken mergeToken = env.getMergeToken();

    if ( mergeToken != null )
    {
      Set<ArcToken> completeSet = new HashSet<ArcToken>();

      for ( JoinRequirementEvaluator evaluator : evaluators )
      {
        if ( !evaluator.isApplicable() )
        {
          evaluator.completeJoinAndContributeTokens( completeSet );
        }
      }

      if ( completeSet.contains( env.getInitiatingToken() ) )
      {
        return new MergeJoinResult( completeSet, mergeToken );
      }
    }

    return IncompleteJoinResult.INSTANCE;
  }

  private void calculateInitiatingTokenCoverage ()
  {
    isSatisfied = true;

    for ( JoinRequirementEvaluator evaluator : evaluators )
    {
      if ( evaluator.isApplicable() && !evaluator.isSatisfied() )
      {
        isSatisfied = false;
      }

      if ( evaluator.isInitiatingTokenIncluded() )
      {
        if ( evaluator.isApplicable() )
        {
          isInitiatingTokenRequired = true;
        }
        else
        {
          isInitiatingTokenOptional = true;
        }
      }
    }
  }
}