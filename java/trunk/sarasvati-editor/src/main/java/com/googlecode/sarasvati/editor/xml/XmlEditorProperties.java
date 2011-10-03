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

package com.googlecode.sarasvati.editor.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "editor-properties")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlEditorProperties
{
  @XmlElement(name = "node")
  protected List<XmlEditorNode>     nodes     = new ArrayList<XmlEditorNode>();

  @XmlElement(name = "external")
  protected List<XmlEditorExternal> externals = new ArrayList<XmlEditorExternal>();

  @XmlAttribute(name="defaultNodeForIncomingArcs")
  protected String defaultNodeForIncomingArcs;

  @XmlAttribute(name="defaultNodeForOutgoingArcs")
  protected String defaultNodeForOutgoingArcs;

  public List<XmlEditorNode> getNodes ()
  {
    return nodes;
  }

  public void setNodes (final List<XmlEditorNode> nodes)
  {
    this.nodes = nodes;
  }

  public List<XmlEditorExternal> getExternals ()
  {
    return externals;
  }

  public void setExternals (final List<XmlEditorExternal> externals)
  {
    this.externals = externals;
  }

  public String getDefaultNodeForIncomingArcs ()
  {
    return defaultNodeForIncomingArcs;
  }

  public void setDefaultNodeForIncomingArcs (final String defaultNodeForIncomingArcs)
  {
    this.defaultNodeForIncomingArcs = defaultNodeForIncomingArcs;
  }

  public String getDefaultNodeForOutgoingArcs ()
  {
    return defaultNodeForOutgoingArcs;
  }

  public void setDefaultNodeForOutgoingArcs (final String defaultNodeForOutgoingArcs)
  {
    this.defaultNodeForOutgoingArcs = defaultNodeForOutgoingArcs;
  }

  @Override
  public String toString ()
  {
    StringBuilder buf = new StringBuilder();
    buf.append( "<editor-properties>\n" );

    for (XmlEditorNode node : nodes)
    {
      buf.append( node );
      buf.append( "\n" );
    }

    for (XmlEditorExternal external : externals)
    {
      buf.append( external );
      buf.append( "\n" );
    }

    buf.append( "</editor-properties>" );
    return buf.toString();
  }
}
