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
                        chung-onn
*/

package com.googlecode.sarasvati.hib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.DiscriminatorValue;

import org.hibernate.Hibernate;
import org.hibernate.Session;

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
import com.googlecode.sarasvati.SarasvatiException;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.annotations.NodeType;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.load.AbstractGraphFactory;
import com.googlecode.sarasvati.load.NodeFactory;
import com.googlecode.sarasvati.load.definition.CustomDefinition;
import com.googlecode.sarasvati.load.properties.DOMToObjectLoadHelper;

public class HibGraphFactory extends AbstractGraphFactory
{
  private Session session;

  HibGraphFactory (final Session session)
  {
    super( HibNode.class );
    this.session = session;
  }

  @Override
  public HibGraph newGraph (final String name,
                            final int version,
                            final String customId)
  {
    final HibGraph newGraph = new HibGraph( name, version, customId );
    session.save( newGraph );
    return newGraph;
  }

  @Override
  public HibArc newArc (final Graph graph,
                        final Node startNode,
                        final Node endNode,
                        final String name)
  {
    if ( !(startNode instanceof HibNodeRef) )
    {
      throw new SarasvatiException( "The start node of an arc must be an instance of HibNodeRef. " +
      		                          "Instances of HibNodeRef are returned from HibNodeFactory#newNode");
    }

    if ( !(endNode instanceof HibNodeRef) )
    {
      throw new SarasvatiException( "The end node of an arc must be an instance of HibNodeRef. " +
                                    "Instances of HibNodeRef are returned from HibNodeFactory#newNode");
    }

    final HibArc arc = new HibArc( graph, startNode, endNode, name );
    session.save( arc );
    graph.getArcs().add( arc );
    return arc;
  }

  @Override
  public HibNodeRef newNode (final Graph graph,
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

    HibNode node = null;
    HibCustomNodeWrapper customNodeWrapper = null;

    if ( newNode instanceof CustomNode )
    {
      customNodeWrapper = new HibCustomNodeWrapper( (CustomNode)newNode );
      node = customNodeWrapper;
    }
    else
    {
      node = (HibNode)newNode;
    }

    node.setGraph( graph );
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

    node.create( session );

    HibNodeRef nodeRef = new HibNodeRef( graph, node, null, null );
    session.save( nodeRef );
    graph.getNodes().add( nodeRef );

    return nodeRef;
  }

  @Override
  public HibArcToken newArcToken (final GraphProcess process,
                                  final Arc arc,
                                  final ExecutionType executionType,
                                  final NodeToken previousToken)
  {
    HibGraphProcess hibProcess = (HibGraphProcess)process;
    Hibernate.initialize( hibProcess.getExecutionQueue() );
    HibArcToken token = new HibArcToken( hibProcess, (HibArc)arc, executionType, (HibNodeToken)previousToken );
    session.save( token );
    return token;
  }

  @Override
  public HibExternal newExternal (final String name,
                                  final Graph graph,
                                  final Graph externalGraph,
                                  final CustomDefinition customDefinition)
  {
    Map<String, String> attrMap = new HashMap<String, String>();
    DOMToObjectLoadHelper.loadCustomIntoMap( customDefinition, attrMap );
    HibExternal external = new HibExternal( name, (HibGraph)graph, (HibGraph)externalGraph, attrMap );
    session.save( external );
    return external;
  }

  @Override
  public HibNodeRef importNode (final Graph graph,
                                final Node node,
                                final External external)
  {
    HibNodeRef nodeRef = (HibNodeRef)node;

    HibNodeRef origNode = node.getExternal() == null ? null : (HibNodeRef)node;

    HibNodeRef newRef = new HibNodeRef( graph, nodeRef.getNode(), origNode, (HibExternal)external );
    session.save( newRef );
    graph.getNodes().add( newRef );

    return newRef;
  }

