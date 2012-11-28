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
package com.googlecode.sarasvati.impl;

import java.util.Date;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GuardAction;
import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.util.SvUtil;

/**
 * Encapsulates a skip node response from {@link Node#guard(Engine, NodeToken)}.
 * Allows specifying an arc name to exit on.
 *
 * @author Paul Lorenz
 */
public class SkipNodeGuardResult implements GuardResult
{
  /**
   * Singleton instance to be used when skipping a node and leaving on arc(s) with the default name.
   */
  public static final SkipNodeGuardResult DEFAULT_ARC_SKIP_NODE_RESULT = new SkipNodeGuardResult( Arc.DEFAULT_ARC );

  protected String exitArcForSkip = null;

  /**
   * Constructor which takes the name of the arc(s) on which to
   * exit the current node.
   *
   * @param arcName The name of the arc(s) to exit on when leaving the node
   */
  public SkipNodeGuardResult (final String arcName)
  {
    this.exitArcForSkip = arcName;
  }

  /**
   * Always returns {@link GuardAction#SkipNode}.
   *
   * @see GuardResult#getGuardAction()
   */
  @Override
  public final GuardAction getGuardAction()
  {
    return GuardAction.SkipNode;
  }

  /**
   * @see GuardResult#getExitArcForSkip()
   */
  @Override
  public String getExitArcForSkip()
  {
    return exitArcForSkip;
  }

  /**
   * @see com.googlecode.sarasvati.GuardResult#getDelayTillTime()
   */
  @Override
  public Date getDelayTillTime()
  {
    throw new UnsupportedOperationException( "getDelayUntilDate should never be called on a GuardResult with action of SkipNode" );
  }

  @Override
  public String toString()
  {
    return SvUtil.equals( Arc.DEFAULT_ARC, exitArcForSkip ) ? "SkipNodeResult"  : "SkipNodeResult (" + exitArcForSkip + ")";
  }

  @Override
  public int hashCode ()
  {
    return exitArcForSkip == null ? 0 : exitArcForSkip.hashCode();
  }

  @Override
  public boolean equals (final Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null || !( obj instanceof SkipNodeGuardResult ) ) return false;
    SkipNodeGuardResult other = (SkipNodeGuardResult)obj;
    if ( exitArcForSkip == null )
    {
      if ( other.exitArcForSkip != null ) return false;
    }
    else if ( !exitArcForSkip.equals( other.exitArcForSkip ) ) return false;
    return true;
  }
}
