[<-- Prev: Core Concepts](EngineConcepts.md)

**NOTE: This is for version 1.0.0-rc2 (and previous)**



# Introduction #
Using Sarasvati usually involves writing process definition files, along with custom node types. Sarasvati uses an XML file format for process definitions. These files can be loaded into in-memory graph structures and executed, or they can be loaded first into a database, and from there loaded and executed.

We first introduce the file format, then explain how to implement custom behavior. Finally, we discuss how to interact with the Sarasvati engine.

# Sarasvati File Format #

## The XML Schema Definition ##
The process definition file format is defined by an XSD, which is [available to view in the project SVN](http://code.google.com/p/sarasvati/source/browse/common/ProcessDefinition.xsd).

We'll explore the Sarasvati file format, starting with the root element and working from there, with examples interspersed.

### Element process-definition ###

---

This is the root element. It should indicate the XML namespace, that being:
```
  http://sarasvati.googlecode.com/ProcessDefinition
```

#### Attributes ####
| **Attribute Name** | **Usage** | **Required?** | **Default** |
|:-------------------|:----------|:--------------|:------------|
| `name` | The unique name for this process definition. | Yes | N/A |

#### Nested Elements ####
  * **node** - Every `process-definition` must have a least one `node` defined.

### Element node ###

---

Nodes in a process definition are defined by the `node` element. Every node must have a name _unique to that file_.

#### Attributes ####
| **Attribute Name** | **Usage** | **Required?** | **Default** |
|:-------------------|:----------|:--------------|:------------|
| `name` | The name of this node. The name must be unique within this process definition. | Yes | N/A |
| `type` | The node type. Determines the node behavior. | No | `node` |
| `isStart` | Specifies whether a node will be presented with a token when the process is started. | No | false |
| `isJoin` | If true, the node will wait for arc tokens on all arcs with a given name before executing. If false, the node will execute as soon as an arc token arrives. | No | false |

#### Built in Node Types ####
  * `node` - Nodes of this type will complete out on the default arc when they are executed. The node type can be useful if a synchronization point is needed. It can also be used as a choice mechanism, by specifying a guard which skips to selected arcs.
  * `wait` - Nodes of this type will enter a wait state when executed. They will continue when completed by external logic. This can be useful when you need to wait on an external event, and no other logic is required.
  * `script` - Requires a `script` element which will contain a script to execute when the node is executed.

#### Nested Elements ####
  * **guard** - Nodes may optionally have a guard element.
  * **arc**   - Nodes may have zero to many arcs, pointing to other nodes in the same process definition.
  * **externalArc** - Nodes may have zero to many external arcs, pointing to or from nodes in other process definitions.
  * _custom_ - Node may have additional elements at the bottom. How the data from these custom elements is loaded, is explained in the section on custom node attributes below.

### Element guard ###

---

A node may contain a GuardLang statement, which will be executed as the node's guard. The guard element has no attributes and may contain no nested elements.

### One Node Example ###
With just `process-definition` and `node` a simple process definition can be built.

The simplest process definition would be a single node. Graphically, it would look like:

![http://sarasvati.googlecode.com/svn/wiki/images/pdfiles/example1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/pdfiles/example1.jpg)

The corresponding XML process definition would look like:

```
<?xml version="1.0"?>
<process-definition name="simplest"
                   xmlns="http://sarasvati.googlecode.com/ProcessDefinition">

  <node name="node-one" isStart="true"/>

</process-definition>
```

While there can be many nodes declared in a process definition, we have as yet, not defined a way of linking them together.

### Element arc ###

---

An `arc` element declares an arc from the enclosing node to the node with the name specified in the `to` attribute. An arc is allowed no nested elements.

#### Attributes ####
| **Attribute Name** | **Usage** | **Required?** | **Default** |
|:-------------------|:----------|:--------------|:------------|
| `to` | Specifies the name of the node this arc goes to. | Yes | N/A |
| `name` | Specifies the arc name. This name need not be unique. | No | null |

### Arc Example One ###

The following example contains three nodes.

![http://sarasvati.googlecode.com/svn/wiki/images/pdfiles/example2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/pdfiles/example2.jpg)

```
<?xml version="1.0"?>
<process-definition name="example2"
                   xmlns="http://sarasvati.googlecode.com/ProcessDefinition">

  <node name="node-one" isStart="true">
    <arc to="node-two"/>
    <arc to="node-three"/>
  </node>

  <node name="node-two"/>
  <node name="node-three"/>

</process-definition>
```

To indicate an arc, an `arc` element is added to the start node. The `to` attribute indicates the name of the target node. A node with that name must exist in the same process definition file.


### Arc Example Two ###

Here is an example with two start nodes. They both have arcs to `node-three`, which is a join node. It will only execute once both `node-one` and `node-two` have completed.

![http://sarasvati.googlecode.com/svn/wiki/images/pdfiles/example3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/pdfiles/example3.jpg)

```
<?xml version="1.0"?>
<process-definition name="example3"
                    xmlns="http://sarasvati.googlecode.com/ProcessDefinition">

  <node name="node-one" isStart="true">
    <arc to="node-three"/>
  </node>

  <node name="node-two" isStart="true">
    <arc to="node-three"/>
  </node>

  <node name="node-three" isJoin="true"/>

</process-definition>
```

Now that we've seen how to create links between nodes in the same process definition, let us examine how to create links to nodes in external process definitions.

### Element externalArc ###

---

An `externalArc` element declares an arc from the enclosing node to the external node with the name specified in the `nodeName` attribute. External arc elements may not contain nested elements.

#### Attributes ####
| **Attribute Name** | **Usage** | **Required?** | **Default** |
|:-------------------|:----------|:--------------|:------------|
| `external` | Specifies the name of the external process definition this arc goes to. | Yes | N/A |
| `instance` | Specifies the specific instance of the external process definition this arc goes to. All external arcs with the same values for `external` and `instance` will go to the same set of nodes. | Yes | N/A |
| `nodeName` | Specifies the name of the node this arc goes to. | Yes | N/A |
| `name` | Specifies the external arc name. This name need not be unique. | No | null |
| `type` | Indicates if the containing node is at the beginning or end of the arc. Valid values are `out` and `in`. If `out` is specified, the containing node is the start node of the arc, otherwise it is the end. | Yes | N/A |

### External Arc Example One ###
To examine external arcs, we'll need at least two process definitions.

This example is from the EngineConcepts section.

```
<?xml version="1.0"?>
<process-definition name="ext"
                    xmlns="http://sarasvati.googlecode.com/ProcessDefinition">

  <node name="A" isJoin="true">
    <arc to="B"/>
  </node>

  <node name="B" isJoin="true"/>

</process-definition>
```

It looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals1.jpg)

The graph which contains external arcs going to 'ext', is below.

```
<?xml version="1.0"?>
<process-definition name="example4"
                    xmlns="http://sarasvati.googlecode.com/ProcessDefinition">

  <node name="P" isStart="true">
    <externalArc external="ext" instance="1" nodeName="A" type="out"/>
  </node>

  <node name="Q" isStart="true">
    <externalArc external="ext" instance="2" nodeName="A" type="out"/>
  </node>

  <node name="R" isStart="true">
    <externalArc external="ext" instance="2" nodeName="B" type="out"/>
  </node>

  <node name="X">
    <arc to="Z"/>

    <externalArc external="ext" instance="1" nodeName="A" type="in"/>
  </node>

  <node name="Y">
    <arc to="Z"/>
    <externalArc external="ext" instance="2" nodeName="B" type="in"/>
  </node>

  <node name="Z" isJoin="true"/>

</process-definition>
```

It looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals4.jpg)

When the process definition is loaded into memory, it will look like:

![http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals5.jpg](http://sarasvati.googlecode.com/svn/wiki/images/concepts-externals5.jpg)

## Java API ##
The process definition file format also supports custom elements and attributes, but before we discuss that, the API needs to be introduced.

### Interfaces ###
The main interface for interacting with process definitions and processes is `Engine`:

```
package com.googlecode.sarasvati;

public interface Engine
{
  GraphProcess startProcess (Graph graph);
  void startProcess (GraphProcess process);
  void cancelProcess (GraphProcess process);
  void finalizeComplete (GraphProcess process);
  void finalizeCancel (GraphProcess process);
  void completeExecution (NodeToken token, String arcName);
  void completeAsynchronous (NodeToken token, String arcName );
  void executeQueuedArcTokens (GraphProcess process);
  GraphRepository<? extends Graph> getRepository ();
  GraphFactory<? extends Graph> getFactory ();
  GraphLoader<? extends Graph> getLoader ();
  void addNodeType (String type, Class<? extends Node> nodeClass );
  void fireEvent (ExecutionEvent event);
  void addExecutionListener (ExecutionListener listener, ExecutionEventType...eventTypes);
  void addExecutionListener (GraphProcess process, ExecutionListener listener, ExecutionEventType...eventTypes);
  void removeExecutionListener (ExecutionListener listener, ExecutionEventType...eventTypes);
  void removeExecutionListener (GraphProcess process, ExecutionListener listener, ExecutionEventType...eventTypes);
  ExecutionListener getExecutionListenerInstance (String type) throws WorkflowException;
  void setupScriptEnv (ScriptEnv env, NodeToken token);
}
```

Process definitions are stored in classes implementing the `Graph` interface.

```
package com.googlecode.sarasvati;

public interface Graph
{
  String getName ();
  int getVersion ();
  List<? extends Arc> getArcs ();
  List<? extends Arc> getInputArcs (Node node);
  List<? extends Arc> getInputArcs (Node node, String arcName);
  List<? extends Arc> getOutputArcs (Node node);
  List<? extends Arc> getOutputArcs (Node node, String arcName);
  List<? extends Node> getStartNodes ();
  List<? extends Node> getNodes ();
}
```

A `Graph` contains instances of `Node` and `Arc`.

`Node` is where the developer can provide custom functionality, and has the following API:

```
package com.googlecode.sarasvati;
public interface Node extends Adaptable
{
  String getName ();
  String getType ();
  boolean isJoin ();
  boolean isStart ();
  String getGuard ();
  Graph getGraph ();
  boolean isExternal ();
  GuardResponse guard (Engine engine, NodeToken token);
  void execute (Engine engine, NodeToken token);
}
```

### Flow of Execution ###
When the engine determines that a node is ready to execute, it will follow this flow:

  1. Generate a `NodeToken` pointing to that node.
  1. Execute the `guard` function on the node. This will return a `GuardResponse`.
    * The `GuardResponse` contains a `GuardAction`, which is an enum having values `AcceptToken`, `DiscardToken` and `SkipNode`.
  1. If the action is `AcceptToken`, the `execute` method will be called. The process will not continue until the `Engine#completeExecution` method is invoked. It must be invoked with the name of the arcs on which to generate `ArcToken`s.
  1. If the action is `DiscardToken`, the token is marked complete and no further execution will take place from this set of tokens.
  1. If the action is `SkipNode`, `Engine.completeExecution` will be called with the arc name contained in the `GuardResponse`.

### Custom logic for Node Execution ###
To provide custom behavior to your nodes, you will override the `execute` method on `Node`. Sarasvati currently provides two implementations of the base API, one memory backed and one database backed, implemented using Hibernate. Other implementations could be made using, for example, pure JDBC or some other persistence mechanism. There are three base classes for nodes.

  * `com.googlecode.sarasvati.mem.MemNode`
  * `com.googlecode.sarasvati.hib.HibNode`
  * `com.googlecode.sarasvati.CustomNode`

If using only the memory backed implementation, `MemNode` should be extended. If using only the hibernate backend, nodes should subclass `HibNode`. `CustomNode` can be used with either or both backends. In to store custom attributes in the database, it uses a key/value pair table. `CustomNode` can only be used if the database mapping doesn't need to be explicitly defined.

### Example One ###
To demonstrate use of each implementation, we start with a node that just prints out "Hello, World". We start with the process definition.

#### Example One: Process Definition ####
```
<?xml version="1.0"?>
<process-definition name="hello-world"
                    xmlns="http://sarasvati.googlecode.com/ProcessDefinition">

  <node name="hello" type="helloWorld" isStart="true"/>

</process-definition>
```

#### Example One: Node implementation ####
If using the memory implementation, the subclass would look like:

```
package com.googlecode.sarasvati.example.mem;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.mem.MemNode;

public class HelloNode extends MemNode {
  @Override public void execute (Engine engine, NodeToken token)
  {
    System.out.println( "Hello, world!" );
    engine.completeExecution( token, Arc.DEFAULT_ARC );
  }
}
```

The hibernate version would look like:

```
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.hib.HibNode;

@Entity
@DiscriminatorValue( "helloWorld" )
public class HelloNode extends HibNode
{
  @Override public void execute (Engine engine, NodeToken token) {
    System.out.println( "Hello, World!" );
    engine.completeExecution( token, Arc.DEFAULT_ARC );
  }
}
```

The hibernate version would also require an insert into the `wf_node_type` table, with type, description and behaviour.

```
 insert into wf_node_type (id, description, behaviour) values ( 'helloWorld', 'Says hello to the world', 'helloWorld' )
```

The behaviour column ties the type to a discriminator specified on the subclass. This allows having multiple types with the same implementation class, if that was desired.

The backend independent version would look like:

```
import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;

public class HelloNode extends CustomNode
{
  @Override
  public void execute (Engine engine, NodeToken token)
  {
    System.out.println( "Hello, World!" );
    engine.completeExecution( token, Arc.DEFAULT_ARC );
  }
}
```

For use with the hibernate backend, a row would still need to be added to the `wf_node_type` table.

```
 insert into wf_node_type (id, description, behaviour) values ( 'helloWorld', 'Says hello to the world', 'custom' )
```

#### Example One: Loading and Running ####
Now we can load the process into memory, or into the database. This is done using a `GraphLoader`, which can be retrieved from the appropriate engine. Before loading the process definition, you will need to tell the engine about your custom node types.

The steps are
  1. Create an engine of the appropriate type
  1. Register custom node types
  1. Load the process definition from XML file
  1. Get the loaded graph from the graph repository associated with the engine
  1. Start a new GraphProcess using the graph

Here are the steps in code for the memory backed implementation.

```
    MemEngine engine = new MemEngine();

    // Tell engine about our custom node type
    engine.addNodeType( "helloWorld", HelloNode.class );

    // Load the process definition (this can throw LoadException or JAXBException
    // The graph will be stored in the GraphRepository for this engine
    engine.getLoader().load( "/path/to/hello-world.wf.xml" );

    // Get the graph from the GraphRepository
    Graph graph = engine.getRepository().getLatestGraph( "hello-world" );

    // start a graph process
    GraphProcess process = engine.startProcess( graph );
```

Here are the steps in code for the hibernate backed implementation. It assumes that you have a means of creating a hibernate `Session` object.

```
    Session session = ...; // get hibernate session
    HibEngine engine = new HibEngine( session );

    // Tell engine about our custom node type
    engine.addNodeType( "helloWorld", HelloNode.class );

    // Load the process definition (this can throw LoadException or JAXBException
    // The graph will be stored in the GraphRepository for this engine
    engine.getLoader().load( "/path/to/hello-world.wf.xml" );

    // Get the graph from the GraphRepository
    Graph graph = engine.getRepository().getLatestGraph( "hello-world" );

    // start a graph process
    GraphProcess process = engine.startProcess( graph );
```

Here are the steps in code using the backend independent custom type with `MemEngine`.

```
    MemEngine engine = new MemEngine();

    // We can either register the type with the Engine or with the DefaultNodeFactory
    // directly.
    // Either tell the engine about our custom node type
    engine.addGlobalCustomNodeType( "helloWorld", HelloNode.class );

    // or tell the DefaultNodeFactory about the node type directly
    DefaultNodeFactory.addGlobalCustomType( "helloWorld", HelloNode.class );

    // Load the process definition (this can throw LoadException or JAXBException
    // The graph will be stored in the GraphRepository for this engine
    engine.getLoader().load( "/path/to/hello-world.wf.xml" );

    // Get the graph from the GraphRepository
    Graph graph = engine.getRepository().getLatestGraph( "hello-world" );

    // start a graph process
    GraphProcess process = engine.startProcess( graph );
```


The call to `startProcess` will create tokens on the start nodes and will continue executing the process until it completes or enters a wait state.

### Custom Attributes ###
Often, custom nodes will need some information with which to do their work. Sarasvati supports this in two ways.

The schema for process definition files has a `<custom>` element which contains an `<xs:any>` element at the end of the node definition. Custom elements may be added here. These can be automatically mapped to properties on custom nodes.

For example, given the following custom node:

```
public class CustomNode extends MemNode {
  String foo;

  public String getFoo () {
    return foo;
  }

  public void setFoo (String foo) {
    this.foo = foo;
  }

  @Override public void execute (Engine engine, NodeToken token) {
    // do something ...
    engine.completeExecution( token, Arc.DEFAULT_ARC );
  }
}
```

The following process definition would load the value `test` into the `custom` property.

```
<?xml version="1.0"?>
<process-definition name="example1"
                   xmlns="http://sarasvati.googlecode.com/ProcessDefinition">

  <node name="test" type="custom" isStart="true">
    <arc to="1"/>

    <custom>
      <foo>test</foo>
    </custom>
  </node>
</process-definition>
```

There several things to note with custom elements.

  * All custom elements must be contained within the `<custom>` tag.
  * Non-string properties on custom node types are supported.
    * Support for primitive types such as boolean, byte, char, short, int, long, float, double as well as their corresponding object types is built in.
    * Support for non-primitive types can be added
      * Subclass com.googlecode.sarasvati.load.properties.BasePropertyMutator
      * Register new mutator with com.googlecode.sarasvati.load.properties.PropertyMutatorRegistry#registerPropertyMutator

Nested objects are supported. For example:

```
  <custom>
    <task>
      <name>test</name>
    </task>
  </custom>
```

The loader would invoke `getTask().setName( ... )` on the custom node.

Attributes are also supported. How they are mapped is based on the contents of the element the attribute is on. If the element has child elements, the attribute will get mapped as a child property. If the element is itself a property, the attribute name will be combined with the element name to get the property name.

```
  <custom>
    <task user="pat">
      <name>test</name>
    </task>
  </custom>
```

This would map the `name` element value to `getTask().setName( ... )` and the `user` attribute to `getTask().setUser( ... )`.

However, the following would be mapped differently:

```
  <custom>
    <task user="pat">
      test
    </task>
  </custom>
```

This would map the text in the `task` element  to `setTask( ... )` and the `user` attribute to `setTaskUser( ... )`.

#### Custom Loader ####
You may also provide custom loading via a subclass of `NodeFactory`. It has the following interface:

```
public interface NodeFactory
{
  Node newNode (String type) throws LoadException;
  void loadCustom (Node node, Object custom) throws LoadException;
}
```

The custom data may be null, a single object, or a list of objects. The object or objects will either be elements of `org.w3c.dom.Element` or JAXB objects, if you have a JAXB mapping for your custom XML.

Instances of `NodeFactory` may be registered on `GraphLoader`.

#### Example Two: Process Definition ####

Here we examine a more complicated example, which uses custom attributes.

```
<?xml version="1.0"?>

<process-definition name="example1"
                   xmlns="http://sarasvati.googlecode.com/ProcessDefinition">

  <node name="start" isStart="true">
    <arc to="1"/>
  </node>

  <node name="1" type="task">
    <arc to="2"/>
    <arc to="3"/>

    <custom>
      <taskName>Enter order</taskName>
      <taskDesc>
        Enter order and billing info
      </taskDesc>
    </custom>
  </node>

  <node type="task" name="2">
    <arc to="4"/>

    <custom>
      <taskName>Bill Customer</taskName>
      <taskDesc>
        Bill the Customer
      </taskDesc>
    </custom>
  </node>

  <node type="task" name="3">
    <arc to="4"/>

    <custom>
      <taskName>Ship product</taskName>
      <taskDesc>
        Package and ship product
      </taskDesc>
    </custom>
  </node>

  <node type="task" name="4" isJoin="true">

    <custom>
      <taskName>Market to Customer</taskName>
      <taskDesc>
        Send marketing material to customer
      </taskDesc>
    </custom>

  </node>

</process-definition>
```

#### Example Two: Node implementation ####

We will need a couple of classes to represent tasks and their state. First we look at the memory based implementation.

First we have an enum for task states.
```
public enum TaskState { Open, Completed, Rejected }
```

Next is the `Task` class.

```
public class Task {
  protected NodeToken nodeToken;
  protected String name;
  protected String description;
  protected TaskState state;

  public Task (NodeToken nodeToken, String name, String description, TaskState state) {
    this.nodeToken = nodeToken;
    this.name = name;
    this.description = description;
    this.state = state;
  }

  public NodeToken getNodeToken () {
    return nodeToken;
  }

  public void setNodeToken (NodeToken nodeToken) {
    this.nodeToken = nodeToken;
  }

  public String getName () {
    return name;
  }

  public String getDescription () {
    return description;
  }

  public TaskState getState () {
    return state;
  }

  public void setState (TaskState state ) {
    this.state = state;
  }

  public boolean isRejectable () {
    Node node = getNodeToken().getNode();
    return !node.getGraph().getOutputArcs( node, "reject" ).isEmpty();
  }
}
```

In our simple example, we need some way of tracking which tasks have been created.

```
public class TaskList {
  protected static List<Task> tasks = new LinkedList<Task>();

  public static List<Task> getTasks () {
    return tasks;
  }
}
```

Finally, the custom node for generating tasks

```
public class TaskNode extends MemNode {
  protected String taskName;
  protected String taskDesc;

  public String getTaskName () {
    return taskName;
  }

  public void setTaskName (String taskName) {
    this.taskName = taskName;
  }

  public String getTaskDesc () {
    return taskDesc;
  }

  public void setTaskDesc (String taskDesc) {
    this.taskDesc = taskDesc;
  }

  @Override public void execute (Engine engine, NodeToken token) {
    Task newTask = new Task( token, getTaskName(), getTaskDesc(), TaskState.Open );
    TaskList.getTasks().add( newTask );
  }
}
```

When a task node is executed, it will create new `Task` instance and add it to a task list. A task can be completed or rejected as seen in the following code snippet:

```
      Task t = ...;

      if ( isCompletion )
      {
        t.setState( TaskState.Completed );
        engine.completeExecution( t.getNodeToken(), Arc.DEFAULT_ARC );
      }
      else if ( isReject && t.isRejectable() )
      {
        t.setState( TaskState.Rejected );
        engine.completeExecution( t.getNodeToken(), "reject" );
      }
```

The primary difference with the database/Hibernate version, is that the node and tasks will require database backing. Let us look at the `TaskNode` class.

```
@Entity
@DiscriminatorValue( "task" )
@SecondaryTable( name="wf_node_task", pkJoinColumns=@PrimaryKeyJoinColumn(name="id"))
public class TaskNode extends HibNode
{
  @Column (name="name", table="wf_node_task")
  protected String taskName;

  @Column (name="description", table="wf_node_task")
  protected String taskDesc;

  public TaskNode() { /* Default constructor for Hibernate */ }

  public String getTaskName () {
    return taskName;
  }

  public void setTaskName (String taskName) {
    this.taskName = taskName;
  }

  public String getTaskDesc () {
    return taskDesc;
  }

  public void setTaskDesc (String taskDesc) {
    this.taskDesc = taskDesc;
  }

  @Override public void execute (Engine engine, NodeToken token) {
    HibEngine hibEngine = (HibEngine)engine;

    Session session = hibEngine.getSession();

    TaskState open = (TaskState)session.load( TaskState.class, 0 );
    Task newTask = new Task( (HibNodeToken)token, getTaskName(), getTaskDesc(), open );
    session.save( newTask );
  }
}
```

[<-- Prev: Core Concepts](EngineConcepts.md)