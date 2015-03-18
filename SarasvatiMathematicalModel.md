

## Concise Natural Language Description ##

  * A graph is made up of nodes and arcs.
  * A subset of nodes are join nodes.
  * A subset of nodes are start nodes. The set of start nodes may be empty, but this case is not useful, as it will not be executable.
  * The set of join nodes and the set of start nodes may intersect.
  * Nodes have the following properties
    * A guard function, which given a node token, returns one of Accept, Discard or Skip.
    * An execute function.
  * Arcs have the following properties
    * start node
    * end node
    * non-unique label
  * A graph is executed by means of a process. A process tracks the current state of execution using node tokens, which exist on nodes, and arc tokens, which exist on arcs.

Graph execution has the following operations
  * StartProcess
    * Parameters: process
    * A graph is execution begins by placing a node token on every start node, and performs the ProcessNodeToken operation on each of these node tokens.
  * ProcessNodeToken
    * Parameters: node token
    * Performed on a newly generated node token.
    * Executes the guard associated with the node
    * If the guard returns Accept: Executes the node
    * If the guard returns Discard: Marks the node token as discarded
    * If the guard returns Skip: Skip includes an arc label. This performs CompleteNodeToken with the given arc label
  * CompleteNodeToken
    * Parameters: node token, label
    * Generates an arc token every outgoing node from the given node with that label
    * ProcessArcToken is performed on each arc token generated
  * ProcessArcToken
    * Parameters: arc token
    * If the end node of the arc is a join node
      * An arc token must exist on all other arcs coming into the same end node which share a label with the current arc. If this condition is met, one arc token on each of the arcs is marked complete and a node token is created on the arc's end node. ProcessNodeToken is called on the new node token.
    * If the end node of the arc is not a join node, the arc token is immediately marked complete, a new node token is generated on the end node of the arc and ProcessNodeToken is performed on the new node token.

## Mathematical Model ##
A Sarasvati executable graph is defined as directed graph, with a token mapping of node and arc tokens over that graph.

### Graph Definition ###

A Sarasvati graph is defined as

**G** = ( **N**, **N<sub>j</sub>**, **N<sub>s</sub>**, **A**, **F<sub>g</sub>**, **F<sub>e</sub>**, **L** )

  * **N** is the set of nodes in the graph
  * **N<sub>j</sub>** is the set of join nodes in the graph. **N<sub>j</sub>** ⊂ **N**.
  * **N<sub>s</sub>** is the set of start nodes in the graph. **N<sub>s</sub>** ⊂ **N**.
  * **A** is the set of arcs in the graph
  * **F<sub>g</sub>** is the set of guard functions used by the graph.
    * Every guard function returns a tuple ( **r**, **l** )
    * **r** ∈ { _Accept_, _Discard_, _Skip_ }
    * **l** ∈ **L** ∪ { {} }
    * if **r** ≠ _Skip_ then **l** = {}
  * **F<sub>e</sub>** is the set of node execution functions used by the graph.
    * Every node execution function accepts the node token which triggered the execution as well as the current process state. The function returns either the same process state, or a new process state.
  * **L** is the set of labels used in the graph

Every node **n** ∈ **N** is a tuple ( **f<sub>g</sub>**, **f<sub>e</sub>** ) where

  * **f<sub>g</sub>** ∈ **F<sub>g</sub>**
  * **f<sub>e</sub>** ∈ **F<sub>e</sub>**

An arc **a** ∈ **A** is a tuple ( **a<sub>start</sub>**, **a<sub>end</sub>**, **l** ) where

  * **a<sub>start</sub>** ∈ **N**
  * **a<sub>end</sub>** ∈ **N**
  * **l** ∈ **L**
  * For convenience the following functions map an arc to the members of its defining tuple.
    * The function _start_( **a** ) = **a<sub>start</sub>**
    * The function _end_( **a** ) = **a<sub>end</sub>**
    * The function _label_( **a** ) = **l**

### Process Definition ###

Formally, a Sarasvati graph execution **S** (also known as a process) is defined as

**S** = ( **G**, **T**, **R**, **P**, **Q**, **i** )

where

  * **G** is the graph being executed
  * **T** is the set of active node tokens
  * **R** is the set of active arc tokens
  * **P** is the set of unprocessed node tokens
  * **Q** is the set of unprocessed arc tokens
  * **i** is the current state counter

A node token **t** ∈ **T** ∪ **P** is defined as a tuple (**n**, **i**) where

  * **n** ∈ **N**
  * **i** corresponds to the process state counter at the time the node token is created
  * For convenience the following function maps an node token to the node in its defining tuple.
    * The function _node_( **t** ) = **n**


An arc token **t** ∈ **R** ∪ **Q** is defined as a tuple (**a**, **i**) where

  * **a** ∈ **A**
  * **i** corresponds to the process state counter at the time the node token is created
  * For convenience the following function maps an arc token to the arc in its defining tuple.
    * The function _arc_( **t** ) = **a**


### Process State Changes ###
There are a set of operations which are performed to advance the state of a process.

  * _startProcess_
  * _processNodeTokens_
  * _processArcTokens_
  * _completeNodeToken_

#### Initial State ####

Every process has the same basic state, through the referenced graph may, of course, be different.

S<sub>0</sub> = (G, {}, {}, {}, {}, 0)

#### startProcess ####
This operation creates node tokens for each start node. The new tokens are placed in the 'pending' node token set. The _processNodeTokens_ operation will then be invoked on the new process state.

