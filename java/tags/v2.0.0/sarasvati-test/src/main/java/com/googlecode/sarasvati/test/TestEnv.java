package com.googlecode.sarasvati.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.jdbc.dialect.DatabaseDialect;
import com.googlecode.sarasvati.jdbc.dialect.PostgreSQLDatabaseDialect;

public class TestEnv
{
  public static enum ExecutionMode
  {
    OneSession
    {
      @Override
      public boolean doCommits()
      {
        return false;
      }
    },
    EachInNewSession,
    Async;

    public boolean doCommits()
    {
      return true;
    }
  }

  public static final String ENGINE_KEY = "engine";
  public static final String DB_KEY = "db";

  public static final String ENGINE_HIBERNATE = "hibernate";
  public static final String ENGINE_MEMORY    = "memory";
  public static final String ENGINE_JDBC      = "jdbc";

  public static final String DATABASE_POSTGRESQL = "postgresql";
  public static final String DATABASE_MYSQL = "mysql";
  public static final String DATABASE_ORACLE = "oracle";

  private static ExecutionMode defaultMode = ExecutionMode.OneSession;
  private static ExecutionMode mode = ExecutionMode.OneSession;

  private static TestEnvProvider envProvider = null;

  public static void resetExecutionModeToDefault()
  {
    TestEnv.mode = TestEnv.defaultMode;
  }

  public static void setExecutionMode(final ExecutionMode mode)
  {
    TestEnv.mode = mode;
  }

  public static ExecutionMode getMode()
  {
    return TestEnv.mode;
  }

  static
  {
    final String testEngine = System.getProperty(ENGINE_KEY);
    final String testDatabase = System.getProperty(DB_KEY);

    init(ExecutionMode.OneSession, testEngine, testDatabase);
  }

  public static void init(final ExecutionMode initDefaultMode, final String testEngine, final String testDatabase)
  {
    TestEnv.defaultMode = initDefaultMode;
    try
    {
      if (envProvider != null)
      {
        envProvider.dispose();
      }

      String username = null;
      String password = null;
      String driver   = null;
      String url      = null;
      String dialect  = null;
      DatabaseDialect dbDialect = null;

      if (ENGINE_HIBERNATE.equals(testEngine) || ENGINE_JDBC.equals(testEngine))
      {
        final Properties props = readDbProperties();

        if (DATABASE_POSTGRESQL.equals(testDatabase))
        {
          username = props.getProperty("postgresql.username");
          password = props.getProperty("postgresql.password");
          driver   = "org.postgresql.Driver";
          url      = props.getProperty("postgresql.url");
          dialect  = "org.hibernate.dialect.PostgreSQLDialect";
          dbDialect = new PostgreSQLDatabaseDialect();
        }
        else if (DATABASE_MYSQL.equals(testDatabase))
        {
          username = props.getProperty("mysql.username");
          password = props.getProperty("mysql.password");
          driver   = "com.mysql.jdbc.Driver";
          url      = props.getProperty("mysql.url");
          dialect  = "org.hibernate.dialect.MySQLDialect";
        }
        else if (DATABASE_ORACLE.equals(testDatabase))
        {
          username = props.getProperty("oracle.username");
          password = props.getProperty("oracle.password");
          driver   = "oracle.jdbc.driver.OracleDriver";
          url      = props.getProperty("oracle.url");
          dialect  = "org.hibernate.dialect.Oracle10gDialect";
        }
        else
        {
          throw new Exception("Database type of " + testDatabase + " not supported in tests yet.");
        }
      }

      if (ENGINE_HIBERNATE.equals(testEngine))
      {
        envProvider = new HibernateTestEnvProvider(username, password, driver, url, dialect);
      }
      else if (ENGINE_JDBC.equals(testEngine))
      {
        envProvider = new JdbcTestEnvProvider(url, username, password, driver, dbDialect);
      }
      else
      {
        envProvider = new MemTestEnvProvider();
      }
    }
    catch ( final Exception e )
    {
      throw new ExceptionInInitializerError( e );
    }

    System.out.println("Running tests with profile: " + testDatabase);
  }


  private static Properties readDbProperties()
  {
    Properties props = new Properties();
    try
    {
      final InputStream in = TestEnv.class.getResourceAsStream("/db.properties");

      if (in == null)
      {
        throw new RuntimeException("db.properties not found");
      }
      try
      {
        props.load(in);
        return props;
      }
      finally
      {
        in.close();
      }
    }
    catch(final IOException ioe)
    {
      throw new RuntimeException("Failed to load database properties from db.properties", ioe);
    }
  }

  public static Engine getEngine()
  {
    return envProvider.getEngine();
  }

  public static void commit()
  {
    envProvider.commit();
  }

  public static GraphProcess refreshProcess(final GraphProcess process)
  {
    return envProvider.refreshProcess(process);
  }

  public static NodeToken refreshToken(final NodeToken token)
  {
    return envProvider.refreshToken(token);
  }
}