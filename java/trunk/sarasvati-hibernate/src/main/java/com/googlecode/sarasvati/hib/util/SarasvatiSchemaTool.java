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

package com.googlecode.sarasvati.hib.util;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Settings;

import com.googlecode.sarasvati.hib.HibEngine;

public class SarasvatiSchemaTool
{
  private AnnotationConfiguration config = new AnnotationConfiguration();

  public SarasvatiSchemaTool (final String hibernateCfg)
  {
    HibEngine.addToConfiguration( config, true );
    config.configure( new File( hibernateCfg ) );
  }

  public String[] generateCreateSchemaDDL ()
  {
    Settings settings = config.buildSettings();
    return config.generateSchemaCreationScript( settings.getDialect() );
  }

  public String[] generateDropSchemaDDL ()
  {
    Settings settings = config.buildSettings();
    return config.generateDropSchemaScript( settings.getDialect() );
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

  @SuppressWarnings("deprecation")
  public void executeDDL (final Session session, final String[] ddl)
    throws SQLException
  {
    Statement stmt = session.connection().createStatement();

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
    SarasvatiSchemaTool createSchema = new SarasvatiSchemaTool( "/home/paul/workspace/wf-java/conf/hibernate.cfg.xml" );
    // createSchema.dropSchema();
    createSchema.createSchema();
  }
}