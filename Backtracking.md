Note: This document assumes that you have read an understood the [core concepts documentation](EngineConcepts.md). However, the general concepts presented here should still make sense without that, if you have basic knowledge of how graph based execution works.



# Motivation #
In graph based workflow, execution proceeds forward from node to node along directed arcs. However, it happens that we wish to allow execution to return to nodes where it has already been. Sometimes this is done because some action needs to be performed repeatedly. Other times, it's because something has gone wrong, and we need to go back to an earlier point to fix things and go back through the process. Here we focus primarily on the second case, and look at different ways of accomplishing this.

# Linear Backtracking #
## Linear Process Definition ##
Here is a simple, linear workflow:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-graph1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-graph1.jpg)

In this case, there is only one progression that can be made. Using squares for node tokens and triangles for arc tokens, the resulting execution looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process1.jpg)

## Linear Backtracking: Manual ##

What if we wish to let a user choose to send the workflow backwards, instead of forwards, say to fix a mistake made earlier. If we wanted to be able to go back, we'd have to set up arcs going backwards. Assuming, we want maximum flexibility, we'd end up with a process definition that looked like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-graph2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-graph2.jpg)

Now we could go from A to B to C, back to B to C to D, back to B to C to D and done. The execution would look like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process2.jpg)

Note that the arc tokens now display the arc name.

We have gained flexibility, but at the cost of making the workflow much more complex. The number of arcs has tripled.

## Linear Backtracking: Automatic ##

What if instead, we could use the existing arcs? After all we know where we've been, and we just wish to go back to a previous good state. So, rather than having to make explicit arcs that go back to all conceivable previous states, we can just **backtrack**. In other words we can just trace our footsteps backwards to where we were.

So, lets says we've gotten up to **C**. At this point, the process execution history looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process3-1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process3-1.jpg)

Now we wish to backtrack to **B**. If we retrace our steps, the process history will now look like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process3-2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process3-2.jpg)

Note that when we traverse arc **2** backward, it's marked in red. This is to mark that we've gone backwards along that arc. The node tokens on **B** and **C**, as well as the forward moving arc token on **2** are also marked, but in blue. This is to note that these actions have been backtracked. When node tokens are backtracked, they are given the opportunity to undo whatever work they did, send out notifications, or do whatever else is required.

We can now move forward again, this time going up to **D**, where the process execution history looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process3-3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process3-3.jpg)

If from here, we once again wish to return to **B**, the execution history will look like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process3-4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process3-4.jpg)

Note that since we are somewhat literally retracing our steps, to get from **D** to **B** we created backwards tokens at arc **3**, node **C** and arc **2**. The corresponding forward tokens have been marked as backtracked.

From here we now finish, and go to the end.

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process3-5.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process3-5.jpg)

# Backtracking across a Split #
## Process Definition ##
Let us now examine a process definition which has splits and joins.

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-graph3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-graph3.jpg)

These complicate manual backtracking a great deal.

## Considerations ##
If one has multiple, concurrent node tokens active after a split, backtracking one of them means that all must be backtracked. The one which has been backtracked must out and find all incomplete concurrent tokens and complete them. It must also set a marker indicating that backtracking should occur. All the tokens must then first be collected by an intermediary node, which will test to see if a backtrack is required. It will then send execution forward or back based on this test.

If all tokens which were generated from the split aren't collected and sent back as a single token then each token sent back to the split will generate a new set of tokens from the split. This could cause many duplicates to be generated.

Here is an example of a graph which would be roughly equivalent to the previous graph, but allows manual backtracking.

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-graph4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-graph4.jpg)

It has a great deal many more arcs, as well as a more complicated structure, to accommodate backtracking to the split.

## Automated ##
To see how automated backtracking would work, let us first progress from **A** to **B** to where both **C** and **D** are open.

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-s1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-s1.jpg)

We can now attempt to backtrack to **B**.

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-s2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-s2.jpg)

The engine will follow all execution history which emanated from **B** and attempt to reverse it. So we can see that it flows backwards from **C** and **D** backwards to **B**.

If we now attempt to backtrack one more step to **A**, the execution history will look like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-s3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-s3.jpg)

Though this appears to be a simple linear backtrack, it's actually slightly complicated. The history starting from **A** includes the backtracking we just did. So the engine must traverse this to get to the current active tokens and backtrack them.

## Variation ##
If we were to backtrack directly from when **C** and **D** were open back to **A**, the execution history would look as follows:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-s4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-s4.jpg)

It looks almost the same, except that the second **B** is marked as a backwards execution, since this time, we went straight across it, instead of stopping there, and then continuing backwards.

# Backtracking Across a Join #
## Process Definition ##
We'll use the same process definition as we used for demonstrating splits.

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-graph3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-graph3.jpg)

## Considerations ##
Going back from a join is both more and less problematic than a split. A join can act as a split and send tokens back all of its inputs. However, it is very difficult to go back to just one of the inputs. If only one of the join inputs is reactivated, then the join will never fire, since it won't have all the required inputs available.

## Automated ##
To start off with, assume execution has proceeded to **E** and the execution history looks like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-j1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-j1.jpg)

If we then want to go back to when **C** was open, the process will now look like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-j2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-j2.jpg)

As expected, **C** is now open. To do so, we backtracked across arc **4**. However, **D** is not open but there is an open arc token on **5**. We only wanted **C** open, not **D**. However, when **C** completes, we want **E** to execute again. However, **E** will only execute if there are arc tokens waiting on arcs **4** and **5**. So, we backtrack arc **5**, but the arc token we create on **5** is left active. Now when **C** completes, **E** will execute as it will have arc tokens on all inputs.

The arc token on **5** is called a **u-turn** and it marked in yellow, since this is what it does. It backtracks onto an arc, but then turns around and goes right back.

If we complete **C** the process will look like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-j3.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-j3.jpg)

If instead of completing **C** we backtracked to **D** as well, the process would now look like:

![http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-j4.jpg](http://sarasvati.googlecode.com/svn/wiki/images/backtrack-process4-j4.jpg)

The u-turn arc token is now marked as backtracked and a node token is now active on **D**.

# Java API #
Backtracking is very easy to use in Sarasvati.

To backtrack, invoke the `backtrack(NodeToken token)` method on your `Engine` instance.

```
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
```

Your custom node classes may override the `isBacktrackable` and `backtrack` methods on `Node`.

`Node#isBacktrackable` will control whether a given invocation of `Engine#backtrack` will succeed. Sometimes, business logic may require that certain actions not be repeated.

`Node#backtrack` gives a place to to undo the the results of your custom node logic, and/or send notifications. This method will not be invoked until after `isBacktrackable` has returned true for all nodes needing to be backtracked.

```
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
```