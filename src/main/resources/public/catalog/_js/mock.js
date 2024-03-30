function getCatalogBooks() {
  books = mockOpenBooks;
  return books;
}
function getCatalogAuthors() {
  authors = mockAuthors;
  return authors;
}
function getCatalogGenres() {
  genres = mockGenres;
  return genres;
}
function getCatalogPublishers() {
  publishers = mockPublishers;
  return publishers;
}
const mockAuthors = [
  {
    id: 81,
    firstName: "Агата",
    lastName: "Кристи",
    middleName: "Милн",
    pseudonym: "Кристианна Брэнд",
  },
  {
    id: 2,
    firstName: "Александр",
    lastName: "Пушкин",
    middleName: "Сергеевич",
    pseudonym: "Иван Петрович Белкин",
  },
  {
    id: 95,
    firstName: "Александр",
    lastName: "Сидоров",
    middleName: "Игоревич",
    pseudonym: "Александр Подземный",
  },
  {
    id: 12,
    firstName: "Александр",
    lastName: "Солженицын",
    middleName: "Исаевич",
    pseudonym: null,
  },
  {
    id: 10,
    firstName: "Алексей",
    lastName: "Пешков",
    middleName: "Максимович",
    pseudonym: "Максим Горький",
  },
  {
    id: 87,
    firstName: "Алиса",
    lastName: "Розенбаум",
    middleName: "Зиновьевна",
    pseudonym: "Айн Рэнд",
  },
  {
    id: 100,
    firstName: "Анна",
    lastName: "Иванова",
    middleName: "Петровна",
    pseudonym: "А. П. Иванов",
  },
  {
    id: 98,
    firstName: "Анна",
    lastName: "Кузнецова",
    middleName: "Михайловна",
    pseudonym: "Анна Путешественница",
  },
  {
    id: 80,
    firstName: "Антон",
    lastName: "Чехов",
    middleName: "Павлович",
    pseudonym: "Антоша Чехонте",
  },
  {
    id: 90,
    firstName: "Антуан",
    lastName: "де Сент-Экзюпери",
    middleName: "Мари Жан Батист",
    pseudonym: null,
  },
  {
    id: 68,
    firstName: "Аркадий",
    lastName: "Стругацкий",
    middleName: "Натанович",
    pseudonym: "С. Бережков",
  },
  {
    id: 27,
    firstName: "Артур",
    lastName: "Кларк",
    middleName: "Чарльз",
    pseudonym: "Charles Willis",
  },
  {
    id: 75,
    firstName: "Артур",
    lastName: "Конан",
    middleName: "Дойль",
    pseudonym: null,
  },
  {
    id: 1,
    firstName: "Борис",
    lastName: "Бугаев",
    middleName: "Николаевич",
    pseudonym: "Андрей Белый",
  },
  {
    id: 9,
    firstName: "Борис",
    lastName: "Пастернак",
    middleName: "Леонидович",
    pseudonym: "Борис Пастернак",
  },
  {
    id: 69,
    firstName: "Борис",
    lastName: "Стругацкий",
    middleName: "Натанович",
    pseudonym: "С. Витицкий",
  },
  {
    id: 39,
    firstName: "Вирджиния",
    lastName: "Вульф",
    middleName: null,
    pseudonym: null,
  },
  {
    id: 97,
    firstName: "Владимир",
    lastName: "Андреев",
    middleName: "Иванович",
    pseudonym: "Владимир Ветеранов",
  },
  {
    id: 74,
    firstName: "Владимир",
    lastName: "Даль",
    middleName: "Иванович",
    pseudonym: "Казак Луганский",
  },
  {
    id: 17,
    firstName: "Владимир",
    lastName: "Маяковский",
    middleName: "Владимирович",
    pseudonym: "Владимир Маяковский",
  },
];

