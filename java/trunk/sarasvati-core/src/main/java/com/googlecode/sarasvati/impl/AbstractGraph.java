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
package com.googlecode.sarasvati.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.util.SvUtil;

/**
 * Provides some base logic for getting input and output arcs
 * for a given node. Works by iterating over the arcs
 * and building the set of associated inputs and outputs. This
 * happens once, the first time a set of input or output arcs
 * are requested.
 *
 * @author Paul Lorenz
 */
public abstract class AbstractGraph implements Graph
{
  protected Map<Node, List<Arc>> inputMap;

  protected Map<Node, List<Arc>> outputMap;

  /**
   * @see Graph#getInputArcs(Node)
   */
  @Override
  public List<Arc> getInputArcs (final Node node)
  {
    if ( inputMap == null )
    {
      initialize();
    }

    List<Arc> result = inputMap.get( node );

    if ( result == null )
    {
      return Collections.emptyList();
    }
    return result;
  }

  /**
   * @see Graph#getInputArcs(Node, String)
   */
  @Override
  public List<Arc> getInputArcs (final Node node, final String arcName)
  {
    List<Arc> arcList = getInputArcs( node );

    if ( arcList.isEmpty() )
    {
      return arcList;
    }

    List<Arc> result = new ArrayList<Arc>( arcList.size() );

    for ( Arc arc : arcList )
    {
      if ( SvUtil.equals( arcName, arc.getName() ) )
      {
        result.add( arc );
      }
    }
    return result;
  }

  /**
   * @see Graph#getOutputArcs(Node)
   */
  @Override
  public List<Arc> getOutputArcs (final Node node)
  {
    if (outputMap == null)
    {
      initialize();
    }

    List<Arc> result = outputMap.get( node );

    if ( result == null )
    {
      return Collections.emptyList();
    }
    return result;
  }

  /**
   * @see Graph#getOutputArcs(Node, String)
   */
  @Override
  public List<Arc> getOutputArcs (final Node node, final String arcName)
  {
    List<Arc> arcList = getOutputArcs( node );

    if ( arcList.isEmpty() )
    {
      return arcList;
    }

    List<Arc> result = new ArrayList<Arc>( arcList.size() );

    for ( Arc arc : arcList )
    {
      if ( SvUtil.equals( arcName, arc.getName() ) )
      {
        result.add( arc );
      }
    }
    return result;
  }

  private void initialize ()
  {
    inputMap  = new HashMap<Node, List<Arc>>();
    outputMap = new HashMap<Node, List<Arc>>();

    for ( Arc arc : getArcs() )
    {
      Node node = arc.getStartNode();
      List<Arc> list = outputMap.get( node );

      if ( list == null )
      {
        list = new LinkedList<Arc>();
        outputMap.put( node, list );
      }

      list.add( arc );

      node = arc.getEndNode();
      list = inputMap.get( node );

      if ( list == null )
      {
        list = new LinkedList<Arc>();
        inputMap.put( node, list );
      }

      list.add( arc );
    }
  }

  /**
   * @see Graph#getStartNodes()
   */
  @Override
  public List<Node> getStartNodes ()
  {
    List<Node> startNodes = new LinkedList<Node>();

    for ( Node node : getNodes() )
    {
      if ( node.isStart() )
      {
        startNodes.add( node );
      }
    }

    return startNodes;
  }
}