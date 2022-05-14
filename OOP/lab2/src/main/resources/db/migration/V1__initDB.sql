create table car
(
    id           serial PRIMARY KEY,
    name         character varying NOT NULL,
    available    bool              NOT NULL default true,
    cost_per_day integer                    default 100
);

insert into car(name)
values ('nis'),
       ('adsfd'),
       ('sjdfhfdj');

insert into car(name, available)
values ('nissd', false);

create table book
(
    id                   serial PRIMARY KEY,
    client_id            character varying NOT NULL,
    car_id               integer           NOT NULL,
    rental_period_in_day integer           NOT NULL,
    cost                 integer           NOT NULL,
    allow                bool              NOT NULL default true,
    paid                 bool              NOT NULL default false,
    cause                character varying          default '',
    FOREIGN KEY (car_id) REFERENCES car (id)
);

insert into book(client_id, car_id, rental_period_in_day, cost)
VALUES ('91e12fce-3bde-4c22-a1c3-dbd898a6ffcd', '2', 12, 120);

create table report
(
    id           serial PRIMARY KEY,
    client_id    character varying NOT NULL,
    car_id       integer           NOT NULL,
    has_injuries bool              NOT NULL,
    message      character varying default '',
    cost         integer default 0,
    FOREIGN KEY (car_id) REFERENCES car (id)
);
