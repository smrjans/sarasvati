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

    Copyright 2009, 2012 Paul Lorenz
*/

package com.googlecode.sarasvati.hib.util;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.jdbc.Work;

import com.googlecode.sarasvati.hib.HibEngine;

public class SarasvatiSchemaTool
{
  private final Configuration config = new Configuration();
  private Dialect dialect = null;

  public SarasvatiSchemaTool (final String hibernateCfg)
  {
    HibEngine.addToConfiguration( config, true );
    config.configure( new File( hibernateCfg ) );
  }

  public Dialect getDialect(final SessionFactory factory)
  {
    Class<?> clazz = null;

    try
    {
      try
      {
        clazz = Class.forName("org.hibernate.engine.spi.SessionFactoryImplementor");
      }
      catch(final ClassNotFoundException cnfe)
      {
        clazz = Class.forName("org.hibernate.engine.SessionFactoryImplementor");
      }

      return (Dialect)clazz.getMethod("getDialect").invoke(factory);
    }
    catch(final Exception e)
    {
      throw new RuntimeException("Failed to invoked SessionFactoryImplementor#getDialect", e);
    }
  }

  public Dialect getDialect()
  {
    if (dialect == null)
    {
      final SessionFactory sessionFactory = config.buildSessionFactory();
      try
      {
        dialect = getDialect(sessionFactory);
      }
      finally
      {
        sessionFactory.close();
      }
    }
    return dialect;
  }

  public String[] generateCreateSchemaDDL ()
  {
    return config.generateSchemaCreationScript(getDialect());
  }

  public String[] generateDropSchemaDDL ()
  {
    return config.generateDropSchemaScript(getDialect());
  }

  public void executeDDL (final String[] ddl) throws Exception
  {
    SessionFactory factory = config.buildSessionFactory();

    Session session = factory.openSession();
    try
    {
      session.getTransaction().begin();
      executeDDL( session, ddl );
      session.getTransaction().commit();
    }
    catch ( Exception e )
    {
      session.getTransaction().rollback();
      throw e;
    }
    finally
    {
      session.close();
    }
  }

  public void executeDDL (final Session session, final String[] ddl)
  {
    session.doWork(new Work()
    {
      @Override
      public void execute(final Connection connection) throws SQLException
      {
        Statement stmt = connection.createStatement();

        try
        {
          for ( String ddlStmt : ddl )
          {
            System.out.println( "DDL: " + ddlStmt );
            stmt.execute( ddlStmt );
          }
        }
        finally
        {
          stmt.close();
        }
      }
    });
  }

  public void createSchema () throws Exception
  {
    executeDDL( generateCreateSchemaDDL() );
  }

  public void dropSchema () throws Exception
  {
    executeDDL( generateDropSchemaDDL() );
  }

  public static void main (final String[] args) throws Exception
  {
    if (args.length < 1)
    {
      System.out.println("No hibernate.cfg.xml specifed.");
      System.exit(-1);
    }

    File file = new File(args[0]);
    if (!file.exists() || !file.canRead() || !file.isFile())
    {
      System.out.println("Given hibernate config file is not present, not readable or not a file");
      System.exit(-1);
    }

    SarasvatiSchemaTool createSchema = new SarasvatiSchemaTool(args[0]);
    // createSchema.dropSchema();
    createSchema.createSchema();
  }
}