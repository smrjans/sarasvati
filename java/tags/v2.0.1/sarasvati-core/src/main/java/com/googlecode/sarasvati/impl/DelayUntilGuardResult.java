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

import java.util.Date;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GuardAction;
import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;

/**
 * Encapsulates a delay until response from {@link Node#guard(Engine, NodeToken)}.
 * Allows specifying the date at which the node guard should be reevaluated.
 *
 * @author Paul Lorenz
 */
public class DelayUntilGuardResult implements GuardResult
{
  private Date delayTillDate;

  /**
   * Constructor which takes the date/time at which the node guard should be reevaluated.
   *
   * @param delayTillDate the date/time at which the node guard should be reevaluated.
   */
  public DelayUntilGuardResult (final Date delayTillDate)
  {
    this.delayTillDate = delayTillDate;
  }

  /**
   * Always returns {@link GuardAction#DelayUntil}.
   *
   * @see GuardResult#getGuardAction()
   */
  @Override
  public final GuardAction getGuardAction()
  {
    return GuardAction.DelayUntil;
  }

  @Override
  public String getExitArcForSkip()
  {
    throw new UnsupportedOperationException( "getExitArcsForSkip should never be called on a GuardResult with action of DelayUntil" );
  }

  /**
   * @see com.googlecode.sarasvati.GuardResult#getDelayTillTime()
   */
  @Override
  public Date getDelayTillTime()
  {
    return delayTillDate;
  }

  @Override
  public String toString()
  {
    return "DelayTillDate ["+ delayTillDate + "]";
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((delayTillDate == null) ? 0 : delayTillDate.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof DelayUntilGuardResult))
    {
      return false;
    }
    DelayUntilGuardResult other = (DelayUntilGuardResult) obj;
    if (delayTillDate == null)
    {
      if (other.delayTillDate != null)
      {
        return false;
      }
    }
    else if (!delayTillDate.equals(other.delayTillDate))
    {
      return false;
    }
    return true;
  }
}
