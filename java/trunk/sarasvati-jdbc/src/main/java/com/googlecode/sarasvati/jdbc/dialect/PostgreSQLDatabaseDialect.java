/**
 * Created on Apr 30, 2009
 */
package com.googlecode.sarasvati.jdbc.dialect;

import com.googlecode.sarasvati.jdbc.JdbcArc;
import com.googlecode.sarasvati.jdbc.JdbcArcToken;
import com.googlecode.sarasvati.jdbc.JdbcGraph;
import com.googlecode.sarasvati.jdbc.JdbcGraphProcess;
import com.googlecode.sarasvati.jdbc.JdbcNode;
import com.googlecode.sarasvati.jdbc.JdbcNodeRef;
import com.googlecode.sarasvati.jdbc.JdbcNodeToken;
import com.googlecode.sarasvati.jdbc.action.AbstractDatabaseAction;
import com.googlecode.sarasvati.jdbc.action.ArcInsertAction;
import com.googlecode.sarasvati.jdbc.action.ArcTokenInsertAction;
import com.googlecode.sarasvati.jdbc.action.DatabaseAction;
import com.googlecode.sarasvati.jdbc.action.GraphInsertAction;
import com.googlecode.sarasvati.jdbc.action.NodeInsertAction;
import com.googlecode.sarasvati.jdbc.action.NodeRefInsertAction;
import com.googlecode.sarasvati.jdbc.action.NodeTokenInsertAction;
import com.googlecode.sarasvati.jdbc.action.ProcessInsertAction;

public class PostgreSQLDatabaseDialect extends AbstractDatabaseDialect
{
  private static final String GRAPH_INSERT_SQL =
    "insert into wf_graph (name, version) values ( ?, ? ) returning id";

  private static final String NODE_INSERT_SQL =
    "insert into wf_node (graph_id, name, type, guard, is_start, join_type) values ( ?, ?, ?, ?, ?, ? ) returning id";

  private static final String NODE_REF_INSERT_SQL =
    "insert into wf_node_ref (graph_id, node_id, instance ) values ( ?, ?, ? ) returning id";

  private static final String ARC_INSERT_SQL =
    "insert into wf_arc (graph_id, a_node_ref_id, z_node_ref_id, name ) values ( ?, ?, ?, ? ) returning id";

  private static final String PROCESS_INSERT_SQL =
    "insert into wf_process (graph_id, state, parent_token_id, create_date, version) values ( ?, 0, ?, ?, 1 ) returning id";

  private static final String NODE_TOKEN_INSERT_SQL =
    "insert into wf_node_token (process_id, node_ref_id, attr_set_id, create_date, execution_type) " +
    " values ( ?, ?, ?, ?, ? ) returning id";

  private static final String ARC_TOKEN_INSERT_SQL =
    "insert into wf_arc_token (process_id, arc_id, parent_token_id, pending, execution_type, create_date) " +
    " values ( ?, ?, ?, ?, ?, ? ) returning id";


  @Override
  public DatabaseAction newGraphInsertAction (JdbcGraph graph)
  {
    return new GraphInsertAction( GRAPH_INSERT_SQL, graph );
  }

  @Override
  public AbstractDatabaseAction newNodeInsertAction (JdbcNode node)
  {
    return new NodeInsertAction( NODE_INSERT_SQL, node );
  }

  @Override
  public AbstractDatabaseAction newArcInsertAction (JdbcArc arc)
  {
    return new ArcInsertAction( ARC_INSERT_SQL, arc );
  }

  @Override
  public AbstractDatabaseAction newNodeRefInsertAction (JdbcNodeRef nodeRef)
  {
    return new NodeRefInsertAction( NODE_REF_INSERT_SQL, nodeRef );
  }

  @Override
  public AbstractDatabaseAction newProcessInsertAction (JdbcGraphProcess process)
  {
    return new ProcessInsertAction( PROCESS_INSERT_SQL, process );
  }

  @Override
  public AbstractDatabaseAction newArcTokenInsertAction (JdbcArcToken arcToken)
  {
    return new ArcTokenInsertAction( ARC_TOKEN_INSERT_SQL, arcToken );
  }

  @Override
  public AbstractDatabaseAction newNodeTokenInsertAction (JdbcNodeToken nodeToken)
  {
    return new NodeTokenInsertAction( NODE_TOKEN_INSERT_SQL, nodeToken );
  }


}