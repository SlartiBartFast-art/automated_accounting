create table colors
(
    id       serial not null,
    coloring varchar(255),
    primary key (id)
);

create table socks
(
    id          serial not null,
    cotton_part int4   not null,
    quantity    int4   not null,
    color_id int8 references colors (id),
    primary key (id)
);