_startProcess_( S<sub>0</sub> ) =
  1. S<sub>1</sub> = (G, {}, {}, { (**n**, 1) | **n** ∈ **N<sub>s</sub>** }, {}, 1)
  1. return _processNodeTokens_( S<sub>1</sub> )

#### processNodeTokens ####
This operation will pull node tokens out of the pending queue and process them, until the queues are empty. Note that the result of _processNodeTokens_( S<sub>i</sub>) may be S<sub>i+1</sub> but it also may be S<sub>i+n</sub> where n is a number greater than 1. This is because _processNodeTokens_ may invoke other operations, which will also change the process state. Thus the result is denoted as S<sub>j</sub>, where j > i.

for S<sub>i</sub> = ( G, T<sub>i</sub>, R<sub>i</sub>, P<sub>i</sub>, Q<sub>i</sub>, i )

S<sub>j</sub> = _processNodeTokens_( S<sub>i</sub>) =
  1. Select t ∈ P<sub>i</sub>
  1. _node_(t) = n = (f<sub>g</sub>, f<sub>n</sub>)
  1. Get the result (r, l) of the guard function f<sub>g</sub>
  1. if r = _Accept_
    1. S<sub>i+1</sub> = ( G, T<sub>i</sub> ∪ t, R<sub>i</sub>, P<sub>i</sub> - { t }, Q<sub>i</sub>, i+1)
    1. return f<sub>n</sub>(S<sub>i+1</sub>, t)
  1. if r = _Discard_
    1. S<sub>i+1</sub> = ( G, T<sub>i</sub>, R<sub>i</sub>, P<sub>i</sub> - { t }, Q<sub>i</sub>, i+1)
    1. return S<sub>i+1</sub>
  1. if r = _Skip_
    1. S<sub>i+1</sub> = ( G, T<sub>i</sub> ∪ {t}, R<sub>i</sub>, P<sub>i</sub> - { t }, Q<sub>i</sub>, i+1)
    1. return _completeNodeToken_( S<sub>i+1</sub>, t, l)

if P<sub>j</sub> ≠ {} then perform _processNodeTokens_( S<sub>j</sub> )

#### completeNodeToken ####
This operation works with a given node token. It requires that an arc label is specified and will generate arc tokens on all outgoing arcs which have that label.

for S<sub>i</sub> = ( G, T<sub>i</sub>, R<sub>i</sub>, P<sub>i</sub>, Q<sub>i</sub>, i )

_completeNodeToken_( S<sub>i</sub>, t, l ) =
  1. t ∈ T<sub>i</sub>
  1. Generate the set of arc tokens.
    1. O = { (a, i) | a ∈ A, _node_( t ) = _start_( a ), _label_( a ) = l }
  1. S<sub>i+1</sub> = ( G, T<sub>i</sub> - { t }, R<sub>i</sub>, P<sub>i</sub>, Q<sub>i</sub> ∪ O, i+1)
  1. return _processArcTokens_( S<sub>i+1</sub> )

#### processArcTokens ####
This operation attempts to complete arc tokens in the pending queue. It will repeat until the pending queue is empty.

for S<sub>i</sub> = ( G, T<sub>i</sub>, R<sub>i</sub>, P<sub>i</sub>, Q<sub>i</sub>, i )

S<sub>j</sub> = _processArcTokens_( S<sub>i</sub>) =
  1. Select t = ∈ Q<sub>i</sub>
  1. currentArc = _arc_( t )
  1. targetNode = _end_( currentArc )
  1. if targetNode ∈ N<sub>j</sub>
    1. First, move the token from the pending set to the active set. S<sub>i+1</sub> = ( G, T<sub>i</sub>, R<sub>i</sub> ∪ { t }, P<sub>i</sub>, Q<sub>i</sub> - { t }, i + 1 }
    1. Determine the set of input arcs I to targetNode which share the label _label_( currentArc ). I = { a | a ∈ A, targetNode = _end_( a ), _label_( a ) = _label_( currentArc ) }
    1. Determine if ∀a ∈ I, ∃t such that t ∈ R<sub>i+1</sub> and _arc_( t ) ∈ I
      1. If no, targetNode can not execute yet and we return S<sub>i+1</sub>
      1. If yes, build a set J such that J = { t | t ∈ R<sub>i+1</sub>, _arc_( t ) ∈ I and ∀t<sub>1</sub>, t<sub>2</sub> ∈ J where t<sub>1</sub> ≠ t<sub>2</sub>, _arc_( t<sub>1</sub> ) ≠ _arc_( t<sub>2</sub>). In other words, find a single arc token for arc in the set I.
      1. S<sub>i+2</sub> = ( G, T<sub>i+1</sub>, R<sub>i+1</sub> - J, P<sub>i+1</sub> ∪ { (targetNode, i), Q<sub>i+1</sub>, i + 2 }
      1. return _processNodeTokens_( S<sub>i+2</sub> )
  1. if targetNode ∉ N<sub>j</sub>
    1. S<sub>i + 1</sub> = ( G, T<sub>i</sub>, R<sub>i</sub>, P<sub>i</sub> ∪ { (targetNode, i) }, Q<sub>i</sub> - { t }, i + i)
    1. return _processNodeTokens_( S<sub>i+1</sub> )

if P<sub>j</sub> ≠ {} then perform _processArcTokens_( S<sub>j</sub> )