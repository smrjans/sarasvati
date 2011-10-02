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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.SarasvatiException;

public class JavaSixScriptRunner implements ScriptRunner
{
  public Collection<String> getSupportedTypes()
  {
    Set<String> types = new HashSet<String>();
    for ( ScriptEngineFactory factory : new ScriptEngineManager().getEngineFactories() )
    {
      types.addAll( factory.getExtensions() );
    }

    return types;
  }

  @Override
  public Object executeScript(final Engine engine, final NodeToken token, final String script, final String userScriptType)
  {
    String scriptType = userScriptType;
    if ( scriptType == null || scriptType.trim().isEmpty() )
    {
      scriptType = "js";
    }

    try
    {
      ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByExtension( scriptType );

      if ( scriptEngine == null )
      {
        throw new SarasvatiException( "No script engine found for type '" + scriptType + "'. " +
                                     "Searched by file extension (i.e. js for javascript, rb for ruby, etc..)" );
      }

      engine.setupScriptEnv( new ScriptEngineEnv( scriptEngine ), token);
      return scriptEngine.eval( script );
    }
    catch ( ScriptException se )
    {
      throw new SarasvatiException( "Script of type " + scriptType + " failed to execute. " +
                                    "Script content: \n" + script, se );
    }
  }


}