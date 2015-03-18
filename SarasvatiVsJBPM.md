

# Introduction #

jBPM is the dominant open-source business process modeling solution at the moment. So the question may arise, how does Sarasvati compare to jBPM (or its PVM underpinnings). This attempts to compare Sarasvati and jBPM. Please note that I am not a jBPM expert, and appreciate any corrections from those more knowledgeable.

## Sarasvati Advantages ##

  * Support for [backtracking](Backtracking.md)
  * Built in support for generating web based visualizations
  * Support for [load time process composition](http://code.google.com/p/sarasvati/wiki/EngineConcepts#Graph_Composition_and_Nested_Processes).
  * More concise model. jBPM requires separate nodes, specific nodes for split and join. In Sarasvati, any node can function as a split and/or join.
    * If explicit splits and joins are desired, Sarasvati can handle this easily.
  * jBPM split/join behavior requires symmetric splits and joins. See bottom for an example of a graph containg split/join asymmetry. Essentially splits and joins must be paired, and the number of outgoing arcs on the split must be the same as the number of incoming arcs on the join.
    * Sarasvati handles asymmetric split joins correctly.
    * Since you can write your own jBPM nodes, this behavior could probably be coded around.
  * In Sarasvati, tokens are never moved, and they maintain a parent/child relationship. This provides a directed acyclic graph (DAG) of the execution history. This allows an easy and natural way of looking at execution history which can be used for visualizations, backtracking, etc.

## jPBM/PVM Advantages ##

  * jBPM has a graphical editor. Sarasvati does not (yet).
  * Built in models for groups/tasks, etc.
  * Greater integration with JBoss (and maybe other) products.
  * More mature product.

## Asymmetric Split/Join Example ##

![http://sarasvati.googlecode.com/svn/wiki/images/asymmetric-split-join.jpg](http://sarasvati.googlecode.com/svn/wiki/images/asymmetric-split-join.jpg)

## Other comparisons ##
[Response to a similar comparison on TheServerSide forms](http://www.theserverside.com/news/thread.tss?thread_id=51707#276579)