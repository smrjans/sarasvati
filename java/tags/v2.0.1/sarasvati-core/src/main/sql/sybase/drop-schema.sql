-- Fix circular reference
IF EXISTS( select name from sysobjects where name = 'FK_process_parent' and type='ri' )
  BEGIN
    exec( "alter table wf_process drop constraint FK_process_parent" )
  END
go

-- DROP EXISTING TABLES
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_task') drop table wf_task
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_task_state') drop table wf_task_state
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_node_task') drop table wf_node_task

IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_token_set_attr') drop table wf_token_set_attr
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_token_set_member_attr') drop table wf_token_set_member_attr
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_token_set_arcmem') drop table wf_token_set_arcmem
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_token_set_nodemem') drop table wf_token_set_nodemem
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_token_set') drop table wf_token_set

IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_token_attr') drop table wf_token_attr
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_node_token_parent') drop table wf_node_token_parent
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_arc_token') drop table wf_arc_token
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_node_token') drop table wf_node_token
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_execution_type') drop table wf_execution_type

IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_process_attr') drop table wf_process_attr
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_process_listener') drop table wf_process_listener
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_process') drop table wf_process
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_process_state') drop table wf_process_state

IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_arc') drop table wf_arc
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_node_ref') drop table wf_node_ref
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_node_attr') drop table wf_node_attr
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_node') drop table wf_node
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_node_type') drop table wf_node_type
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_node_join_type') drop table wf_node_join_type
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_guard_action') drop table wf_guard_action

IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_external_attr') drop table wf_external_attr
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_external') drop table wf_external

IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_graph_listener') drop table wf_graph_listener
IF EXISTS (SELECT * FROM sysobjects WHERE name='wf_graph') drop table wf_graph
go