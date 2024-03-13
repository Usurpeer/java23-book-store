-- Данные для таблицы "catalog"."genre"
INSERT INTO "catalog"."genre" ("title")
VALUES
('Абсурд'),
('Автобиография'),
('Аллегория'),
('Антиутопия'),
('Бизнес-книга'),
('Биография'),
('Военная проза'),
('Декадентство'),
('Детектив'),
('Детективный роман'),
('Детская литература'),
('Деловая литература'),
('Документальная литература'),
('Дистопия'),
('Драма'),
('Духовная литература'),
('История'),
('Исторический роман'),
('Исторический проза'),
('Историческая литература'),
('Исторический фантастика'),
('Инвестиции'),
('Комедия'),
('Классика'),
('Классическая литература'),
('Культурология'),
('Литература XX века'),
('Лингвистика'),
('Литературная фикция'),
('Лирика'),
('Магический реализм'),
('Мемуары'),
('Мистика'),
('Мистическая проза'),
('Морская проза'),
('Научная фантастика'),
('Научно-популярная литература'),
('Общественная драма'),
('Поэзия'),
('Поэма'),
('Проза'),
('Приключения'),
('Приключенческая литература'),
('Психология'),
('Психологический роман'),
('Психологическая литература'),
('Психологический триллер'),
('Природоведение'),
('Повесть'),
('Рассказ'),
('Реализм'),
('Роман'),
('Романтика'),
('Реалистическая проза'),
('Романтическая литература'),
('Русская литература'),
('Сатира'),
('Саморазвитие'),
('Сага'),
('Семейная сага'),
('Справочник'),
('Сборник'),
('Сказка'),
('Сказки'),
('Стихотворения'),
('Советская литература'),
('Современная русская литература'),
('Творчество'),
('Триллер'),
('Трактаты'),
('Трагедия'),
('Женская литература'),
('Зарубежная литература'),
('Ужасы'),
('Фантастика'),
('Фантастический боевик'),
('Философия'),
('Философский роман'),
('Философская литература'),
('Философская проза'),
('Фэнтези'),
('Хоррор'),
('Эссе'),
('Юмор'),
('Юмористическая проза'),
('Юмористическая поэзия')
;

