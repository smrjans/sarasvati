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
package com.googlecode.sarasvati;

import java.util.Collection;

import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.event.HasEventQueue;
import com.googlecode.sarasvati.load.GraphFactory;

/**
 * A GraphProcess represents a currently executing workflow graph.
 *
 * @author Paul Lorenz
 */
public interface GraphProcess extends HasEventQueue
{
  /**
   * Returns the graph being executed.
   *
   * @return The graph being executed.
   */
  Graph getGraph ();

  /**
   * Returns the environment for the process.
   *
   * @return An Env containing variables defined on this process
   */
  Env getEnv ();

  /**
   * Returns the list of all NodeTokens.
   *
   * @return The list of all NodeTokens.
   */
  Collection<NodeToken> getNodeTokens ();

  /**
   * Returns the list of current ArcTokens.
   *
   * @return The list of current ArcTokens.
   */
  Collection<ArcToken> getActiveArcTokens ();

  /**
   * Returns the list of current NodeTokens.
   *
   * @return The list of current NodeTokens.
   */
  Collection<NodeToken> getActiveNodeTokens ();

  /**
   * Adds a NodeToken to the list of all NodeTokens for this process.
   *
   * @param token The token being added
   */
  void addNodeToken (NodeToken token);

  /**
   * Adds an ArcToken to the list of active ArcTokens. Active ArcTokens
   * have been executed, but are not complete. They are waiting
   * for other ArcTokens to arrive at their target node (which will
   * be a join node).
   *
   * @param token The token being added
   */
  void addActiveArcToken (ArcToken token);

  /**
   * Removes an ArcToken from the list of active ArcTokens.
   *
   * @param token The token being removed.
   */
  void removeActiveArcToken (ArcToken token);

  /**
   * Adds an arc token to the execution queue
   *
   * @param token to enqueue
   */
  void enqueueArcTokenForExecution (ArcToken token);

  /**
   * Returns the next {@link ArcToken} to execute
   *
   * @return The next {@link ArcToken} to execute
   */
  ArcToken dequeueArcTokenForExecution ();

  /**
   * Return true if the arc token queue is empty, false otherwise
   *
   * @return True if the arc token queue is empty
   */
  boolean isArcTokenQueueEmpty ();

  /**
   * Adds a NodeToken to the list of active NodeTokens.
   *
   * @param token The token being added
   */
  void addActiveNodeToken (NodeToken token);

  /**
   * Removes a NodeToken from the list of active NodeTokens.
   *
   * @param token The token being removed.
   */
  void removeActiveNodeToken (NodeToken token);

  /**
   * Returns the current {@link ProcessState}
   *
   * @return The current {@link ProcessState}
   */
  ProcessState getState ();

  /**
   * Sets the current {@link ProcessState}. The state should
   * be set to {@link ProcessState#Created} by {@link GraphFactory#newProcess(Graph)}.
   * It should then be set to {@link ProcessState#Executing} by
   * {@link Engine#startProcess(GraphProcess)}
   *
   * @param state The new {@link ProcessState}
   */
  void setState (ProcessState state);

  /**
   * Returns true if the process state is either {@link ProcessState#PendingCancel} or {@link ProcessState#Canceled}
   *
   * @return True if the process state is either {@link ProcessState#PendingCancel} or {@link ProcessState#Canceled}
   */
  boolean isCanceled ();

  /**
   * Returns true if the process state is either {@link ProcessState#PendingCompletion} or {@link ProcessState#Completed}
   *
   * @return True if the process state is either {@link ProcessState#PendingCompletion} or {@link ProcessState#Completed}
   */
  boolean isComplete ();

  /**
   * Returns true if the process state is {@link ProcessState#Executing}
   *
   * @return True if the process state is {@link ProcessState#Executing}
   */
  boolean isExecuting ();

  /**
   * Return true if the process has any active arc or node tokens. A process
   * with no active tokens should be marked pending complete.
   *
   * @return True if the process has active tokens, false otherwise.
   */
  boolean hasActiveTokens ();

  /**
   * If this a nested process, then this will return the
   * parent {@link NodeToken} and null otherwise.
   *
   * A nested process is one was started from a different process. The process is
   * started when a specific node is executed. The node will be in a wait state
   * while the nested process executes.  When the nested process completes, the
   * node execution will be completed.
   *
   * @return If this is a nested process, this will return the parent {@link NodeToken}, and null otherwise.
   */
  NodeToken getParentToken ();

  /**
   * Returns the collection of NodeTokens which have been created on the given Node.
   *
   * @param node The node whose tokens we are interested in
   * @param engine The engine that may assist in generating the list.
   *
   * @return the collection of NodeTokens which have been created on the given Node.
   */
  Collection<NodeToken> getTokensOnNode (Node node, Engine engine);
}