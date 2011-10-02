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
package com.googlecode.sarasvati.script;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.SarasvatiException;

public class BSFScriptRunner implements ScriptRunner
{
  static
  {
    BSFManager.registerScriptingEngine( "js", "org.apache.bsf.engines.javascript.JavaScriptEngine", new String[] { "js" } );
  }

  @Override
  public Object executeScript(final Engine engine, final NodeToken token, final String script, final String userScriptType)
  {
    BSFManager bsfManager = new BSFManager();

    String scriptType = userScriptType;

    try
    {
      if ( scriptType == null || scriptType.trim().isEmpty() )
      {
        scriptType = BSFManager.getLangFromFilename( "dummy.js" );
      }

      engine.setupScriptEnv( new BSFManagerEnv( bsfManager ), token );
      return bsfManager.eval( scriptType, token.getNode().getName() + ".script", 1, 1, script );
    }
    catch (BSFException e)
    {
      throw new SarasvatiException( "Script of type " + scriptType + " failed to execute. " +
                                   "Script content: " + script, e );
    }
  }
}