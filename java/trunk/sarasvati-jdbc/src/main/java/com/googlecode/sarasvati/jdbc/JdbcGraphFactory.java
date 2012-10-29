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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ArcTokenSetMember;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.External;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.NodeTokenSetMember;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.jdbc.dialect.DatabaseDialect;
import com.googlecode.sarasvati.load.AbstractGraphFactory;
import com.googlecode.sarasvati.load.NodeFactory;
import com.googlecode.sarasvati.load.definition.CustomDefinition;
import com.googlecode.sarasvati.load.properties.DOMToObjectLoadHelper;

public class JdbcGraphFactory extends AbstractGraphFactory
{
  private final JdbcEngine engine;

  public JdbcGraphFactory (final JdbcEngine engine)
  {
    super( JdbcNode.class );
    this.engine = engine;
  }

  public JdbcEngine getEngine ()
  {
    return engine;
  }

  private DatabaseDialect getDialect ()
  {
    return engine.getDatabaseDialect();
  }

  @Override
  public Node importNode (final Graph graph,
                          final Node node,
                          final External external)
  {
    JdbcNodeRef nodeRef = (JdbcNodeRef)node;

    JdbcNodeRef origNode = node.getExternal() == null ? null : (JdbcNodeRef)node;

    JdbcNodeRef newRef = new JdbcNodeRef( (JdbcGraph)graph, nodeRef.getNode(), origNode, (JdbcExternal)external );
    getDialect().newNodeRefInsertAction( newRef ).execute( engine );
    graph.getNodes().add( newRef );
    return newRef;
  }

  @Override
  public Arc newArc (final Graph graph,
                     final Node startNode,
                     final Node endNode,
                     final String name)
  {
    JdbcNodeRef startNodeRef = (JdbcNodeRef)startNode;
    JdbcNodeRef endNodeRef   = (JdbcNodeRef)endNode;

    JdbcArc arc = new JdbcArc( (JdbcGraph)graph, startNodeRef, endNodeRef, name );
    getDialect().newArcInsertAction( arc ).execute( engine );
    return arc;
  }

  @Override
  public JdbcGraph newGraph (final String name,
                             final int version,
                             final String customId)
  {
    JdbcGraph graph = new JdbcGraph( name, version, customId );
    getDialect().newGraphInsertAction( graph ).execute( engine );
    return graph;
  }

  @Override
  public Node newNode (final Graph graph,
                       final String name,
                       final String type,
                       final JoinType joinType,
                       final String joinParam,
                       final boolean isStart,
                       final String guard,
                       final List<Object> customList)
  {
    NodeFactory nodeFactory = getNodeFactory( type );
    Node newNode = nodeFactory.newNode( type );

    JdbcNode node = null;
    JdbcCustomNodeWrapper customNodeWrapper = null;

    if (newNode instanceof CustomNode )
    {
      customNodeWrapper = new JdbcCustomNodeWrapper( (CustomNode)newNode );
      node = customNodeWrapper;
    }
    else
    {
      node = (JdbcNode)newNode;
    }

    if ( customList != null )
    {
      for ( Object custom : customList )
      {
        Map<String, String> customProps = nodeFactory.loadCustom( newNode, custom );

        // If this is a custom node, we need save the properties in the CustomNodeWrapper
        // as well as in the CustomNode, so that they can be set back in when the CustomNode
        // is re-created, after being loaded from the database
        if ( customNodeWrapper != null )
        {
          customNodeWrapper.importProperties( customProps );
        }
      }
    }

    node.setGraph( (JdbcGraph)graph );
    node.setName( name );
    node.setType( type );
    node.setStart( isStart );
    node.setJoinType( joinType );
    node.setJoinParam( joinParam );
    node.setGuard( guard );

    getDialect().newNodeInsertAction( node ).execute( engine );

    node.afterCreate( engine );

    JdbcNodeRef nodeRef = new JdbcNodeRef( (JdbcGraph)graph, node, null, null );
    getDialect().newNodeRefInsertAction( nodeRef ).execute( engine );
    return nodeRef;
  }