-- Данные для таблицы "catalog"."author"
INSERT INTO "catalog"."author" ("first_name", "last_name", "middle_name", "pseudonym")
VALUES
('Борис', 'Бугаев', 'Николаевич', 'Андрей Белый'),
('Александр', 'Пушкин', 'Сергеевич', 'Иван Петрович Белкин'),
('Лев', 'Толстой', 'Николаевич', 'граф Толстой'),
('Федор', 'Достоевский', 'Михайлович', 'Достоевский'),
('Николай', 'Гоголь', 'Васильевич', 'Гоголь'),
('Иван', 'Тургенев', 'Сергеевич', 'Недобобов'),
('Михаил', 'Лермонтов', 'Юрьевич', 'Лермонтов'),
('Владимир', 'Набоков', 'Владимирович', 'В. Сирин'),
('Борис', 'Пастернак', 'Леонидович', 'Борис Пастернак'),
('Алексей', 'Пешков', 'Максимович', 'Максим Горький'),
('Иван', 'Бунин', 'Алексеевич', 'Озёрский'),
('Александр', 'Солженицын', 'Исаевич', NULL),
('Игорь', 'Лотарёв', 'Васильевич', 'Игорь Северянин'),
('Сергей', 'Довлатов', 'Донатович', NULL),
('Мария', 'Гайнер', 'Александровна', 'Мария Гайдар'),
('Сергей', 'Есенин', 'Александрович', 'Сергей Есенин'),
('Владимир', 'Маяковский', 'Владимирович', 'Владимир Маяковский'),
('Джон ', 'Толкин', 'Рональд Руэл', NULL),
('Уильям', 'Шекспир', NULL, NULL),
('Фридрих', 'Ницше', 'Вильгельм', NULL),
('Габриэль', 'Гарсия', 'Маркес', NULL),
('Фрэнсис', 'Скотт', 'Фицджеральд', NULL),
('Чарльз', 'Диккенс', 'Джон', 'Boz'),
('Джейн', 'Остин', NULL, NULL),
('Эрик Артур', 'Блэйк', NULl, 'Джордж Оруэлл'),
('Джон', 'Стейнбек', NULL, NULL),
('Артур', 'Кларк', 'Чарльз', 'Charles Willis'),
('', '', '', 'Харпер Ли'),
('Джек', 'Керуак', NULL, NULL),
('Эмили', 'Бронте', NULL, 'Эллис Белл'),
('Джеймс', 'Джойс', NULL, NULL),
('Уильям', 'Голдинг', 'Джеральд', NULL),
('Льюис', 'Кэрролл', NULL, 'Lewis Carroll'),
('Грегор', 'Макдональд', NULL, NULL),
('Светлана', 'Мартынчик', NULL, 'Макс Фрай'),
('Хьюго', 'Герц', 'Вольф', NULL),
('Джеймс', 'Фенимор', 'Купер', 'Джеймс Фенимор Купер'),
('Эдгар', 'По', NULL, 'Edgar A. Perry'),
('Вирджиния', 'Вульф', NULL, NULL),
('Пауль Томас', 'Манн', NULL, NULL),
('Сэмюэл Ленгхорн', 'Клеменс', NULL, 'Марк Твен'),
('Эдит', 'Вартон', NULL, 'Эдит Уортон'),
('Уильям Сомерсет', 'Моэм', NULL, 'Уильям Сомерсет Моэм'),
('Рудольф', 'Эрих', 'Распе', NULL),
('Михаил', 'Булгаков', 'Афанасьевич', 'Тускарора'),
('Даниил', 'Хармс', 'Иванович', 'Хармс'),
('Рей', 'Брэдбери', 'Дуглас', 'Elliott'),
('Генри', 'Миллер', 'Валентайн', NULL),
('Маргарит Энн', 'Джонсон', NULL,'Майя Анжелу'),
('Шарлотта', 'Бронте', NULL, 'Каррер Белл'),
('Лоренс', 'Даррелл', 'Джордж', NULL),
('Юрий', 'Олеша', 'Карлович', 'Зубило'),
('Франц', 'Кафка', NULL, NULL),
('Герберт', 'Уэллс', 'Джордж', 'H. G. Wells'),
('Джон', 'Рональд', 'Руэл', 'Толкин'),
('Эдгар', 'Берроуз', 'Рейз', NULL),
('Жюль', 'Габриэль', 'Верн', NULL),
('Стивен', 'Кинг', 'Эдвин', 'Стивен Кинг'),
('Джон', 'Грин', NULL, NULL),
('Эрих Пауль', 'Ремарк', NULL, 'Ремарк'),
('Луи Фердинанд', 'Целин', NULL, NULL),
('Гейтс', 'Билл', NULL, NULL),
('Николас', 'Спаркс', NULL, NULL),
('Теодор Альберт', 'Драйзер', 'Герман', NULL),
('Ирвин', 'Гилберт', 'Шамфорофф', 'Ирвин Шоу'),
('Кеннет', 'Грэм', NULL, NULL),
('Волтер', 'Исаак', NULL, NULL),
('Аркадий', 'Стругацкий', 'Натанович', 'С. Бережков'),
('Борис', 'Стругацкий', 'Натанович', 'С. Витицкий'),
('Михаил', 'Шолохов', 'Александрович', NULL),
('Федор', 'Тютчев', 'Иванович', NULL),
('Григо́рий', 'Чхартишви́ли', 'Ша́лвович', 'Бори́с Акунин'),
('Даниил', 'Хармс', 'Иванович', 'Иван Топорышкин'),
('Владимир', 'Даль', 'Иванович', 'Казак Луганский'),
('Артур', 'Конан', 'Дойль', NULL),
('Оскар', 'Уайльд', 'Финеган', NULL),
('Юлиан', 'Барнс', 'Патрик', 'Дэн Кавана'),
('Маргарет', 'Митчелл', 'Маньян', NULL),
('Джон', 'Фаулз', 'Роберт', NULL),
('Антон', 'Чехов', 'Павлович', 'Антоша Чехонте'),
('Агата', 'Кристи', 'Милн', 'Кристианна Брэнд'),
('Эрнест', 'Хемингуэй', 'Миллер', NULL),
('Дэниэл', 'Браун', 'Герхард', 'Дэн Браун'),
('Харуки', 'Мураками', NULL, NULL),
('Джоан', 'Роулинг', NULL, NULL),
('Джон', 'Чейни', 'Гриффит', 'Джек Лондон'),
('Алиса', 'Розенбаум', 'Зиновьевна', 'Айн Рэнд'),
('Джозеф', 'Киплинг', 'Редьярд', NULL),
('Джером', 'Сэлинджер', 'Дэвид', 'Джей Ди'),
('Антуан', 'де Сент-Экзюпери', 'Мари Жан Батист', NULL),
('Иоганн', 'Гёте', 'Вольфганг', NULL),
('Григорий', 'Остер', 'Бенционович', 'Остёр'),
('Джеймс', 'Паттерсон', 'Брендан', NULL),
('Мария', 'Иванова', 'Петровна', 'Мариэтта Светлова'),
('Александр', 'Сидоров', 'Игоревич', 'Александр Подземный'),
('Екатерина', 'Козлова', 'Дмитриевна', 'Ева Туманова'),
('Владимир', 'Андреев', 'Иванович', 'Владимир Ветеранов'),
('Анна', 'Кузнецова', 'Михайловна', 'Анна Путешественница'),
('Павел', 'Смирнов', 'Александрович', 'Павел Приключенец'),
('Анна', 'Иванова', 'Петровна', 'А. П. Иванов'),
('Владимир', 'Петров', 'Сергеевич', 'В. С. Петров'),
('Екатерина', 'Сидорова', 'Александровна', 'Е. А. Сидорова')
;