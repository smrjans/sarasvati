-- DROP EXISTING TABLES
drop table if exists wf_graph cascade;
drop table if exists wf_graph_listener cascade;
drop table if exists wf_process_state cascade;
drop table if exists wf_process cascade;
drop table if exists wf_process_attr cascade;
drop table if exists wf_process_listener cascade;
drop table if exists wf_node_type cascade;
drop table if exists wf_node_join_type cascade;
drop table if exists wf_node cascade;
drop table if exists wf_node_attr cascade;
drop table if exists wf_external cascade;
drop table if exists wf_external_attr cascade;
drop table if exists wf_node_ref cascade;
drop table if exists wf_arc cascade;
drop table if exists wf_guard_action cascade;
drop table if exists wf_execution_type cascade;
drop table if exists wf_node_token cascade;
drop table if exists wf_arc_token cascade;
drop table if exists wf_node_token_parent cascade;
drop table if exists wf_token_attr cascade;
drop table if exists wf_token_set cascade;
drop table if exists wf_token_set_attr cascade;
drop table if exists wf_token_set_arcmem cascade;
drop table if exists wf_token_set_nodemem cascade;
drop table if exists wf_token_set_member_attr cascade;

-- -----------------------------------------------------------------------------
-- CREATE NEW TABLES
-- -----------------------------------------------------------------------------

create table wf_graph (
    id          int               not null primary key auto_increment,
    name        varchar(255)      not null,
    version     int               not null,
    custom_id   varchar(255)      null,
    create_date timestamp         not null default current_timestamp
) engine=innodb charset=utf8;

alter table wf_graph
    add constraint wf_graph_unique
        unique (name,version);
        
create table wf_graph_listener (
    id              int           not null primary key auto_increment,
    type            varchar(255)  not null,
    event_type_mask int           not null,
    graph_id        int           not null references wf_graph
) engine=innodb charset=utf8;

alter table wf_graph_listener
    add constraint wf_graph_listener_unique
        unique(graph_id, type);
        
create table wf_process_state (
    id          int               not null primary key auto_increment,
    description varchar(255)      not null
) engine=innodb charset=utf8;

insert into wf_process_state (description) values ( 'Created' );
insert into wf_process_state (description) values ( 'Executing' );
insert into wf_process_state (description) values ( 'Pending Completion' );
insert into wf_process_state (description) values ( 'Completed' );
insert into wf_process_state (description) values ( 'Pending Cancel' );
insert into wf_process_state (description) values ( 'Canceled' );

create table wf_process (
    id              int           not null primary key auto_increment,
    graph_id        int           not null references wf_graph,
    state           int           not null references wf_process_state,
    parent_token_id int           null,
    create_date     timestamp     not null default current_timestamp,
    version         int           not null
) engine=innodb charset=utf8;

create index wf_process_idx on wf_process (graph_id, state);

create table wf_process_attr (
  process_id  int               not null references wf_process,
  name        varchar(64)       not null,
  value       varchar(2000)     not null
) engine=innodb charset=utf8;

alter table wf_process_attr
    add primary key (process_id, name);
    
create table wf_process_listener (
    id              int           not null primary key auto_increment,
    type            varchar(255)  not null,
    event_type_mask int           not null,
    process_id      int           not null references wf_process
) engine=innodb charset=utf8;

alter table wf_process_listener
    add constraint wf_listener_unique
        unique(process_id, type);
        
create table wf_node_type (
    id          varchar(255)      not null primary key,
    description varchar(255)      not null,
    behaviour   varchar(255)      not null references wf_node_type
) engine=innodb charset=utf8;

insert into wf_node_type values ( 'node', 'Generic node allowing for many inputs, many outputs and guards', 'node' );
insert into wf_node_type values ( 'custom', 'Supertype for user custom node types', 'custom' );
insert into wf_node_type values ( 'wait', 'Node which enters a wait state when executed', 'custom' );
insert into wf_node_type values ( 'script', 'Node which executes a script', 'custom' );
insert into wf_node_type values ( 'nested', 'Node which executes a nested process', 'custom' );

create table wf_node_join_type (
    id          int               not null primary key auto_increment,
    description varchar(255)      not null
) engine=innodb charset=utf8;

insert into wf_node_join_type (description) values ( 'Or: Join is completed whenever any arc token arrives at the node' );
insert into wf_node_join_type (description) values ( 'And: Join is completed when there are arc tokens on all incoming arcs to a node' );
insert into wf_node_join_type (description) values ( 'Label And: Join is completed when there are arc tokens on all incoming arcs with the same name as that of the arc that the current incoming arc token is on.' );
insert into wf_node_join_type (description) values ( 'TokenSet And: Join is completed when all active arc tokens in the token set arrive and there are no active node tokens. Non token-set token will cause an exception to be raised.' );
insert into wf_node_join_type (description) values ( 'Class: User defined join type' );
insert into wf_node_join_type (description) values ( 'TokenSet Or: Join is completed when all active arc tokens in the token set arrive and there are no active node tokens. Non token-set tokens will fallback to the OR strategy.' );
insert into wf_node_join_type (description) values ( 'First: Join is completed by the first arc token to arrive. Subsequent tokens are merged.' );
insert into wf_node_join_type (description) values ( 'JoinLang: Evaluates the join lang statement given in the join parameter.' );

create table wf_node (
    id              int           not null primary key auto_increment,
    graph_id        int           not null references wf_graph,
    name            varchar(255)  not null,
    join_type       int           not null references wf_node_join_type,
    join_param      varchar(255)  null,
    is_start        char(1)       not null,
    type            varchar(255)  not null references wf_node_type,
    guard           varchar(255)  null
) engine=innodb charset=utf8;

