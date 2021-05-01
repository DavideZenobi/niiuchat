SET FOREIGN_KEY_CHECKS = 0;

alter table files
    add media_type varchar(36) comment 'The file media type' after name
;

SET FOREIGN_KEY_CHECKS = 1;
