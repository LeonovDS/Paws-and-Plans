insert into priority(name, award) values ('LOW', 1), ('MEDIUM', 2), ('HIGH', 3);

insert into pet(kind, kind_translation, price) values ('paws', 'Лапкинс', 10);

insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature-paws-sad.png', -40, 40
from pet where kind = 'paws';
insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature-paws-angry.png', -100, -20
from pet where kind = 'paws';
insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature-paws.png', 20, 100
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
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Ррр! Если ты не выполнишь задание, я буду очень недоволен, человек! Накорми меня немедленно!', -100, -20
from pet where kind = 'paws';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Мяу... Почему ты забыл покормить меня? Я такой голодный...', -30, 30
from pet where kind = 'paws';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Мяу, хозяин! Как же я рад тебя видеть! Давай выполнять задачи!', 30, 100
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
select id, '/images/creature-unicorn-angry.png', -100, -20
from pet where kind = 'unicorn';
insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature-unicorn-sad.png', -40, 40
from pet where kind = 'unicorn';
insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature-unicorn.png', 20, 100
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
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Неужели тебе безразлична моя участь? Накорми меня немедленно!', -100, -20
from pet where kind = 'unicorn';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Мой добрый друг, почему ты медлишь с выполнением задач? Я так голоден и жду твоей заботы', -30, 30
from pet where kind = 'unicorn';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'О, хозяин, как чудесно, что ты здесь! Твоя помощь воистину ценна, давай скорее приступим к заданиям', 30, 100
from pet where kind = 'unicorn';


insert into pet(kind, kind_translation, price) values ('div', 'Див', 10);

insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature-angry.png', -100, -20
from pet where kind = 'div';
insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature-sad.png', -40, 40
from pet where kind = 'div';
insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, '/images/creature.png', 20, 100
from pet where kind = 'div';

insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Гррр! Твоя медлительность раздражает меня! Если ты не выпонишь задание сейчас же, я разгневаюсь!', -100, -20
from pet where kind = 'div';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Печалит меня, что ты забываешь о моих нуждах. Я так нуждаюсь в пище, пожалуйста, помоги мне', -30, 30
from pet where kind = 'div';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'Приветствую, хозяин! Твои успехи в выполнении заданий радуют меня. Продолжай в том же духе!', 30, 100
from pet where kind = 'div';
