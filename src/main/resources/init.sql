CREATE DATABASE IF NOT EXISTS customerdb2;
USE customerdb2;

drop table if exists customer;

CREATE TABLE customer
(
    id integer NOT NULL,
    fullname character varying(255),
    email character varying(255),
    CONSTRAINT customer_pkey PRIMARY KEY (id)
);

drop table if exists debezium_offsets;

CREATE TABLE debezium_offsets
(
    ke character varying(255) NOT NULL,
    va blob,
    CONSTRAINT debezium_offsets_pkey PRIMARY KEY (ke)
);