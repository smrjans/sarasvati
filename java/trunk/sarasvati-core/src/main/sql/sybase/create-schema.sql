IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_graph')
  BEGIN
    exec( "
      print 'Creating table wf_graph'
      create table wf_graph
      (
        id          bigint       IDENTITY NOT NULL PRIMARY KEY,
        name        varchar(255)          NOT NULL,
        version     int                   NOT NULL,
        custom_id   varchar(255)          NOT NULL,
        create_date datetime              DEFAULT getDate() NOT NULL
      ) with identity_gap = 10

      print 'Creating unique constraint on wf_graph'
      ALTER TABLE wf_graph
        ADD CONSTRAINT wf_graph_unique
          UNIQUE(name,version)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_graph already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_graph_listener')
  BEGIN
    exec( "
      print 'Creating table wf_graph_listener'
      create table wf_graph_listener
      (
        id              bigint IDENTITY NOT NULL PRIMARY KEY,
        type            varchar(255)    NOT NULL,
        event_type_mask int             NOT NULL,
        graph_id        bigint          NOT NULL REFERENCES wf_graph
      ) with identity_gap = 10

      print 'Creating unique constraint on wf_graph_listener'
      ALTER TABLE wf_graph_listener
        ADD CONSTRAINT wf_graph_listener_unique
          UNIQUE(graph_id, type)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_graph_listener already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_process_state')
  BEGIN
    exec( "
      print 'Creating table wf_process_state'
      create table wf_process_state
      (
        id          int          NOT NULL PRIMARY KEY,
        description varchar(255) NOT NULL
      )
    ")
  END
ELSE
  BEGIN
    print 'Table wf_process_state already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_process')
  BEGIN
    exec( "
      print 'Creating table wf_process'
      create table wf_process
      (
        id              bigint       IDENTITY NOT NULL PRIMARY KEY,
        graph_id        bigint                NOT NULL REFERENCES wf_graph,
        state           int                   NOT NULL REFERENCES wf_process_state,
        parent_token_id bigint                NULL,
        create_date     datetime              DEFAULT getDate() NOT NULL,
        version         int                   NOT NULL
      ) with identity_gap = 10

      print 'Creating index on wf_process(graph_id, state)'
      create index wf_process_idx on wf_process (graph_id, state)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_process already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_process_attr')
  BEGIN
    exec( "
      print 'Creating table wf_process_attr'
      create table wf_process_attr
      (
        process_id   bigint        NOT NULL REFERENCES wf_process,
        name         varchar(64)   NOT NULL,
        value        varchar(1800) NULL
      )

      print 'Creating primary key on wf_process_attr'
      ALTER TABLE wf_process_attr
        ADD PRIMARY KEY (process_id, name)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_process already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_process_listener')
  BEGIN
    exec( "
      print 'Creating table wf_process_listener'
      create table wf_process_listener
      (
        id              bigint       IDENTITY NOT NULL PRIMARY KEY,
        type            varchar(255)          NOT NULL,
        event_type_mask int                   NOT NULL,
        process_id      bigint                NOT NULL REFERENCES wf_process
      ) with identity_gap = 10

      print 'Creating unique constraint on wf_process_listener(process_id, type)'
      ALTER TABLE wf_process_listener
        ADD CONSTRAINT wf_listener_unique
          UNIQUE(process_id, type)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_process_listener already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_node_type')
  BEGIN
    exec( "
      print 'Creating table wf_node_type'
      create table wf_node_type
      (
         id          varchar(255) NOT NULL PRIMARY KEY,
         description varchar(255) NOT NULL,
         behaviour   varchar(255) NOT NULL REFERENCES wf_node_type
      )
    ")
  END
ELSE
  BEGIN
    print 'Table wf_node_type already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_node_join_type')
  BEGIN
    exec( "
      print 'Creating table wf_node_join_type'
      create table wf_node_join_type
      (
        id int NOT NULL PRIMARY KEY,
        description varchar(255) NOT NULL
      )
    ")
  END
ELSE
  BEGIN
    print 'Table wf_node_join_type already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_node')
  BEGIN
    exec( "
      print 'Creating table wf_node'
      create table wf_node
      (
        id              bigint       IDENTITY NOT NULL PRIMARY KEY,
        graph_id        bigint                NOT NULL REFERENCES wf_graph,
        name            varchar(255)          NOT NULL,
        join_type       int                   NOT NULL REFERENCES wf_node_join_type,
        join_param      varchar(255)          NULL,
        is_start        char(1)               NOT NULL,
        type            varchar(255)          NOT NULL REFERENCES wf_node_type,
        guard           varchar(255)          NULL
      ) with identity_gap = 10

      print 'Creating unique constraint on wf_node(graph_id, name)'
      ALTER TABLE wf_node
        ADD CONSTRAINT wf_node_unique
          UNIQUE(graph_id, name)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_node already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_node_attr')
  BEGIN
    exec( "
      print 'Creating table wf_node_attr'
      create table wf_node_attr
      (
        node_id  bigint        NOT NULL REFERENCES wf_node,
        name     varchar(140)  NOT NULL,
        value    varchar(1800) NULL
      )

      print 'Creating primary key for table wf_node_attr'
      ALTER TABLE wf_node_attr
        ADD PRIMARY KEY (node_id, name)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_node_attr already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_external')
  BEGIN
    exec( "
      print 'Creating table wf_external'
      create table wf_external
      (
        id                bigint       IDENTITY NOT NULL PRIMARY KEY,
        name              varchar(255)          NOT NULL,
        graph_id          bigint                NOT NULL references wf_graph,
        external_graph_id bigint                NOT NULL references wf_graph
      )
    ")
  END
ELSE
  BEGIN
    print 'Table wf_external already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_external_attr')
  BEGIN
    exec( "
      print 'Creating table wf_external_attr'
      create table wf_external_attr
      (
        external_id  bigint        NOT NULL REFERENCES wf_external,
        name         varchar(140)  NOT NULL,
        value        varchar(1800) NULL
      )

      print 'Creating primary key for table wf_external_attr'
      ALTER TABLE wf_external_attr
        ADD PRIMARY KEY (external_id, name)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_external_attr already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_node_ref')
  BEGIN
    exec( "
      print 'Creating table wf_node_ref'
      create table wf_node_ref
      (
        id          bigint       IDENTITY NOT NULL PRIMARY KEY,
        node_id     bigint                NOT NULL REFERENCES wf_node,
        graph_id    bigint                NOT NULL REFERENCES wf_graph,
        parent_id   bigint                NULL REFERENCES wf_node_ref,
        external_id bigint                NULL REFERENCES wf_external
      ) with identity_gap = 10

      print 'Creating index on wf_node_ref (graph_id)'
      create index wf_node_ref_graph_idx on wf_node_ref (graph_id)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_node_ref already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_arc')
  BEGIN
    exec( "
      print 'Creating table wf_arc'
      create table wf_arc
      (
        id            bigint       IDENTITY NOT NULL PRIMARY KEY,
        graph_id      bigint                NOT NULL REFERENCES wf_graph,
        a_node_ref_id bigint                NOT NULL REFERENCES wf_node_ref,
        z_node_ref_id bigint                NOT NULL REFERENCES wf_node_ref,
        name          varchar(255)          NULL
      ) with identity_gap = 10

      print 'Creating index on wf_arc(graph_id)'
      create index wf_arc_graph_idx on wf_arc (graph_id)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_arc already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_guard_action')
  BEGIN
    exec( "
      print 'Creating table wf_guard_action'

      create table wf_guard_action
      (
        id           int         NOT NULL PRIMARY KEY,
        name         varchar(50) NOT NULL
      )

    ")
  END
ELSE
  BEGIN
    print 'Table wf_guard_action already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_execution_type')
  BEGIN
    exec( "
      print 'Creating table wf_execution_type'

      create table wf_execution_type
      (
        id            int          NOT NULL PRIMARY KEY,
        name          varchar(255) NOT NULL
      )

    ")
  END
ELSE
  BEGIN
    print 'Table wf_execution_type already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_node_token')
  BEGIN
    exec( "
      print 'Creating table wf_node_token'

      create table wf_node_token
      (
        id             bigint    IDENTITY NOT NULL PRIMARY KEY,
        process_id     bigint             NOT NULL REFERENCES wf_process,
        node_ref_id    bigint             NOT NULL REFERENCES wf_node_ref,
        attr_set_id    bigint             NULL     REFERENCES wf_node_token,
        create_date    datetime           DEFAULT getDate() NOT NULL,
        guard_action   int                NULL     REFERENCES wf_guard_action,
        execution_type int                NOT NULL REFERENCES wf_execution_type,
        complete_date  datetime           NULL
      ) with identity_gap = 100

      print 'Creating index on wf_node_token(process_id, complete_date)'
      create index wf_node_token_idx on wf_node_token(process_id, complete_date)

      print 'Creating foreign key from wf_process.parent_token_id to wf_node_token'
      ALTER TABLE wf_process
        ADD CONSTRAINT FK_process_parent
          FOREIGN KEY (parent_token_id)
            REFERENCES wf_node_token

    ")
  END
ELSE
  BEGIN
    print 'Table wf_node_token already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_arc_token')
  BEGIN
    exec( "
      print 'Creating table wf_arc_token'

      create table wf_arc_token
      (
        id              bigint    IDENTITY NOT NULL PRIMARY KEY,
        process_id      bigint             NOT NULL REFERENCES wf_process,
        arc_id          bigint             NOT NULL REFERENCES wf_arc,
        parent_token_id bigint             NOT NULL REFERENCES wf_node_token,
        pending         char(1)            NOT NULL,
        execution_type  int                NOT NULL REFERENCES wf_execution_type,
        create_date     datetime           DEFAULT getDate() NOT NULL,
        complete_date   datetime           NULL
      ) with identity_gap = 100

      print 'Creating index on wf_arc_token(process_id, complete_date, pending)'
      create index wf_arc_token_idx on wf_arc_token(process_id, complete_date, pending)

      print 'Creating index on wf_arc_token(parent_token_id)'
      create index wf_arc_token_parent_idx on wf_arc_token(parent_token_id)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_arc_token already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_node_token_parent')
  BEGIN
    exec( "
      print 'Creating table wf_node_token_parent'
      create table wf_node_token_parent
      (
         node_token_id bigint NOT NULL REFERENCES wf_node_token,
         arc_token_id  bigint NOT NULL REFERENCES wf_arc_token
      )

      print 'Creating primary key for table wf_node_token_parent'
      ALTER TABLE wf_node_token_parent
        ADD PRIMARY KEY (node_token_id, arc_token_id)

      print 'Creating index on wf_node_token_parent(arc_token_id)'
      create index wf_node_token_parent_idx on wf_node_token_parent (arc_token_id)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_node_token_parent already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_token_attr')
  BEGIN
    exec( "
      print 'Creating table wf_token_attr'
      create table wf_token_attr
      (
        attr_set_id  bigint        NOT NULL REFERENCES wf_node_token,
        name         varchar(64)   NOT NULL,
        value        varchar(1800) NULL
      )

      print 'Creating primary key for table wf_token_attr'
      ALTER TABLE wf_token_attr
        ADD PRIMARY KEY (attr_set_id, name)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_token_attr already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_token_set')
  BEGIN
    exec( "
      print 'Creating table wf_token_set'

      create table wf_token_set
      (
        id               bigint  IDENTITY NOT NULL PRIMARY KEY,
        process_id       bigint           NOT NULL REFERENCES wf_process,
        name             varchar(255)     NOT NULL,
        max_member_index int              NOT NULL,
        complete         char(1)          NOT NULL
      ) with identity_gap = 100
    ")
  END
ELSE
  BEGIN
    print 'Table wf_token_set already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_token_set_attr')
  BEGIN
    exec( "
      print 'Creating table wf_token_set_attr'

      create table wf_token_set_attr
      (
        token_set_id  bigint        NOT NULL REFERENCES wf_token_set,
        name          varchar(64)   NOT NULL,
        value         varchar(1800) NULL
      )

      print 'Creating primary key for table wf_token_set_env'
      ALTER TABLE wf_token_set_attr
        ADD PRIMARY KEY (token_set_id, name)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_token_set_attr already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_token_set_arcmem')
  BEGIN
    exec( "
      print 'Creating table wf_token_set_arcmem'

      create table wf_token_set_arcmem
      (
        id            bigint  IDENTITY NOT NULL PRIMARY KEY,
        token_set_id  bigint           NOT NULL REFERENCES wf_token_set,
        token_id      bigint           NOT NULL REFERENCES wf_arc_token,
        member_index  int              NOT NULL
      ) with identity_gap = 100

      print 'Creating index on wf_token_set_arcmem(token_id)'
      create index wf_token_set_arcmem_t_idx on wf_token_set_arcmem(token_id)

      print 'Creating index on wf_token_set_arcmem(token_set_id)'
      create index wf_token_set_arcmem_ts_idx on wf_token_set_arcmem(token_set_id)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_token_set_arcmem already exists'
  END
go

IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_token_set_nodemem')
  BEGIN
    exec( "
      print 'Creating table wf_token_set_nodemem'

      create table wf_token_set_nodemem
      (
        id            bigint  IDENTITY NOT NULL PRIMARY KEY,
        token_set_id  bigint           NOT NULL REFERENCES wf_token_set,
        token_id      bigint           NOT NULL REFERENCES wf_node_token,
        member_index  int              NOT NULL
      ) with identity_gap = 100

      print 'Creating index on wf_token_set_nodemem(token_id)'
      create index wf_token_set_nodemem_t_idx on wf_token_set_nodemem(token_id)

      print 'Creating index on wf_token_set_nodemem(token_set_id)'
      create index wf_token_set_nodemem_ts_idx on wf_token_set_nodemem(token_set_id)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_token_set_arcmem already exists'
  END
go


IF NOT EXISTS (SELECT name FROM sysobjects WHERE name='wf_token_set_member_attr')
  BEGIN
    exec( "
      print 'Creating table wf_token_set_member_attr'

      create table wf_token_set_member_attr
      (
        id            bigint IDENTITY  NOT NULL PRIMARY KEY,
        token_set_id  bigint           NOT NULL REFERENCES wf_token_set,
        member_index  int              NOT NULL,
        name          varchar(64)      NOT NULL,
        value         varchar(1800)    NULL
      )

      print 'Creating index on wf_token_set_nodemem(token_set_id)'
      create index wf_token_set_member_attr_idx on wf_token_set_member_attr(token_set_id)
    ")
  END
ELSE
  BEGIN
    print 'Table wf_token_set_member_attr already exists'
  END
go


IF NOT EXISTS (SELECT id FROM wf_process_state)
  BEGIN
    print 'Adding process states'
    insert into wf_process_state values ( 0, 'Created' )
    insert into wf_process_state values ( 1, 'Executing' )
    insert into wf_process_state values ( 2, 'Pending Completion' )
    insert into wf_process_state values ( 3, 'Completed' )
    insert into wf_process_state values ( 4, 'Pending Cancel' )
    insert into wf_process_state values ( 5, 'Canceled' )
  END
ELSE
  BEGIN
    print 'Process states already added'
  END
go

IF NOT EXISTS (SELECT id FROM wf_node_type)
  BEGIN
    print 'Adding node types'
    insert into wf_node_type values ( 'node', 'Generic node allowing for many inputs, many outputs and guards', 'node' )
    insert into wf_node_type values ( 'custom', 'Supertype for user custom node types', 'custom' )
    insert into wf_node_type values ( 'wait', 'Node which enters a wait state when executed', 'custom' )
    insert into wf_node_type values ( 'script', 'Node which executes a script', 'custom' )
    insert into wf_node_type values ( 'nested', 'Node which executes a nested process', 'custom' )
  END
ELSE
  BEGIN
    print 'Node types already added'
  END
go

IF NOT EXISTS (SELECT id FROM wf_node_join_type)
  BEGIN
    print 'Adding node join types'
    insert into wf_node_join_type values ( 0, 'Or: Join is completed whenever any arc token arrives at the node' )
    insert into wf_node_join_type values ( 1, 'And: Join is completed when there are arc tokens on all incoming arcs to a node' )
    insert into wf_node_join_type values ( 2, 'Label And: Join is completed when there are arc tokens on all incoming arcs with the same name as that of the arc that the current incoming arc token is on.' )
    insert into wf_node_join_type values ( 3, 'TokenSet And: Join is completed when all active arc tokens in the token set arrive and there are no active node tokens. Non token-set token will cause an exception to be raised.' )
    insert into wf_node_join_type values ( 4, 'Class: User defined join type' )
    insert into wf_node_join_type values ( 5, 'TokenSet Or: Join is completed when all active arc tokens in the token set arrive and there are no active node tokens. Non token-set tokens will fallback to the OR strategy.' )
    insert into wf_node_join_type values ( 6, 'First: Join is completed by the first arc token to arrive. Subsequent tokens are merged.' )
    insert into wf_node_join_type values ( 7, 'JoinLang: Evaluates the join lang statement given in the join parameter.' );
  END
ELSE
  BEGIN
    print 'Node Join types already added'
  END
go

IF NOT EXISTS (SELECT id FROM wf_guard_action)
  BEGIN
    print 'Adding guard actions'
    insert into wf_guard_action values ( 0, 'Accept Token' )
    insert into wf_guard_action values ( 1, 'Discard Token' )
    insert into wf_guard_action values ( 2, 'Skip Node' )
  END
ELSE
  BEGIN
    print 'Guard actions already added'
  END
go

IF NOT EXISTS (SELECT id FROM wf_execution_type)
  BEGIN
    print 'Adding execution types'
    insert into wf_execution_type values ( 0, 'Forward' )
    insert into wf_execution_type values ( 1, 'Forward/Backtracked' )
    insert into wf_execution_type values ( 2, 'Backtracked' )
    insert into wf_execution_type values ( 3, 'U-Turn' )
    insert into wf_execution_type values ( 4, 'U-Turn/Backtracked' )
  END
ELSE
  BEGIN
    print 'Execution types already added'
  END
go

