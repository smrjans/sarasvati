[<-- Prev: Why graph based workflow?](GraphBasedWorkflow.md) ... [Next: Using Sarasvati -->](UsingSarasvati.md)

**NOTE: This is for version 1.0.0-rc2 (and previous)**

# Core Concepts #

## Contents ##
  * [Introduction](#Introduction.md)
  * [Definitions](#Definitions.md)
  * [Graph Execution Explained](#Sarasvati_Graph_Execution.md)
    * [Legend](#Legend.md)
    * [Single Node](#Single_Node.md)
    * [Two Nodes](#Two_Nodes.md)
    * [Split and Join - With Wait States](#Split_and_Join_with_Wait_States.md)
    * [Multithreading](#Multithreading.md)
    * [Split and Join - Without Wait States](#Split_and_Join_without_Wait_States.md)
    * [Flow Control with Guards using Skip](#Flow_Control_with_Guards_using_Skip.md)
    * [Flow Control with Guards using Discard](#Flow_Control_with_Guards_using_Discard.md)
    * [Flow Control with Guards using Named Arcs](#Flow_Control_with_Guards_using_Named_Arcs.md)
    * [Flow Control from Node Completion using Named Arcs](#Flow_Control_from_Node_Completion_using_Named_Arcs.md)
  * [Graph Composition and Nested Processes](#Graph_Composition_and_Nested_Processes.md)
    * [Graph Composition Example One](#Graph_Composition_Example_One.md)
    * [Graph Composition Example Two](#Graph_Composition_Example_Two.md)
    * [Nested Processes Example](#Nested_Processes_Example.md)
  * [Execution Environment](#Execution_Environment.md)
    * [Process Attributes](#Process_Attributes.md)
    * [Token Attributes](#Token_Attributes.md)

## Introduction ##
Graph based workflow/business process management engines are common. They have areas of commonality, but they also vary greatly in concept and implementation. For example, there are differences in how concurrency and synchronization are modeled and in how modularity and re-use are promoted.

We begin with the some definitions, move on to features likely to be common across most engines, then explain Sarasvati specifics.

## Definitions ##

Graphs come with a set of common terms. To begin with, a graph is made up of a set of things, hereafter referred to as **nodes** and a set of connections between _nodes_, know as **arcs**.

  * **Graph** - A set of nodes, with a set of arcs connecting the nodes. While graphs have a wider applicability, graph here is synonymous with process definition.
    * Also know as: Process Definition, Network, Workflow

  * **Node** - An element of a graph. A node corresponds roughly to an action as defined [previously](Workflow.md). Nodes can be thought of as pieces of code, waiting to be executed when their turn comes.
    * Also known as: Vertex, Place

  * **Arc** - A directed connection between two nodes. _Directed_ means that arcs have a start node and an end node. In some cases, an arc may have a label, or name.
    * Also know as: Edge, Transition

  * **Predecessor** - If two nodes are connected by an arc then the node at the beginning of the arc is the _predecessor_ of the node at the end of the arc. How nodes are connected by arcs defines the order of execution. Generally a node may not execute until at least one, potentially many or all, of its predecessors have executed. Nodes may have many arcs exiting and entering them.

These definitions cover the parts of a process definition. However, they don't cover how that process definition is actually executed. When a process definition gets executed, the execution is called a **process**. Somehow, a process must track which nodes are being executed. This is generally accomplished by placing markers called **token** on the active nodes.

  * **Process** - An executing process definition. A process definition may have have zero, one or many processes executing at any given time.
    * Also known as: Case, Instance, Workflow

  * **Token** - The set of active tokens marks the current process state. Tokens generally point to a node which is currently executing. Tokens sometimes have associated state, which is a way of passing data from node to node.
    * Also know as: Execution

[Back to Table of Contents](#Contents.md)

## Sarasvati Graph Execution ##

Let us start with a simple process definition, the classic 'Hello World'. When executed, this process will print out 'Hello, World!' and then complete.

#### Legend ####

---

First, we introduce a graphical notation for process definitions and execution. Not all the symbols will make sense immediately, but they will all be explained.

![http://sarasvati.googlecode.com/svn/wiki/images/legend.jpg](http://sarasvati.googlecode.com/svn/wiki/images/legend.jpg)

[Back to Table of Contents](#Contents.md)

#### Single Node ####

---

The simplest useful process definition would consist of a simple node. Here is the graphical representation:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts1.jpg)

How will this process be executed? First the engine needs to determine where to start execution.

  * **Start Node** - A node at which a token will be placed when process execution begins.

There are various ways of handling this. For example, there may be a specific type of node designated for start positions. All nodes of this type will have tokens placed in them at process start. Alternately, nodes may have an attribute which indicates whether or not they are a start node, allowing any node to be a start node. Sarasvati takes this second approach.

Assuming that the 'Hello World' node is a start node, execution would begin by creating a new **node token** at the 'Hello World' node.

  * **Node Token** - A token situated at a node. Node tokens track the response of the node guard (see below). They may also have attributes.

With the addition of the node token, the process would now look like:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts1-1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts1-1.jpg)

As you can see, the node now has an active node token stationed on it.

At this point the node has not yet been executed. Before it can be, its **guard** would need to be invoked.

  * **Node Guard** - Nodes have functionality associated with them, which will be executed when a node token is accepted into the node. However, before a node is executed, its guard will be executed. The guard is allowed one of three responses:
    * **Accept** - The node will be executed.
    * **Discard** - The node token will be marked as discarded and the node will not be executed.
    * **Skip**    - The node will _not_ be executed, however, processing will continue as if the node had completed execution normally.

By default, a node's guard will return Accept. The node will then be **executed**. This should cause 'Hello, World!' to be printed out.

  * **Node Execution** - When a node is executed, whatever custom logic has been assigned by the developer will run. To complete node execution, the node must inform the engine that that the given node token has been completed. Node completion may happen synchronously as part of the execution of the node function or it may happen later, asynchronously.

As there are no further steps in the process, it is now **complete** and looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts1-2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts1-2.jpg)

  * **Process Completion** - A process with no active tokens is considered complete.

The entire process can be viewed [here](ConceptsExample1.md).

[Back to Table of Contents](#Contents.md)

#### Two Nodes ####

---

Let's now example a slightly more complicated example. Instead of a single node, we'll have two, the first of which prints out 'Hello', the second prints out 'World'. It looks as follows:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts2.jpg)

The _Hello_ node is a **predecessor** of the _World_ node. This dependency is indicated by the directed arc.

As the _Hello_ node is marked as a start node, a node token will be placed there when the process begins executing.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts2-1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts2-1.jpg)

When the node token on _Hello_ is completed, an **arc token** will be generated on the outgoing arc.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts2-2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts2-2.jpg)

  * **Arc Token** - A token situated on an arc. Arc tokens exist so that join nodes (see below) know when to execute. Arc tokens may not have attributes.

Since the arc on which the arc token is situated goes into a **non-join node**, a node token will be created on _World_ immediately.

  * **Non-join node** - A node which is activated whenever an arc token arrives on an incoming node. Activated means the incoming arc token is completed and an active node token is created. This stands in contrast to a **join node**, where active arc tokens must exist on all incoming arcs with the same name.

The process now looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts2-3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts2-3.jpg)

The _World_ node will now run its guard and then execute. Finally the node token will be completed.


![http://sarasvati.googlecode.com/svn/wiki/images/concepts2-4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts2-4.jpg)

The entire process can be viewed [here](ConceptsExample2.md).

[Back to Table of Contents](#Contents.md)


#### Split and Join with Wait States ####

---


Let us now examine an example which contains concurrent execution.

The process describes an approval process.
  1. A request is made
  1. Two approvals must be obtained
  1. The request is granted

The process looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3.jpg)

This a simplified system, since it does not allow approvals to be denied.

There is more than one way that this process could be executed.

  * If the approvals are granted by people, the nodes will almost certainly be executed asynchronously. This means that when a token arrives at _Approval 1_, the node will generate a notification to the user who is to do the approval. The token will then enter a **wait state**. Execution may continue elsewhere in the process, but this token will wait until the user enters the system and grants approval.
  * If approvals are done by software which does a check and then returns immediately the tokens will not have enter a **wait state**, but may continue immediately.

  * **Wait State** - When a token enters a node and the node is executed, it may choose not to immediately continue process execution at the end of the node method. In this case the token will remain in the node until it is complete asynchronously. While the token is waiting to be completed, it is considered to be in a wait state.

Let us view process execution for both these cases, starting with the case where approvals are done by people and thus tokens will need to enter wait states.

Execution will begin as usual, by placing a node token in the nodes marked as being start nodes.

The _Request_ node will be executed. It generates a task for the requester to complete. Until the requester has filled out out the request and completed the task, the token will be in a wait state. During this time the process will look like:


![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-1.jpg)

**Question:** What happens once the _Request_ has been completed? Which arc or arcs will arc tokens be generated on?

**Answer:** Sarasvati requires that an arc name be specified when completing a node token. All arcs with this name will have arc tokens generated on them.

Some things to note:

  * Most arcs have no name specified. They are considered to have the 'default' name.
  * Usually when completing a node token, the default arc name will be given.
  * Each arc will have an arc token placed on it in turn. No specific order is guaranteed
  * When an arc token is placed on an arc, it will continue on to its end node immediately and see if the node can be executed.


So now the node token on _Request_ has been completed and arc tokens will be generated on the outgoing arcs. First a node token will be generated on the upper arc (though order of arc execution is not guaranteed).

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-2.jpg)

This arc leads to a node which can be executed. The arc token will be completed and a node token will be placed in the _Approval 1_ node.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-3.jpg)

Here the node token will enter a wait state. Since no further execution can take place here, an arc token will now be generated on the second outgoing arc.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-4.jpg)

Again, since node _Approval 2_ can be executed immediately, the arc token will be completed and a node token will be created. It will also enter into a wait state once the notification to the user has been created.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-5.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-5.jpg)

At some point one of the approvals will be completed. Let's say that it's _Approval 2_. This will mark the node token complete and generate an arc token on the outgoing arc.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-6.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-6.jpg)

Now the engine will see if the  _Grant_ node can be executed. However, as the dashed border indicates, the _Grant_ node is a **join node**.

  * **Join node** - When an arc token arrives a join node, arc tokens must exist on all other arcs **with the same name** before the node will accept a node token.

Since there are two arcs with the 'default' name coming into _Grant_, and only one of them has an arc token, the node can not be executed at this time. Execution will halt at this point.

At some point later, the token at _Approval 1_ is completed. This generates an arc token on the outgoing node.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-7.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-7.jpg)

Now when the engine tries to execute _Grant_ it finds arc tokens on all the incoming 'default' arcs. These arc tokens are marked complete and a node token is generated on _Grant_.


![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-8.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-8.jpg)

Once the _Grant_ task is finished, its node token will also be completed and the process will be complete.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-9.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-9.jpg)

The entire process can be viewed [here](ConceptsExampleSplitJoinOne.md).

[Back to Table of Contents](#Contents.md)

#### Multithreading ####

---

As seen the previous example, a process may have multiple tokens active concurrently. Does this imply that each token executes in a separate thread? No. Concurrency here is like that of multiple programs running on a single chip. Each runs in turns, but may present the appearance of running simultaneously.

However, true multithreading can be done at the node level. Each node when executed, may hand off its work to a background thread. The node token will then enter a wait state, and other nodes may be executed. When the background task is complete, it may then complete the node token, allowing further execution.

Note that only one thread may safely execute the process at any given time, and care must be taken to serialize access to the process itself.

[Back to Table of Contents](#Contents.md)

#### Split and Join without Wait States ####

---

Lets now take a look at the same process, except now the approvals will be done by software and will not require a wait state.

The execution will be the same up to the point where _Approval 1_ is executing.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-3.jpg)

Previously, the node token went into a wait state. This time, the approval is done synchronously and the token will be completed. This will generate an arc token on the outgoing arc.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-10.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-10.jpg)

Again, the _Grant_ node is a join node, so it will wait for an arc token on the other incoming arc before executing. Execution will continue on the lower outgoing arc of _Request_.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-11.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-11.jpg)

Execution will continue into _Approval 2_.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-12.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-12.jpg)

This execution will also finish synchronously and an arc token will be generated on the outgoing arc.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-13.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-13.jpg)

Execution will finish as before now that all required incoming arcs have tokens on them.

The entire process can be viewed [here](ConceptsExampleSplitJoinTwo.md).

[Back to Table of Contents](#Contents.md)

#### Flow Control with Guards using Skip ####

---

Now that we've seen how execution can split across arcs and join nodes can bring current executions back together, let us examine how to select which outgoing arcs receive tokens and which nodes get executed.

This example uses almost the same process as the previous example. The difference is that either or both approvals may be optional, depending on what is being requested.

Let us pick up execution after the request has been entered and an arc token generated on the upper arc:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-2.jpg)

Now the node token will be generated in _Approval 1_.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-3.jpg)

However, remember that this does **not** mean that the node will immediately execute. First the **guard** must be invoked. Up until now, the guard has always been assumed to just return **Accept**. This time however, the guard is intelligent. It will check to see if this approval is required. If not, it will return a **Skip** response.

  * **Skip** - A guard response which indicates that the node should not be executed, but that execution should continue on the outgoing nodes. An arc name may be specifying indicated which arcs should be used. If no arc name is given, arcs with the default name (unnamed arcs) will be used.

Assume that _Approval 1_ is not required. The node token will marked as having skipped the node, and execution will continue on the outgoing arc.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts3-16.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts3-16.jpg)

For the case where the guard for _Approval 2_ returns Accept, the entire execution can be seen [here](SkipExampleOne.md).

[Back to Table of Contents](#Contents.md)

#### Flow Control with Guards using Discard ####

---

Having seen Skip, let us examine how to use the Discard response from guards. The same basic process definition is used, only this time, the assumption is that only one of the guards is required.

The graph now looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts4.jpg)

Because we are using discard, only one token will reach _Grant_. This is why the _Grant_ node is no longer a **join node**.

Execution begins as normal. We pick up execution where a node token has been generated in _Approval 1_.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts4-3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts4-3.jpg)

In this case, the guard determines that _Approval 1_ is not required, and returns a **Discard** response.

  * **Discard** - A guard response indicating that the node token should be marked as discard, the node should **not** be executed and no tokens will be generated on outgoing arcs.

The process now looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts4-4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts4-4.jpg)

The node token has been discarded, and execution has continued from the completion of _Request_ where an arc token has been generated on the lower outgoing arc. Execution will now continue.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts4-5.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts4-5.jpg)

_Approval 2_ will accept its node token and will continue normally.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts4-6.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts4-6.jpg)

![http://sarasvati.googlecode.com/svn/wiki/images/concepts4-7.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts4-7.jpg)

Remember, because _Grant_ is no longer a join node, it will have a node token generated on it as soon as any arc tokens arrived.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts4-8.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts4-8.jpg)

The complete process can be seen [here](ConceptsExampleDiscardOne.md).

[Back to Table of Contents](#Contents.md)

#### Flow Control with Guards using Named Arcs ####

---

This same basic process could be implemented using a guard which returns Skip along with an arc name.

In this variant, a _Select_ node has been inserted after _Request_. This node has no functionality, it only exists to give the guard a place to run.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts5.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts5.jpg)

Let us pick it up after process started, as _Select_ has a node token generated on it, and its guard is invoked.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts5-3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts5-3.jpg)

The _Select_ guard will return a **Skip** response which includes the arc name on which to exit. **All arcs with this name will have an arc token generated on them**. In this case, let us say the guard determines that _Approval 2_ is required. It returns **Skip two**. An arc token is then generated on all arcs named _two_ (of which is there only one in this case).

![http://sarasvati.googlecode.com/svn/wiki/images/concepts5-4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts5-4.jpg)

From here execution continues as normal.

The complete process can be seen [here](ConceptsExampleSkipTwo.md).

[Back to Table of Contents](#Contents.md)

#### Flow Control from Node Completion using Named Arcs ####

---

As mentioned previously, when a node token is completed, an arc name must be specified. Arc tokens will be generated on all outgoing arcs with that name. So the previous example could also be implemented like this:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts6.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts6.jpg)

Instead of using the guard on the _Select_ node, the _Request_ node will specify which arc to exit on.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts6-1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts6-1.jpg)

If we again specify _two_, then an arc token will be generated on that arc.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts6-2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts6-2.jpg)

