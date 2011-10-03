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

import java.util.HashMap;
import java.util.Map;

public class DefaultRubricFunctionRepository implements RubricFunctionRepository
{
  private static final DefaultRubricFunctionRepository GLOBAL_INSTANCE = new DefaultRubricFunctionRepository();

  public static DefaultRubricFunctionRepository getGlobalInstance ()
  {
    return GLOBAL_INSTANCE;
  }

  protected final Map<String, RubricPredicate> predicateMap = new HashMap<String, RubricPredicate>();
  protected final Map<String, RubricDateFunction> dateFunctionMap = new HashMap<String, RubricDateFunction>();
  protected final Map<String, RubricStringFunction> stringFunctionMap = new HashMap<String, RubricStringFunction>();

  public void registerPredicate (final String name, final RubricPredicate predicate)
  {
    predicateMap.put( name, predicate );
  }

  public void unregisterPredicate (final String name)
  {
    predicateMap.remove( name );
  }

  public void registerDateFunction (final String name, final RubricDateFunction dateFunction)
  {
    dateFunctionMap.put( name, dateFunction );
  }

  public void unregisterDateFunction (final String name)
  {
    dateFunctionMap.remove( name );
  }

  public void registerStringFunction (final String name, final RubricStringFunction stringFunction)
  {
    stringFunctionMap.put( name, stringFunction );
  }

  public void unregisterStringFunction (final String name)
  {
    dateFunctionMap.remove( name );
  }

  /**
   * @see com.googlecode.sarasvati.rubric.env.RubricFunctionRepository#getPredicate(java.lang.String)
   */
  @Override
  public RubricPredicate getPredicate (final String predicate)
  {
    return predicateMap.get( predicate );
  }

  /**
   * @see com.googlecode.sarasvati.rubric.env.RubricFunctionRepository#getDateFunction(java.lang.String)
   */
  @Override
  public RubricDateFunction getDateFunction (final String dateFunction)
  {
    return dateFunctionMap.get( dateFunction );
  }

  /**
   * @see com.googlecode.sarasvati.rubric.env.RubricFunctionRepository#getStringFunction(java.lang.String)
   */
  @Override
  public RubricStringFunction getStringFunction (final String functionName)
  {
    return stringFunctionMap.get( functionName );
  }
}