  @Override
  public GraphProcess newProcess (final Graph graph)
  {
    return newNestedProcess( graph, null );
  }

  @Override
  public GraphProcess newNestedProcess (final Graph graph, final NodeToken parentToken)
  {
    JdbcGraphProcess process = new JdbcGraphProcess( (JdbcGraph)graph, (JdbcNodeToken)parentToken );
    getDialect().newProcessInsertAction( process ).execute( engine );
    return process;
  }

  @Override
  public NodeToken newNodeToken (final GraphProcess process,
                                 final Node node,
                                 final ExecutionType executionType,
                                 final List<ArcToken> parents,
                                 final NodeToken envSource)
  {
    // Here we setup the token attributes for the new node
    // If the node has no predecessors, it will have no attributes
    // If it has only one processor (or only one processor with attributes)
    // it will inherit the attributes of that one node
    // Otherwise, the attributes of all predecessor nodes will get merged into
    // a single set.
    List<ArcToken> envParents = envSource == null ? parents : envSource.getParentTokens();

    JdbcNodeToken attrSetToken = null;
    Map<String,String> attrMap = new HashMap<String,String>();
    Map<String,Object> transientAttributes = new HashMap<String, Object>();
    boolean isMerge = false;

    for ( ArcToken arcToken : envParents )
    {
      JdbcNodeToken parent = (JdbcNodeToken)arcToken.getParentToken();

      if ( parent.getAttrSetToken() == null )
      {
        continue;
      }
      if ( attrSetToken == null )
      {
        attrSetToken = parent.getAttrSetToken();
      }
      else if ( !isMerge )
      {
        attrMap.putAll( attrSetToken.getAttrMap() );
        isMerge = true;
      }

      if ( isMerge )
      {
        attrMap.putAll( parent.getAttrMap() );
      }

      Env mergeEnv = parent.getEnv();
      for ( String name : mergeEnv.getTransientAttributeNames() )
      {
        transientAttributes.put( name, mergeEnv.getTransientAttribute( name ) );
      }
    }

    JdbcNodeToken token = new JdbcNodeToken( (JdbcGraphProcess)process,
                                             (JdbcNodeRef)node,
                                             attrSetToken,
                                             executionType,
                                             attrMap,
                                             parents,
                                             transientAttributes);

    getDialect().newNodeTokenInsertAction( token ).execute( engine );

    return token;
  }

  @Override
  public ArcToken newArcToken (final GraphProcess process,
                               final Arc arc,
                               final ExecutionType executionType,
                               final NodeToken parent,
                               final boolean tokenSetMember)
  {
    JdbcArcToken token = new JdbcArcToken( (JdbcGraphProcess)process,
                                           (JdbcArc)arc,
                                           executionType,
                                           (JdbcNodeToken)parent,
                                           tokenSetMember );

    getDialect().newArcTokenInsertAction( token ).execute( engine );

    return token;
  }

  @Override
  public External newExternal (final String name,
                               final Graph graph,
                               final Graph externalGraph,
                               final CustomDefinition customDefinition)
  {
    Map<String, String> attrMap = new HashMap<String, String>();
    DOMToObjectLoadHelper.loadCustomIntoMap( customDefinition, attrMap );
    JdbcExternal external = new JdbcExternal( name, (JdbcGraph)graph, (JdbcGraph)externalGraph, attrMap );
    return external;
  }

  @Override
  public TokenSet newTokenSet (final GraphProcess process,
                               final String name,
                               final int maxMemberIndex)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ArcTokenSetMember newArcTokenSetMember (final TokenSet tokenSet,
                                                 final ArcToken token,
                                                 final int memberIndex)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NodeTokenSetMember newNodeTokenSetMember (final TokenSet tokenSet,
                                                   final NodeToken token,
                                                   final int memberIndex)
  {
    // TODO Auto-generated method stub
    return null;
  }
}