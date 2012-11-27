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
package com.googlecode.sarasvati.test.perf;

import java.util.LinkedList;
import java.util.List;

import com.googlecode.sarasvati.load.definition.ProcessDefinition;
import com.googlecode.sarasvati.xml.XmlProcessDefinition;

public class TestPerfStats
{
  private ProcessDefinition xmlProcDef;

  private List<Long> insertAvgs = new LinkedList<Long>();
  private List<Long> loadAvgs = new LinkedList<Long>();

  private long currentInsert = 0L;
  private int countInsert = 0;

  private long currentLoad = 0L;
  private int countLoad = 0;

  private static int rollover;

  public TestPerfStats (final ProcessDefinition xmlProcDef)
  {
    this.xmlProcDef = xmlProcDef;
  }

  public void addInsertStat (final long stat)
  {
    currentInsert += stat;
    countInsert++;

    if ( countInsert == rollover )
    {
      insertAvgs.add( currentInsert / countInsert );
      currentInsert = 0;
      countInsert = 0;
    }
  }

  public void addLoadStat (final long stat)
  {
    currentLoad += stat;
    countLoad++;

    if ( countLoad == rollover )
    {
      loadAvgs.add( currentLoad / countLoad );
      currentLoad = 0;
      countLoad = 0;
    }
  }

  public String getName ()
  {
    return xmlProcDef.getName();
  }

  public ProcessDefinition getXmlProcDef ()
  {
    return xmlProcDef;
  }

  public void setXmlProcDef (final XmlProcessDefinition xmlProcDef)
  {
    this.xmlProcDef = xmlProcDef;
  }

  public static int getRollover ()
  {
    return rollover;
  }

  public static void setRollover (final int rollover)
  {
    TestPerfStats.rollover = rollover;
  }

  public void dumpStats ()
  {
    System.out.println( "Graph: " + getName() );
    int count = 0;
    for ( long time : insertAvgs )
    {
      System.out.println( "Insert avg " + (++count) + ": " + time );
    }
    count = 0;
    for ( long time : loadAvgs )
    {
      System.out.println( "Load avg " + (++count) + ": " + time );
    }
  }
}