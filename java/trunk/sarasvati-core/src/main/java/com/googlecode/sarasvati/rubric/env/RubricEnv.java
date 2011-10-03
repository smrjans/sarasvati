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

package com.googlecode.sarasvati.rubric.env;

import java.util.Calendar;
import java.util.Date;

/**
 * A RubricEnv does the actual evaluation of the predicates,
 * date functions and date offset that are used in a Rubric
 * program.
 *
 * @author Paul Lorenz
 */
public interface RubricEnv extends PredicateEnv
{
  /**
   * Evaluates the named date function
   *
   * @param dateFunction The name of the date function to evaluate
   *
   * @return The result of the date function.
   */
  Date evalDateFunction (String dateFunction);

  /**
   * Evaluates the given string function (function returning a string value).
   *
   * @param stringFunction The name of the string function to evaluate
   *
   * @return The result of the string function
   */
  String evalStringFunction (String stringFunction);

  /**
   * Calculates a date, offset by the given amount to the passed in date.
   * The units parameter will correspond to one of
   *
   * <ul>
   *   <li> {@link Calendar#HOUR}
   *   <li> {@link Calendar#DATE}
   *   <li> {@link Calendar#WEEK_OF_YEAR}
   * <ul>
   *
   * How the date calculation is done is up to the implementing class.
   * A simple implementation would map on to {@link Calendar#add(int, int)}.
   * A more complicated implementation might take business days or other
   * business rules into account.
   *
   * @param date The date to calculate the offset from
   * @param business Indicates whether the calculation should takes busines days/hours etc
   *                 into account.
   * @param offset The amount of the offset.
   * @param unit The unit of the offset. These map the units defined in {@link Calendar},
   *             such as {@link Calendar#DATE} or {@link Calendar#WEEK_OF_YEAR}.
   *
   * @return The offset relative to the given date.
   */
  Date evalRelativeDate (Date date, boolean business, int offset, int unit);
}