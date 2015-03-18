# Introduction #

The end goal is to allow template nodes. Template nodes are nodes that that may be allowed to execute many times in parallel. Another node will then collect all the executions.

An example usage would be as part of an approvals process. The process might contain a section which looks like

(Determine approvals need) -> (Approval) -> (Check Results)

The first step would figure out how many approvals are needed. Then the **Approval** node would be executed once for each approval needed. The **Check Results** node would not execute until all the approvals had been completed. So if there were two approvals required, the execution would look something like

```
(Determine approvals need) ---> (Approval) ----> (Check Results)
                           \                 /
                            \-> (Approval) -/
```

# Token Sets #
Templates will be implemented using token sets. When completing a node execution, the user be able specify that the completion should generate a token set as well as how many arc tokens should be generated. A new type of join will be added, tokenSetAnd, which will wait for all members of the given token set to either be completed or be waiting on incoming arcs of the node.

## Token Set interfaces ##

`TokenSet`

| **Attribute Name** | **Type** |
|:-------------------|:---------|
| id | long |
| name | String |
| env | Env |

`TokenSetMember`
| **Attribute Name** | **Type** |
|:-------------------|:---------|
| id | long |
| token | Token |
| set | TokenSet |
| memberIndex | int |

## Java API ##

Add methods to `Engine` interface:

```
  completeExecutionWithTokenSet( NodeToken token, 
                                 String arcname,                               
                                 int numberOfTokens, 
                                 Env initialEnv,
                                 String tokenSetName )

  // here token set name defaults to the name of the node that the token is
  // pointing to
  completeExecutionWithTokenSet( NodeToken token, 
                                 String arcname, 
                                 int numberOfTokens, 
                                 Env initialEnv )

```

This will create `numberOfTokens` tokens on the given arc name, tied to a new token set with the given name and which has the given initial `Env`.

## Join Types ##
`isJoin` attribute will be replaces with `joinType`. Currently, there are only two types of join behaviour.

  * isJoin = false: The node fires whenever a token arrives
  * isJoin = true: The node fires when a token arrives on an arc of a given name, and there are arc tokens present on all incoming arcs which have the same name.

There will be four types of join.

| **Join Type** | **Behaviour** |
|:--------------|:--------------|
| or | Node fires whenever an arc token arrives. Equivalent to current 'isJoin=false' behavior.  |
| and | Node fires when an arc token arrives and there are arc tokens on all incoming arcs|
| labelAnd | The node fires when a token arrives on an arc of a given name, and there are arc tokens present on all incoming arcs which have the same name. Equivalent to current 'isJoin=true' behavior' |
| tokenSetAnd | The node fires when all tokens that are part of the given token set are completed, discard or are waiting at the join node |

tokenSetAnd behavior may require some refinement. What happens if all the tokens that are going to make it to the join node, do so, then later the last token in the token set is discarded? How do we notify the join to try again?

Will also need to add an optional parameter tokenJoinName, so the tokenSetAnd knows what token set it is waiting for.

```
<node joinType="or|and|labelAnd|tokenSetAnd" tokenJoinName="...">
```