From there, execution will continue.

The complete process can be seen [here](ConceptsExampleSelectArcOne.md).

[Back to Table of Contents](#Contents.md)

## Graph Composition and Nested Processes ##

---

Much like any software, a set of process definitions can grow larger, more complex and more intertwined as time goes. One solution used in the broader software world is encapsulation. This involves pulling out common functionality and breaking up large pieces into smaller components. These same techniques can be used with a set of process definitions. Rather than using copy/paste, sections of process definitions that are common can be extracted. Large process definitions can be split out into smaller components.

Sarasvati supports two ways of doing encapsulation, each with it's own advantages and disadvantages. The first is **graph composition**, the second is **nested processes**. Both of these techniques allow complete process definitions and components that have been split out to be defined in separately. The difference lies in when they are composed.

  * **Load-time composition** - Graph composition brings the disparate elements together at load time. The main definition being loaded may refer to other definitions. These definitions will be loaded as well and they will all be combined into a single definition. This single definition will execute as if it had been defined in a single file.
  * **Run-time composition** - Nested processes use composition at runtime. The main definition will be loaded. When this definition is executed, a node may start a nested process. This nested process will execute and when completed, the main process will continue.

Now that we have general idea of how graph composition and nested processes compare, let us investigate them in more detail.

  * **Graph composition** - The set of process definitions may be seen as a single, disconnected graph. A node may contain arcs to nodes in other process definitions. These arcs are referred to as **external arcs**. When the process definition is loaded, referenced external process definitions will be loaded as well. All the process definitions will be composed into a single, larger graph. The external arcs will become regular arcs. The same external processes definition may be embedded more than once. Each **external instance** of an external process definition will be given a unique identifier.
    * Advantages
      * Interactions with external process definitions are not limited to a single node. The connections may be as complicated as within process definition.
      * Since the graph is not nested, execution is simple.
      * All nodes will share a single process variable scope, allowing easy sharing of variables.
    * Restrictions
      * Recursion is not allowed, since this would lead to an infinite loop during loading. **NOTE:** As in regular programing, recursive structures can be implemented using non-recursive techniques.
      * All nodes will share a single process variable scope. Sometimes it is desirable to have shared state for a subset of the nodes in a process definition.
      * The version of an external graph is set when the process definition is loaded, rather than when nodes from that graph are executed. If an external process definition is updated, process definitions referring to it must be reloaded as well to pick up the changes.

  * **External Arc** - An arc which has an endpoint in an external process definition. While normal arcs are always specified as originating in the node where they are defined (aka **out arcs**), it is not possible to add arcs to an external process. Therefore external arcs may either be **in arcs** or **out arcs**. Note that external arcs may be **named** just like regular arcs.
    * **Out Arc** - An arc which starts in the defining node and ends in a specified node
    * **In Arc** - An arc which starts in a specified node and ends in the node in which it is defined.

  * **External Instance** - A specific external process definition may be referenced multiple times. It may also be imported into the referring process definition multiple times, or just a single time. Each external arc names a specific instance of the external process definition.

  * **Nested Process** - A node in an executing process may create a separate, new process (of the same or different process definition). This new process is known a nested process. The new process gets initialized with the process state of the containing process and the current token. When the nested process completes, the token in the containing process will be completed.
    * Advantages
      * The nested process will have it's own process state
      * Processes may nested recursively
      * Nested processes will always use the latest version process definition at the time the node is executed.
    * Restrictions
      * The interaction with the nested process must all be contained by a single node. The nested process will execute in isolation. The nodes in the nested process won't interact with the those of the containing process in any way.


[Back to Table of Contents](#Contents.md)

#### Graph Composition Example One ####

---


Let's look at an examples of how this works in practice. Here is a small process definition which we want to embed. This process definition will be named _ext_.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals1.jpg)

It only has two nodes. Notice that both nodes are join nodes, even though one node has no inputs and the other only has one. However, in the composed graph these nodes may have more inputs.

Next is the process definition which will be using _ext_.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals2.jpg)

This process definition looks very different from previous examples. It isn't even fully connected.

Some things to note:
  * The external arcs are labeled with the process definition name, instance and node name that they are intended to link to.
  * In this case, all the arcs are connecting to the same instance of _ext_, instance 1.
  * Both in and out external arcs may connect to any node in the target external. They are not limited to just start nodes, for example.

When the graph is loaded, the composed version will look as follows:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals3.jpg)

