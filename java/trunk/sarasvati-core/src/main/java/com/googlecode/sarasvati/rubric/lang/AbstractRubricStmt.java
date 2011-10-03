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


public abstract class AbstractRubricStmt implements RubricStmt
{
  @Override
  public RubricStmtDateSymbol asDateSymbol ()
  {
    return null;
  }

  @Override
  public RubricStmtIf asIf ()
  {
    return null;
  }

  @Override
  public RubricStmtRelativeDate asRelativeDate ()
  {
    return null;
  }

  @Override
  public RubricStmtResult asResult ()
  {
    return null;
  }

  @Override
  public RubricStmtStringSymbol asStringSymbol ()
  {
    return null;
  }

  @Override
  public boolean isStringSymbol ()
  {
    return false;
  }

  @Override
  public boolean isDateSymbol ()
  {
    return false;
  }

  @Override
  public boolean isIf ()
  {
    return false;
  }

  @Override
  public boolean isRelativeDate ()
  {
    return false;
  }

  @Override
  public boolean isResult ()
  {
    return false;
  }
}
