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
package com.googlecode.sarasvati.editor.model;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.sarasvati.editor.xml.XmlEditorExternal;
import com.googlecode.sarasvati.editor.xml.XmlEditorNode;
import com.googlecode.sarasvati.editor.xml.XmlEditorProperties;
import com.googlecode.sarasvati.load.definition.ArcDefinition;
import com.googlecode.sarasvati.load.definition.CustomDefinition;
import com.googlecode.sarasvati.load.definition.ExternalArcDefinition;
import com.googlecode.sarasvati.load.definition.ExternalDefinition;
import com.googlecode.sarasvati.load.definition.NodeDefinition;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;
import com.googlecode.sarasvati.load.properties.DOMToObjectLoadHelper;
import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.xml.XmlArc;
import com.googlecode.sarasvati.xml.XmlCustom;
import com.googlecode.sarasvati.xml.XmlExternal;
import com.googlecode.sarasvati.xml.XmlExternalArc;
import com.googlecode.sarasvati.xml.XmlJoinType;
import com.googlecode.sarasvati.xml.XmlNode;
import com.googlecode.sarasvati.xml.XmlProcessDefinition;

public class EditorGraphFactory
{
  public static class XmlSaveData
  {
    private final XmlProcessDefinition xmlProcDef;
    private final XmlEditorProperties xmlEditorProps;
    public XmlSaveData (final XmlProcessDefinition xmlProcDef, final XmlEditorProperties xmlEditorProps)
    {
      this.xmlProcDef = xmlProcDef;
      this.xmlEditorProps = xmlEditorProps;
    }

    public XmlProcessDefinition getXmlProcDef ()
    {
      return xmlProcDef;
    }

    public XmlEditorProperties getXmlEditorProps ()
    {
      return xmlEditorProps;
    }
  }

  public static EditorGraph loadFromXml (final ProcessDefinition procDef,
                                         final XmlEditorProperties editorProps)
  {
    GraphState graphState = null;

    if ( editorProps == null )
    {
      graphState = new GraphState();
    }
    else
    {
      graphState = new GraphState( editorProps.getDefaultNodeForIncomingArcs(),
                                   editorProps.getDefaultNodeForOutgoingArcs() );
    }

    EditorGraph graph = new EditorGraph( graphState );
    graph.setName( procDef.getName() );

    Map<String, EditorNode> nodeMap = new HashMap<String, EditorNode>();
    Map<String, EditorExternal> externalMap = new HashMap<String, EditorExternal>();

    for ( NodeDefinition nodeDef : procDef.getNodes() )
    {
      CustomDefinition custom = nodeDef.getCustom();
      Map<String,String> customProperties = new LinkedHashMap<String,String>();
      DOMToObjectLoadHelper.loadCustomIntoMap( custom, customProperties );

      NodeState nodeState = new NodeState( nodeDef.getName(),
                                           nodeDef.getType(),
                                           nodeDef.getJoinType(),
                                           nodeDef.getJoinParam(),
                                           nodeDef.isStart(),
                                           nodeDef.getGuard(),
                                           customProperties,
                                           false );
      EditorNode node = new EditorNode( nodeState );

      if ( nodeDef.getX() != null && nodeDef.getY() != null )
      {
        node.setX( nodeDef.getX() );
        node.setY( nodeDef.getY() );
      }

      graph.addNode( node );
      nodeMap.put( node.getState().getName(), node );
    }

    for ( ExternalDefinition externalDef : procDef.getExternals() )
    {
      Map<String,String> customProperties = new LinkedHashMap<String,String>();
      DOMToObjectLoadHelper.loadCustomIntoMap( externalDef.getCustom(), customProperties );

      ExternalState externalState = new ExternalState( externalDef.getName(), externalDef.getProcessDefinition(), customProperties, false );
      EditorExternal external = new EditorExternal( externalState );

      if ( externalDef.getX() != null && externalDef.getY() != null )
      {
        external.setX( externalDef.getX() );
        external.setY( externalDef.getY() );
      }

      graph.addExternal( external );
      externalMap.put( external.getState().getName(), external );
    }

    for ( NodeDefinition nodeDef : procDef.getNodes() )
    {
      for ( ArcDefinition arcDef : nodeDef.getArcs() )
      {
        String externalEnd = null;
        EditorGraphMember<?> startMember = nodeMap.get( nodeDef.getName() );
        EditorGraphMember<?> endMember = null;

        if ( arcDef.isToExternal() )
        {
          endMember = externalMap.get( arcDef.getExternal() );
          externalEnd = arcDef.getTo();
        }
        else
        {
          endMember = nodeMap.get( arcDef.getTo() );
        }

        ArcState state = new ArcState( SvUtil.nullIfBlank( arcDef.getName() ),
                                       null,
                                       SvUtil.nullIfBlank( externalEnd ) );
        EditorArc arc = new EditorArc( state, startMember, endMember );

        graph.addArc( arc );
      }
    }

    for ( ExternalDefinition externalDef : procDef.getExternals() )
    {
      EditorGraphMember<?> startMember = externalMap.get( externalDef.getName() );

      for ( ExternalArcDefinition externalArcDef : externalDef.getExternalArcs() )
      {
        EditorGraphMember<?> endMember = null;
        String externalEnd = null;

        if ( externalArcDef.isToExternal() )
        {
          endMember = externalMap.get( externalArcDef.getExternal() );
          externalEnd = externalArcDef.getTo();
        }
        else
        {
          endMember = nodeMap.get( externalArcDef.getTo() );
        }

        ArcState state = new ArcState( SvUtil.nullIfBlank( externalArcDef.getName() ),
                                       SvUtil.nullIfBlank( externalArcDef.getFrom() ),
                                       SvUtil.nullIfBlank( externalEnd ) );
        EditorArc arc = new EditorArc( state, startMember, endMember );

        graph.addArc( arc );
      }
    }

    if ( editorProps != null )
    {
      for ( XmlEditorNode xmlEditorNode : editorProps.getNodes() )
      {
        EditorNode node = nodeMap.get( xmlEditorNode.getName() );
        if ( node != null )
        {
          node.setX( xmlEditorNode.getX() );
          node.setY( xmlEditorNode.getY() );
        }
      }

      for ( XmlEditorExternal xmlEditorExternal : editorProps.getExternals() )
      {
        EditorExternal external = externalMap.get( xmlEditorExternal.getName() );
        if ( external != null )
        {
          external.setX( xmlEditorExternal.getX() );
          external.setY( xmlEditorExternal.getY() );
        }
      }
    }

    return graph;
  }

