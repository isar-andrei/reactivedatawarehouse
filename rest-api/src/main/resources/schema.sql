create table user_dim
(
    user_id serial not null
        constraint user_dim_pkey
            primary key,
    first_name varchar not null,
    last_name varchar not null,
    weight numeric(5,2) not null,
    height numeric(4,2) not null,
    gender varchar not null,
    birthday date not null,
    country varchar not null,
    enabled boolean default true,
    user_created_at timestamp not null,
    updated_at timestamp not null
);

create table nutrition_dim
(
    nutrition_id serial not null
        constraint nutrition_dim_pkey
            primary key,
    name varchar not null
        constraint nutrition_dim_name_key
            unique,
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

create index d_date_date_actual_idx
    on date_dim (date);

create table diet_fact
(
    diet_id serial not null
        constraint diet_fact_pkey
            primary key,
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

