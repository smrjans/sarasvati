# Sarasvati #

## Looking for New Maintainer ##
Due to a new job and a new child, I no longer have time to maintain Sarasvati to the level that the project and its users deserve. If you have a developer using Sarasvati and would like to be able to incorporate bug fixes and new features, please let me know on the developer forum.

## Overview ##
Sarasvati is a workflow/bpm engine based on graph execution. It has a simple core which allows for different implementations. For example one may change the persistence mechanism (or avoid it altogether), change token representation or build additional functionality on top.

## Features ##
  * Simple graph execution based core
  * [Backtracking](Backtracking.md)
  * [Process Definition/Graph Visualizations](ProcessDefinitionVisualization.md)
  * [Process Visualizations](ProcessVisualization.md)
  * Process level attributes
  * Token attributes
    * Efficient copy-on-write database storage of token attributes
  * Execution history available through 'immutable' tokens
  * Node guards allow bypassing nodes or discarding tokens for flow control
    * Domain specific language ([Rubric](Rubric.md)) for user understandable guards
  * XML file format for process definitions
  * Java implementation
    * Hibernate backed engine for DB persistence
    * Memory backed engine for cases where persistence is not required.

### Introduction to workflow ###
  * [What is workflow?](Workflow.md)
  * [Why graph based workflow?](GraphBasedWorkflow.md)

### Getting started with Sarasvati ###
  * [Getting the source code](CheckoutInstructions.md)
  * [Sarasvati core concepts](EngineConcepts.md)
  * [How to use Sarasvati and integrate it into your project](UsingSarasvati.md)
  * [Why is the project named Sarasvati?](ProjectName.md)
  * [The project README, which lists required Java version and libraries](http://sarasvati.googlecode.com/svn/java/trunk/README)

### News ###
  * March 02, 2013 - v2.02 release
    * [Changelog](http://code.google.com/p/sarasvati/issues/list?can=1&q=Milestone%3D2.0.2)
  * December 02, 2012 - v2.0.1 of Sarasvati released
    * [Announcement](https://groups.google.com/d/topic/sarasvati-wf-users/3R1ad7YzW7Y/discussion)
    * [Full Changelog](http://code.google.com/p/sarasvati/issues/list?can=1&q=Milestone%3D2.0.0)
  * September 21, 2011 - v1.0.5 of the Sarasvati Editor released
    * Fixed [Issue 70](https://code.google.com/p/sarasvati/issues/detail?id=70)
  * July 24, 2011 - v1.0.4 of Sarasvati released.
    * [Full Changelog](http://code.google.com/p/sarasvati/issues/list?can=1&q=Milestone%3D1.0.4)
  * May 21, 2010 - v1.0.3 of Sarasvati released.
    * [Full Changelog](http://code.google.com/p/sarasvati/issues/list?can=1&q=Milestone%3D1.0.3)
  * April 28, 2010 - v1.0.0 of Sarasvati released.
    * [Full Changelog](http://code.google.com/p/sarasvati/issues/list?can=1&q=Milestone%3D1.0.0)
  * March 17, 2009 - v1.0.0-rc2 of Sarasvati released for Java.
    * [Announcement](http://groups.google.com/group/sarasvati-wf-users/browse_thread/thread/70937d4bf2389377)
    * [Full Changelog](http://code.google.com/p/sarasvati/issues/list?can=1&q=label%3AMilestone-1.0.0-rc2)
  * February 23, 2009 - v1.0.0-rc1 of Sarasvati released for Java.
    * [Announcement](http://groups.google.com/group/sarasvati-wf-users/browse_thread/thread/8631018b2373513c/8c59c9990ec4c8f5?#8c59c9990ec4c8f5)
    * [Full Changelog](http://code.google.com/p/sarasvati/issues/list?can=1&q=label%3AMilestone-1.0.0-rc1)
  * December 9, 2008 - v1.0.0-beta3 of Sarasvati released for Java.
    * [Announcement](http://groups.google.com/group/sarasvati-wf-users/browse_thread/thread/eb398dd717f45437)
    * [Full Changelog](http://code.google.com/p/sarasvati/issues/list?can=1&q=label%3A1.0.0-beta3)
  * October 27, 2008 - v1.0.0-beta2 of Sarasvati released for Java.
    * Minor API improvements and bug fixes
  * October 6, 2008 - v1.0.0-beta1 of Sarasvati released for Java.
    * Cleaned up process definition file format somewhat, including fixing validation
    * Added support for embedding script (such as javascript) in process definition files, to be executed when the node is executed
    * Bug fixes
  * September 4, 2008 - v1.0.0-alpha2 of Sarasvati released for Java.
    * Added Adaptable interface for Nodes
    * Removed visual components into separate project
    * Fixed several bugs
  * August 22, 2008 - v1.0.0-alpha1 of Sarasvati released for Java.
    * Support for event listeners (global and per-process)
    * Support for nested processes
    * Standardized GraphFactory and GraphRepository
    * Allow direct bean style setting of node attributes from XML
    * Updated model to allow better recovery from failed node execution. Paves way for transaction boundaries.
  * August 10, 2008 - v0.3 of Sarasvati released for Haskell.
    * Updates support for process states
    * Changes library name from Sarasvati to sarasvati-haskell, for consistency.
  * July 29, 2008 - v0.3 of Sarasvati released for Java
    * Introduces explicit process states allowing for asynchronous process completions.
    * Bug fixes
  * July 24, 2008 - v0.2 of Sarasvati released for Java and Haskell
    * This introduces process level variables. Previously variables were only allowed on tokens. There are also some minor fixes.
  * June 29, 2008 - v0.1 of Sarasvati released for Java.
  * June 23, 2008 - Examples for Sarasvati Haskell released. I refreshed the v0.1.0 release with some updates to the build, including a dependency on Cabal 1.4.
  * June 22, 2008 - v0.1 of Sarasvati released for Haskell. This is an alpha release. Documentation is incomplete. This will be the focus of release v0.2. Some Haskell example code as well the Java version should be released in the next week or so.
  * June 12, 2008 - Java loader complete. Work on memory backed engine is also complete.
  * June 12, 2008 - XML Process definition file format reworked. Now has XML Schema Definition rather than DTD. This is allows embedding of elements defined in other schemas.
  * June 2, 2008 - More documentation and packaging work on Haskell version. Once XML -> Memory and XML -> DB loaders are documented and packaged, should be ready for an alpha release.
  * May 26, 2008 - Start memory backed engine for java version. Basic engine is implemented. However,will need memory backed graph loader before this is useful or testable.
  * May 26, 2008 - Implemented GuardLang for java version
  * May 21, 2008 - Documenting and building out wiki pages.
  * May 20, 2008 - Source code imported. I used [svn2svn](http://code.google.com/p/svn2svn/) to extract two projects out of the many in my home repository.
  * May 18, 2008 - Project created on Google Code. Thank you to [Stefan Webb](http://sarasvati.sf.net), for allowing me to use the name.