SET FOREIGN_KEY_CHECKS = 0;

alter table users
    add file_id varchar(36) comment 'The user avatar image file' after status
;

alter table users
    add foreign key (file_id) references files(id)
        on update cascade
        on delete set null
;

SET FOREIGN_KEY_CHECKS = 1;
