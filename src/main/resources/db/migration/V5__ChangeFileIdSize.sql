SET FOREIGN_KEY_CHECKS = 0;

alter table files
    modify id varchar(100)
;

alter table users
    change column file_id avatar_id varchar(100) comment 'The user avatar image file' after status
;

alter table attachments
    modify file_id varchar(100)
;

SET FOREIGN_KEY_CHECKS = 1;
