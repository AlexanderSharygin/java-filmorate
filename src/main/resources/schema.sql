create table if not exists FRIEND_STATUSES
(
    STATUS_ID BIGINT auto_increment
        primary key,
    NAME      CHARACTER VARYING(50) not null
);

create table if not exists GENRES
(
    GENRE_ID BIGINT auto_increment
        primary key,
    NAME     CHARACTER VARYING(50) not null
);

create table if not exists RATES
(
    ID   BIGINT auto_increment
        primary key,
    NAME CHARACTER VARYING(50) not null
);

create table if not exists FILMS
(
    ID           BIGINT auto_increment
        primary key,
    NAME         CHARACTER VARYING(50)  not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    RATE_ID      BIGINT                 not null,
    constraint FILMS_UNIQUE
        unique (NAME, RELEASE_DATE),
    constraint FILMS_FK
        foreign key (RATE_ID) references RATES
);

create index if not exists FILMS_FK_INDEX_3
    on FILMS (RATE_ID);

create unique index if not exists PRIMARY_KEY_3
    on FILMS (ID);

create table if not exists GENRES_FILMS
(
    GENRE_ID BIGINT not null,
    FILMS_ID BIGINT not null,
    constraint GENRES_FILMS_PK
        primary key (GENRE_ID, FILMS_ID),
    constraint GENRES_FILMS_FK
        foreign key (FILMS_ID) references FILMS
            on update cascade on delete set null,
    constraint GENRES_FILMS_FK2
        foreign key (GENRE_ID) references GENRES
            on update cascade on delete set null
);

create table if not exists USERS
(
    USER_ID  BIGINT auto_increment primary key,
    NAME     CHARACTER VARYING(50) not null,
    EMAIL    CHARACTER VARYING(50) not null
        unique,
    BIRTHDAY DATE                  not null,
    LOGIN    CHARACTER VARYING(50) not null
);

create unique index if not exists PRIMARY_KEY_4D
    on USERS (USER_ID);


create table if not exists LIKES
(
    USER_ID BIGINT not null,
    FILM_ID BIGINT not null,
    constraint LIKES_PK2
        primary key (USER_ID, FILM_ID),
    constraint LIKES_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade,
    constraint LIKES_FK2
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade
);

create table if not exists USERS_USERS
(
    USERS_ID  BIGINT not null,
    FRIEND_ID BIGINT not null,
    STATUS_ID BIGINT not null,
    constraint USERS_USERS_PK2
        primary key (USERS_ID, FRIEND_ID),
    constraint USERS_USERS_FK
        foreign key (USERS_ID) references USERS
            on update cascade on delete cascade,
    constraint USERS_USERS_FK2
        foreign key (FRIEND_ID) references USERS
            on update cascade on delete cascade,
    constraint USERS_USERS_FK3
        foreign key (STATUS_ID) references FRIEND_STATUSES
            on update cascade on delete cascade,
    check ("USERS_USERS"."USERS_ID" <> "USERS_USERS"."FRIEND_ID")
);



