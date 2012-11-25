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

    Copyright 2008, 2012 Paul Lorenz
*/

package com.googlecode.sarasvati.rubric.lang;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.rubric.visitor.RubricVisitor;

public class RubricStmtRelativeDate extends AbstractRubricStmt implements RubricDateStmt
{
  private static Map<String, Integer> unitMapping = new HashMap<String, Integer>();

  static
  {
    unitMapping.put( "second", Calendar.SECOND );
    unitMapping.put( "seconds", Calendar.SECOND );
    unitMapping.put( "minute", Calendar.MINUTE );
    unitMapping.put( "minutes", Calendar.MINUTE );
    unitMapping.put( "hour", Calendar.HOUR );
    unitMapping.put( "hours", Calendar.HOUR );
    unitMapping.put( "day", Calendar.DATE );
    unitMapping.put( "days", Calendar.DATE );
    unitMapping.put( "week", Calendar.WEEK_OF_YEAR );
    unitMapping.put( "weeks", Calendar.WEEK_OF_YEAR );
  }

  protected int offset;
  protected int unit;
  protected boolean business;
  protected RubricStmtDateSymbol dateSymbolExpr;

  public RubricStmtRelativeDate (final int offset, final boolean business, final String unitName, final String type, final String symbol)
  {
    this.offset = offset;

    if ( "before".equals( type ) )
    {
      this.offset = -offset;
    }

    this.unit = unitMapping.get( unitName.toLowerCase() );
    this.business = business;
    this.dateSymbolExpr = new RubricStmtDateSymbol( symbol );
  }

  public int getOffset ()
  {
    return offset;
  }

  public int getUnit ()
  {
    return unit;
  }

  public boolean isBusiness ()
  {
    return business;
  }

  public RubricStmtDateSymbol getDateSymbolExpr ()
  {
    return dateSymbolExpr;
  }

  @Override
  public Date eval (final RubricEnv env)
  {
    Date baseDate = dateSymbolExpr.eval( env );
    return env.evalRelativeDate( baseDate, business, offset, unit );
  }

  @Override
  public void traverse (final RubricVisitor visitor)
  {
    visitor.visit( this );
    dateSymbolExpr.traverse( visitor );
  }

  @Override
  public String toString ()
  {
    String unitDesc = "?";

    for ( Entry<String, Integer> entry : unitMapping.entrySet() )
    {
      if ( entry.getValue().equals( unit ) )
      {
        unitDesc = entry.getKey();
        break;
      }
    }

    return offset + " " + (business ? " business " : "" ) + unitDesc + ( offset > 0 ? " after " : " before " ) + dateSymbolExpr.getSymbol();
  }

  @Override
  public boolean isEqualTo (final RubricStmt stmt)
  {
    if ( !stmt.isRelativeDate() )
    {
      return false;
    }

    RubricStmtRelativeDate other = stmt.asRelativeDate();
    return other.getDateSymbolExpr().isEqualTo( dateSymbolExpr ) &&
           other.getOffset() == offset &&
           other.getUnit() == unit;
  }

  @Override
  public RubricStmtRelativeDate asRelativeDate ()
  {
    return this;
  }

  @Override
  public boolean isRelativeDate ()
  {
    return true;
  }
}