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

package com.googlecode.sarasvati.mem;

import com.googlecode.sarasvati.DelayedTokenScheduler;
import com.googlecode.sarasvati.EngineFactory;
import com.googlecode.sarasvati.event.DefaultExecutionEventQueue;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.impl.BaseEngine;
import com.googlecode.sarasvati.impl.TimerBasedDelayedTokenScheduler;
import com.googlecode.sarasvati.load.GraphLoader;
import com.googlecode.sarasvati.load.GraphLoaderImpl;
import com.googlecode.sarasvati.load.GraphValidator;

public class MemEngine extends BaseEngine
{
  protected MemGraphRepository repository;

  /**
   * Creates a new MemEngine with the default application context.
   * Each application context can have it's own set of global
   * listeners. MemEngine instances constructed via this constructor
   * will use the global graph cache.
   */
  public MemEngine ()
  {
    this( DEFAULT_APPLICATION_CONTEXT );
  }

  /**
   * Creates a new MemEngine with the default application context.
   * Each application context can have it's own set of global
   * listeners.
   *
   * @param useGlobalGraphCache Specifies whether this engine should
   *                            use the global graph cache, or a
   *                            cache specific to this instance.
   */
  public MemEngine (final boolean useGlobalGraphCache)
  {
    this( DEFAULT_APPLICATION_CONTEXT, useGlobalGraphCache );
  }

  /**
   * Creates a new MemEngine with the given application context.
   * Each application context has it's own set of global listeners.
   *
   * This allows different applications running the same JVM to
   * have different sets of listeners without having to add
   * them at the process level.
   *
   * MemEngine instances constructed via this constructor
   * will use the global graph cache.
   *
   * @param applicationContext The application context
   */
  public MemEngine (final String applicationContext)
  {
    this( applicationContext, true );
  }

  /**
   * Creates a new MemEngine with the given application context.
   * Each application context has it's own set of global listeners.
   *
   * This allows different applications running the same JVM to
   * have different sets of listeners without having to add
   * them at the process level.
   *
   * @param applicationContext The application context
   * @param useGlobalGraphCache Specifies whether this engine should
   *                            use the global graph cache, or a
   *                            cache specific to this instance.
   */
  public MemEngine (final String applicationContext, final boolean useGlobalGraphCache)
  {
    super( applicationContext );
    this.repository = useGlobalGraphCache ? MemGraphRepository.INSTANCE : new MemGraphRepository(false);
  }

  /**
   * Creates a new MemEngine with the given application context.
   * Each application context has it's own set of global listeners.
   *
   * This allows different applications running the same JVM to
   * have different sets of listeners without having to add
   * them at the process level.
   *
   * @param applicationContext The application context
   * @param repository The repository to use for loading graphs and as a graph source
   */
  public MemEngine (final String applicationContext, final MemGraphRepository repository)
  {
    super( applicationContext );
    this.repository = repository;
  }

  @Override
  public MemGraphFactory getFactory ()
  {
    return MemGraphFactory.INSTANCE;
  }

  @Override
  public MemGraphRepository getRepository ()
  {
    return repository;
  }

  @Override
  public GraphLoader<MemGraph> getLoader()
  {
    return getLoader( null );
  }

  @Override
  public GraphLoader<MemGraph> getLoader (final GraphValidator validator)
  {
    return new GraphLoaderImpl<MemGraph>( getFactory(), getRepository(), validator );
  }

  @Override
  public MemEngine newEngine ()
  {
    return new MemEngine(applicationContext, repository);
  }

  /**
   * Returns a DelayedTokenScheduler which uses the same engine in use
   *
   * @see com.googlecode.sarasvati.Engine#getDelayedTokenScheduler()
   */
  @Override
  public DelayedTokenScheduler getDelayedTokenScheduler()
  {
    return TimerBasedDelayedTokenScheduler.newDelayedTokenScheduler(newEngineFactory());
  }

  /**
   * Provides a subclass to override which execution event listeners are added to
   * new global queues. By default this adds the listeners from {@link BaseEngine} 
   * as well as the following listeners:
   * <ul>
   *  <li>{@link TokenSetDeadEndListener}</li>
   * </ul>
   *
   * @param queue The new global queue
   */
  @Override
  protected void contributeGlobalListeners (final DefaultExecutionEventQueue queue)
  {
    super.contributeGlobalListeners(queue);
    queue.addListener( new TokenSetDeadEndListener(),
                       ExecutionEventType.ARC_TOKEN_INCOMPLETE_JOIN,
                       ExecutionEventType.NODE_TOKEN_COMPLETED );
  }
  
  private EngineFactory<MemEngine> newEngineFactory()
  {
    return new EngineFactory<MemEngine>()
    {
      @Override
      public MemEngine getEngine()
      {
        return MemEngine.this;
      }

      @Override
      public void dispose(final MemEngine engine)
      {
        // Does nothing by default
      }

      @Override
      public void dispose(final MemEngine engine, final Throwable t)
      {
        // Does nothing by default
      }
    };
  }
}