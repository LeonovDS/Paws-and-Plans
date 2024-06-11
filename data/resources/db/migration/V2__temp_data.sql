insert into auth(login, password) values ('demo', 'demo');

insert into task(user_id, name, description, priority, is_completed)
select id, 'test task', 'test description', 'low', false
from auth where login = 'demo';

insert into task(user_id, name, description, priority, is_completed)
select id, 'another task', 'another description', 'high', false
from auth where login = 'demo';

insert into task(user_id, name, description, priority, is_completed)
select id, 'yet another task', 'yet another description', 'medium', true
from auth where login = 'demo';


with pet_id as (
    select id from pet where kind = 'cat'
)
insert into app_user(id, pet_name, happiness, coins, current_pet)
select id, 'kuzya', 0, 0, (select id from pet_id)
from auth where login = 'demo';