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

package com.googlecode.sarasvati.example.hib;

import java.net.URL;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.googlecode.sarasvati.hib.HibEngine;

public class HibTestSetup
{
  protected static SessionFactory sessionFactory = null;

  public static void init (final boolean createSchema) throws Exception
  {
    final Configuration config = new Configuration();

    if ( createSchema )
    {
      config.setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
    }

    HibEngine.addToConfiguration( config, false );

    config.addAnnotatedClass( HibExampleTaskNode.class );
    config.addAnnotatedClass( Task.class );
    config.addAnnotatedClass( InitNode.class );
    config.addAnnotatedClass( DumpNode.class );
    config.addAnnotatedClass( AsyncNode.class );

    URL url = HibTestSetup.class.getClassLoader().getResource( "hibernate.cfg.xml" );

    if ( url == null )
    {
      System.out.println( "ERROR: No hibernate.cfg.xml found in classpath!\n" +
                          "\tIn order to run the examples, you must create hibernate.cfg.xml in the examples/ directory.\n" +
                          "\tYou can use the entries in conf/ as a starting point." );
      System.exit( -1 );
    }

    config.configure( url );
    sessionFactory = config.buildSessionFactory();
  }

  public static Session openSession ()
  {
    return sessionFactory.openSession();
  }

  public static void main (final String[] args) throws Exception
  {
    init( true );
  }
}
