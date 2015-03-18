[<-- Prev: What is Workflow](Workflow.md) ... [Next: Core Concepts  -->](EngineConcepts.md)

# Graphs and Processes #

Graphs have been used for a long time to visually represent processes. Some examples are:

  * [Flowcharts](http://en.wikipedia.org/wiki/Flowcharts)
  * Unified Modeling Language (UML)
    * [Activity diagrams](http://en.wikipedia.org/wiki/Activity_diagram)
    * [State Machine Diagram](http://en.wikipedia.org/wiki/State_diagram#UML_state_diagram)
  * [Finite State Automata](http://en.wikipedia.org/wiki/Finite_state_automata)

Graphs are visual, intuitive and ubiquitous. That finite state machines are graphs shows their expressive power. A graph combined with some storage is roughly equivalent to a Turing Machine, capable of executing any computation.

## Alternatives ##
There are other ways of representing workflows. For example, one could just list out the actions along with their dependencies. The engine could then properly sequence the actions, For example, the simple Order Fulfillment example could be defined as

  1. Enter Order for Sales depends on nothing
  1. Bill Customer for  Billing depends on 1
  1. Ship Product for Warehouse depends on 1
  1. Market to Customer for Marketing depends on 2, 3

While this would work fine for simple processes, it doesn't offer a way to define cycles. If for example, the warehouse people determine that they are out of stock, they may need to send the workflow back to the sales department so they can interact with the customer. This mechanism also lacks a clear means of flow control. For example, a single workflow may cover the order fulfillment process for many different product types. It is likely that some sections of the workflow would only apply to specific products.


[<-- Prev: What is Workflow](Workflow.md) ... [Next: Core Concepts  -->](EngineConcepts.md)