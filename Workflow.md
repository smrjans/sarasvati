[Next: Why graph based workflow? -->](GraphBasedWorkflow.md)

# Introduction #

To help understand what workflow is, we start with definitions of the main components of a workflow system.

## Definitions ##

  * **Actor** - A person or program which performs some _action_.
  * **Action** - Something to be performed by an _actor_. Once an _actor_ is notified that a given _action_ is to be completed, they may perform it synchronously or asynchronously. It may take hours or days to complete the _action_.
  * **Process Definition** - A set of actions which need to be performed. The actions have a defined order in which they must be performed. Some actions may be performed concurrently with others.
  * **Process** - An instantiation of a _process definition_. Each _process definition_ may have many processes running at once. A _process definition_ can be compared to the on disk image of a program, where the _process_ is comparable to an executing program (possibly with multiple threads of execution). Or from an OO perspective, a _process definition_ is analogous to a class defintion and a _process_ is like an instantiated object of that class.
  * **Workflow Engine** - A program, library or API which can load _process definitions_ and from them, generate and execute a _processes_.

Workflow is a label for systems which enable the building of process definitions and the execution of processes.

## Why workflow? ##
So what is so special about workflow? After all, dependencies can be handled programatically. If action A is followed by action B, then action A can just invoke action B when it is complete. Concurrency can be handed by threads.

What workflow generally provides over a manual implementation is
  * Ease of implementation
    * If there are complicated dependencies, these are tracked by the workflow engine. Each action doesn't need to worry about what comes before or after it. This also allows actions to be more easily abstracted and reused.
    * The workflow engine also ideally handles persistence. Processes can be long running, taking weeks or months to complete. The workflow engine will handle persisting the state of the workflow, so if the containing program dies, needs to be update or restarted, the  process will not be lost.
  * Ease of definition
    * Workflow system generally provide an easier way of designing and/or specifying process definitions than by doing it manually in code.
    * Generally workflow systems can read in process definitions in a human readable file format.
    * Many workflow systems also provide visual editors.

## Examples ##


---

_**Example 1: Order Fulfillment**_

---

The scenario here is of a small company which sells beach balls. They have a process for servicing an order.

#### Actors ####
  * Sales (person)
  * Billing (program)
  * Warehouse (person)
  * Marketing (program)

#### The process ####
  1. The sales person receives an order and enters it into the system.
    1. This includes the type number of products ordered and the payment information.
  1. Once this is complete, the order will go to both billing and the warehouse.
  1. While the warehouse people package and ship the order, the billing system will perform whatever credit card transactions are necessary.
  1. Once the product is both shipped and billed, a marketing system will determine what promotional material and/or special offers to send to the customer, in order to elicit future business.

Graphically, the process could be represented as follows:

![http://sarasvati.googlecode.com/svn/wiki/images/example1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/example1.jpg)


---

_**Example 2: Document conversion**_

---

This scenario concerns a news aggregation company called NewsCO which takes in news from various sources and republishes it in a variety of formats. Here we will look at a simplified workflow which handles two input formats.

#### Actors ####
  * RSS -> NewsCO XML format Converter (program)
  * Plain text -> NewsCO XML format converter (program)
  * Editor (person)
  * NewsCO XML web publisher (program)
  * NewsCO XML Analyzer (program)
  * NewsCO XML RSS publisher (program)

#### The process ####
  1. The process begins when an article arrives, either from an RSS feed or via a file drop in plain text format.
  1. Conversion:
    1. If the article is in RSS format, the RSS XML will be converted to a proprietary XML format (the NewsCO XML format)
    1. If the article is in plain text, it will be parsed and converted to the NewsCO XML format.
    1. If there is an error in the conversion process, the article will be handed to a human editor who will manually do the conversion
  1. Publishing
    1. The NewCO XML will be converted to a webpage and posted on a web site for consumption by the public. The web publisher is an asynchronous program which accepts XML and later provides notification when the publishing is complete. This could be a separate workflow.
    1. NewsCO customers receive RSS feeds of articles based on keywords. The article will be scanned for keywords. A database entry will be created.
    1. Each customer who has expressed interest in a keyword found in the article will have their RSS feed updated with a link to the published web article.


Graphically, the process could be represented as follows:

![http://sarasvati.googlecode.com/svn/wiki/images/example2.jpg](http://sarasvati.googlecode.com/svn/wiki/images/example2.jpg)

[Next: Why graph based workflow? -->](GraphBasedWorkflow.md)