const mockGenres = [
  {
    id: 1,
    title: "Абсурд",
  },
  {
    id: 2,
    title: "Автобиография",
  },
  {
    id: 3,
    title: "Аллегория",
  },
  {
    id: 4,
    title: "Антиутопия",
  },
  {
    id: 5,
    title: "Бизнес-книга",
  },
  {
    id: 6,
    title: "Биография",
  },
  {
    id: 7,
    title: "Военная проза",
  },
  {
    id: 8,
    title: "Декадентство",
  },
  {
    id: 12,
    title: "Деловая литература",
  },
  {
    id: 9,
    title: "Детектив",
  },
  {
    id: 10,
    title: "Детективный роман",
  },
  {
    id: 11,
    title: "Детская литература",
  },
  {
    id: 14,
    title: "Дистопия",
  },
  {
    id: 13,
    title: "Документальная литература",
  },
  {
    id: 15,
    title: "Драма",
  },
  {
    id: 16,
    title: "Духовная литература",
  },
  {
    id: 72,
    title: "Женская литература",
  },
  {
    id: 73,
    title: "Зарубежная литература",
  },
  {
    id: 22,
    title: "Инвестиции",
  },
  {
    id: 20,
    title: "Историческая литература",
  },
];

const mockPublishers = [
  'Издательство "Средиземье"',
  'Издательство "Historical Explorations"',
  "Издательство Загадки",
  "Издательство Экспериментальные Миры",
  'Издательство "Ленинград"',
  'Издательство "Фэнтези"',
  "Издательство Морские Приключения",
  "Издательство Преображения",
  'Издательство "Underground Press"',
  'Издательство "J. B. Lippincott & Co."',
  "Издательство Классика",
  "Издательство Романтические Сюжеты",
  'Издательство "Castle Secrets"',
  "Издательство Загадка",
  'Издательство "Проспект"',
  'Издательство "New American Library"',
  'Издательство "Современник"',
  'Издательство "Mystery Books"',
  "Современник",
  'Издательство "Классика"',
  'Издательство "Русская литература"',
  'Издательство "Всемирное слово"',
  "Издательство Овощной Мир",
  "Издательство Чудо",
  'Издательство "Шекспир"',
  "Издательство Размышления",
  'Издательство "Русская книга"',
  'Издательство "Фантастика и реальность"',
  'Издательство "Ancient Mysteries"',
  'Издательство "Древние времена"',
  'Издательство "Книжный мир"',
  'Издательство "Faber and Faber"',
  "Издательство Русское Слово",
  'Издательство "Гранд"',
  "Московский рабочий",
  'Издательство "Романтика"',
  'Издательство "Доброта и социум"',
  'Издательство "HarperCollins"',
  'Издательство "Время и пространство"',
  'Издательство "Космос"',
  'Издательство "Темные загадки"',
  "Издательство Океанские Открытия",
  'Издательство "Престиж"',
  'Издательство "Психология и мозг"',
  'Издательство "Романтическая литература"',
  'Издательство "Молчание"',
  'Издательство "Детский мир"',
  'Издательство "Secker and Warburg"',
  'Издательство "Starlight Adventures"',
  'Издательство "Горизонт"',
  'Издательство "AST"',
  "Издательство Потерянные Времена",
  'Издательство "Бизнес и успех"',
  'Издательство "Послевоенная Германия"',
  "Издательство Пустынные Путешествия",
  'Издательство "Подростковые истории"',
  'Издательство "Фантастическая литература"',
  'Издательство "Magic World"',
  'Издательство "Современная классика"',
  'Издательство "Философия и жизнь"',
  'Издательство "Enlightenment Books"',
  'Издательство "Спутник"',
  "Издательство Драм",
  'Издательство "Книжный клуб"',
  'Издательство "John Murray"',
  'Издательство "G. P. Putnams Sons',
  'Издательство "Timeless Books"',
  'Издательство "Simon & Schuster"',
  'Издательство "Глобальные Путешествия"',
  "Издательство Откровение",
  'Издательство "B.W. Huebsch"',
  "Издательство Вдохновение",
  'Издательство "Советский писатель"',
  "Издательство Фантастика",
  "Издательство Современник",
  "Издательство Сюрреализм",
  'Издательство "Азбука"',
  "Издательство Сказки",
  'Издательство "Deep Sea Publishing"',
  "Издательство Лениздат",
  "Reynal & Hitchcock",
  'Издательство "Апокалиптические приключения"',
  'Издательство "Эксмо"',
  'Издательство "Археологические открытия"',
  'Издательство "Ancient Artefacts"',
  "Издательство Загадок",
  "Little, Brown and Company",
  "Издательство АСТ",
  'Издательство "Освещение"',
  'Издательство "Иностранка"',
  'Издательство "Mystery House"',
  'Издательство "Альпина Паблишер"',
  'Издательство "Мысль"',
  'Издательство "Хроника"',
  'Издательство "Мир"',
  'Издательство "Фантастика"',
  "Издательство Новый Мир",
  'Издательство "Научная фантастика"',
  'Издательство "Truth Seeker"',
  'Издательство "Лидер"',
  'Издательство "Строительство и прогресс"',
  'Издательство "Художественная литература"',
  'Издательство "Развитие"',
  'Издательство "Covici-Friede"',
  'Издательство "Правда"',
  'Издательство "Ницше"',
  "Издательство ОЛМА-Пресс",
  'Издательство "Библиотека всемирной литературы"',
  'Издательство "Космический мир"',
  'Издательство "Литературное наследство"',
  "Издательство Эксмо",
  'Издательство "Дрофа"',
  'Издательство "Государственное издательство"',
  'Издательство "Литературная Россия"',
  'Издательство "Penguin Random House"',
  'Издательство "Технологии и инновации"',
  "Издательство Магические Путешествия",
  'Издательство "Мировая литература"',
  'Издательство "Farrar, Straus and Giroux"',
  'Издательство "Русская классика"',
  "Издательство Детектив",
  'Издательство "Прогресс"',
  'Издательство "Macmillan Publishers"',
  "Издательство Комедия",
  "Random House",
  'Издательство "Harper Perennial"',
  "Johann Georg Cotta",
  "Издательство Охотник",
  'Издательство "АртПринт"',
  'Издательство "Манн, Иванов и Фербер"',
  "Издательство Память",
  "Издательство Литературные изыски",
  'Издательство "Классическая проза"',
  'Издательство "Махаон"',
  'Издательство "Эпоха"',
  'Издательство "Алхимик"',
  'Издательство "Phaedra"',
  "Издательство Мистические Рассказы",
  'Издательство "Космические открытия"',
  "Издательский дом Ридерз Дайджест",
  'Издательство "Загадка"',
  "Издательство Возрождение",
  "Издательство Романтика",
  'Издательство "Философия и познание"',
  'Издательство "Эмоции"',
  "Издательство Мудрость",
  'Издательство "Приключения"',
  'Издательство "Война и мир"',
  'Издательство "История"',
  'Издательство "Путешествия"',
  "Издательство Октябрь",
  'Издательство "Inspiration Books"',
  'Издательство "Тайные истории"',
  "Bloomsbury Publishing",
  "Издательство Путешествия в Неизведанное",
  'Издательство "Shakespeare and Company"',
  'Издательство "Новое начало"',
  'Издательство "Литера"',
  'Издательство "The Viking Press"',
  'Издательство "Ballantine Books"',
  'Издательство "Просвещение"',
  'Издательство "Sylvia Beach"',
  'Издательство "Historical Mysteries"',
  'Издательство "Томас Кэутли, издатель"',
  'Издательство "Искусство и культура"',
  'Издательство "Темные истории"',
  'Издательство "Путешествие"',
  'Издательство "Harper & Brothers"',
  'Издательство "Cosmos Press"',
  'Издательство "Viking Press"',
  'Издательство "Московский рабочий"',
  'Издательство "Научная мысль"',
  "Издательство Абсурд",
  'Издательство "Thomas Egerton"',
  'Издательство "Москва"',
  "Издательство Мистика",
  "Macmillan Publishers",
  'Издательство "Литературное агентство Михаила Шолохова"',
  "Макмиллан",
  'Издательство "Украинская слово"',
  'Издательство "Самопознание"',
  'Издательство "Литературное наследие"',
  'Издательство "Магический реализм"',
  'Издательство "Журнально-газетная тип.',
  'Издательство "Freedom Press"',
  'Издательство "Археология"',
  "Издательство Тайн",
  'Издательство "Подводные открытия"',
  'Издательство "Великая книга"',
  'Издательство "Молодая гвардия"',
  'Издательство "Журнально-газетная лавка"',
];

