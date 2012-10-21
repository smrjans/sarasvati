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
package com.googlecode.sarasvati.jdbc.dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.jdbc.JdbcArc;
import com.googlecode.sarasvati.jdbc.JdbcEngine;
import com.googlecode.sarasvati.jdbc.JdbcGraph;
import com.googlecode.sarasvati.jdbc.JdbcGraphProcess;
import com.googlecode.sarasvati.jdbc.JdbcNode;
import com.googlecode.sarasvati.jdbc.JdbcNodeRef;
import com.googlecode.sarasvati.jdbc.JdbcPropertyNode;
import com.googlecode.sarasvati.jdbc.action.AbstractDatabaseAction;
import com.googlecode.sarasvati.jdbc.action.AbstractExecuteUpdateAction;
import com.googlecode.sarasvati.jdbc.action.AbstractGraphLoadAction;
import com.googlecode.sarasvati.jdbc.action.ArcLoadAction;
import com.googlecode.sarasvati.jdbc.action.DatabaseLoadAction;
import com.googlecode.sarasvati.jdbc.action.NodeLoadAction;
import com.googlecode.sarasvati.jdbc.action.NodePropertyLoadAction;
import com.googlecode.sarasvati.jdbc.action.ProcessLoadAction;


public abstract class AbstractDatabaseDialect implements DatabaseDialect
{
  private static final String SELECT_LATEST_GRAPH_SQL =
    "select id, name, version from wf_graph " +
    " where name = ? and version in (select max(version) from wf_graph where name = ?)";

  private static final String SELECT_ALL_GRAPHS_SQL =
    "select id, name, version from wf_graph";

  private static final String SELECT_GRAPHS_BY_NAME_SQL =
    "select id, name, version from wf_graph where name = ?";

  private static final String SELECT_GRAPHS_BY_ID_SQL =
    "select id, name, version from wf_graph where id = ?";

  private static final String SELECT_NODES_SQL =
    "select ref.id as ref_id, ref.instance, node.id, node.name, node.type, node.join_type, node.is_start, node.guard" +
    "  from wf_node_ref ref join wf_node node on ref.node_id = node.id " +
    " where ref.graph_id = ?";

  private static final String SELECT_ARCS_SQL =
    "select id, a_node_ref_id, z_node_ref_id, name from wf_arc where graph_id = ?";

  private static final String INSERT_NODE_PROPERTY_SQL =
    "insert into wf_node_attr (node_id, name, value) values (?, ?, ?)";

  private static final String SELECT_NODE_PROPERTIES_SQL =
    "select name, value from wf_node_attr where node_id = ?";

  private static final String SELECT_PROCESS_SQL =
    "select graph_id, state, parent_token_id, create_date, version" +
    "  from wf_process where id = ?";

  protected Map<Class<?>,Object> userData = new HashMap<Class<?>,Object> ();

  @Override
  public DatabaseLoadAction<JdbcArc> newArcLoadAction (final JdbcGraph graph)
  {
    return new ArcLoadAction( SELECT_ARCS_SQL, graph );
  }

  @Override
  public DatabaseLoadAction<JdbcGraph> newGraphByNameLoadAction (final String name)
  {
    return new AbstractGraphLoadAction( SELECT_GRAPHS_BY_NAME_SQL )
    {
      @Override
      protected void setParameters (final PreparedStatement stmt) throws SQLException
      {
        stmt.setString( 1, name );
      }
    };
  }

  @Override
  public DatabaseLoadAction<JdbcGraph> newGraphByIdLoadAction (final long graphId)
  {
    return new AbstractGraphLoadAction( SELECT_GRAPHS_BY_ID_SQL )
    {
      @Override
      protected void setParameters (final PreparedStatement stmt) throws SQLException
      {
        stmt.setLong( 1, graphId );
      }
    };
  }

  @Override
  public DatabaseLoadAction<JdbcGraph> newGraphLoadAction ()
  {
    return new AbstractGraphLoadAction( SELECT_ALL_GRAPHS_SQL )
    {
      @Override
      protected void setParameters (final PreparedStatement stmt) throws SQLException
      {
        // no parameters to set
      }
    };
  }

  @Override
  public DatabaseLoadAction<JdbcGraph> newLatestGraphByNameLoadAction (final String name)
  {
    return new AbstractGraphLoadAction( SELECT_LATEST_GRAPH_SQL )
    {
      @Override
      protected void setParameters (final PreparedStatement stmt) throws SQLException
      {
        stmt.setString( 1, name );
        stmt.setString( 2, name );
      }
    };
  }

  @Override
  public DatabaseLoadAction<JdbcNodeRef> newNodeLoadAction (final JdbcGraph graph, final JdbcEngine engine)
  {
    return new NodeLoadAction( SELECT_NODES_SQL, graph, engine );
  }

  @Override
  public AbstractDatabaseAction newNodePropertyInsertAction (final JdbcNode node, final String key, final String value)
  {
    return new AbstractExecuteUpdateAction( INSERT_NODE_PROPERTY_SQL )
    {
      @Override
      protected void setParameters (final PreparedStatement stmt) throws SQLException
      {
        stmt.setLong( 1, node.getId() );
        stmt.setString( 2, key );
        stmt.setString( 3, value );
      }
    };
  }

  @Override
  public AbstractDatabaseAction newNodePropertiesLoadAction (final JdbcPropertyNode node)
  {
    return new NodePropertyLoadAction( SELECT_NODE_PROPERTIES_SQL, node );
  }

  @Override
  public DatabaseLoadAction<JdbcGraphProcess> newProcessLoadAction (final long processId,
                                                                    final JdbcEngine engine)
  {
    return new ProcessLoadAction( SELECT_PROCESS_SQL, processId, engine );
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getUserData (final Class<T> key)
  {
    return (T)userData.get( key );
  }

  @Override
  public <T> void setUserData (final Class<T> key, final T value)
  {
    userData.put( key, value );
  }
}