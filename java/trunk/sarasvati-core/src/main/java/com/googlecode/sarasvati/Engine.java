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

import java.util.List;
import java.util.Map;

import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.event.EventActionType;
import com.googlecode.sarasvati.event.EventActions;
import com.googlecode.sarasvati.event.ExecutionEvent;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.event.ExecutionListener;
import com.googlecode.sarasvati.impl.AcceptTokenGuardResult;
import com.googlecode.sarasvati.impl.DelayUntilGuardResult;
import com.googlecode.sarasvati.join.lang.JoinLangEnv;
import com.googlecode.sarasvati.load.GraphFactory;
import com.googlecode.sarasvati.load.GraphLoader;
import com.googlecode.sarasvati.load.GraphRepository;
import com.googlecode.sarasvati.load.GraphValidator;
import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.script.ScriptEnv;


/**
 * An {@link Engine} executes a process. A {@link Graph} specifies
 * how it should be executed and a {@link GraphProcess} tracks the current state
 * of execution. But it is an Engine which creates instances of {@link ArcToken},
 * {@link NodeToken} and {@link GraphProcess} and which invokes
 * {@link Node#guard(Engine, NodeToken)} and {@link Node#execute(Engine, NodeToken)}.
 * <p>
 * Unless an Engine implementation states otherwise, Engine instances should not
 * be considered thread-safe. A new Engine instance should be created for
 * each thread that needs one.
 *
 * @author Paul Lorenz
 */
public interface Engine
{
  /**
   * Starts an instance of the latest graph with the given name.
   * Is the equivalent of doing
   * <pre>
   *  Graph graph = engine.getRepository().getLatestGraph( graphName );
   *  GraphProcess process = engine.startProcess( graph );
   * </pre>
   *
   * If no process definition exists for the given name, a {@link SarasvatiException}
   * will be thrown.
   *
   * @param graphName The name of the graph to execute.
   * @throws SarasvatiException If no process definition is defined for that name.
   *
   * @return The new GraphProcess.
   */
  GraphProcess startProcess (String graphName);

  /**
   * Starts an instance of the latest graph with the given name.
   * Is the equivalent of doing
   * <pre>
   *  Graph graph = engine.getRepository().getLatestGraph( graphName );
   *  GraphProcess process = engine.startProcess( graph, initialEnv );
   * </pre>
   *
   * If no process definition exists for the given name, a {@link SarasvatiException}
   * will be thrown.
   *
   * @param graphName The name of the graph to execute.
   * @param initialEnv A starting environment for the process. All attributes from
   *                   the given environment will be copied into the process environment
   *                   before execution begins.
   * @throws SarasvatiException If no process definition is defined for that name.
   *
   * @return The new GraphProcess.
   */
  GraphProcess startProcess (String graphName, Env initialEnv);

  /**
   * Given a {@link Graph}, creates a new {@link GraphProcess} executing that graph.
   * A {@link NodeToken} will be generated on each start nodes (determined by
   * {@link Graph#getStartNodes()}), and these NodeTokens will be executed.
   * If the graph does not contain Nodes which go into a wait state, the
   * {@link GraphProcess} returned will be completed.
   *
   * @param graph The {@link Graph} to execute.
   * @return A {@link GraphProcess} executing the given {@link Graph}.
   */
  GraphProcess startProcess (Graph graph);

  /**
   * Given a {@link Graph}, creates a new {@link GraphProcess} executing that graph.
   * A {@link NodeToken} will be generated on each start nodes (determined by
   * {@link Graph#getStartNodes()}), and these NodeTokens will be executed.
   * If the graph does not contain Nodes which go into a wait state, the
   * {@link GraphProcess} returned will be completed.
   * <p>
   * Before the process is started, the given initial environment will be copied
   * into that of the new process. Nulls passed in will be ignored.
   *
   * @param graph The {@link Graph} to execute.
   * @param initialEnv A starting environment for the process. All attributes from
   *                   the given environment will be copied into the process environment
   *                   before execution begins.
   * @return A {@link GraphProcess} executing the given {@link Graph}.
   */
  GraphProcess startProcess (Graph graph, Env initialEnv);

  /**
   * Sometimes it is desirable to separate process creation from
   * starting execution of the process. For example, one may wish
   * to set some variables into the process environment before
   * starting execution.
   *
   * startProcess will generate a new {@link NodeToken} on each
   * start node contained in the given process.
   *
   * @param process The process on which to begin execution.
   */
  void startProcess (GraphProcess process);

  /**
   * Cancels the given process. The process state is set to {@link ProcessState#PendingCancel}.
   *
   * @param process The process to cancel
   */
  void cancelProcess (GraphProcess process);

