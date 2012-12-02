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
package com.googlecode.sarasvati.jdbc.dialect;

import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.jdbc.JdbcArc;
import com.googlecode.sarasvati.jdbc.JdbcArcToken;
import com.googlecode.sarasvati.jdbc.JdbcEngine;
import com.googlecode.sarasvati.jdbc.JdbcGraph;
import com.googlecode.sarasvati.jdbc.JdbcGraphProcess;
import com.googlecode.sarasvati.jdbc.JdbcNode;
import com.googlecode.sarasvati.jdbc.JdbcNodeRef;
import com.googlecode.sarasvati.jdbc.JdbcNodeToken;
import com.googlecode.sarasvati.jdbc.JdbcPropertyNode;
import com.googlecode.sarasvati.jdbc.action.DatabaseAction;
import com.googlecode.sarasvati.jdbc.action.DatabaseLoadAction;

/**
 * Interface to perform database-specific logic, such as
 * inserts, updates, selects and deletes.
 *
 * @author Paul Lorenz
 */
public interface DatabaseDialect
{
  /**
   * Return a {@link DatabaseAction} to insert a new graph into the database.
   *
   * @param graph The graph to insert
   *
   * @return The insert action.
   */
  DatabaseAction newGraphInsertAction (JdbcGraph graph);

  /**
   * Return a {@link DatabaseAction} to insert a new process into the database.
   *
   * @param process The graph process to insert
   *
   * @return The insert action.
   */
  DatabaseAction newProcessInsertAction (JdbcGraphProcess process);

  /**
   * Return a {@link DatabaseAction} to insert a new node into the database.
   *
   * @param node The node to insert
   *
   * @return The insert action
   */
  DatabaseAction newNodeInsertAction (JdbcNode node);

  /**
   * Returns a {@link DatabaseAction} to insert a new node ref into the database.
   *
   * @param nodeRef The node ref to insert
   *
   * @return The insert action
   */
  DatabaseAction newNodeRefInsertAction (JdbcNodeRef nodeRef);

  /**
   * Returns a {@link DatabaseAction} to insert a new arc into the database.
   *
   * @param arc The arc to insert
   *
   * @return The insert action
   */
  DatabaseAction newArcInsertAction (JdbcArc arc);

  /**
   * Returns a {@link DatabaseAction} to insert a new node token into the database.
   *
   * @param arc The node token to insert
   *
   * @return The insert action
   */
  DatabaseAction newNodeTokenInsertAction (JdbcNodeToken nodeToken);

  /**
   * Returns a {@link DatabaseAction} to insert a new arc token into the database.
   *
   * @param arc The arc token to insert
   *
   * @return The insert action
   */
  DatabaseAction newArcTokenInsertAction (JdbcArcToken arcToken);

  /**
   * Returns a {@link DatabaseAction} to insert a node property into the database.
   *
   * @param node The node that the property is tied to
   * @param key The property key
   * @param value The property value
   *
   * @return The insert action
   */
  DatabaseAction newNodePropertyInsertAction (JdbcNode node, String key, String value);

  /**
   * Returns a {@link DatabaseAction} to load the node's associated
   * properties.
   *
   * @param node The {@link JdbcPropertyNode} whose properties need to be loaded.
   *
   * @return The load action
   */
  DatabaseAction newNodePropertiesLoadAction (JdbcPropertyNode node);

  /**
   * Returns a {@link DatabaseLoadAction} which will load all graphs from the database.
   *
   * @return The load action
   */
  DatabaseLoadAction<JdbcGraph> newGraphLoadAction ();

  /**
   * Returns a {@link DatabaseLoadAction} which will load all graphs with the given name.
   *
   * @param name The name of the graphs to load
   *
   * @return The load action
   */
  DatabaseLoadAction<JdbcGraph> newGraphByNameLoadAction (String name);

  /**
   * Returns a {@link DatabaseLoadAction} which will load the latest graph with the given name.
   *
   * @param name The name of the graph to load
   *
   * @return The load action
   */
  DatabaseLoadAction<JdbcGraph> newLatestGraphByNameLoadAction (String name);

  /**
   * Returns a {@link DatabaseLoadAction} which will load the graph with the given id.
   *
   * @param graphId The id of the graph to load
   *
   * @return The load action
   */
  DatabaseLoadAction<JdbcGraph> newGraphByIdLoadAction (long graphId);

  /**
   * Returns a {@link DatabaseLoadAction} which will load nodes into the given graph.
   *
   * @param graph The graph whose nodes are to be loaded.
   * @param engine The engine to use to help instantiate nodes
   *
   * @return The load action
   */
  DatabaseLoadAction<JdbcNodeRef> newNodeLoadAction (JdbcGraph graph, JdbcEngine engine);

  /**
   * Returns a {@link DatabaseLoadAction} which will load arcs into the given graph.
   *
   * @param graph The graph whose arcs are to be loaded.
   *
   * @return The load action
   */
  DatabaseLoadAction<JdbcArc> newArcLoadAction (JdbcGraph graph);

  /**
   * Returns a {@link DatabaseLoadAction} which will load the given {@link GraphProcess}.
   *
   * @param processId The id of the process to load
   * @param engine     The engine to use to load the graph and other constituent parts of the process
   * @return The load action
   */
  DatabaseLoadAction<JdbcGraphProcess> newProcessLoadAction (long processId, JdbcEngine engine);

  /**
   * Allows user data to be stored in the Dialect. This can be useful
   * if the user wishes to store an ActionFactory with the dialect,
   * so as to be available from the {@link JdbcNode#afterCreate(JdbcEngine)}
   * and {@link JdbcNode#afterLoad(JdbcEngine)} methods.
   * @param <T> The type of data to store
   *
   * @param key The class of the data to store
   * @param value The data to store
   */
  <T> void setUserData (Class<T> key, T value);

  /**
   * Allows retrieval of user database previously stored in this dialect
   * using {@link DatabaseDialect#setUserData(Class, Object)}.
   *
   * @see DatabaseDialect#setUserData(Class, Object)
   *
   * @param <T> The type of the user data to return
   *
   * @param key The class of the user data to return
   *
   * @return The user data for that type that was previous stored in this dialect.
   */
  <T> T getUserData (Class<T> key);
}