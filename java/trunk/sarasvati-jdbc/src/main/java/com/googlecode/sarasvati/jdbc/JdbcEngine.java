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
package com.googlecode.sarasvati.jdbc;

import java.sql.Connection;

import com.googlecode.sarasvati.impl.BaseEngine;
import com.googlecode.sarasvati.jdbc.dialect.DatabaseDialect;
import com.googlecode.sarasvati.load.GraphLoader;
import com.googlecode.sarasvati.load.GraphLoaderImpl;
import com.googlecode.sarasvati.load.GraphValidator;

public class JdbcEngine extends BaseEngine
{
  protected JdbcGraphFactory factory;
  protected JdbcGraphRepository repository;
  protected Connection connection;
  protected DatabaseDialect dialect;

  public JdbcEngine ()
  {
    super(DEFAULT_APPLICATION_CONTEXT);
  }

  public JdbcEngine (final String applicationContext)
  {
    super(applicationContext);
  }

  public JdbcEngine (final Connection connection, final DatabaseDialect dialect)
  {
    super(DEFAULT_APPLICATION_CONTEXT);
    this.connection = connection;
    this.dialect = dialect;
    this.factory = new JdbcGraphFactory( this );
    this.repository = new JdbcGraphRepository( this );
  }

  public JdbcEngine (final Connection connection, final DatabaseDialect dialect, final String applicationContext)
  {
    super(applicationContext);
    this.connection = connection;
    this.dialect = dialect;
    this.factory = new JdbcGraphFactory( this );
    this.repository = new JdbcGraphRepository( this );
  }

  @Override
  protected JdbcEngine newEngine ()
  {
    JdbcEngine engine =  new JdbcEngine(applicationContext);
    engine.connection = connection;
    engine.factory = factory;
    engine.repository = repository;
    return engine;
  }

  @Override
  public JdbcGraphFactory getFactory ()
  {
    return factory;
  }

  @Override
  public GraphLoader<JdbcGraph> getLoader ()
  {
    return new GraphLoaderImpl<JdbcGraph>( getFactory(), getRepository(), null);
  }

  @Override
  public GraphLoader<JdbcGraph> getLoader (final GraphValidator validator)
  {
    return new GraphLoaderImpl<JdbcGraph>(getFactory(), getRepository(), validator);
  }

  @Override
  public JdbcGraphRepository getRepository ()
  {
    return repository;
  }

  public DatabaseDialect getDatabaseDialect ()
  {
    return dialect;
  }

  public Connection getConnection ()
  {
    return connection;
  }
}