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
package com.googlecode.sarasvati;

import com.googlecode.sarasvati.adapter.Adaptable;
import com.googlecode.sarasvati.env.ReadEnv;

/**
 * A node corresponds to an action in a process definition.
 * It may perform some function. In some cases it may not
 * complete immediately, but enter a wait state. At some
 * point it will return and the process can then continue execution.
 *
 * <br/>
 *
 * Every node can have a guard associated with it. This guard
 * will determine if an incoming token is accepted (and the
 * node functionality executed), or discarded, or if the token
 * is passed through without executing the node.
 *
 * <br/>
 *
 * Every node may have multiple incoming arcs. If isJoin()
 * return true, then tokens coming in will wait for tokens
 * to be present on all arcs with the same label.
 *
 * <br/>
 *
 * Every node may also have multiple outgoing arcs. When
 * a node token is completed, it may pick which arcs to exit
 * on by passing an arc label. Every arc with the given label
 * will have an arc token places on it.
 *
 * @author Paul Lorenz
 */
public interface Node extends Adaptable
{
  /**
   * Returns the node's unique id. The id must be unique to the Graph, but may be
   * globally unique.
   *
   * @return The node's unique id.
   */
  Long getId ();

  /**
   * Returns the node name. Every node must have a name which
   * is unique in it's process definition;
   *
   * @return The node name.
   */
  String getName ();

  /**
   * Every node has a type. The default is 'node'. Nodes of
   * different types may have different (user defined) behavior
   * when the execute method is invoked.
   *
   * @return The type
   */
  String getType ();

  /**
   * Returns the {@link JoinType} of the node.
   *
   * @return The {@link JoinType} of the node.
   */
  JoinType getJoinType ();

  /**
   * Each node may specify a parameter to be used by the join strategy.
   * For example, it can be used to tell a token set join which token
   * set to join on, by name. For a custom join, it may indicate the
   * join type, or provide some other information to the join.
   * <p>
   * May be null.
   *
   * @return The join parameter.
   */
  String getJoinParam ();

  /**
   * Returns the {@link JoinStrategy} to be used when an
   * {@link ArcToken} arrives at this Node.
   *
   * @param arc The arc of the incoming arc token. Allows
   *              different join strategies for different
   *              arcs.
   *
   * @return The {@link JoinStrategy} to be used when an
   *         {@link ArcToken} arrives at this Node.
   */
  JoinStrategy getJoinStrategy (Arc arc);

  /**
   * Returns true if this node is a start node. Start nodes
   * will have a token placed in them when the process is
   * started
   *
   * @return True if the node is a start node.
   */
  boolean isStart ();

  /**
   * Every node may have a guard associated with it. The guard
   * may be blank or null, which by default, will be treated as
   * an Accept. If it is not null or blank, the guard method
   * may interpret it in some fashion. It may be a GuardLang
   * statement, it could be some other script language or it
   * could be interpreted in some other way entirely.
   *
   * @return The guard
   */
  String getGuard ();

  /**
   * Returns the graph that this node belongs to.
   *
   * @return The associated Graph
   */
  Graph getGraph ();

  /**
   * Returns true if this node was imported from an external process definition,
   * false otherwise.
   *
   * @return True if this node was imported from an external process definition.
   */
  boolean isImportedFromExternal ();

  /**
   * Returns true if the specific execution of this Node by the given
   * NodeToken can be backtracked.
   *
   * @param engine The engine doing the backtracking
   * @param token The token being backtracked
   * @return True if the node can be backtracked, false otherwise.
   */
  boolean isBacktrackable (Engine engine, NodeToken token);

  /**
   * Does whatever work is necessary to backtrack this execution. For example,
   * a task node may send a notification that the task has been backtracked.
   *
   * @param engine The engine doing the backtracking
   * @param token The specific token being backtracked.
   */
  void backtrack (Engine engine, NodeToken token);

