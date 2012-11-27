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

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.script.ScriptRunnerFactory;

public class ScriptNode extends CustomNode
{
  protected String execute;
  protected String backtrack;
  protected String allowBacktrack;

  protected String executeType;
  protected String backtrackType;
  protected String allowBacktrackType;

  public String getExecute ()
  {
    return execute;
  }

  public void setExecute (final String execute)
  {
    this.execute = execute;
  }

  public String getBacktrack ()
  {
    return backtrack;
  }

  public void setBacktrack (final String backtrack)
  {
    this.backtrack = backtrack;
  }

  public String getAllowBacktrack ()
  {
    return allowBacktrack;
  }

  public void setAllowBacktrack (final String allowBacktrack)
  {
    this.allowBacktrack = allowBacktrack;
  }

  public String getExecuteType ()
  {
    return executeType;
  }

  public void setExecuteType (final String executeType)
  {
    this.executeType = executeType;
  }

  public String getBacktrackType ()
  {
    return backtrackType;
  }

  public void setBacktrackType (final String backtrackType)
  {
    this.backtrackType = backtrackType;
  }

  public String getAllowBacktrackType ()
  {
    return allowBacktrackType;
  }

  public void setAllowBacktrackType (final String allowBacktrackType)
  {
    this.allowBacktrackType = allowBacktrackType;
  }

  @Override
  public void execute (final Engine engine, final NodeToken token)
  {
    ScriptRunnerFactory.getScriptRunner().executeScript( engine, token, execute, executeType );

    if ( !token.isComplete() )
    {
      engine.complete( token, Arc.DEFAULT_ARC );
    }
  }

  @Override
  public void backtrack (final Engine engine, final NodeToken token)
  {
    if ( backtrack != null && !backtrack.trim().isEmpty() )
    {
      ScriptRunnerFactory.getScriptRunner().executeScript( engine, token, backtrack, backtrackType );
    }
  }

  @Override
  public boolean isBacktrackable (final Engine engine, final NodeToken token)
  {
    if ( allowBacktrack != null && !allowBacktrack.trim().isEmpty() )
    {
      return (Boolean)ScriptRunnerFactory.getScriptRunner().executeScript( engine, token, allowBacktrack, allowBacktrackType );
    }

    return super.isBacktrackable( engine, token );
  }
}