  /**
   * Called by the engine when the process is detected to be completed.
   * It will set the state to {@link ProcessState#Completed} and perform
   * whatever cleanup is required.
   * <p>
   * If an {@link ExecutionListener} returns an {@link EventActions} object
   * specifying {@link EventActionType#DELAY_PROCESS_FINALIZE_COMPLETE}, then
   * this method will *not* be called, and be manually invoked from user logic
   * <p>
   * If this process is a nested process, at this point the containing
   * token will be completed.
   *
   * @param process The process being completed.
   */
  void finalizeComplete (GraphProcess process);

  /**
   * Called by the engine when a process is cancelled, via {@link Engine#cancelProcess(GraphProcess)}.
   * It will set the state to {@link ProcessState#Canceled} and perform whatever
   * cleanup is required.
   * <p>
   * If an {@link ExecutionListener} returns an {@link EventActions} object
   * specifying {@link EventActionType#DELAY_PROCESS_FINALIZE_CANCEL}, then
   * this method will *not* be called, and be manually invoked from user logic
   * <p>
   * @param process The process being canceled.
   */
  void finalizeCancel (GraphProcess process);

  /**
   * Continues execution of a process in a wait state.
   * If a call to {@link Node#execute(Engine, NodeToken)} does not contain a
   * call to {@link Engine#complete(NodeToken, String)}, then execution
   * of the graph will halt at that point. This is generally referred to as a wait
   * state. It may happen, for example, if the action represented by that node
   * must be done by a human or some external system.
   *
   * <br/>
   *
   * When the external system has determined that the {@link Node} has completed its
   * work, it should invoke this method to continue executing the process.
   *
   * <br/>
   *
   * If the token belongs to a process which is _not_ in state {@link ProcessState#Executing}
   * this call will return immediately.
   *
   * @param token   The {@link NodeToken} to resume execution on
   * @param arcName The name of the {@link Arc} (or arcs, as more than one {@link Arc} can
   *                have the same name) to generate ArcTokens on.
   *
   */
  void complete (NodeToken token, String arcName);

  /**
   * Continues execution of a process in a wait state.
   * If a call to {@link Node#execute(Engine, NodeToken)} does not contain a
   * call to {@link Engine#complete(NodeToken, String)}, then execution
   * of the graph will halt at that point. This is generally referred to as a wait
   * state. It may happen, for example, if the action represented by that node
   * must be done by a human or some external system.
   *
   * <br/>
   *
   * When the external system has determined that the {@link Node} has completed its
   * work, it should invoke this method to continue executing the process.
   *
   * <br/>
   *
   * If the token belongs to a process which is _not_ in state {@link ProcessState#Executing}
   * this call will return immediately.
   *
   * @param token    The {@link NodeToken} to resume execution on
   * @param arcNames The names of the {@link Arc} to generate ArcTokens on. Multiple arcs may
   *                 have the same name, this will execute on all arcs that match the given
   *                 set of names.
   */
  void completeMany (NodeToken token, String...arcNames);

  /**
   * Marks the given node token completed and generates the next set of arc tokens.
   * However, these arc tokens will not be processed. Execution may be
   * continued later with a call to {@link Engine#executeQueuedArcTokens(GraphProcess)}.
   *
   * @param token   The token to mark completed
   * @param arcName The name of the {@link Arc} (or arcs, as more than one {@link Arc} can
   *                have the same name) to generate ArcTokens on.
   */
  void completeAsynchronous (NodeToken token, String arcName);

  /**
   * Marks the given node token completed and generates the next set of arc tokens.
   * However, these arc tokens will not be processed. Execution may be
   * continued later with a call to {@link Engine#executeQueuedArcTokens(GraphProcess)}.
   *
   * @param token   The token to mark completed
   * @param arcNames The names of the {@link Arc} to generate ArcTokens on. Multiple arcs may
   *                 have the same name, this will execute on all arcs that match the given
   *                 set of names.
   */
  void completeManyAsynchronous (NodeToken token, String...arcNames);

  /**
   * Marks the given node token, creates new token set and generates the next set
   * of arc tokens as members of that new token set.
   *
   * @param token The node token to complete
   * @param arcName The name of the {@link Arc} (or arcs, as more than one {@link Arc} can
   *                have the same name) to generate ArcTokens on.
   * @param tokenSetName The token set name
   * @param numberOfTokens The number of tokens to generate on each arc
   * @param asynchronous If true, the engine will return after creating the arc tokens.
   *                     If false, the new arc tokens will be processed immediately.
   * @param initialEnv The initial environment for the new token set. May be null.
   * @param initialMemberEnv The initial environment for the new token set members. May be null.
   */
  void completeWithNewTokenSet (NodeToken token,
                                String arcName,
                                String tokenSetName,
                                int numberOfTokens,
                                boolean asynchronous,
                                Env initialEnv,
                                Map<String, List<?>> initialMemberEnv );

