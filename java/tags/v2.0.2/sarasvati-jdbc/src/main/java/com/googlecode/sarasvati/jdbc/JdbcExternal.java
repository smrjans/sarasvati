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
package com.googlecode.sarasvati.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.External;
import com.googlecode.sarasvati.env.ReadEnv;
import com.googlecode.sarasvati.impl.MapEnv;

public class JdbcExternal implements External, JdbcObject
{
  protected Long id;
  protected String name;
  protected JdbcGraph graph;
  protected JdbcGraph externalGraph;

  protected Map<String, String> attrMap = new HashMap<String, String>();

  protected ReadEnv env;

  public JdbcExternal (final String name,
                       final JdbcGraph graph,
                       final JdbcGraph externalGraph,
                       final Map<String, String> attrMap)
  {
    this( null, name, graph, externalGraph, attrMap );
  }

  public JdbcExternal (final Long id,
                       final String name,
                       final JdbcGraph graph,
                       final JdbcGraph externalGraph,
                       final Map<String, String> attrMap)
  {
    this.id = id;
    this.name = name;
    this.graph = graph;
    this.externalGraph = externalGraph;
    this.attrMap = attrMap;
  }

  @Override
  public Long getId ()
  {
    return id;
  }

  @Override
  public boolean isMutable ()
  {
    return false;
  }

  @Override
  public void setId (Long id)
  {
    this.id = id;
  }

  @Override
  public JdbcGraph getExternalGraph ()
  {
    return externalGraph;
  }

  @Override
  public JdbcGraph getGraph ()
  {
    return graph;
  }

  @Override
  public String getName ()
  {
    return name;
  }

  @Override
  public ReadEnv getEnv ()
  {
    if ( env == null )
    {
      env = new MapEnv( Collections.unmodifiableMap( attrMap ) );
    }
    return env;
  }
}
