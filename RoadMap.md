# Roadmap #

Sarasvati is fairly stable at this point, and we are workings towards a 1.0 release.



## 1.0 TODO ##

### Documentation: In Progress ###
  1. Documentation needs to be updated for changes.
    * Split out external - DONE
    * isJoin => joinType - DONE
    * Env                - DONE
    * Execution Listener - DONE
    * Convert visualization docs - DONE
    * Convert backtracking doc   - DONE
    * hibernate node type handling - DONE
    * Graph validation framework - DONE
    * Finish reworking API docs
    * Token Sets documentation
    * Editor docs

Note:
  * Need to fix external example image 2 (has incorrect link) - DONE

## 1.0 Complete ##

### Editor: Completed ###

A graphical editor for process definitions is in progress. It is now usable for process definitions that don't use externals.

  * Undo/Redo framework: Done
  * Load: Done
  * Save: Done
  * Add nodes: Done
  * Delete nodes: Done
  * Add external: Done
  * Delete external: Done
  * Move nodes: Done
  * Connect nodes: Done
  * Change connections: Done
  * Remove connections: Done
  * Edit node properties: Done
  * Edit external properties: Done
  * Edit arc properties: Done
  * Cleanup undo/redo MDI interactions: Done
  * Autolayout: Done
  * Self arcs: Done
  * Arc labels: Done
  * Cleanup move/add/reconnect interactions: Done
  * Check for unsaved: Done
  * Graph validation on save: Done
  * Preferences
    * Node Types
      * Basic node type management: Done
      * Select icon type: Done
      * Select icon color: Done
  * Select
    * Select single for delete: Done
    * Selected multiple for delete: Done
    * Select multipe for move: Done
    * Cut: Done
    * Copy: Done
    * Paste: Done
  * Process Definition Library: Done
  * Autoset file extension: Done
  * Start with new PD on startup. If a file is opened and new PD is untouched, replace it with edit: Done
  * Don't output namespace for `<custom>`: Done
  * Investigate shortestpathrouter weirdness: Done
  * Use right click context menu for nodes/externals for cut/copy/delete/edit properties: Done
  * Add info text for join types: Done
  * Move layout info to `<editor>` section of PD file: Done

### Token Sets/Template Nodes: Completed ###

See [design](TokenSetsDesign.md).

  * Milestone I
    * Basic template based split join working in memory engine
    * Status: Done
  * Milestone II
    * Basic template based split join working in hibernate engine
    * Status: Done
  * Milestone III
    * TokenSet env working in memory engine
    * Status: Done
  * Milestone IV
    * TokenSet env working in hibernate engine
    * Status: Done
  * Milestone V
    * TokenSet integration with backtracking
    * Status: Done
  * Milestone VI
    * Allow specification of a the name of a token set to join on in a TokenSet
    * Status: Done

### Performance Tuning: Completed ###
Analyzed SQL generated from hibernate. Ran explain plan on each statement and made sure that no table scans were being performed.

## Post 1.0 Release ##

### JDBC Backend: In progress ###

This would be good as a fall back for users who don't use hibernate. Also, it would make sure that the interfaces are sufficiently flexible.

Implementation started.

### Broader Database support: Some work done ###

  * Investigate if schema can be generate from Hibernate mappings
    * Issue with generating FKs to enum tables
    * Issue text columns on postgesql vs varchars in other dbs
  * Investigate table name mappings, so users can pick their own table names
    * Use hibernate NamingStrategy
    * Make sure we don't hardcode tables/column name, if possible.
  * Need test suite for DB
    * Test against following databases
      * Postgresql
      * Sybase
      * MySQL
      * Oracle
      * DB2
      * Some simple file based engine? BerkleyDB?

### Improve Editor ###
  * Guard Editor
  * More node type icons
  * Allow customization: ie: user defined drop downs for user properties
  * Graph properties
    * Define allowed properties, when used as external
    * Define allowed input/output points, when used as external
    * Define default input/output points, when used as external

### Other ###
Possibilities:

  * Improve visualizations
  * Additional backends
    * Google AppEngine/Bigtable?
  * Porting to .NET/Python/Ruby?
    * Porting to C# is a priority, based on request.

Potential runtime upgrade algorithm: Rerun workflow history over new graph.
  1. Need a way to determine which complete action was performed on a node token.
    1. Could be tracked, or maybe inferred
  1. If a node was removed, can do lookahead on history to see if an equivalent node exists.
  1. If a node was added, can have user agent decide if node should be skipped or stopped at.
  1. At fork points, will be able to determine if additional paths were added and can have a user agent determine if new nodes are to be skipped or halted at.

JoinLang: Could write DSL to specify forks
> require arc "foo" and arc "bar"
> require at least 5 tokens
