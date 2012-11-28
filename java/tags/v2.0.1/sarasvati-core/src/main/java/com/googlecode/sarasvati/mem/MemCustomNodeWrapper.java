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

package com.googlecode.sarasvati.mem;

import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.CustomNodeWrapper;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.NodeToken;

public class MemCustomNodeWrapper extends MemNode implements CustomNodeWrapper
{
  protected CustomNode customNode;

  public MemCustomNodeWrapper (final CustomNode customNode)
  {
    this.customNode = customNode;
    this.customNode.setNodeWrapper( this );
  }

  @Override
  public CustomNode getCustomNode (final Engine engine)
  {
    return customNode;
  }

  @Override
  public void execute (final Engine engine, final NodeToken token)
  {
    getCustomNode( engine ).execute( engine, token );
  }

  @Override
  public void backtrack (final Engine engine, final NodeToken token)
  {
    getCustomNode( engine ).backtrack( engine, token );
  }

  @Override
  public boolean isBacktrackable (final Engine engine, final NodeToken token)
  {
    return getCustomNode( engine ).isBacktrackable( engine, token );
  }

  @Override
  public GuardResult defaultGuard (final Engine engine, final NodeToken token)
  {
    return super.guard( engine, token );
  }

  @Override
  public <T> T getDefaultAdaptor (final Class<T> clazz)
  {
    return super.getAdaptor( clazz );
  }
}