[Back to Table of Contents](#Contents.md)

#### Graph Composition Example Two ####

---

The previous example referenced only a single instance. Here is the example using two instances 'ext.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals4.jpg)

When it is loaded, the composed graph looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals5.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals5.jpg)

As you can see, we now have two copies of _ext_ embedded in the process definition. One copy will be made for each unique instance referenced. A process definition can have references to any number of different external definitions and each external process definition can be imported any number of times.

[Back to Table of Contents](#Contents.md)

#### Nested Processes Example ####
The above example could not be implemented with nested processes because a nested process must be represented by a single node in the parent process. So, here is a similar, but simpler example using nested processes.

![http://sarasvati.googlecode.com/svn/wiki/images/concepts-nested1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts-nested1.jpg)

Nodes `S` and `T` both refer to the nested process named `nested`. Note that `nested` is almost the same as `ext`, except that the first node is a start node. This is because `nested` will be executed as a separate process. If it didn't have a start node, it would not execute.

When `S` and `T` execute, each will spawn a separate process. When `S` is executed, it will have an incomplete node token `t`. As part of execution it will start a new `nested` process `P` which have have the token `t` as a parent. When `P` completes, it will check if it has a parent token, and finding that it does, will complete `t`. This will allow execution to continue in the original process.

[Back to Table of Contents](#Contents.md)

## Execution Environment ##

---

While executing your process definitions, it may be desirable to have some shared state or to send data between nodes via the tokens. Sarasvati supports both these things via the execution **environment**. Each process has an environment on which attributes/variables can be set. In addition, each token also has its own environment.

  * **Environment** - A set of key/value attributes.

When using a memory backed engine, all environment attributes are stored in memory. However, when using a database backed engine, we may wish to persist only certain attributes. Also, storing objects in the database can be complicated, storing arbitrary objects in memory is easier than doing so in the database. By default, attributes are **persistent**, however, there is a separate set of variables which are **transient**.

  * **Persistent Attributes** - These attributes will be stored for the lifetime of the process. There may be restriction on what can be (easily) stored a persistent attribute, since it may need to be stored in a database table.

  * **Transient Attributes** - These attributes will be stored in memory, only as long as the process and/or token is in memory. For a memory backed engine, these have the same scope as the persistent attributes. There are no restrictions on what can be stored as a transient attribute.

#### Process Attributes ####

---

If you want state that is accessible from anywhere during process execution, then attributes can be set on the process environment. These attributes are visible and mutable by all nodes.

#### Token Attributes ####

---

Each **node token** also has its own environment. Arc tokens do not have an environment, because they do not execute in the same way that node tokens do, and thus have no need for private state. Node tokens are initialized with the state of their **parent tokens**.

  * **Parent token** - Each node tokens has zero to many parents.
    * A node token on a start node has no parents. It will start with an empty environment.
    * A node token on a node with one incoming arc of a given name has a single parent. Its environment will be copied from the parent.
    * A node token on a join node may have multiple parents, one for each arc of the same name. In this case the environments of all the parents must be combined in some way. By default, each environment will be imported into that of the new node. So if more than one parent has an attribute with the same one, the last one imported will overwrite the previous values. This behavior may be overridden, but if this is a concern, then using process level attributes may be advisable.

[Back to Table of Contents](#Contents.md)