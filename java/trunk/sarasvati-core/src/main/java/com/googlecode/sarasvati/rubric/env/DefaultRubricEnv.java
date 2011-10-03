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

    Copyright 2008, 2009 Paul Lorenz
 */

package com.googlecode.sarasvati.rubric.env;

import java.util.Calendar;
import java.util.Date;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.rubric.RubricException;

/**
 * Basic RubricEnv implementation which uses a {@link RubricFunctionRepository}
 * as a source for predicate and date function and which uses Calendar to do
 * relative date evaluations. Business days/hours are not handled differently
 * then regular relative dates.
 *
 * @author Paul Lorenz
 */
public class DefaultRubricEnv implements RubricEnv
{
  protected Engine                   engine;
  protected NodeToken                token;
  protected RubricFunctionRepository functionRepository;

  public DefaultRubricEnv (final Engine engine,
                           final NodeToken token,
                           final RubricFunctionRepository functionRepository)
  {
    this.engine = engine;
    this.token = token;
    this.functionRepository = functionRepository;
  }

  @Override
  public Date evalDateFunction (final String dateFunction)
  {
    RubricDateFunction rubricDateFunction = functionRepository.getDateFunction( dateFunction );
    if ( rubricDateFunction == null )
    {
      throw new RubricException( "Evaluation failed. Unknown date function '" + dateFunction + "'" );
    }
    return rubricDateFunction.eval( engine, token );
  }

  @Override
  public String evalStringFunction (final String stringFunction)
  {
    RubricStringFunction rubricStringFunction = functionRepository.getStringFunction( stringFunction );
    if ( rubricStringFunction == null )
    {
      throw new RubricException( "Evaluation failed. Unknown string function '" + stringFunction + "'" );
    }
    return rubricStringFunction.eval( engine, token );
  }

  @Override
  public boolean evalPredicate (final String predicate)
  {
    RubricPredicate rubricPredicate = functionRepository.getPredicate( predicate );
    if ( rubricPredicate == null )
    {
      throw new RubricException( "Evaluation failed. Unknown predicate '" + predicate + "'" );
    }
    return rubricPredicate.eval( engine, token );
  }

  @Override
  public Date evalRelativeDate (final Date date, final boolean business, final int offset, final int unit)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime( date );
    cal.add( unit, offset );
    return cal.getTime();
  }
}