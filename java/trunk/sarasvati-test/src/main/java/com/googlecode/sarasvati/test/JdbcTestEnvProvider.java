package com.googlecode.sarasvati.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.jdbc.JdbcEngine;
import com.googlecode.sarasvati.jdbc.JdbcGraphProcess;
import com.googlecode.sarasvati.jdbc.dialect.DatabaseDialect;

public class JdbcTestEnvProvider implements TestEnvProvider
{
  private final String url;
  private final String username;
  private final String password;
  private final DatabaseDialect dialect;

  private Connection connection = null;
  private JdbcEngine engine = null;

  public JdbcTestEnvProvider (final String url,
                              final String username,
                              final String password,
                              final String driver,
                              final DatabaseDialect dialect)
  {
    this.url = url;
    this.username = username;
    this.password = password;
    this.dialect = dialect;

    try
    {
      Class.forName(driver);
    }
    catch(final Exception e)
    {
      throw new RuntimeException("Unable to load JDBC driver class", e);
    }
  }

  private void newEngine()
  {
    try
    {
      if (connection == null)
      {
        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
      }
      else
      {
        connection.rollback();
      }

      engine = new JdbcEngine(connection, dialect, "JDBCTestContext");
    }
    catch(final Exception e)
    {
      throw new RuntimeException("Failed to create JDBC connection: " + e.getMessage(), e);
    }
  }

  @Override
  public Engine getEngine()
  {
    if (engine == null)
    {
      newEngine();
    }
    return engine;
  }

  @Override
  public void commit()
  {
    if (connection != null)
    {
      try
      {
        connection.commit();
      }
      catch(final SQLException sqle)
      {
        throw new RuntimeException(sqle.getMessage(), sqle);
      }
      engine = null;
    }
  }

  /* (non-Javadoc)
   * @see com.googlecode.sarasvati.test.TestEnvProvider#refreshProcess(com.googlecode.sarasvati.GraphProcess)
   */
  @Override
  public GraphProcess refreshProcess(final GraphProcess process)
  {
    final JdbcGraphProcess jdbcProcess = (JdbcGraphProcess) process;
    return engine.getRepository().loadProcess(jdbcProcess.getId());
  }

  /* (non-Javadoc)
   * @see com.googlecode.sarasvati.test.TestEnvProvider#refreshToken(com.googlecode.sarasvati.NodeToken)
   */
  @Override
  public NodeToken refreshToken(final NodeToken token)
  {
    //final JdbcNodeToken jdbcToken = (JdbcNodeToken)token;
    throw new UnsupportedOperationException("refreshToken not yet implemented");
    //return engine.getRepository().loadProcess(jdbcProcess.getId());
  }

  /* (non-Javadoc)
   * @see com.googlecode.sarasvati.test.TestEnvProvider#dispose()
   */
  @Override
  public void dispose()
  {
    try
    {
      if (connection != null)
      {
        connection.rollback();
        connection.close();
        connection = null;
        engine = null;
      }
    }
    catch(final Exception e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }

  }
}