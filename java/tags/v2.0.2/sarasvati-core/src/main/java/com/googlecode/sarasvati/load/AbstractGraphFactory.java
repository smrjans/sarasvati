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
package com.googlecode.sarasvati.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.impl.NestedProcessNode;
import com.googlecode.sarasvati.impl.ScriptNode;
import com.googlecode.sarasvati.impl.WaitNode;

public abstract class AbstractGraphFactory implements GraphFactory
{
  static
  {
    DefaultNodeFactory.addGlobalCustomType( "nested", NestedProcessNode.class );
    DefaultNodeFactory.addGlobalCustomType( "script", ScriptNode.class );
    DefaultNodeFactory.addGlobalCustomType( "wait",   WaitNode.class );
  }

  protected Map<String, NodeFactory>  factoryMap = new HashMap<String, NodeFactory>();
  protected DefaultNodeFactory        defaultNodeFactory;

  public AbstractGraphFactory (final Class<? extends Node> defaultClass)
  {
    this.defaultNodeFactory = new DefaultNodeFactory( defaultClass );
  }

  @Override
  public NodeFactory getNodeFactory (final String type)
  {
    NodeFactory nodeFactory = factoryMap.get( type );
    return nodeFactory == null ? defaultNodeFactory : nodeFactory;
  }

  @Override
  public void addType (final String type, final Class<? extends Node> clazz)
  {
    defaultNodeFactory.addType( type, clazz );
  }

  @Override
  public void addGlobalCustomType (final String type, final Class<? extends CustomNode> nodeClass)
  {
    DefaultNodeFactory.addGlobalCustomType( type, nodeClass );
  }

  @Override
  public void addNodeFactory (final String type, final NodeFactory nodeFactory)
  {
    factoryMap.put( type, nodeFactory );
  }

  @Override
  public NodeToken newNodeToken (final GraphProcess process,
                                 final Node node,
                                 final List<ArcToken> parents )
  {
    return newNodeToken( process, node, ExecutionType.Forward, parents, null );
  }
}