  @Override
  public HibNodeToken newNodeToken (final GraphProcess process,
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

    HibNodeToken attrSetToken = null;
    Map<String,String> attrMap = new HashMap<String,String>();
    Map<String,Object> transientAttributes = new HashMap<String, Object>();
    boolean isMerge = false;

    for ( ArcToken arcToken : envParents )
    {
      HibNodeToken parent = (HibNodeToken)arcToken.getParentToken();

      Env mergeEnv = parent.getEnv();
      for ( String name : mergeEnv.getTransientAttributeNames() )
      {
        transientAttributes.put( name, mergeEnv.getTransientAttribute( name ) );
      }

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
        attrMap.putAll( parent.getAttrSetToken().getAttrMap() );
      }
    }

    HibNodeToken token = new HibNodeToken( (HibGraphProcess)process,
                                           (HibNodeRef)node,
                                           attrSetToken,
                                           executionType,
                                           attrMap,
                                           parents,
                                           transientAttributes);
    session.save( token );
    return token;
  }

  @Override
  public HibGraphProcess newProcess (final Graph graph)
  {
    HibGraphProcess process = new HibGraphProcess( (HibGraph)graph);
    session.save( process );
    return process;
  }

  @Override
  public HibGraphProcess newNestedProcess (final Graph graph,
                                           final NodeToken parentToken)
  {
    HibGraphProcess process = new HibGraphProcess( (HibGraph)graph);
    process.setParentToken( parentToken );
    session.save( process );
    return process;
  }

  @Override
  public TokenSet newTokenSet (final GraphProcess process,
                               final String name,
                               final int maxMemberIndex)
  {
    HibTokenSet tokenSet = new HibTokenSet( (HibGraphProcess)process, name, maxMemberIndex );
    session.save( tokenSet );
    return tokenSet;
  }

  @Override
  public HibArcTokenSetMember newArcTokenSetMember (final TokenSet tokenSet,
                                                    final ArcToken token,
                                                    final int memberIndex)
  {
    HibArcTokenSetMember setMember =
      new HibArcTokenSetMember( (HibTokenSet)tokenSet, (HibArcToken)token, memberIndex );

    session.save( setMember );
    return setMember;
  }

  @Override
  public HibNodeTokenSetMember newNodeTokenSetMember (final TokenSet tokenSet,
                                                      final NodeToken token,
                                                      final int memberIndex)
  {
    HibNodeTokenSetMember setMember =
      new HibNodeTokenSetMember( (HibTokenSet)tokenSet, (HibNodeToken)token, memberIndex );

    session.save( setMember );
    return setMember;
  }

  protected void ensureDatabaseTypeExists (final String type, final Class<? extends Node> clazz)
  {
    HibNodeType hibNodeType = (HibNodeType) session.get( HibNodeType.class, type );

    /*
     * Node type doesn't exist in database yet, attempt to insert
     */
    if ( hibNodeType == null )
    {
      String behavior = null;
      String description = "User defined type";

      if ( CustomNode.class.isAssignableFrom( clazz ) )
      {
        behavior = HibCustomNodeWrapper.class.getAnnotation( DiscriminatorValue.class ).value();
      }
      else
      {
        DiscriminatorValue discriminator = clazz.getAnnotation( DiscriminatorValue.class );

        if ( discriminator != null )
        {
          behavior = discriminator.value();
        }
      }

      NodeType nodeType = clazz.getAnnotation( NodeType.class );
      if ( nodeType != null )
      {
        description = nodeType.value();
      }

      if ( type.equals( behavior ) )
      {
        hibNodeType = new HibNodeType( type, description );
      }
      else
      {
        HibNodeType behaviorType = (HibNodeType) session.load( HibNodeType.class, behavior );
        hibNodeType = new HibNodeType( type, description, behaviorType );
      }

      session.save( hibNodeType );
    }

    super.addType( type, clazz );
  }

  @Override
  public void addType (final String type, final Class<? extends Node> clazz)
  {
    ensureDatabaseTypeExists(type, clazz);
    super.addType( type, clazz );
  }

  @Override
  public void addGlobalCustomType(final String type, final Class<? extends CustomNode> nodeClass)
  {
    ensureDatabaseTypeExists(type, nodeClass);
    super.addGlobalCustomType(type, nodeClass);
  }
}