create sequence seq_user start with 1000;
grant select, usage on seq_user to postgres;
create table sec_user
(
    id                bigint         not null primary key,
    first_name        varchar(255)   not null,
    last_name         varchar(255)   not null,
    user_type         varchar(255)   not null,
    status            varchar(255)   not null,
    email             varchar(255)   not null,
    registration_date timestamp(255) not null,
    mobile_number     varchar(255)   not null,
    password          varchar(255)   not null,
    role              varchar(255)   not null,
    constraint unique_email unique (email),
    constraint unique_mobile_number unique (mobile_number)
);
grant select, insert, update on sec_user to postgres;

create sequence seq_attachment start with 1000;
grant select, usage on seq_attachment to postgres;
create table attachment
(
    id             bigint       not null primary key,
    name           varchar(255) not null,
    type           varchar(255) not null,
    content_type   varchar(255) not null,
    creation_ts    timestamp    not null,
    is_active      boolean      not null,
    author_user_id bigint
        constraint fk_attachment_author_user_id references sec_user,
    constraint uq_attachment_name unique (name)
);
grant select, insert, update, delete on attachment to postgres;

create sequence seq_token start with 1000;
grant select, usage on seq_token to postgres;
create table token
(
    id         bigint       not null primary key,
    token      varchar(255) not null unique,
    token_type varchar(255) not null,
    revoked    boolean      not null,
    expired    boolean      not null,
    user_id    bigint,
    foreign key (user_id) references sec_user (id),
    constraint unique_token unique (token)
);
grant select, insert, update on token to postgres;

create sequence seq_course start with 1000;
grant select, usage on seq_course to postgres;
create table course
(
    id               bigint        not null primary key,
    title            varchar(255)  not null,
    description      varchar(4000) not null,
    category         varchar(255)  not null,
    instructor_id    bigint        not null,
    max_capacity     integer       not null,
    current_capacity integer       not null,
    syllabus_id      bigint references attachment (id),
    foreign key (instructor_id) references sec_user (id),
    constraint uq_title unique (title)
);
grant select, insert, update, delete on course to postgres;

create table course_student
(
    course_id  bigint not null,
    student_id bigint not null,
    constraint fk_course foreign key (course_id) references course (id),
    constraint fk_student foreign key (student_id) references sec_user (id),
    primary key (course_id, student_id)
);

create sequence seq_lesson start with 1000;
grant select, usage on seq_lesson to postgres;
create table lesson
(
    id                  bigint        not null primary key,
    course_id           bigint        not null,
    title               varchar(255)  not null,
    content             varchar(4000) not null,
    duration_in_minutes integer       not null,
    start_time          timestamp     not null,
    foreign key (course_id) references course (id),
    constraint uq_course_title_start_time unique (course_id, title, start_time)
);
grant select, insert, update, delete on lesson to postgres;

create table lesson_attachments
(
    lesson_id     bigint not null,
    attachment_id bigint not null,
    constraint fk_lesson_lesson_attachment_id foreign key (attachment_id) references attachment (id),
    constraint fk_lesson_attachment_lesson_id foreign key (lesson_id) references lesson (id)
);

create sequence seq_enrollment start with 1000;
grant select, usage on seq_enrollment to postgres;
create table enrollment
(
    id              bigint         not null primary key,
    student_id      bigint         not null,
    course_id       bigint         not null,
    enrollment_date timestamp(255) not null,
    progress        numeric        not null,
    status          varchar(255)   not null,
    is_active       boolean        not null,
    foreign key (student_id) references sec_user (id),
    foreign key (course_id) references course (id),
    constraint unique_enrollment unique (student_id, course_id)
);
grant select, insert, update, delete on enrollment to postgres;

create sequence seq_review start with 1000;
grant select, usage on seq_review to postgres;
create table review
(
    id         bigint         not null primary key,
    course_id  bigint         not null,
    student_id bigint         not null,
    comment    varchar(4000),
    rating     int            not null,
    create_ts  timestamp(255) not null,
    foreign key (course_id) references course (id),
    foreign key (student_id) references sec_user (id)
);
grant select, insert, update, delete on review to postgres;