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

import java.util.Collection;
import java.util.Set;

import com.googlecode.sarasvati.ArcToken;

public abstract class MultiTokenRequirementEvaluator<T extends JoinRequirement> extends AbstractJoinRequirementEvaluator<T>
{
  private Collection<ArcToken> joinTokens;

  public MultiTokenRequirementEvaluator (final JoinLangEnv env, final T requirement)
  {
    super( env, requirement );
  }

  protected void includeTokens (final Collection<ArcToken> tokens)
  {
    joinTokens = tokens;
  }

  @Override
  public void completeJoinAndContributeTokens (final Set<ArcToken> tokens)
  {
    tokens.addAll( joinTokens );
  }

  @Override
  public boolean isSatisfied ()
  {
    return joinTokens != null;
  }

  @Override
  public boolean isInitiatingTokenIncluded ()
  {
    return joinTokens != null && joinTokens.contains( getEnv().getInitiatingToken() );
  }
}
