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
package com.googlecode.sarasvati.test.hash;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.xml.XmlLoader;
import com.googlecode.sarasvati.xml.XmlProcessDefinition;


public class GraphHashTest
{
  public static Map<String, String> hashesMap = new HashMap<String, String>();

  public static void loadWorkflows () throws Exception
  {
    XmlLoader xmlLoader = new XmlLoader();

    File basePath = new File( "common/test-wf/" );

    FilenameFilter filter = new FilenameFilter()
    {
      @Override
      public boolean accept (final File dir, final String name)
      {
        return name.endsWith( ".wf.xml" ) && !name.equals( "demo-example.wf.xml" );
      }
    };

    for ( File file : basePath.listFiles( filter ) )
    {
      XmlProcessDefinition pd = xmlLoader.translate( file );
      String digest = pd.getMessageDigest();
      hashesMap.put( pd.getName(), digest );
      System.out.println( pd.getName() + ": " + digest );
    }
  }

  public static void scanForChanges () throws Exception
  {
    long start = System.currentTimeMillis();
    XmlLoader xmlLoader = new XmlLoader();

    File basePath = new File( "common/test-wf/" );

    FilenameFilter filter = new FilenameFilter()
    {
      @Override
      public boolean accept (final File dir, final String name)
      {
        return name.endsWith( ".wf.xml" ) && !name.equals( "demo-example.wf.xml" );
      }
    };

    int count = 0;

    for ( int i = 0 ; i < 10; i++ )
    {
    for ( File file : basePath.listFiles( filter ) )
    {
      XmlProcessDefinition pd = xmlLoader.translate( file );

      if ( !hashesMap.containsKey( pd.getName() ) )
      {
        System.out.println( pd.getName() + " is new" );
        hashesMap.put( pd.getName(), pd.getMessageDigest() );
      }
      else
      {
        String old = hashesMap.get( pd.getName() );
        String current = pd.getMessageDigest();
        if ( !old.equals( current ) )
        {
          System.out.println( pd.getName() + " has changed.\n\tOld: " + old + "\n\tCurrent: " + current );
          hashesMap.put( pd.getName(), current );
        }
      }
      count++;
    }

    }

    System.out.println( "Scan took " + (System.currentTimeMillis() - start) + "ms to scan " + count + " files" );
  }

  public static void main (final String[] args) throws Exception
  {
    loadWorkflows();
    while ( true )
    {
      System.out.println( "Press <enter> to scan for changes." );
      new BufferedInputStream( System.in ).read();
      scanForChanges();
      System.out.println( "Finished scanning" );
    }
  }
}
