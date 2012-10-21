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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.sarasvati.jdbc.action.DatabaseLoadAction;
import com.googlecode.sarasvati.jdbc.dialect.DatabaseDialect;
import com.googlecode.sarasvati.load.GraphRepository;

public class JdbcGraphRepository implements GraphRepository<JdbcGraph>
{
  public interface JdbcCache
  {
    void notifyLoaded (JdbcObject obj);

    <T> T get (Class<T> clazz, Long id);
  }

  public class DefaultCache implements JdbcCache
  {
    protected Map<String, Object> map = new HashMap<String, Object> ();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get (Class<T> clazz, Long id)
    {
      String key = clazz.getName() + id;
      return (T)map.get( key );
    }

    @Override
    public void notifyLoaded (JdbcObject obj)
    {
      String key = obj.getClass().getName() + obj.getId();
      map.put( key, obj );
    }
  }

  private final JdbcEngine engine;
  private final JdbcCache  cache = new DefaultCache ();

  public JdbcGraphRepository (final JdbcEngine engine)
  {
    this.engine = engine;
  }

  private DatabaseDialect getDialect ()
  {
    return engine.getDatabaseDialect();
  }

  @Override
  public void addGraph (JdbcGraph graph)
  {
    // Does nothing, since this is handled by the DB
  }

  @Override
  public List<JdbcGraph> getGraphs (final String name)
  {
    DatabaseLoadAction<JdbcGraph> action = getDialect().newGraphByNameLoadAction( name );
    action.execute( engine );
    return loadGraphs( action.getResult() );
  }

  @Override
  public List<JdbcGraph> getGraphs ()
  {
    DatabaseLoadAction<JdbcGraph> action = getDialect().newGraphLoadAction();
    action.execute( engine );
    return loadGraphs( action.getResult() );
  }

  @Override
  public JdbcGraph getLatestGraph (final String name)
  {
    DatabaseLoadAction<JdbcGraph> action = getDialect().newLatestGraphByNameLoadAction( name );
    action.execute( engine );
    List<JdbcGraph> result = loadGraphs( action.getResult() );
    return result.isEmpty() ? null : result.get( 0 );
  }

  public JdbcGraph getGraph (final long graphId)
  {
    JdbcGraph graph = cache.get( JdbcGraph.class, graphId );

    if ( graph != null )
    {
      return graph;
    }

    DatabaseLoadAction<JdbcGraph> action = getDialect().newGraphByIdLoadAction( graphId );
    action.execute( engine );
    List<JdbcGraph> result = loadGraphs( action.getResult() );
    return result.isEmpty() ? null : result.get( 0 );
  }

  protected List<JdbcGraph> loadGraphs (List<JdbcGraph> list)
  {
    List<JdbcGraph> result = new ArrayList<JdbcGraph>( list.size() );

    for ( JdbcGraph graph : list )
    {
      JdbcGraph cached = cache.get( JdbcGraph.class, graph.getId() );
      if ( cached != null )
      {
        result.add( cached );
      }
      else
      {
        loadNodes( graph );
        loadArcs( graph );
        cache.notifyLoaded( graph );
        result.add( graph );
      }
    }

    return result;
  }

  public JdbcGraphProcess loadProcess (final long processId)
  {
    DatabaseLoadAction<JdbcGraphProcess> action = getDialect().newProcessLoadAction( processId, engine );
    action.execute( engine );
    return action.getFirstResult();
  }

  protected void loadNodes (final JdbcGraph graph)
  {
    DatabaseLoadAction<JdbcNodeRef> stmt = getDialect().newNodeLoadAction( graph, engine );
    stmt.execute( engine );
  }

  public void loadArcs (final JdbcGraph graph)
  {
    DatabaseLoadAction<JdbcArc> action = getDialect().newArcLoadAction( graph );
    action.execute( engine );
  }
}