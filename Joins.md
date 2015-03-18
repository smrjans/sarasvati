# Introduction #
Whenever an arc token arrives at a node, a decision must be made as to what to do. There are three options to select from.

  1. The conditions for executing the node have not been met. The arc token will remain in an incomplete state. The arc token will await some point in the future, when another arc token may arrive that causes the node execution criteria to be met.
  1. The conditions for executing the node may be met. All the arc tokens that contributed to this will be become the parents of the new node token. When the conditions for executing the node are met, this will be known as completing the join.
  1. The conditions for executing the node were met in the past, and the node has already executed. The arc token will be retroactively added in as a parent of the node token that was already created. The action is called a merge.

# Join API #
Sarasvati includes several built in join strategies. Join strategies are defined by implementing the [JoinStrategy interface](http://sarasvati.googlecode.com/svn/java/tags/v1.0.4/doc/javadoc/com/googlecode/sarasvati/JoinStrategy.html). A Node will generally indicate which join strategy use by having its JoinType set. The JoinType enum corresponds to a specific JoinStrategy.

A JoinStrategy instance, when asked to perform a join, will return a JoinResult. The JoinResult indicates which of the three courses to take (complete the join, merge the token into a previous join or do nothing). It will also, in the case of a completion, return the set of arc tokens contributing to the join. In the case of a merge, it will return the NodeToken to which to add the ArcToken as a parent.

# Join Types #

  * AND
    * Requires an arc token on every incoming arc
    * Uses the AndJoinStrategy.

  * OR
    * A join of this type will be satisfied any time an arc token arrives at the node.
    * Uses the OrJoinStrategy.

  * LABEL
    * A join of this type will be satisfied when an arc token arrives, and there are arc tokens waiting at all other incoming arcs to the node which share the same name/label as the arc that the arc token is arriving on.
    * Uses the LabelJoinStrategy.

  * FIRST
    * The first arc token to arrive will satisfy the join. Subsequent arc tokens arriving will be merged.
    * Uses the MergeJoinStrategy with OrJoinStrategy as a fallback.

  * TOKEN\_SET
    * A token set join will be satisfied when all active arc tokens in the set are on incoming arcs to the same node and there are no active node tokens in the token set. An exception will be raised if a non-token set token arrives.
    * Uses the TokenSetJoinStrategy.

  * TOKEN\_SET\_OR
    * A token set or join will be satisfied when all active arc tokens in the set are on incoming arcs to the same node and there are no active node tokens in the token set. The OrJoinStrategy will be used as a fallback if a non-token set token arrives.
    * Uses the TokenSetOrJoinStrategy.

  * CLASS
    * A class join assumes that the associated join parameter specifies a class implemented the JoinStrategy interface. An instance of that class will be used to perform the join.
    * Uses the ClassJoinStrategy.


  * JOINLANG
    * Assumes that the join param is a program in the JoinLang domain specific language.
    * Uses the JoinLangJoinStrategy.

# JoinLang #
JoinLang is a domain specific language for specifying join criteria. JoinLang programs take the form of require statements in blocks.

In loose EBNF, it would be defined as

```
  Program = RequireBlock ('or' RequireBlock)*

  RequireBlock = RequireStatement (RequireStatement)*

  RequireStatement = require node <name> (when <expr>)?
                   | require tokenset <name> (when <expr>)?
                   | require all arcs (labelled <name> (when <expr)?)?
                   | require at least <number> arcs (labelled <name> (when <expr>)?)?
```

The 'when' expressions are all Rubric expressions.

Some examples would be

**Example one:**
```
  require at least 3 arcs labelled "Approved" 
```

**Example two:**
```
    require node "Expedited Shipping" when Order.isExpedited
    require node "Regular Shipping" when not Order.isExpedited
    require node "Billing"
```

**Example three:**
```
    require node "Expedited Shipping" when Order.isExpedited
    require node "Regular Shipping" when not Order.isExpedited
    require node "Billing"
    or 
    require node "Customer Pickup"
    require node "Billing"
```

Many of the other join strategies can be expressed in terms of JoinLang.

# Custom Join Strategies #
A custom join strategy may be provided in one of two ways. Either the CLASS join type is specified. In this case the join param on the node is assumed to be the name of class implementing JoinStrategy. This class must have a public, no-arg constructor.

The other way a custom join strategy may be used is by using a custom subclass of Node which overrides the getJoinStrategy method.