  /**
   * Reevaluates a node token which was previously delayed by a guard result of
   * {@link DelayUntilGuardResult}.
   *
   * @param token The token to reevaluated.
   */
  void reevaluateDelayedToken(final NodeToken token);

  /**
   * If this process has any {@link ArcToken}s queued for execution, this method
   * will execute them.
   *
   * @param process The process whose queued arc tokens to queue
   */
  void executeQueuedArcTokens (GraphProcess process);

  /**
   * Returns an appropriate {@link GraphRepository} for this {@link Engine}. Subclasses
   * may override this to provide custom behavior.
   *
   * @return An appropriate {@link GraphRepository} for this {@link Engine}
   */
  GraphRepository<? extends Graph> getRepository ();

  /**
   * Returns an appropriate {@link GraphFactory} for this {@link Engine}. Subclasses
   * may override this provide customer behavior.
   *
   * @return A {@link GraphFactory} which will generate the appropriate types for this {@link Engine}.
   */
  GraphFactory getFactory ();

  /**
   * Returns an appropriate {@link GraphLoader} for this {@link Engine}. Subclasses
   * may override this provide customer behavior.
   * <p>
   * Equivalent to getLoader( null )
   * <p>
   *
   * @return A {@link GraphLoader} which, by default, will use the factory and repository from this engine.
   */
  GraphLoader<? extends Graph> getLoader ();

  /**
   * Returns an appropriate {@link GraphLoader} for this {@link Engine}. Subclasses
   * may override this provide customer behavior.
   *
   * @return A {@link GraphLoader} which, by default, will use the factory and repository from this engine.
   */
  GraphLoader<? extends Graph> getLoader (GraphValidator validator);

  /**
   * Adds the type to the {@link GraphFactory} for this engine. Specifies
   * what class will be used for a given node type, when loading process
   * definitions from XML file.
   *
   * @param type The type identifier, as used in the process definition file
   * @param nodeClass The node class which will be instantiated for this type
   */
  void addNodeType (String type, Class<? extends Node> nodeClass );

  /**
   * Adds the type to the {@link GraphFactory} for this engine. Specifies
   * what class will be used for a given node type, when loading process
   * definitions from XML file.
   * <p>
   * Adds a class for a custom node type globally, for all GraphFactory instances.
   * Only custom types can have global instances, since they are backend agnostic.
   *
   * @param type The type identifier, as used in the process definition file
   * @param nodeClass The custom node class which will be instantiated for this type
   */
  void addGlobalCustomNodeType (String type, Class<? extends CustomNode> nodeClass );

  /**
   * This will send the given event to listeners who have registered for
   * events on all processes and to listeners who have registered for events
   * on the process that originated this event.
   *
   * @param event The event to send to all interested listeners.
   * @return EventActions Listeners may return EventActions which may influence
   *         execution by, for example, delaying further execution.
   */
  EventActions fireEvent (ExecutionEvent event);

  /**
   * Adds a listener of the given type for the given event types for all processes.
   * It is not added to each process individually, but rather added to a global set
   * of listeners. Global generally means global for the application scope of this
   * engine. The application scope is just a string name, specified in the engine
   * constructor. If the default constructor is used, the default application
   * scope is used, which is the empty string.
   *
   * <br/>
   *
   * {@link ExecutionListener} Global execution listeners must be thread safe. Because
   * listeners are specified by type, they must have a default constructor and be
   * instantiatable by a call to listenerClass.newInstance().
   *
   * <br/>
   * Global execution listeners are specified by class for consistency with process
   * level execution listeners, which can be persisted in a database by class name.
   *
   * @param listenerClass The listener type to be added
   * @param eventTypes The event types to be notified for. If no types are specified, the listener is added for all event types.
   */
  void addExecutionListener (Class<? extends ExecutionListener> listenerClass, ExecutionEventType...eventTypes);

  /**
   * Adds a listener for the given event types for the given process.
   *
   * <br/>
   *
   * {@link ExecutionListener} Execution listeners must be thread safe. Because
   * listeners are specified by type, they must have a default constructor and be
   * instantiatable by a call to listenerClass.newInstance().
   *
   * <br/>
   * Process level execution listeners are specified by class because they may be
   * stored in the database as class names. This is so they can be reinstantiated
   * after a JVM restart.
   *
   *
   * @param process The process to add the listener for, or null for all processes
   * @param listenerClass The listener type to be added.
   * @param eventTypes The event types to be notified for. If no listener types are specified, the listener is added for all event types.
   */
  void addExecutionListener (GraphProcess process, Class<? extends ExecutionListener> listenerClass, ExecutionEventType...eventTypes);

