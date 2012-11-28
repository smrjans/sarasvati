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

    Copyright 2008-2009 Paul Lorenz
*/

package com.googlecode.sarasvati.test;

import java.util.Calendar;
import java.util.Date;

import com.googlecode.sarasvati.rubric.RubricCompiler;
import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.rubric.lang.RubricStmt;
import com.googlecode.sarasvati.rubric.lang.RubricStmtRelativeDate;
import com.googlecode.sarasvati.rubric.visitor.RubricVisitorAdaptor;

public class RubricTest
{
  public static void eval (final String testStmt) throws Exception
  {
    RubricStmt stmt = RubricCompiler.compile( testStmt );

    RubricEnv env = new RubricEnv()
    {
      @Override
      public Date evalRelativeDate (final Date date, final boolean business, final int offset, final int unit)
      {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        cal.add( unit, offset );
        return cal.getTime();
      }

      @Override
      public boolean evalPredicate (final String predicate)
      {
        if ( "a".equals( predicate ) )
        {
          return true;
        }

        if ( "b".equals( predicate ) )
        {
          return false;
        }

        System.out.println( "Evaluating guard predicate: " + predicate );
        return true;
      }

      @Override
      public Date evalDateFunction (final String dateFunction)
      {
        if ( "now".equals( dateFunction ) )
        {
          return new Date();
        }

        throw new IllegalArgumentException( "No such named date: " + dateFunction );
      }

      @Override
      public String evalStringFunction (final String stringFunction)
      {
        if ( "hello".equals( stringFunction ) )
        {
          return "world";
        }
        if ( "ping".equals( stringFunction ) )
        {
          return "pong";
        }

        throw new IllegalArgumentException( "No such named string: " + stringFunction );
      }


    };

    System.out.println( stmt.eval( env ) );
  }

  public static void main(final String[] args) throws Exception
  {
    RubricStmt stmt = RubricCompiler.compile( "(1 business day after now)" );
    stmt.traverse( new RubricVisitorAdaptor()
    {
      @Override
      public void visit (final RubricStmtRelativeDate relativeDateStmt)
      {
        System.out.println( relativeDateStmt );
      }
    } );

    String[] tests = { "if a or b then (1 week after now) else Discard",
                       "\"Hello \\\"there!\"",
                       "\"foo\"",
                       "50",
                       "(5 hours before now)",
                       "if test1 or test2 then if test3 and not test4 then Accept else Skip else Skip reject",
                       "if not test1 or not test2 then if test3 and not test4 then Accept else Skip else Skip reject",
                       "if ( test1 and test2 ) then if test3 then Accept else Skip else Skip reject",
                       "if not ( test1 and test2 ) then if test3 then Accept else Skip else Skip reject",
                       "if ( not test1 and not test2 ) or (test3 and test4) then Discard else Skip",
                       "if (a or b) then Accept else Discard",
                       "if (a or b) and c then 0 else 1",
                       "if a then @hello else @ping"};

    for ( String test : tests )
    {
      eval( test );
    }
  }
}
