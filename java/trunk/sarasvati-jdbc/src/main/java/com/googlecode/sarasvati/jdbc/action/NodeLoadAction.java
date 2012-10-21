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
package com.googlecode.sarasvati.jdbc.action;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.jdbc.JdbcCustomNodeWrapper;
import com.googlecode.sarasvati.jdbc.JdbcEngine;
import com.googlecode.sarasvati.jdbc.JdbcGraph;
import com.googlecode.sarasvati.jdbc.JdbcNode;
import com.googlecode.sarasvati.jdbc.JdbcNodeRef;
import com.googlecode.sarasvati.load.NodeFactory;

public class NodeLoadAction extends AbstractLoadAction<JdbcNodeRef>
{
  protected final JdbcGraph graph;
  protected final JdbcEngine engine;

  protected Map<Long, JdbcNode> nodeMap = new HashMap<Long,JdbcNode>();

  public NodeLoadAction (final String sql,
                              final JdbcGraph graph,
                              final JdbcEngine engine)
  {
    super( sql, false );
    this.graph = graph;
    this.engine = engine;
  }

  @Override
  protected JdbcNodeRef loadObject (ResultSet row) throws SQLException
  {
    long nodeRefId    = row.getLong( 1 );
    // String instance   = row.getString( 2 );
    long nodeId       = row.getLong( 3 );
    String name       = row.getString( 4 );
    String type       = row.getString( 5 );
    JoinType joinType = JoinType.values()[ row.getInt( 6 ) ];
    boolean isStart   = "Y".equalsIgnoreCase( row.getString( 7 ) );
    String guard      = row.getString( 8 );

    JdbcNode node = nodeMap.get( nodeId );

    if ( node == null )
    {
      NodeFactory nodeFactory = engine.getFactory().getNodeFactory( type );
      Node loadNode = nodeFactory.newNode( type );
      JdbcCustomNodeWrapper customNodeWrapper = null;

      if ( loadNode instanceof CustomNode )
      {
        customNodeWrapper = new JdbcCustomNodeWrapper( (CustomNode)loadNode );
        node = customNodeWrapper;
      }
      else
      {
        node = (JdbcNode)loadNode;
      }

      node.setId( nodeId );
      node.setGraph( graph );
      node.setName( name );
      node.setType( type );
      node.setStart( isStart );
      node.setJoinType( joinType );
      node.setGuard( guard );

      node.afterLoad( engine );

      nodeMap.put( nodeId, node );
    }

    JdbcNodeRef nodeRef = new JdbcNodeRef( nodeRefId, graph, node, null, null );

    graph.getNodes().add( nodeRef );
    return nodeRef;
  }

  @Override
  protected void setParameters (PreparedStatement stmt) throws SQLException
  {
    stmt.setLong( 1, graph.getId() );
  }
}
