insert into priority(name, award) values ('LOW', 1), ('MEDIUM', 2), ('HIGH', 3);

insert into pet(kind, kind_translation, price) values ('paws', 'Лапкинс', 10);

insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature-paws.png', -100, 100
from pet where kind = 'paws';

insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Эй, работай уже, есть хочу', -100, -20
from pet where kind = 'paws';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Эй, давай еще в том же темпе', -30, 30
from pet where kind = 'paws';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Эй, ты молодец, но продолжай работать', 30, 100
from pet where kind = 'paws';

insert into pet(kind, kind_translation, price) values ('underwater', 'Монотон', 10);

insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature-underwater.png', -100, 100
from pet where kind = 'underwater';

insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Ты ма-ло ра-бо-та-ешь', -100, -20
from pet where kind = 'underwater';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Ста-рай-ся луч-ше. Ты мо-жешь', -30, 30
from pet where kind = 'underwater';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Мо-ло-дец! Та-ак дер-жать', 30, 100
from pet where kind = 'underwater';

insert into pet(kind, kind_translation, price) values ('unicorn', 'Единорог', 10);

insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature-unicorn.png', -100, 100
from pet where kind = 'unicorn';

insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Работай, а не то на рог насажу', -100, -20
from pet where kind = 'unicorn';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Кто не работает, тот ест одну лишь траву. Как я', -30, 30
from pet where kind = 'unicorn';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Тебя подбросить до радуги?', 30, 100
from pet where kind = 'unicorn';
