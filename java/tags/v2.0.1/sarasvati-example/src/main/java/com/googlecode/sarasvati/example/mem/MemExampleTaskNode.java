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

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.example.TaskUtil;
import com.googlecode.sarasvati.mem.MemNode;

public class MemExampleTaskNode extends MemNode
{
  protected String taskName;
  protected String taskDesc;

  public String getTaskName ()
  {
    return taskName;
  }

  public void setTaskName (final String taskName)
  {
    this.taskName = taskName;
  }

  public String getTaskDesc ()
  {
    return taskDesc;
  }

  public void setTaskDesc (final String taskDesc)
  {
    this.taskDesc = taskDesc;
  }

  @Override
  public void backtrack (final Engine engine, final NodeToken token)
  {
    TaskUtil.backtrackTask( engine, token );
  }

  @Override
  public void execute (final Engine engine, final NodeToken token)
  {
    TaskUtil.createTask( engine, token, getTaskName(), getTaskDesc() );
  }
}