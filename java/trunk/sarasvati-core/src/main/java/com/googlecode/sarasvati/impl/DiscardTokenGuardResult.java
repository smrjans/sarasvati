/**
 * Created on Nov 19, 2009
 */
package com.googlecode.sarasvati.impl;

import com.googlecode.sarasvati.GuardAction;
import com.googlecode.sarasvati.GuardResult;

public final class DiscardTokenGuardResult implements GuardResult
{
  public static final GuardResult INSTANCE = new DiscardTokenGuardResult();

  private DiscardTokenGuardResult ()
  {
    /* Don't allow this class to be used other than via the INSTANCE */
  }

  @Override
  public String getExitArcForSkip()
  {
    throw new UnsupportedOperationException(
      "getExitArcsForSkip should never be called on a GuardResult with action of DiscardToken" );
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