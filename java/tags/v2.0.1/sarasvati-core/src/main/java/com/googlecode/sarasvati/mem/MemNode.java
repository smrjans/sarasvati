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

import java.util.concurrent.atomic.AtomicLong;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.JoinStrategy;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.adapter.NodeAdapterManager;
import com.googlecode.sarasvati.env.ReadEnv;
import com.googlecode.sarasvati.impl.MapEnv;
import com.googlecode.sarasvati.impl.NestedReadEnv;

public class MemNode implements Node, Cloneable
{
  protected static final AtomicLong idGenerator = new AtomicLong();

  protected Graph    graph;
  protected Graph    definingGraph;

  protected long     id;
  protected String   name;
  protected String   type;
  protected JoinType joinType;
  protected String   joinParam;
  protected boolean  isStart;
  protected String   guard;

  protected MemNode     originatingExternalNode;
  protected MemExternal external;

  protected ReadEnv externalEnv;

  public MemNode ()
  {
    /* Default constructor */
  }

  @Override
  public Long getId ()
  {
    return id;
  }

  public void initId ()
  {
    this.id = idGenerator.incrementAndGet();
  }

  @Override
  public void execute (final Engine engine, final NodeToken token)
  {
    engine.complete( token, Arc.DEFAULT_ARC );
  }

  /**
   * Will use the {@link NodeAdapterManager} to produce an adapter.
   * Subclasses may override this behavior.
   *
   * @see Node#getAdaptor(Class)
   */
  @Override public <T> T getAdaptor (final Class<T> clazz)
  {
    return NodeAdapterManager.getAdaptor( this, clazz );
  }

  @Override
  public Graph getGraph ()
  {
    return graph;
  }

  public void setGraph (final Graph graph)
  {
    this.graph = graph;
  }

  public Graph getDefiningGraph ()
  {
    return definingGraph;
  }

  public void setDefiningGraph (final Graph definingGraph)
  {
    this.definingGraph = definingGraph;
  }

  @Override
  public String getGuard ()
  {
    return guard;
  }

  public void setGuard (final String guard)
  {
    this.guard = guard;
  }

  @Override
  public String getName ()
  {
    return name;
  }

  public void setName (final String name)
  {
    this.name = name;
  }

  @Override
  public String getType ()
  {
    return type;
  }

  public void setType (final String type)
  {
    this.type = type;
  }

  @Override
  public GuardResult guard (final Engine engine, final NodeToken token)
  {
    return engine.evaluateGuard( token, guard );
  }

  @Override
  public JoinType getJoinType ()
  {
    return joinType;
  }

  public void setJoinType (final JoinType joinType)
  {
    this.joinType = joinType;
  }

  @Override
  public String getJoinParam ()
  {
    return joinParam;
  }

  public void setJoinParam (final String joinParam)
  {
    this.joinParam = joinParam;
  }

  @Override
  public JoinStrategy getJoinStrategy (final Arc arc)
  {
    return joinType.getJoinStrategy();
  }

  @Override
  public boolean isStart ()
  {
    return isStart;
  }

  public void setStart (final boolean isStart)
  {
    this.isStart = isStart;
  }

  @Override
  public boolean isImportedFromExternal ()
  {
    return external != null;
  }

  /**
   * Does nothing by default. Can be overridden by subclasses.
   * @see Node#backtrack(Engine, NodeToken)
   */
  @Override
  public void backtrack (final Engine engine,
                         final NodeToken token)
  {
    // does nothing by default. Can be overridden by subclasses.
  }

  /**
   * Returns true. Can be overridden by subclasses.
   *
   * @see Node#isBacktrackable(Engine, NodeToken)
   */
  @Override
  public boolean isBacktrackable (final Engine engine,
                                  final NodeToken token)
  {
    return true;
  }

  @Override
  public MemExternal getExternal ()
  {
    return external;
  }

  public void setExternal (final MemExternal external)
  {
    this.external = external;
  }

  @Override
  public MemNode getOriginatingExternalNode ()
  {
    return originatingExternalNode;
  }

  public void setOriginatingExternalNode (final MemNode originatingExternalNode)
  {
    this.originatingExternalNode = originatingExternalNode;
  }

  @Override
  public ReadEnv getExternalEnv ()
  {
    if ( external == null )
    {
      return MapEnv.READONLY_EMPTY_INSTANCE;
    }

    if ( externalEnv == null )
    {
      if ( originatingExternalNode == null )
      {
        externalEnv = external.getEnv();
      }
      else
      {
        externalEnv = new NestedReadEnv( external.getEnv(), originatingExternalNode.getExternalEnv() );
      }
    }

    return externalEnv;
  }

  @Override
  public MemNode clone ()
  {
    try
    {
      return (MemNode)super.clone();
    }
    catch ( CloneNotSupportedException cnse )
    {
      throw new RuntimeException( cnse );
    }
  }
}