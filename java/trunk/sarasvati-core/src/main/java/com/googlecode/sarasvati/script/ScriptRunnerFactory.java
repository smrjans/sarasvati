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

public class ScriptRunnerFactory
{
  private static ScriptRunner scriptRunner = null;

  static
  {
    try
    {
      Class.forName( "javax.script.ScriptEngineManager" );
      scriptRunner = (ScriptRunner)Class.forName( "com.googlecode.sarasvati.script.JavaSixScriptRunner" ).newInstance();
    }
    catch (Exception e)
    {
      System.out.println( "Java 6 script environment not found." );
      // Not running in java 6 environment
    }

    if (scriptRunner == null )
    {
      try
      {
        Class.forName( "org.apache.bsf.BSFManager" );
        scriptRunner = (ScriptRunner)Class.forName( "com.googlecode.sarasvati.script.BSFScriptRunner" ).newInstance();
      }
      catch (Exception e)
      {
        System.out.println( "Apache BSF script environment not found." );
        // Not running in java 6 environment
      }
    }
  }

  public static void setScriptRunner (final ScriptRunner scriptRunner)
  {
    ScriptRunnerFactory.scriptRunner = scriptRunner;
  }

  public static ScriptRunner getScriptRunner ()
  {
    return scriptRunner;
  }
}