  /**
   * When a NodeToken is created, the associated Node will not
   * automatically be executed. First, the guard function is called,
   * which will indicate which action should be taken. The possible
   * actions are:
   *
   * <ul>
   *    <li> {@link GuardAction#DiscardToken}: The token will be discard.
   *    <li> {@link GuardAction#SkipNode}: Skip the node.
   *         The {@link Node#execute(Engine, NodeToken)} method will not
   *         be called. The {@link GuardResult} will indicate which
   *         arc(s) to leave on.
   *    <li> {@link GuardAction#AcceptToken}: Accept the token. The execute function of the
   *         Node will be called.
   *
   * </ul>
   *
   * @param engine The engine being used to execute the process
   * @param token  The node token which is currently entering a node
   *
   * @return A GuardResult
   */
  GuardResult guard (Engine engine, NodeToken token);

  /**
   * Performs Node specific logic. Either from the execute method,
   * or later from outside, the
   * {@link Engine#complete(NodeToken, String)} method
   * must be called to continue executing the {@link GraphProcess}.
   *
   * @param engine The {@link Engine} which is performing the execution.
   * @param token The {@link NodeToken} which is currently executing in this node.
   */
  void execute (Engine engine, NodeToken token);

  /**
   * If the node is defined in an external, returns the external and null otherwise.
   *
   * @return If the node is defined in an external, returns the external and null otherwise.
   */
  External getExternal ();

  /**
   * If a node is defined in an external, this will return the node as
   * defined in the external graph. Otherwise it will return null. Generally,
   * this is only useful if you wish to examine the external associated with the
   * originating node.
   * <p>
   * Note: If the originating node is the original node, it will not have an external
   *       associated with it. Since this method exists to allow access to multiple
   *       levels of external, this method may return null instead of the original
   *       node.
   *
   * <p>
   * For example, given the following three graphs, where graph II uses graph I and
   * graph III uses graph II, you could look up different values defined in the externals.
   *
   * <pre>
   *  <process-definition name="Graph I">
   *    <node name="A"/>
   *  </process-definition>
   *
   *  <process-definition name="Graph II">
   *    <external name="G1" processDefinition="Graph I">
   *      <custom>
   *        <foo>bar</foo>
   *        <hello>world</hello>
   *      </custom>
   *    </external>
   *
   *    <node name="B">
   *      <arc external="G1" to="A"/>
   *    </node>
   *  </process-definition>
   *
   *  <process-definition name="Graph III">
   *    <external name="G2" processDefinition="Graph II">
   *      <custom>
   *        <foo>baz</foo>
   *      </custom>
   *    </external>
   *
   *    <node name="C">
   *      <arc external="G2" to="B"/>
   *    </node>
   *  </process-definition>
   *
   * If you get node A from graph III, you can observe the following:
   *
   *   Node nodeA = ...;
   *   nodeA.getExternalEnv().getAttribute( "foo" ) // returns "baz"
   *   nodeA.getExternalEnv().getAttribute( "hello" ) // returns "world"
   *   nodeA.getExternal().getEnv().getAttribute( "foo" ) // returns "baz"
   *   nodeA.getExternal().getEnv().getAttribute( "hello" ) // returns null
   *   nodeA.getOriginatingExternalNode().getExternal().getEnv().getAttribute( "foo" ) // returns "bar"
   *   nodeA.getOriginatingExternalNode().getExternal().getEnv().getAttribute( "hello" ) // returns "world"
   * </pre>
   *
   * @return If node is an external node, returns the node as defined in the external, otherwise returns null.
   */
  Node getOriginatingExternalNode ();

  /**
   * Returns a read-only environment containing all attributes defined for
   * all associated externals. See {@link Node#getOriginatingExternalNode()}
   * for examples usage.
   *
   * @return A read-only environment containing all attributes defined for
   *         associated externals.
   */
  ReadEnv getExternalEnv ();
}
