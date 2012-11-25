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
package com.googlecode.sarasvati;

import java.util.Timer;
import java.util.TimerTask;

public class SimpleDelayedTokenScheduler implements DelayedTokenScheduler
{
  final EngineFactory engineFactory;

  // Make sure this isn't initialized until first use
  private static enum TimerContainer
  {
    INSTANCE;

    public final Timer timer = new Timer(getClass().getName() + "TimerThread", true);
  }

  private static Timer getTimer()
  {
    return TimerContainer.INSTANCE.timer;
  }

  public SimpleDelayedTokenScheduler(final EngineFactory engineFactory)
  {
    this.engineFactory = engineFactory;
  }

  @Override
  public void scheduleDelayedToken(final NodeToken token)
  {
    getTimer().schedule(new TokenReevaluateTimerTask(token), token.getDelayUntilTime());
  }

  public static void shutdown()
  {
    getTimer().cancel();
  }

  private class TokenReevaluateTimerTask extends TimerTask
  {
    private final NodeToken token;

    public TokenReevaluateTimerTask (final NodeToken token)
    {
      this.token = token;
    }

    /**
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run()
    {
      final Engine engine = engineFactory.getEngine();
      try
      {
        engine.reevaluateDelayedToken(token);
        engineFactory.dispose(engine, true);
      }
      catch(final Exception e)
      {
        engineFactory.dispose(engine, false);
      }
    }
  }
}