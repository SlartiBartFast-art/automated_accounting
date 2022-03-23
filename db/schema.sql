create table socks
(
    id          serial not null,
    color       varchar(255),
    cotton_part int4   not null,
    quantity    int4   not null,
    primary key (id)
);

insert into socks(color, cotton_part, quantity)
VALUES('red', 40, 15);
insert into socks(color, cotton_part, quantity)
VALUES('green', 55, 52);
insert into socks(color, cotton_part, quantity)
VALUES('yellow', 60, 17);
insert into socks(color, cotton_part, quantity)
VALUES('white', 70, 80);
insert into socks(color, cotton_part, quantity)
VALUES('blue', 45, 45);
insert into socks(color, cotton_part, quantity)
VALUES('dark', 35, 75);
insert into socks(color, cotton_part, quantity)
VALUES('red', 70, 25);
insert into socks(color, cotton_part, quantity)
VALUES('green', 75, 70);
insert into socks(color, cotton_part, quantity)
VALUES('white', 80, 180);