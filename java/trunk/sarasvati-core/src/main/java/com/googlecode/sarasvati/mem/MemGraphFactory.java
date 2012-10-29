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

    Copyright 2008-2009 Paul Lorenz
*/

package com.googlecode.sarasvati.mem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.External;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.impl.MapEnv;
import com.googlecode.sarasvati.load.AbstractGraphFactory;
import com.googlecode.sarasvati.load.NodeFactory;
import com.googlecode.sarasvati.load.definition.CustomDefinition;
import com.googlecode.sarasvati.load.properties.DOMToObjectLoadHelper;

public class MemGraphFactory extends AbstractGraphFactory
{
  public static final MemGraphFactory INSTANCE = new MemGraphFactory();

  public MemGraphFactory ()
  {
    super( MemNode.class );
  }

  @Override
  public MemGraph newGraph (final String name,
                            final int version,
                            final String customId)
  {
    return new MemGraph( name, customId );
  }

  @Override
  public Arc newArc (final Graph graph,
                     final Node startNode,
                     final Node endNode,
                     final String name)
  {
    MemArc arc = new MemArc( name, startNode, endNode );
    graph.getArcs().add( arc );
    return arc;
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

    MemNode node = null;

    if ( newNode instanceof CustomNode )
    {
      node = new MemCustomNodeWrapper( (CustomNode)newNode );
    }
    else
    {
      node = (MemNode)newNode;
    }

    node.initId();
    node.setGraph( graph );
    node.setDefiningGraph( graph );
    node.setName( name );
    node.setType( type );
    node.setJoinType( joinType );
    node.setJoinParam( joinParam );
    node.setStart( isStart );
    node.setGuard( guard );

    if ( customList != null )
    {
      for ( Object custom : customList )
      {
        nodeFactory.loadCustom( newNode, custom );
      }
    }

    graph.getNodes().add( node );

    return node;
  }

  @Override
  public Node importNode (final Graph graph,
                          final Node node,
                          final External external)
  {
    MemNode memNode = (MemNode)node;
    MemNode newNode = memNode.clone();
    newNode.setGraph( graph );
    newNode.setExternal( (MemExternal)external );

    if ( node.getExternal() != null )
    {
      newNode.setOriginatingExternalNode( (MemNode)node );
    }

    graph.getNodes().add( newNode );

    return newNode;
  }

  @Override
  public External newExternal (final String name,
                               final Graph graph,
                               final Graph externalGraph,
                               final CustomDefinition customDefinition)
  {
    HashMap<String, String> attributes = new HashMap<String, String> ();
    DOMToObjectLoadHelper.loadCustomIntoMap( customDefinition, attributes );
    return new MemExternal( name, graph, externalGraph, new MapEnv( Collections.unmodifiableMap( attributes ) ) );
  }

  @Override
  public ArcToken newArcToken (final GraphProcess process,
                               final Arc arc,
                               final ExecutionType executionType,
                               final NodeToken parent,
                               final boolean tokenSetMember)
  {
    return new MemArcToken( arc, process, executionType, parent );
  }

  @Override
  public NodeToken newNodeToken (final GraphProcess process,
                                 final Node node,
                                 final ExecutionType executionType,
                                 final List<ArcToken> parents,
                                 final NodeToken envParent)
  {
    MemGraphProcess memGraphProcess = (MemGraphProcess)process;

    MemNodeToken token = new MemNodeToken( memGraphProcess.nextTokenId(), node, process, executionType, parents );
    Env env = token.getEnv();

    List<ArcToken> envTokens = envParent == null ? parents : envParent.getParentTokens();

    for ( ArcToken t : envTokens )
    {
      env.importEnv( t.getParentToken().getEnv() );
    }

    return token;
  }

  @Override
  public MemGraphProcess newProcess (final Graph graph)
  {
    return new MemGraphProcess( graph );
  }

  @Override
  public MemGraphProcess newNestedProcess (final Graph graph,
                                           final NodeToken parentToken)
  {
    final MemGraphProcess process = newProcess( graph );
    process.setParentToken( parentToken );
    ((MemNodeToken)parentToken).setNestedProcess(process);
    return process;
  }

  @Override
  public MemTokenSet newTokenSet (final GraphProcess process,
                                  final String name,
                                  final int maxMemberIndex)
  {
    return new MemTokenSet( process, name, maxMemberIndex );
  }

  @Override
  public MemArcTokenSetMember newArcTokenSetMember (final TokenSet tokenSet,
                                                    final ArcToken token,
                                                    final int memberIndex)
  {
    return new MemArcTokenSetMember( tokenSet, (MemArcToken)token, memberIndex );
  }

  @Override
  public MemNodeTokenSetMember newNodeTokenSetMember (final TokenSet tokenSet,
                                                      final NodeToken token,
                                                      final int memberIndex)
  {
    return new MemNodeTokenSetMember( tokenSet, (MemNodeToken)token, memberIndex );
  }
}