alter table wf_node
    add constraint wf_node_unique
        unique(graph_id, name);
        
create table wf_node_attr (
    node_id  int                  not null references wf_node,
    name     varchar(64)          not null,
    value    varchar(2000)        not null
) engine=innodb charset=utf8;

alter table wf_node_attr
    add primary key (node_id, name);
    
create table wf_external (
    id                int         not null primary key auto_increment,
    name              text        not null,
    graph_id          int         not null references wf_graph,
    external_graph_id int         not null references wf_graph
) engine=innodb charset=utf8;

create table wf_external_attr (
  external_id  int                not null references wf_external,
  name         varchar(255)       not null,
  value        varchar(2000)      null
) engine=innodb charset=utf8;

alter table wf_external_attr
    add primary key (external_id, name);
    
create table wf_node_ref (
    id          int               not null primary key auto_increment,
    node_id     int               not null references wf_node,
    graph_id    int               not null references wf_graph,
    parent_id   int               null references wf_node_ref,
    external_id int               null references wf_external
) engine=innodb charset=utf8;

create index wf_node_ref_graph_idx on wf_node_ref (graph_id);

create table wf_arc (
    id            int             not null primary key auto_increment,
    graph_id      int             not null references wf_graph,
    a_node_ref_id int             not null references wf_node_ref,
    z_node_ref_id int             not null references wf_node_ref,
    name          varchar(255)    null
) engine=innodb charset=utf8;

create index wf_arc_graph_idx on wf_arc (graph_id);

create table wf_guard_action (
    id          int               not null primary key auto_increment,
    name         varchar(50)      not null
) engine=innodb charset=utf8;

insert into wf_guard_action (name) values ( 'Accept Token' );
insert into wf_guard_action (name) values ( 'Discard Token' );
insert into wf_guard_action (name) values ( 'Skip Node' );

create table wf_execution_type (
    id          int               not null primary key auto_increment,
    name        varchar(255)      not null
) engine=innodb charset=utf8;

insert into wf_execution_type (name) values ( 'Forward' );
insert into wf_execution_type (name) values ( 'Forward/Backtracked' );
insert into wf_execution_type (name) values ( 'Backtracked' );
insert into wf_execution_type (name) values ( 'U-Turn' );
insert into wf_execution_type (name) values ( 'U-Turn/Backtracked' );

create table wf_node_token (
    id              int             not null primary key auto_increment,
    process_id      int             not null references wf_process,
    node_ref_id     int             not null references wf_node_ref,
    attr_set_id     int             null     references wf_node_token,
    create_date     timestamp       not null default current_timestamp,
    guard_action    int             null references wf_guard_action,
    execution_type  int             not null references wf_execution_type,
    complete_date   timestamp       null
) engine=innodb charset=utf8;

create index wf_node_token_idx on wf_node_token(process_id, complete_date);

alter table wf_process
    add constraint FK_process_parent
        foreign key (parent_token_id) references wf_node_token(id);
        
create table wf_arc_token (
    id              int             not null primary key auto_increment,
    process_id      int             not null references wf_process,
    arc_id          int             not null references wf_arc,
    parent_token_id int             not null references wf_node_token,
    pending         char(1)         not null,
    execution_type  int             not null references wf_execution_type,
    create_date     timestamp       not null default current_timestamp,
    complete_date   timestamp       null
);

create index wf_arc_token_idx on wf_arc_token(process_id, complete_date, pending);

create index wf_arc_token_parent_idx on wf_arc_token(parent_token_id);

create table wf_node_token_parent (
    node_token_id   int             not null references wf_node_token,
    arc_token_id    int             not null references wf_arc_token
);

alter table wf_node_token_parent
    add primary key (node_token_id, arc_token_id);

create index wf_node_token_parent_idx on wf_node_token_parent (arc_token_id);

create table wf_token_attr (
    attr_set_id     int             not null references wf_node_token,
    name            varchar(64)     not null,
    value           varchar(2000)   not null
);

alter table wf_token_attr
    add primary key (attr_set_id, name);
    
create table wf_token_set (
    id              int             not null primary key auto_increment,
    process_id      int             not null references wf_process,
    name            varchar(255)    not null,
    max_member_index int            not null,
    complete        char(1)         not null
);

create table wf_token_set_attr (
    token_set_id    int             not null references wf_token_set,
    name            varchar(64)     not null,
    value           varchar(2000)   null
);

alter table wf_token_set_attr
    add primary key (token_set_id, name);
    
create table wf_token_set_arcmem (
    id              int             not null primary key auto_increment,
    token_set_id    int             not null references wf_token_set,
    token_id        int             not null references wf_arc_token,
    member_index    int             not null
);

create index wf_token_set_arcmem_t_idx on wf_token_set_arcmem(token_id);

create index wf_token_set_arcmem_ts_idx on wf_token_set_arcmem(token_set_id);

create table wf_token_set_nodemem (
    id              int             not null primary key auto_increment,
    token_set_id    int             not null references wf_token_set,
    token_id        int             not null references wf_node_token,
    member_index    int             not null
);

create index wf_token_set_nodemem_t_idx on wf_token_set_nodemem(token_id);

create index wf_token_set_nodemem_ts_idx on wf_token_set_nodemem(token_set_id);

create table wf_token_set_member_attr (
    id              int             not null primary key auto_increment,
    token_set_id    int             not null references wf_token_set,
    member_index    int             not null,
    name            varchar(64)     not null,
    value           varchar(2000)   not null
);

create index wf_token_set_member_attr_idx on wf_token_set_member_attr(token_set_id);