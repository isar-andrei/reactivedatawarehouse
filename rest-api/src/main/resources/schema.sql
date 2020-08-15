------------------------ user_dim ------------------------
create table user_dim
(
    user_id serial not null
        constraint user_dim_pkey
            primary key,
    user_uuid uuid not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    weight numeric(5,2) not null,
    height numeric(4,2) not null,
    gender varchar(6) not null,
    birthday date not null,
    updated_at timestamp default now() not null,
    username varchar(50) not null,
    email varchar(50) not null,
    user_created_at timestamp default now() not null
);

alter table user_dim owner to postgres;

create unique index user_dim_user_uuid_uindex
    on user_dim (user_uuid);

create unique index user_dim_username_uindex
    on user_dim (username);

create unique index user_dim_email_uindex
    on user_dim (email);

------------------------ nutritrion_dim ------------------------
create table nutrition_dim
(
    nutrition_id serial not null
        constraint nutrition_dim_pkey
            primary key,
    nutrition_uuid uuid not null,
    name varchar(50) not null,
    calories real not null
        constraint nutrition_dim_calories_check
            check (calories >= (0)::double precision),
    fat real default 0 not null
        constraint nutrition_dim_fat_check
            check (fat >= (0)::double precision),
    saturated_fat real default 0 not null
        constraint nutrition_dim_saturated_fat_check
            check (saturated_fat >= (0)::double precision),
    carbohydrates real default 0 not null
        constraint nutrition_dim_carbohydrates_check
            check (carbohydrates >= (0)::double precision),
    fiber real default 0 not null
        constraint nutrition_dim_fiber_check
            check (fiber >= (0)::double precision),
    sugar real default 0 not null
        constraint nutrition_dim_sugar_check
            check (sugar >= (0)::double precision),
    protein real default 0 not null
        constraint nutrition_dim_protein_check
            check (protein >= (0)::double precision),
    sodium real default 0 not null
        constraint nutrition_dim_sodium_check
            check (sodium >= (0)::double precision)
);

alter table nutrition_dim owner to postgres;

create unique index nutrition_dim_nutrition_id_uindex
    on nutrition_dim (nutrition_id);

create unique index nutrition_dim_nutrition_uuid_uindex
    on nutrition_dim (nutrition_uuid);

------------------------ time_dim ------------------------
create table time_dim
(
    time_id integer not null
        constraint time_dim_pk
            primary key,
    time char(5) not null,
    hour char(2) not null,
    minute char(2) not null,
    period varchar(20) not null
);

alter table time_dim owner to postgres;

------------------------ date_dim ------------------------

create table date_dim
(
    date_id integer not null
        constraint d_date_date_dim_id_pk
            primary key,
    date date not null,
    epoch bigint not null,
    day_name varchar(9) not null,
    day integer not null,
    month integer not null,
    month_name varchar(9) not null,
    year integer not null,
    weekend boolean not null
);

alter table date_dim owner to postgres;

create index d_date_date_actual_idx
    on date_dim (date);

------------------------ diet_fact ------------------------

create table diet_fact
(
    diet_id serial not null
        constraint diet_fact_pkey
            primary key,
    diet_uuid uuid not null,
    nutrition_key integer not null
        constraint diet_fact_nutrition_key_fkey
            references nutrition_dim,
    user_key integer not null
        constraint diet_fact_user_key_fkey
            references user_dim,
    time_key integer not null
        constraint diet_fact_time_key_fkey
            references time_dim,
    date_key integer not null
        constraint diet_fact_date_key_fkey
            references date_dim,
    serving_quantity real not null
        constraint diet_fact_serving_quantity_check
            check (serving_quantity > (0)::double precision),
    calories_consumed real not null
        constraint diet_fact_calories_consumed_check
            check (calories_consumed >= (0)::double precision),
    diet_created_at timestamp not null
);

alter table diet_fact owner to postgres;

create unique index diet_fact_diet_id_uindex
    on diet_fact (diet_id);

------------------------ exercise_dim ------------------------

create table exercise_dim
(
    exercise_id serial not null
        constraint exercise_dim_pk
            primary key,
    exercise_uuid uuid not null,
    compcode varchar(5) not null,
    met numeric(3,1) not null,
    category varchar(25) not null,
    description varchar(250) not null
);

alter table exercise_dim owner to postgres;

create unique index exercise_dim_id_uindex
    on exercise_dim (exercise_id);

create unique index exercise_dim_uuid_uindex
    on exercise_dim (exercise_uuid);

------------------------ activity_fact ------------------------

create table activity_fact
(
    activity_id serial not null
        constraint activity_fact_pk
            primary key,
    activity_uuid uuid not null,
    activity_exercise_key integer not null
        constraint activity_fact_activity_exercise_key_fkey
            references exercise_dim,
    activity_user_key integer not null
        constraint activity_fact_activity_user_key_fkey
            references user_dim,
    activity_time_key integer not null
        constraint activity_fact_activity_time_key_fkey
            references time_dim,
    activity_date_key integer not null
        constraint activity_fact_activity_date_key_fkey
            references date_dim,
    duration integer not null,
    calories_burned real not null,
    activity_created_at timestamp not null
);

alter table activity_fact owner to postgres;

create unique index activity_fact_activity_id_uindex
    on activity_fact (activity_id);

------------------------ triggers ------------------------

create function trigger_set_timestamp() returns trigger
    language plpgsql
as $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$;

alter function trigger_set_timestamp() owner to postgres;

create trigger set_timestamp
    before update
    on user_dim
    for each row
execute procedure trigger_set_timestamp();

------------------------ TimescaleDB ------------------------
-- psql -U postgres -h localhost
-- CREATE database datawarehouse;
-- \c datawarehouse
-- CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;
--
--
-- SELECT create_hypertable('activity_fact', 'activity_id', chunk_time_interval => 1000);
-- SELECT create_hypertable('diet_fact', 'diet_id', chunk_time_interval => 1000);

