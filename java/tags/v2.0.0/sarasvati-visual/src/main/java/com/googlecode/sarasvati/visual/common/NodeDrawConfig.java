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
/**
 * Created on May 7, 2008
 */
package com.googlecode.sarasvati.visual.common;

import java.awt.Color;

import com.googlecode.sarasvati.GuardAction;
import com.googlecode.sarasvati.NodeToken;

public class NodeDrawConfig
{
  /**
   * RED, dark pink red:        153, 0, 51
   * BLUE: dark cyan azure:     0, 153, 204
   * YELLOW: light hard yellow: 255, 255, 51
   * GREEN: dark faded yellow:  153, 153, 0
   * ORANGE: dark hard orange:  204, 102, 0
   */
  private static int maxNodeRadius = 20;
  private static int verticalNodeSpacing   = 50;
  private static int horizontalNodeSpacing = 100;
  private static boolean vertical = true;

  public static final Color NODE_BORDER          = Color.black; // new Color( 0, 0, 200 );
  public static final Color NODE_BORDER_SELECTED = new Color( 204, 204, 204 );

  public static final Color NODE_BACKGROUND      = new Color( 153, 153, 153 );
  public static final Color NODE_BG_ACTIVE       = new Color( 153, 153, 0 );
  public static final Color NODE_BG_DISCARDED    = new Color( 204, 102, 0 );
  public static final Color NODE_BG_SKIPPED      = new Color( 255, 204, 51 );
  public static final Color NODE_BG_COMPLETED    = new Color( 0, 153, 204 );
  public static final Color NODE_BG_BACKTRACKED  = new Color( 153, 0, 51 );

  public static Color getColor (final NodeToken token)
  {
    if ( token == null )
    {
      return NodeDrawConfig.NODE_BACKGROUND;
    }

    if ( token.getExecutionType().isBacktracked() )
    {
      return NODE_BG_BACKTRACKED;
    }

    if ( token.getGuardAction() == GuardAction.AcceptToken )
    {
      return token.isComplete() ?  NodeDrawConfig.NODE_BG_COMPLETED : NodeDrawConfig.NODE_BG_ACTIVE;
    }

    if ( token.getGuardAction() == GuardAction.DiscardToken )
    {
      return NodeDrawConfig.NODE_BG_DISCARDED;
    }

    return NodeDrawConfig.NODE_BG_SKIPPED;
  }

  public static int getMaxNodeRadius ()
  {
    return maxNodeRadius;
  }

  public static void setMaxNodeRadius (final int newMaxNodeRadius)
  {
    maxNodeRadius = newMaxNodeRadius;
  }

  public static int getVerticalNodeSpacing ()
  {
    return verticalNodeSpacing;
  }

  public static void setVerticalNodeSpacing (final int newVerticalNodeSpacing)
  {
    verticalNodeSpacing = newVerticalNodeSpacing;
  }

  public static int getHorizontalNodeSpacing ()
  {
    return horizontalNodeSpacing;
  }

  public static void sethorizontalNodeSpacing (final int newHorizontalNodeSpacing)
  {
    horizontalNodeSpacing = newHorizontalNodeSpacing;
  }

  public static void setVertical (final boolean isVertical)
  {
    vertical = isVertical;
  }

  public static boolean isVertical ()
  {
    return vertical;
  }
}