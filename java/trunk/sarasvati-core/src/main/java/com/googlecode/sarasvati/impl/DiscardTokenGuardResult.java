/**
 * Created on Nov 19, 2009
 */
package com.googlecode.sarasvati.impl;

import java.util.Date;

import com.googlecode.sarasvati.GuardAction;
import com.googlecode.sarasvati.GuardResult;

public enum DiscardTokenGuardResult implements GuardResult
{
  INSTANCE;

  @Override
  public String getExitArcForSkip()
  {
    throw new UnsupportedOperationException( "getExitArcsForSkip should never be called on a GuardResult with action of DiscardToken" );
  }

  /**
   * @see com.googlecode.sarasvati.GuardResult#getDelayTillTime()
   */
  @Override
  public Date getDelayTillTime()
  {
    throw new UnsupportedOperationException( "getDelayUntilDate should never be called on a GuardResult with action of DiscardToken" );
  }

  @Override
  public GuardAction getGuardAction()
  {
    return GuardAction.DiscardToken;
  }

  @Override
  public String toString()
  {
    return "DiscardTokenGuardResult";
  }
}