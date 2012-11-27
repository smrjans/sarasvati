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

    Copyright 2012 Paul Lorenz
*/
package com.googlecode.sarasvati.impl;

import java.util.Timer;
import java.util.TimerTask;

import com.googlecode.sarasvati.DelayedTokenScheduler;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.EngineFactory;
import com.googlecode.sarasvati.NodeToken;

public enum TimerBasedDelayedTokenScheduler
{
  INSTANCE;

  // Make sure this isn't initialized until first use
  private static enum TimerContainer
  {
    TIMER_SINGLETON;

    public final Timer timer = new Timer(getClass().getName() + "TimerThread", true);
  }

  private static Timer getTimer()
  {
    return TimerContainer.TIMER_SINGLETON.timer;
  }

  public static <T extends Engine> DelayedTokenScheduler newDelayedTokenScheduler(final EngineFactory<T> engineFactory)
  {
    return new DelayedTokenScheduler()
    {
      @Override
      public void scheduleDelayedToken(final NodeToken token)
      {
        TimerBasedDelayedTokenScheduler.INSTANCE.scheduleDelayedToken(token, engineFactory);
      }
    };
  }

  public <T extends Engine> void scheduleDelayedToken(final NodeToken token, final EngineFactory<T> engineFactory)
  {
    getTimer().schedule(new TokenReevaluateTimerTask<T>(engineFactory, token), token.getDelayUntilTime());
  }

  public static void shutdown()
  {
    getTimer().cancel();
  }

  private class TokenReevaluateTimerTask<T extends Engine> extends TimerTask
  {
    private final EngineFactory<T> engineFactory;
    private final NodeToken token;

    public TokenReevaluateTimerTask (final EngineFactory<T> engineFactory, final NodeToken token)
    {
      this.engineFactory = engineFactory;
      this.token = token;
    }

    /**
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run()
    {
      final T engine = engineFactory.getEngine();
      try
      {
        engine.reevaluateDelayedToken(token);
        engineFactory.dispose(engine);
      }
      catch(final Throwable t)
      {
        engineFactory.dispose(engine, t);
      }
    }
  }
}