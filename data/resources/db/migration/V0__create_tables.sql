create table if not exists auth (
    id uuid primary key default gen_random_uuid(),
    login varchar(255) unique not null,
    password varchar(255) not null
);

create table if not exists priority (
    name varchar(255) primary key not null,
    award integer not null
);

create table if not exists task (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references auth(id),
    name text not null default '',
    description text not null default '',
    deadline timestamp not null default current_timestamp,
    priority varchar(255) references priority(name),
    is_completed boolean not null default false
);

create table if not exists pet (
    id uuid primary key default gen_random_uuid(),
    kind varchar(255) not null,
    price integer not null
);

create table if not exists pet_image (
    id uuid primary key default gen_random_uuid(),
    pet_id uuid not null references pet(id),
    url text not null,
    min_happiness integer not null,
    max_happiness integer not null
);

create table if not exists pet_quote (
    id uuid primary key default gen_random_uuid(),
    pet_id uuid not null references pet(id),
    quote text not null,
    min_happiness integer not null,
    max_happiness integer not null
);

create table if not exists app_user (
    id uuid primary key references auth(id) on delete cascade,
    pet_name varchar(255) not null,
    happiness integer not null,
    coins integer not null,
    current_pet uuid references pet(id)
);

create table if not exists possession (
    id uuid primary key default gen_random_uuid(),
    owner_id uuid not null references app_user(id),
    petid uuid not null references pet(id)
);
