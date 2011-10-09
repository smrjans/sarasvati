-- DROP EXISTING TABLES
CREATE OR REPLACE PROCEDURE sarasvatiDropSequence(seqName VARCHAR2)
    AS
        seqCount INTEGER;
    BEGIN
        SELECT COUNT(*) INTO seqCount FROM user_sequences WHERE LOWER(sequence_name) = LOWER(seqName);

    IF seqCount > 0 THEN
      EXECUTE IMMEDIATE ('drop sequence ' || seqName);
    END IF;
END;
/

CREATE OR REPLACE PROCEDURE sarasvatiDropTable(tableName VARCHAR2)
    AS
        tableCount INTEGER;
    BEGIN
        SELECT COUNT(*) INTO tableCount FROM user_tables WHERE LOWER(table_name) = LOWER(tableName);

    IF tableCount > 0 THEN
      EXECUTE IMMEDIATE ('drop table ' || tableName);
      sarasvatiDropSequence( tableName || '_seq' );
    END IF;
END;
/

execute sarasvatiDropTable( 'wf_task' );
execute sarasvatiDropTable( 'wf_task_state' );
execute sarasvatiDropTable( 'wf_node_task' );

create sequence wf_task_seq start with 1 increment by 1;

create table wf_node_task
(
  id          number(20)   NOT NULL PRIMARY KEY REFERENCES wf_node,
  name        varchar(127) NOT NULL,
  description varchar(255) NOT NULL
);

create table wf_task_state
(
   id          int          NOT NULL  PRIMARY KEY,
   description varchar(255) NOT NULL
);

insert into wf_task_state values ( 0, 'Open' );
insert into wf_task_state values ( 1, 'Complete' );
insert into wf_task_state values ( 2, 'Rejected' );
insert into wf_task_state values ( 3, 'Canceled' );

create table wf_task
(
  id            number(20)   NOT NULL PRIMARY KEY,
  node_token_id number(20)   NULL REFERENCES wf_node_token,
  name          varchar(127) NOT NULL,
  description   varchar(255) NOT NULL,
  state         int          NOT NULL REFERENCES wf_task_state
);

drop procedure sarasvatiDropTable;
drop procedure sarasvatiDropSequence;