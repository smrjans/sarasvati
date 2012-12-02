drop table if exists wf_task;
drop table if exists wf_task_state;
drop table if exists wf_node_task;

create table wf_node_task
(
  id          int  NOT NULL PRIMARY KEY REFERENCES wf_node,
  name        text NOT NULL,
  description text NOT NULL
);

create table wf_task_state
(
   id          int  NOT NULL  PRIMARY KEY,
   description text NOT NULL
);

insert into wf_task_state values ( 0, 'Open' );
insert into wf_task_state values ( 1, 'Complete' );
insert into wf_task_state values ( 2, 'Rejected' );
insert into wf_task_state values ( 3, 'Canceled' );

create table wf_task
(
  id            serial NOT NULL PRIMARY KEY,
  node_token_id int    NULL REFERENCES wf_node_token,
  name          text   NOT NULL,
  description   text   NOT NULL,
  state         int    NOT NULL REFERENCES wf_task_state
);
