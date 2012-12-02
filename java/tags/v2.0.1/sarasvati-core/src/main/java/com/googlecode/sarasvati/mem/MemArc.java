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

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Node;

public class MemArc implements Arc
{
  protected String  name;
  protected Node startNode;
  protected Node endNode;

  public MemArc (final String name, final Node startNode, final Node endNode)
  {
    this.name = name;
    this.startNode = startNode;
    this.endNode = endNode;
  }

  @Override
  public Node getEndNode ()
  {
    return endNode;
  }

  @Override
  public String getName ()
  {
    return name;
  }

  @Override
  public Node getStartNode ()
  {
    return startNode;
  }

  @Override
  public boolean isSelfArc ()
  {
    return startNode.equals( endNode );
  }

  @Override
  public String toString ()
  {
    return "[MemArc start=" + startNode.getName() + " end=" + endNode.getName() + " name=" + name + "]";
  }
}
