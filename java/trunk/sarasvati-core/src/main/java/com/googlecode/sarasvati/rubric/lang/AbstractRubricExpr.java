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

    Copyright 2008 Paul Lorenz
*/
package com.googlecode.sarasvati.rubric.lang;


public abstract class AbstractRubricExpr implements RubricExpr
{
  @Override
  public boolean isAnd ()
  {
    return false;
  }

  @Override
  public boolean isNot ()
  {
    return false;
  }

  @Override
  public boolean isOr ()
  {
    return false;
  }

  @Override
  public boolean isSymbol ()
  {
    return false;
  }

  @Override
  public RubricExprAnd asAnd ()
  {
    return null;
  }

  @Override
  public RubricExprNot asNot ()
  {
    return null;
  }

  @Override
  public RubricExprOr asOr ()
  {
    return null;
  }

  @Override
  public RubricExprSymbol asSymbol ()
  {
    return null;
  }
}
