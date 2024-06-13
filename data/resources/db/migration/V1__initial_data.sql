insert into priority(name, award) values ('LOW', 1), ('MEDIUM', 2), ('HIGH', 3);

insert into pet(kind, price) values ('cat', 10);

insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, 'img/sad_cat.png', -100, -20
from pet where kind = 'cat';
insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, 'img/neutral_cat.png', -30, 30
from pet where kind = 'cat';
insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, 'img/happy_cat.png', 30, 100
from pet where kind = 'cat';

insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'I am so sad', -100, -20
from pet where kind = 'cat';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'I am so neutral', -30, 30
from pet where kind = 'cat';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'I am so happy', 30, 100
from pet where kind = 'cat';

insert into pet(kind, price) values ('dog', 10);

insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, 'img/sad_dog.png', -100, -20
from pet where kind = 'dog';
insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, 'img/neutral_dog.png', -30, 30
from pet where kind = 'dog';
insert into pet_image(pet_id, url, min_happiness, max_happiness)
select id, 'img/happy_dog.png', 30, 100
from pet where kind = 'dog';

insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'I am so sad (dog)', -100, -20
from pet where kind = 'dog';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'I am so neutral (dog)', -30, 30
from pet where kind = 'dog';
insert into pet_quote(pet_id, quote, min_happiness, max_happiness)
select id, 'I am so happy (dog)', 30, 100
from pet where kind = 'dog';
