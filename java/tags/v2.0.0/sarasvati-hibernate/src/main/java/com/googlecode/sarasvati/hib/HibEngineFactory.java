package com.googlecode.sarasvati.hib;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.googlecode.sarasvati.EngineFactory;

public class HibEngineFactory implements EngineFactory<HibEngine>
{
  private final SessionFactory sessionFactory;
  private final String context;

  public HibEngineFactory(final SessionFactory sessionFactory, final String applicationContext)
  {
    this.sessionFactory = sessionFactory;
    this.context = applicationContext;
  }

  @Override
  public HibEngine getEngine()
  {
    final Session session = sessionFactory.openSession();
    session.getTransaction().begin();
    return new HibEngine(context, session);
  }

  /**
   * @see com.googlecode.sarasvati.EngineFactory#dispose(com.googlecode.sarasvati.Engine)
   */
  @Override
  public void dispose(final HibEngine engine)
  {
    final Session session = engine.getSession();
    session.flush();
    session.getTransaction().commit();
    session.close();
  }

  /**
   * @see com.googlecode.sarasvati.EngineFactory#dispose(com.googlecode.sarasvati.Engine, java.lang.Throwable)
   */
  @Override
  public void dispose(final HibEngine engine, final Throwable t)
  {
    final Session session = engine.getSession();
    session.flush();
    session.getTransaction().rollback();
    session.close();
  }
}