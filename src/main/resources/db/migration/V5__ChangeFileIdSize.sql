SET FOREIGN_KEY_CHECKS = 0;

alter table files
    modify id varchar(100)
;

alter table users
    rename column file_id to avatar_id
;

alter table attachments
    modify file_id varchar(100)
;

SET FOREIGN_KEY_CHECKS = 1;
