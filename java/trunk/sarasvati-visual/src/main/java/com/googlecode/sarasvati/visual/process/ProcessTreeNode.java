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
package com.googlecode.sarasvati.visual.process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.visual.common.NodeDrawConfig;

public class ProcessTreeNode implements VisualProcessNode
{
  protected ProcessTreeNode parent;

  protected NodeToken token;
  protected Node      node;
  protected int       depth = -1;
  protected int       index;

  protected int       originX;
  protected int       originY;

  private List<ProcessTreeArc> children = new LinkedList<ProcessTreeArc>();
  private List<ProcessTreeArc> parents  = new LinkedList<ProcessTreeArc>();

  public ProcessTreeNode (final ProcessTreeNode parent, final Node node)
  {
    this.parent = parent;
    this.node = node;
  }

  public void addParent (final ProcessTreeArc parentArc)
  {
    parents.add( parentArc );
  }

  public ProcessTreeNode (final NodeToken token)
  {
    this.token = token;
  }

  public ProcessTreeNode getParent ()
  {
    return parent;
  }

  public NodeToken getParentToken ()
  {
    ProcessTreeNode current = parent;

    while ( current != null )
    {
      if ( current.getToken() == null )
      {
        current = current.getParent();
      }
      else
      {
        return current.getToken();
      }
    }

    return token;
  }

  @Override
  public Node getNode ()
  {
    return token == null ? node : token.getNode();
  }

  @Override
  public NodeToken getToken ()
  {
    return token;
  }

  public List<ProcessTreeArc> getChildren ()
  {
    return children;
  }

  public List<ProcessTreeArc> getParents ()
  {
    return parents;
  }

  public List<ProcessTreeNode> getNodeParents ()
  {
    List<ProcessTreeNode> parentNodes = new ArrayList<ProcessTreeNode>( parents.size() );

    for ( ProcessTreeArc parentArc : parents )
    {
      parentNodes.add( parentArc.getParent() );
    }

    return parentNodes;
  }

  public void addChild (final ProcessTreeArc child)
  {
    children.add( child );
  }

  public int getDepth ()
  {
    return depth;
  }

  public void setDepth (final int depth)
  {
    this.depth = depth;
  }

  public int getIndex()
  {
    return index;
  }

  public void setIndex( final int index )
  {
    this.index = index;
  }

  public void addToLayer (final List<ProcessTreeNode> layer)
  {
    this.index = layer.size();
    layer.add( this );
    recalculateOrigin();
  }

  public boolean isStartTokenNode ()
  {
    return token != null && token.getParentTokens().isEmpty() && token.getNode().isStart();
  }

  public boolean isTokenOnArc (final Arc arc)
  {
    for (ProcessTreeArc ptArc : children )
    {
      if ( ptArc.getArc().equals( arc ) && ptArc.getToken() != null )
      {
        return true;
      }
    }

    return false;
  }

  public boolean isCompletedNodeToken ()
  {
    return token != null && token.isComplete();
  }

  public boolean hasNonCompleteNodeTokenParent ()
  {
    for ( ProcessTreeArc parentArc : parents )
    {
      ProcessTreeNode currentParent = parentArc.getParent();
      if ( currentParent != null && currentParent.getToken() != null && !currentParent.getToken().isComplete() )
      {
        return true;
      }
    }
    return false;
  }

  public boolean hasLowerParent (final ProcessTreeNode selectedParent, final ProcessTree tree)
  {
    for ( ProcessTreeArc parentArc : parents )
    {
      ProcessTreeNode currentParent = parentArc.getParent();
      if ( currentParent == selectedParent || currentParent == this )
      {
        continue;
      }

      if ( ( currentParent.getDepth() > selectedParent.getDepth() ||
             currentParent.getDepth() == -1 ) &&
            !isAncestor( currentParent, tree ) )
      {
        return true;
      }
    }
    return false;
  }

  public boolean isAncestor (final ProcessTreeNode ptNode, final ProcessTree tree)
  {
    Set<ProcessTreeNode> visited = new HashSet<ProcessTreeNode>();

    Queue<ProcessTreeNode> queue = new LinkedList<ProcessTreeNode>();
    queue.add( ptNode );

    while( !queue.isEmpty() )
    {
      for ( ProcessTreeArc currentArc : queue.remove().parents )
      {
        ProcessTreeNode selectedParent = currentArc.getParent();
        if ( visited.contains( selectedParent ) )
        {
          continue;
        }
        visited.add( selectedParent );

        // Ignore back arcs in calculating ancestry
        if ( tree.isBackArc( currentArc.getArc() ) )
        {
          continue;
        }

        if ( selectedParent == this )
        {
          return true;
        }
        queue.add( selectedParent );
      }
    }
    return false;
  }

  public void recalculateOrigin ()
  {
    int xBasis = NodeDrawConfig.isVertical() ? getIndex() : getDepth();
    int yBasis = NodeDrawConfig.isVertical() ? getDepth() : getIndex();

    originX = ((xBasis + 1) * NodeDrawConfig.getHorizontalNodeSpacing()) +
              (xBasis * (NodeDrawConfig.getMaxNodeRadius() << 1)) +
              NodeDrawConfig.getMaxNodeRadius();

    originY = ((yBasis) * NodeDrawConfig.getVerticalNodeSpacing()) +
              (yBasis * (NodeDrawConfig.getMaxNodeRadius() << 1)) +
              NodeDrawConfig.getMaxNodeRadius();
  }

  public int getOriginX ()
  {
    return originX;
  }

  public int getOriginY ()
  {
    return originY;
  }

  public int getOriginX (final int width)
  {
    return originX + (NodeDrawConfig.getMaxNodeRadius() - (width >> 1));
  }

  public int getOriginY (final int height)
  {
    return originY + (NodeDrawConfig.getMaxNodeRadius() - (height >> 1));
  }

  public int getOffset ()
  {
    return NodeDrawConfig.getMaxNodeRadius() >> 1;
  }
}