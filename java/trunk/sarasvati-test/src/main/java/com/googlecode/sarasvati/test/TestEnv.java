package com.googlecode.sarasvati.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.mem.MemEngine;

public class TestEnv
{
  public static final String ENGINE_KEY = "engine";
  public static final String DB_KEY = "db";

  public static final String ENGINE_HIBERNATE = "hibernate";
  public static final String ENGINE_MEMORY    = "memory";

  public static final String DATABASE_POSTGRESQL = "postgresql";
  public static final String DATABASE_MYSQL = "mysql";
  public static final String DATABASE_ORACLE = "oracle";

  protected static SessionFactory sessionFactory = null;

  static
  {
    final String testEngine = System.getProperty(ENGINE_KEY);
    final String testDatabase = System.getProperty(DB_KEY);

    try
    {
      final boolean hibEngine = ENGINE_HIBERNATE.equals(testEngine);
      
      final Properties props = readDbProperties();
      
      if (hibEngine)
      {
        if (DATABASE_POSTGRESQL.equals(testDatabase))
        {
          init(props.getProperty("postgresql.username"), 
               props.getProperty("postgresql.password"),
               "org.postgresql.Driver",
               props.getProperty("postgresql.url"),
               "org.hibernate.dialect.PostgreSQLDialect");
        }
        else if (DATABASE_MYSQL.equals(testDatabase))
        {
          init(props.getProperty("mysql.username"), 
               props.getProperty("mysql.password"),
               "com.mysql.jdbc.Driver",
               props.getProperty("mysql.url"),
               "org.hibernate.dialect.MySQLDialect");
        }
        else if (DATABASE_ORACLE.equals(testDatabase))
        {
          init(props.getProperty("oracle.username"), 
               props.getProperty("oracle.password"),
               "oracle.jdbc.driver.OracleDriver",
               props.getProperty("oracle.url"), 
               "org.hibernate.dialect.Oracle10gDialect" );
        }
        else
        {
          throw new Exception("Database type of " + testDatabase + " not supported in tests yet.");
        }
      }
    }
    catch ( final Exception e )
    {
      throw new ExceptionInInitializerError( e );
    }

    System.out.println("Running tests with profile: " + testDatabase);
  }

  public static void init (final String username,
                           final String password,
                           final String driver,
                           final String url,
                           final String dialect) throws Exception
  {
    Configuration config = new Configuration();

    HibEngine.addToConfiguration( config, false );

    config.setProperty( "hibernate.dialect", dialect );
    config.setProperty( "hibernate.connection.username", username );
    config.setProperty( "hibernate.connection.password", password );
    config.setProperty( "hibernate.connection.driver_class", driver );
    config.setProperty( "hibernate.connection.url", url );

    final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    doc.appendChild(doc.createElement("hibernate-configuration"));
    doc.getFirstChild().appendChild(doc.createElement("session-factory"));

    config.configure(doc);

    sessionFactory = config.buildSessionFactory();
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
  
  public static void openSession ()
  {
    session = sessionFactory.openSession();
    session.beginTransaction();
    engine = new HibEngine(session);
  }

  private static Engine engine = null;
  private static Session session = null;

  public static Engine getEngine()
  {
    if (engine == null)
    {
      if (sessionFactory == null)
      {
        engine = new MemEngine();
      }
      else
      {
        openSession();
      }
    }
    return engine;
  }

  public static Engine commitSession()
  {
    if (session != null)
    {
      session.flush();
      session.getTransaction().commit();
      session.close();
      openSession();
    }

    return engine;
  }
}