  /**
   * Will remove the given listener type from the set of global listeners. If no event types are specified,
   * the listener will be removed for all event types. Otherwise it will be removed for only the
   * specified event types.
   *
   *
   * @param listenerClass The type of listener to remove
   * @param eventTypes The set of event types to remove the listener for, or none to remove for all
   */
  void removeExecutionListener (Class<? extends ExecutionListener> listenerClass, ExecutionEventType...eventTypes);

  /**
   * Will remove the listener from the given proces. If no event types are specified, the listener
   * will be removed for all event types. Otherwise it will be removed for only the specified event types.
   *
   * <br/>
   * The listener doesn't need to match exactly. All listeners of this type will be matched. What matches
   * types is determined by the implementation, but usually it means same class.
   *
   *
   * @param process The process to remove the listener from, or null to remove from the global listener set
   * @param listenerClass The type of listener to remove
   * @param eventTypes The set of event types to remove the listener for, or none to remove for all
   */
  void removeExecutionListener (GraphProcess process, Class<? extends ExecutionListener> listenerClass, ExecutionEventType...eventTypes);

  /**
   * Adds whatever variables of interest to the script environment. May be overridden
   * by subclasses. By default this will setup two variables:
   * <br/>
   * <ul>
   *    <li> engine - This engine
   *    <li> token  - The given NodeToken
   * </ul>
   *
   * @param env The script environment to add variables to
   * @param token The NodeToken which is currently being executed
   */
  void setupScriptEnv (ScriptEnv env, NodeToken token);

  /**
   * Creates a {@link RubricEnv} to be used to evaluate a Rubric statement
   * defining a guard for the given {@link NodeToken}.
   *
   * @param token The token which will provide some of the state for the RubricEnv
   * @return A RubricEnv for this engine and the given NodeToken.
   */
  RubricEnv newRubricEnv (NodeToken token);

  /**
   * Creates a {@link JoinLangEnv} to be used to evaluate a JoinLang statement
   * controlling if the given {@link ArcToken} satisfies a join.
   *
   * @param token The token which will provide some of the state for the JoinLangEnv
   * @return A JoinLangEnv for this engine and the given {@link ArcToken}.
   */
  JoinLangEnv newJoinLangEnv (ArcToken token);

  /**
   * Nodes, by default, will pass off guard evaluation to the Engine. This allows
   * engine subclasses to easily override the default behavior and use a rules
   * engine or scripting language for guards.
   *
   * @param token The NodeToken for which the guard is being evaluated.
   * @param guard The guard statement to be evaluated. Maybe blank or null, which
   *              by convention should cause {@link AcceptTokenGuardResult}
   *              to be returned.
   *
   * @return The response based on the guard.
   */
  GuardResult evaluateGuard (NodeToken token, String guard);

  /**
   * Allows retrying an arc token, to see if the conditions have changed and 
   * its execution can now be completed.
   * 
   * @param token The token to retry
   * @return The resulting join action
   */
  JoinAction retryIncompleteArcToken (final ArcToken token);
  

  /**
   * Since an Engine can have state specific to the currently executing process,
   * there are times we want to create a new engine (such as when executing a
   * nested process). If this engine is being created to execute a nested process,
   * the forNested flag should be set to true, so that we track the current engine
   * as the parent.
   *
   * @param forNested Indicates whether this new engine is being created to track
   *        execution of a nested process.
   * @return A copy of the current engine which can be used to execute a nested process
   */
  Engine newEngine (boolean forNested);

  /**
   * If this engine was created to execute a nested process, it will remember the engine
   * which created it.
   *
   * @return The parent engine.
   */
  Engine getParentEngine ();

  /**
   * Backtracks execution to the point where the given node token was active. The token
   * must be complete and must not have been backtracked before. If it's not complete,
   * there isn't any point in backtracking to it. If it has already been backtracked,
   * the execution has either returned to a point previous to that token, or there is
   * a newer, non-backtracked token at that node now.
   *
   * @param token The destination token to backtrack to.
   */
  void backtrack (NodeToken token);

  /**
   * Returns the service used to scheudle re-evaluation of delayed tokens.
   *
   * @return the service used to scheudle re-evaluation of delayed tokens.
   */
  DelayedTokenScheduler getDelayedTokenScheduler();
}