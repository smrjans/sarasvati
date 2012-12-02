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
package com.googlecode.sarasvati.visual;

import javax.swing.Icon;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.visual.icon.OvalNodeIcon;
import com.googlecode.sarasvati.visual.icon.RectangularNodeIcon;
import com.googlecode.sarasvati.visual.icon.SmallCircleNodeIcon;
import com.googlecode.sarasvati.visual.process.VisualProcessArc;
import com.googlecode.sarasvati.visual.process.VisualProcessNode;

/**
 * Provides default implementations for methods in the
 * {@link ProcessToImageMap} interface. All methods return
 * null unless overridden.
 *
 * @author Paul Lorenz
 * @author chungonn
 */
public class ProcessToImageMapAdapter implements ProcessToImageMap
{
  private final String taskType;

  public ProcessToImageMapAdapter ()
  {
    this("task");
  }

  public ProcessToImageMapAdapter (final String taskType)
  {
    this.taskType = taskType;
  }

  /**
   * Returns true unless overridden
   * @see com.googlecode.sarasvati.visual.ProcessToImageMap#drawArcLabels(Arc)
   */
  @Override
  public boolean drawArcLabels (final Arc arc)
  {
    return true;
  }

  /**
   * Returns the defaultValue for all arcs
   *
   * @see ProcessToImageMap#isBackArc(Arc, boolean)
   */
  @Override
  public boolean isBackArc (final Arc arc, final boolean defaultValue)
  {
    return defaultValue;
  }

  /**
   * Returns a {@link RectangularNodeIcon} for nodes with type of 'task' and a
   * {@link OvalNodeIcon} for all other nodes.
   *
   * @see ProcessToImageMap#iconForNode(VisualProcessNode)
   */
  @Override
  public Icon iconForNode (final VisualProcessNode node)
  {
    if ( getTaskType().equalsIgnoreCase( node.getNode().getType() ) )
    {
      return new RectangularNodeIcon( node.getNode(), node.getToken() );
    }
    else if ( node.getNode().getType().equalsIgnoreCase( "end" ) )
    {
      return new SmallCircleNodeIcon();
    }
    return new OvalNodeIcon( node.getNode(), node.getToken() );
  }

  /**
   * TaskType default is "task". It is used to determine the type of node icon
   * returns by {@link #iconForNode(VisualProcessNode)}
   *
   */
  public String getTaskType ()
  {
    return taskType;
  }

  @Override
  public String hoverForArc (final VisualProcessArc arc)
  {
    return null;
  }

  @Override
  public String hoverForNode (final VisualProcessNode node)
  {
    return null;
  }

  @Override
  public String hrefForArc (final VisualProcessArc arc)
  {
    return null;
  }

  @Override
  public String hrefForNode (final VisualProcessNode node)
  {
    return null;
  }
}
