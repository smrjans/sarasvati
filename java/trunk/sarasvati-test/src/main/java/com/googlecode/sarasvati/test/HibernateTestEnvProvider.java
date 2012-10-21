package com.googlecode.sarasvati.test;

import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.hib.HibGraphProcess;
import com.googlecode.sarasvati.hib.HibNodeToken;

public class HibernateTestEnvProvider implements TestEnvProvider
{
  private Session session = null;
  private SessionFactory sessionFactory = null;
  private HibEngine engine;

  public HibernateTestEnvProvider (final String username,
                                   final String password,
                                   final String driver,
                                   final String url,
                                   final String dialect)
    throws Exception
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

  public void openSession ()
  {
    session = sessionFactory.openSession();
    session.beginTransaction();
    engine = new HibEngine(session);
  }

  @Override
  public Engine getEngine()
  {
    if (engine == null)
    {
      openSession();
    }
    return engine;
  }

  /**
   * @see com.googlecode.sarasvati.test.TestEnvProvider#commit()
   */
  @Override
  public void commit()
  {
    if (session != null)
    {
      session.flush();
      session.getTransaction().commit();
      session.close();
      session = null;
      openSession();
    }
  }

  @Override
  public GraphProcess refreshProcess(final GraphProcess p)
  {
    return engine.getRepository().loadProcess(((HibGraphProcess)p).getId());
  }

  @Override
  public NodeToken refreshToken(final NodeToken token)
  {
    return engine.getRepository().loadNodeToken(((HibNodeToken)token).getId());
  }

  /**
   * @see com.googlecode.sarasvati.test.TestEnvProvider#dispose()
   */
  @Override
  public void dispose()
  {
    if (session != null)
    {
      session.getTransaction().rollback();
      session.close();
      session = null;
    }

    if (sessionFactory != null)
    {
      sessionFactory.close();
      sessionFactory = null;
    }
  }
}
