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

    Copyright 2009 Paul Lorenz
*/
package com.googlecode.sarasvati.visual.process;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;

public class ProcessTreeArc implements VisualProcessArc
{
  protected ArcToken token;
  protected Arc      arc;
  protected ProcessTreeNode parent;
  protected ProcessTreeNode child;

  public ProcessTreeArc(final ArcToken token, final ProcessTreeNode parent, final ProcessTreeNode child)
  {
    this.token  = token;
    this.parent = parent;
    this.child  = child;
  }

  public ProcessTreeArc(final Arc arc, final ProcessTreeNode parent, final ProcessTreeNode child)
  {
    this.arc    = arc;
    this.parent = parent;
    this.child  = child;
  }

  @Override
  public ArcToken getToken()
  {
    return token;
  }

  @Override
  public Arc getArc ()
  {
    return token == null ? arc : token.getArc();
  }

  public ProcessTreeNode getParent()
  {
    return parent;
  }

  public ProcessTreeNode getChild()
  {
    return child;
  }
}
