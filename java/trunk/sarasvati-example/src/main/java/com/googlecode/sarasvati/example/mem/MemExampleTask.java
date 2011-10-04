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
package com.googlecode.sarasvati.example.mem;

import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.example.TaskState;

public class MemExampleTask
{
  protected NodeToken nodeToken;

  protected String name;
  protected String description;

  protected TaskState state;

  public MemExampleTask (final NodeToken nodeToken, final String name, final String description, final TaskState state)
  {
    this.nodeToken = nodeToken;
    this.name = name;
    this.description = description;
    this.state = state;
  }

  public NodeToken getNodeToken ()
  {
    return nodeToken;
  }

  public void setNodeToken (final NodeToken nodeToken)
  {
    this.nodeToken = nodeToken;
  }

  public String getName ()
  {
    return name;
  }

  public String getDescription ()
  {
    return description;
  }

  public TaskState getState ()
  {
    return state;
  }

  public void setState (final TaskState state )
  {
    this.state = state;
  }

  public boolean isRejectable ()
  {
    Node node = getNodeToken().getNode();
    return !node.getGraph().getOutputArcs( node, "reject" ).isEmpty();
  }
}