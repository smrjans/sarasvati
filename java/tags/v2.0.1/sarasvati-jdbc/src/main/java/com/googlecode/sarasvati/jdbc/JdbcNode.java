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
package com.googlecode.sarasvati.jdbc;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.External;
import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.JoinStrategy;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.adapter.NodeAdapterManager;
import com.googlecode.sarasvati.env.ReadEnv;
import com.googlecode.sarasvati.impl.MapEnv;

public class JdbcNode implements Node, JdbcObject
{
  protected Long      id;
  protected String    name;
  protected String    type;
  protected String    guard;
  protected boolean   isStart;
  protected JoinType  joinType;
  protected String    joinParam;

  protected JdbcGraph graph;

  public JdbcNode ()
  {
    // default constructor
  }

  @Override
  public Long getId ()
  {
    return id;
  }

  @Override
  public void setId (final Long id)
  {
    this.id = id;
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
  public String getGuard ()
  {
    return guard;
  }

  public void setGuard (final String guard)
  {
    this.guard = guard;
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

  /**
   * @see com.googlecode.sarasvati.Node#getJoinStrategy(com.googlecode.sarasvati.Arc)
   */
  @Override
  public JoinStrategy getJoinStrategy(final Arc arc)
  {
    return joinType.getJoinStrategy();
  }

  @Override
  public JdbcGraph getGraph ()
  {
    return graph;
  }

  public void setGraph (final JdbcGraph graph)
  {
    this.graph = graph;
  }

  @Override
  public External getExternal ()
  {
    return null;
  }

  @Override
  public ReadEnv getExternalEnv ()
  {
    return MapEnv.READONLY_EMPTY_INSTANCE;
  }

  @Override
  public Node getOriginatingExternalNode ()
  {
    return null;
  }

  /**
   * Does nothing by default. Can be overridden by subclasses.
   * @see Node#backtrack(Engine, NodeToken)
   */
  @Override
  public void backtrack (final Engine engine, final NodeToken token)
  {
    // does nothing by default
  }

  /**
   * Returns true. Can be overridden by subclasses.
   *
   * @see Node#isBacktrackable(Engine,NodeToken)
   */
  @Override
  public boolean isBacktrackable (final Engine engine, final NodeToken token)
  {
    return true;
  }

  @Override
  public GuardResult guard (final Engine engine, final NodeToken token)
  {
    return engine.evaluateGuard( token, guard );
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
  @Override
  public <T> T getAdaptor (final Class<T> clazz)
  {
    return NodeAdapterManager.getAdaptor( this, clazz );
  }

  @Override
  public boolean isImportedFromExternal ()
  {
    return false;
  }

  @Override
  public boolean isMutable ()
  {
    return false;
  }

  /**
   * Called after this node is persisted to the database. Allows subclasses
   * to persist additional attributes to the database.
   *
   * @param engine The {@link JdbcEngine} to use to work with the database
   */
  public void afterCreate (final JdbcEngine engine)
  {
    // does nothing by default.
  }

  /**
   * Called after this node is loaded from the database. Allows subclasses
   * to persist additional attributes to the database.
   *
   * @param engine The {@link JdbcEngine} to use to work with the database
   */
  public void afterLoad (final JdbcEngine engine)
  {
    // does nothing by default.
  }
}