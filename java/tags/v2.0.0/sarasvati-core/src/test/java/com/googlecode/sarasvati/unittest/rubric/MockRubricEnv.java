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

package com.googlecode.sarasvati.unittest.rubric;

import java.util.Date;

import com.googlecode.sarasvati.rubric.env.RubricEnv;

public class MockRubricEnv implements RubricEnv
{
  public static final MockRubricEnv INSTANCE = new MockRubricEnv();

  @Override
  public Date evalDateFunction (final String dateFunction)
  {
    return null;
  }

  @Override
  public String evalStringFunction (final String stringFunction)
  {
    return null;
  }

  @Override
  public boolean evalPredicate (final String predicate)
  {
    return false;
  }

  @Override
  public Date evalRelativeDate (final Date date, final boolean business, final int offset, final int unit)
  {
    return null;
  }
}