// Книги со статусом OPEN, не буду делать реализацию с книгами не в наличии, думаю не успею
//export
const mockOpenBooks = [
  {
    id: 1,
    title: "Гарри Поттер и Философский камень",
    description:
      "Первая книга знаменитой серии повествует о мальчике по имени Гарри Поттер, который узнает о своих волшебных способностях и поступает в Школу магии и волшебства Хогвартс. Вместе с новыми друзьями Роном и Гермионой, Гарри погружается в захватывающие приключения, раскрывая тайны своего прошлого и сражаясь с силами зла. Главный артефакт первой книги - философский камень, который обладает способностью дарить бессмертие, и за ним охотятся темные силы. Гарри и его друзья должны сделать все возможное, чтобы предотвратить зловещие планы и сохранить мирное существование магического мира.",
    status: "OPEN",
    price: 530.9,
    publisher: "Bloomsbury Publishing",
    year: 1997,
    genres: [
      {
        id: 81,
        title: "Фэнтези",
      },
    ],
    authors: [
      {
        id: 85,
        firstName: "Джоан",
        lastName: "Роулинг",
        middleName: null,
        pseudonym: null,
      },
    ],
    imageName: "default_book.png",
  },
  {
    id: 3,
    title: "Атлант расправил плечи",
    description:
      'Этот роман является философским манифестом, где автор выражает свои идеи об индивидуализме, капитализме и свободе личности. В центре сюжета - Даглас Таггарт, владелец железнодорожной компании, который пытается спасти свою фирму от уничтожения со стороны правительства, которое пытается наложить на нее все более тяжелые регулирования. Вместе с ним в борьбе за свободу стоят его союзники, включая гениального изобретателя Джона Галта. Однако, чтобы спасти мир, Галт и его друзья решают принять радикальные меры, замыкаясь в таинственном месте, известном как Атлант, и отказываясь от своего творческого потенциала в угоду системы, которая не ценит и не признает их вклада. Роман "Атлант расправил плечи" представляет собой мощное и эмоциональное обращение к читателям о необходимости защищать свободу личности и индивидуальные права.',
    status: "OPEN",
    price: 580,
    publisher: "Random House",
    year: 1957,
    genres: [
      {
        id: 36,
        title: "Научная фантастика",
      },
      {
        id: 9,
        title: "Детектив",
      },
      {
        id: 78,
        title: "Философский роман",
      },
    ],
    authors: [
      {
        id: 87,
        firstName: "Алиса",
        lastName: "Розенбаум",
        middleName: "Зиновьевна",
        pseudonym: "Айн Рэнд",
      },
    ],
    imageName: "default_book.png",
  },
  {
    id: 4,
    title: "Маугли",
    description:
      "Рассказы о приключениях Маугли, мальчика, который вырос в джунглях Индии и был воспитан волками. Вместе с другими животными джунглей, такими как медведь Балу и пантера Багира, Маугли исследует мир дикой природы, сталкивается с опасностями и обучается жизненно важным навыкам выживания. Его история полна приключений, дружбы и важных уроков о природе и человеческом обществе.",
    status: "OPEN",
    price: 390.5,
    publisher: "Macmillan Publishers",
    year: 1894,
    genres: [
      {
        id: 52,
        title: "Роман",
      },
      {
        id: 62,
        title: "Сборник",
      },
    ],
    authors: [
      {
        id: 88,
        firstName: "Джозеф",
        lastName: "Киплинг",
        middleName: "Редьярд",
        pseudonym: null,
      },
    ],
    imageName: "default_book.png",
  },
  {
    id: 5,
    title: "Над пропастью во ржи",
    description:
      "Роман повествует о нескольких днях из жизни Холдена Колфилда, шестнадцатилетнего подростка, который после неудачного семестра в школе решает сбежать из пансионата и отправиться в Нью-Йорк. Во время своего путешествия он сталкивается с различными людьми и ситуациями, которые заставляют его задуматься о жизни, смысле существования, честности и фальши. Он испытывает чувство отчуждения и непонимания в окружающем мире, и его внутренний монолог становится центральным мотивом романа.",
    status: "OPEN",
    price: 420.5,
    publisher: "Little, Brown and Company",
    year: 1951,
    genres: [
      {
        id: 54,
        title: "Реалистическая проза",
      },
      {
        id: 46,
        title: "Психологическая литература",
      },
    ],
    authors: [
      {
        id: 89,
        firstName: "Джером",
        lastName: "Сэлинджер",
        middleName: "Дэвид",
        pseudonym: "Джей Ди",
      },
    ],
    imageName: "default_book.png",
  },
  {
    id: 47,
    title: "Сказки",
    description:
      "Сборник философских и абсурдных сказок, написанных в нестандартном стиле.",
    status: "OPEN",
    price: 900,
    publisher: "Издательство Чудо",
    year: 1939,
    genres: [
      {
        id: 1,
        title: "Абсурд",
      },
      {
        id: 77,
        title: "Философия",
      },
    ],
    authors: [
      {
        id: 73,
        firstName: "Даниил",
        lastName: "Хармс",
        middleName: "Иванович",
        pseudonym: "Иван Топорышкин",
      },
    ],
    imageName: "default_book.png",
  },
  {
    id: 48,
    title: "Рассказы",
    description:
      "Сборник непредсказуемых и запутанных рассказов, полных неожиданных поворотов сюжета.",
    status: "OPEN",
    price: 1100,
    publisher: "Издательство Сюрреализм",
    year: 1942,
    genres: [
      {
        id: 1,
        title: "Абсурд",
      },
      {
        id: 84,
        title: "Юмор",
      },
      {
        id: 77,
        title: "Философия",
      },
    ],
    authors: [
      {
        id: 73,
        firstName: "Даниил",
        lastName: "Хармс",
        middleName: "Иванович",
        pseudonym: "Иван Топорышкин",
      },
    ],
    imageName: "default_book.png",
  },
  {
    id: 8,
    title: "Приключения Чиполлино",
    description:
      "В центре сюжета - приключения Чипполино, маленького лука, который начинает говорить и приобретает характер человека. Чипполино живет в небольшом итальянском городке и сталкивается с различными приключениями, пытаясь защитить себя и свою семью от насилия и несправедливости. В ходе своих приключений Чипполино встречает различных персонажей, включая злого карлика Томатина и щедрого Маркетти, и становится символом сопротивления и надежды для всех овощей и фруктов в городе. Эта сказка наполнена яркими персонажами, забавными ситуациями и важными моральными уроками для детей о справедливости, дружбе и сопротивлении притеснениям.",
    status: "OPEN",
    price: 500.5,
    publisher: "Издательство Овощной Мир",
    year: 2020,
    genres: [
      {
        id: 11,
        title: "Детская литература",
      },
      {
        id: 63,
        title: "Сказка",
      },
    ],
    authors: [
      {
        id: 92,
        firstName: "Григорий",
        lastName: "Остер",
        middleName: "Бенционович",
        pseudonym: "Остёр",
      },
    ],
    imageName: "default_book.png",
  },
  {
    id: 10,
    title: "Сфинкс",
    description:
      "Это роман о жизни и приключениях главного героя, который оказывается втянутым в запутанные интриги и загадки. Главный герой путешествует по разным странам, сталкивается с разнообразными характерами и событиями, ищет ответы на вопросы о смысле жизни и своем месте в мире. Роман обладает глубоким психологическим содержанием и захватывающим сюжетом, который привлекает внимание читателя до самого конца.",
    status: "OPEN",
    price: 550,
    publisher: "Издательство Пустынные Путешествия",
    year: 1937,
    genres: [
      {
        id: 52,
        title: "Роман",
      },
    ],
    authors: [
      {
        id: 43,
        firstName: "Уильям Сомерсет",
        lastName: "Моэм",
        middleName: null,
        pseudonym: "Уильям Сомерсет Моэм",
      },
    ],
    imageName: "default_book.png",
  },
  {
    id: 11,
    title: "Река жизни",
    description:
      "Это роман о жизненных путешествиях и стремлениях главного героя. История начинается с его детства и следует за ним на протяжении жизни, открывая его внутренние противоречия, успехи и поражения. В книге освещаются темы любви, страсти, семейных отношений и поиска смысла жизни. Автор проникающе рассматривает характеры персонажей и их взаимодействие с окружающим миром, создавая глубокий и увлекательный рассказ о человеческой судьбе.",
    status: "OPEN",
    price: 700.5,
    publisher: "Издательство Романтические Сюжеты",
    year: 1951,
    genres: [
      {
        id: 52,
        title: "Роман",
      },
    ],
    authors: [
      {
        id: 43,
        firstName: "Уильям Сомерсет",
        lastName: "Моэм",
        middleName: null,
        pseudonym: "Уильям Сомерсет Моэм",
      },
    ],
    imageName: "default_book.png",
  },
  {
    id: 12,
    title: "Старик и море",
    description:
      'Роман рассказывает историю старика по имени Сантьяго, кубинского рыбака, который уже долгое время не имеет удачи в своей профессии. Однажды он отправляется в море в поисках крупной рыбы и поймал огромного меча, который он боролся с ним на протяжении нескольких дней. Несмотря на его старость и физические трудности, Сантьяго не сдается и в итоге побеждает рыбу. Однако, на пути обратно домой, его трофей становится объектом жажды акул, что приводит к потере большей части его добычи. "Старик и море" - это глубокий роман о человеческой выносливости, настойчивости и достоинстве перед лицом суровых испытаний природы. Этот пронзительный рассказ о приключениях, борьбе и потерях оставляет читателя сильным впечатлением и моральными размышлениями.',
    status: "OPEN",
    price: 800.5,
    publisher: "Издательство Морские Приключения",
    year: 1952,
    genres: [
      {
        id: 35,
        title: "Морская проза",
      },
      {
        id: 52,
        title: "Роман",
      },
      {
        id: 27,
        title: "Литература XX века",
      },
    ],
    authors: [
      {
        id: 82,
        firstName: "Эрнест",
        lastName: "Хемингуэй",
        middleName: "Миллер",
        pseudonym: null,
      },
    ],
    imageName: "default_book.png",
  },
  {
    id: 13,
    title: "И целый день дождь...",
    description:
      "Описывает день в жизни старика, который проходит под дождем. Главный герой проводит время, занимаясь повседневными делами на своем участке, взаимодействуя с природой и вспоминая прошлые события своей жизни. Дождь становится символом меланхолии и одиночества, которые охватывают героя внутренне. Рассказ погружает читателя в атмосферу тоски, ностальгии и внутренних размышлений старика, создавая образы и образцы его жизни в воображении читателя.",
    status: "OPEN",
    price: 750.9,
    publisher: "Издательство Потерянные Времена",
    year: 1925,
    genres: [
      {
        id: 50,
        title: "Рассказ",
      },
      {
        id: 41,
        title: "Проза",
      },
    ],
    authors: [
      {
        id: 82,
        firstName: "Эрнест",
        lastName: "Хемингуэй",
        middleName: "Миллер",
        pseudonym: null,
      },
    ],
    imageName: "default_book.png",
  },
];