  public static XmlSaveData exportToXml (final EditorGraph graph) throws IOException
  {
    XmlProcessDefinition xmlProcDef = new XmlProcessDefinition();
    xmlProcDef.setName( graph.getName() );

    XmlEditorProperties xmlEditorProps = new XmlEditorProperties();
    xmlEditorProps.setDefaultNodeForIncomingArcs( graph.getState().getDefaultNodeForIncomingArcs() );
    xmlEditorProps.setDefaultNodeForOutgoingArcs( graph.getState().getDefaultNodeForOutgoingArcs() );

    Map<EditorNode,XmlNode> nodeMap = new HashMap<EditorNode, XmlNode>();
    Map<EditorExternal,XmlExternal> externalMap = new HashMap<EditorExternal, XmlExternal>();

    for ( EditorNode node : graph.getNodes() )
    {
      XmlNode xmlNode = new XmlNode();
      NodeState state = node.getState();
      xmlNode.setName( state.getName() );
      xmlNode.setType( state.getType() );
      xmlNode.setGuard( SvUtil.nullIfBlank( state.getGuard() ) );
      xmlNode.setJoinType( XmlJoinType.getXmlJoinType( state.getJoinType() ) );
      xmlNode.setJoinParam( SvUtil.nullIfBlank( state.getJoinParam() ) );

      if ( state.isStart() )
      {
        xmlNode.setStart( true );
      }

      EditorNodeType nodeType = state.getEditorNodeType();
      Set<String> cdataTypes = Collections.emptySet();
      if ( nodeType != null )
      {
        cdataTypes = nodeType.getCDataTypes();
      }
      List<Object> customList = DOMToObjectLoadHelper.mapToDOM( state.getCustomProperties(), cdataTypes );

      if ( !customList.isEmpty() )
      {
        XmlCustom custom = new XmlCustom();
        custom.setCustom( customList );
        xmlNode.setCustom( custom );
      }

      xmlProcDef.getNodes().add( xmlNode );
      nodeMap.put( node, xmlNode );

      XmlEditorNode xmlEditorNode = new XmlEditorNode();
      xmlEditorNode.setName( state.getName() );
      xmlEditorNode.setX( node.getX() );
      xmlEditorNode.setY( node.getY() );

      xmlEditorProps.getNodes().add( xmlEditorNode );
    }

    for ( EditorExternal external : graph.getExternals() )
    {
      XmlExternal xmlExternal = new XmlExternal();
      ExternalState state = external.getState();
      xmlExternal.setName( state.getName() );
      xmlExternal.setProcessDefinition( state.getGraphName() );

      Set<String> emptySet = Collections.emptySet();
      List<Object> customList = DOMToObjectLoadHelper.mapToDOM( state.getCustomProperties(), emptySet );

      if ( !customList.isEmpty() )
      {
        XmlCustom custom = new XmlCustom();
        custom.setCustom( customList );
        xmlExternal.setCustom( custom );
      }

      xmlProcDef.getExternals().add( xmlExternal );
      externalMap.put( external, xmlExternal );

      XmlEditorExternal xmlEditorExternal = new XmlEditorExternal();
      xmlEditorExternal.setName( state.getName() );
      xmlEditorExternal.setX( external.getX() );
      xmlEditorExternal.setY( external.getY() );

      xmlEditorProps.getExternals().add( xmlEditorExternal );
    }

    for ( EditorArc arc : graph.getArcs() )
    {
      ArcState state = arc.getState();

      if ( arc.isExternalInArc() )
      {
        XmlExternal external = externalMap.get( arc.getStart() );
        XmlExternalArc xmlExternalArc = new XmlExternalArc();
        xmlExternalArc.setFrom( state.getExternalStart() );
        xmlExternalArc.setName( state.getLabel() );
        if ( arc.isExternalOutArc() )
        {
          xmlExternalArc.setExternal( arc.getEnd().getName() );
          xmlExternalArc.setTo( state.getExternalEnd() );
        }
        else
        {
          xmlExternalArc.setTo( arc.getEnd().getName() );
        }
        external.getExternalArcs().add( xmlExternalArc );
      }
      else
      {
        XmlNode node = nodeMap.get( arc.getStart() );
        XmlArc xmlArc = new XmlArc();
        xmlArc.setName( state.getLabel() );

        if ( arc.isExternalOutArc() )
        {
          xmlArc.setExternal( arc.getEnd().getState().getName() );
          xmlArc.setTo( state.getExternalEnd() );
        }
        else
        {
          xmlArc.setTo( arc.getEnd().getName() );
        }
        node.getArcs().add( xmlArc );
      }
    }

    return new XmlSaveData( xmlProcDef, xmlEditorProps );
  }
}