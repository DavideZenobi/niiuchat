SET FOREIGN_KEY_CHECKS = 0;

create table files(id varchar(36) primary key,
                   name varchar(100),
                   type varchar(30),
                   path varchar(255),
                   create_date datetime default now()
);

alter table attachments
    drop column path,
    add file_id varchar(36) after type
;

alter table attachments
    add foreign key (file_id) references files(id)
        on update cascade
        on delete set null
;

SET FOREIGN_KEY_CHECKS = 1;
