package com.googlecode.sarasvati.integtest;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.mem.MemEngine;

public class TestEnv
{
  protected static SessionFactory sessionFactory = null;

  static
  {
    final String testEngine = System.getProperty("sarasvati.test.targetEngine");
    final String testDatabase = System.getProperty("sarasvati.test.targetDB");

    try
    {
      final boolean hibEngine = "hibernate".equals(testEngine);

      if (hibEngine && "postgresql".equals(testDatabase))
      {
        init( "paul", "integtests",
              "org.postgresql.Driver",
              "jdbc:postgresql://localhost:5432/paul",
              "org.hibernate.dialect.PostgreSQLDialect" );
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
    AnnotationConfiguration config = new AnnotationConfiguration();

    HibEngine.addToConfiguration( config, false );

    if ( url == null )
    {
      System.out.println( "ERROR: No hibernate.cfg.xml found in classpath!\n" +
                          "\tIn order to run the examples, you must create hibernate.cfg.xml in the examples/ directory.\n" +
                          "\tYou can use the entries in conf/ as a starting point." );
      System.exit( -1 );
    }

    config.setProperty( "hibernate.dialect", dialect );
    config.setProperty( "hibernate.connection.username", username );
    config.setProperty( "hibernate.connection.password", password );
    config.setProperty( "hibernate.connection.driver_class", driver );
    config.setProperty( "hibernate.connection.url", url );
    config.configure();

    sessionFactory = config.buildSessionFactory();
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