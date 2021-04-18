SET FOREIGN_KEY_CHECKS = 0;

-- user_status
create table user_status(id varchar(20) primary key);

insert into user_status values
('ACTIVE'),
('INACTIVE')
;

-- users

create table users(id bigint primary key auto_increment,
                   username varchar(30) unique,
                   password varchar(60),
                   email varchar(100) unique,
                   status varchar(20) default 'ACTIVE',
                   create_date datetime default now(),
                   update_date datetime,
                   foreign key (status) references user_status(id)
                       on update cascade
                       on delete restrict
);

create index users_email_status on users(email, status);

-- roles

create table roles(
    id varchar(20) primary key
);

insert into roles values
('ROLE_ADMIN'),
('ROLE_USER')
;

-- user roles

create table users_roles(user_id bigint,
                         role_id varchar(20),
                         primary key (user_id, role_id)
);

alter table users_roles
    add foreign key (user_id) references users(id)
        on update cascade
        on delete cascade,
    add foreign key (role_id) references roles(id)
        on update cascade
           on delete cascade
;

-- chats

create table chats(group_id varchar(36),
                   user_id bigint,
                   create_date datetime,
                   update_date datetime,
                   primary key (group_id, user_id),
                   foreign key (user_id) references users(id)
                       on update cascade
                       on delete cascade
);

-- messages

create table messages(id varchar(36) primary key,
                      group_id varchar(36),
                      user_id bigint,
                      has_attachment boolean default false,
                      message text,
                      timestamp bigint,
                      create_date datetime
);

create index messages_group_id_timestamp on messages(group_id, timestamp);

create index messages_group_id_user_id on messages(group_id, user_id);

alter table messages
    add foreign key (group_id, user_id) references chats(group_id, user_id)
        on update cascade
        on delete cascade
;

-- historical_messages

create table historical_messages(id varchar(36) primary key,
                                 group_id varchar(36),
                                 user_id bigint,
                                 has_attachment boolean default false,
                                 message text,
                                 timestamp bigint,
                                 create_date datetime
);

create index historical_messages_group_id_user_id on historical_messages(group_id, user_id);

-- attachments

create table attachments(id varchar(36) primary key,
                         message_id varchar(36),
                         path varchar(255),
                         type varchar(5),
                         create_date datetime,
                         foreign key (message_id) references messages(id)
                             on update cascade
                             on delete set null
);

SET FOREIGN_KEY_CHECKS = 1;
