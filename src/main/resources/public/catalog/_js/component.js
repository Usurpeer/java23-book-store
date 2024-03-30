import {
  getImagePath,
  isBookInCart,
  addInCart,
  delBookCart,
  autoFormat,
} from "../../_js/helpers.js";

export function renderCatalogBooks(books, divCards) {
  // Очищаем содержимое div перед добавлением новых карточек
  divCards.innerHTML = "";

  // Цикл для создания карточек для каждой книги
  books.forEach((book) => {
    const card = createProductCard(book);
    divCards.appendChild(card);
  });
}

function createProductCard({
  id,
  title,
  authors,
  genres,
  price,
  imageName,
  year,
}) {
  // Создание внешнего div с классами
  const cardCol = document.createElement("div");
  cardCol.className =
    "col-lg-4 col-md-4 col-sm-6 mt-3 d-flex justify-content-center";

  const card = document.createElement("div");
  card.className = "card product-card";

  // изображение
  const image = createImg(imageName, id);
  image.className = "clickable";

  // создание следующего div
  const cardBody = document.createElement("div");
  cardBody.className = "card-body";

  // создание названия
  const cardTitle = document.createElement("h5");
  cardTitle.className = "card-title clickable";
  cardTitle.textContent = title;
  cardTitle.onclick = function () {
    goToBookId(id);
  };

  // создание авторов
  const authorsText = authorsToCard(authors);

  // создание жанров
  const genresText = genresToCard(genres);

  // год издания
  const yearPub = document.createElement("p");
  yearPub.className = "card-text";
  yearPub.textContent = "Год выпуска: " + year;

  const container = document.createElement("div");
  container.className = "container";

  const row = document.createElement("div");
  row.className = "row align-items-center";

  const priceCol = document.createElement("div");
  priceCol.className = "col-lg-6 col-md-12 col-sm-6 col-6 m-0 p-0 pb-md-1";

  const priceText = document.createElement("p");
  priceText.className = "card-text";
  priceText.textContent = "Цена: " + autoFormat(price, "number");

  const buttonCol = document.createElement("div");
  buttonCol.className =
    "col-lg-6 d-flex justify-content-lg-end align-items-center col-md-12 justify-content-md-center align-items-md-center col-sm-6 justify-content-sm-end align-items-sm-end col-6 justify-content-end";

  const button = document.createElement("a");
  button.href = "#";
  button.className = "btn btn-primary";

  if (isBookInCart(id)) {
    button.textContent = "В корзине";
    button.classList.add("btn-danger");
  } else {
    button.classList.remove("btn-danger");
    button.textContent = "В корзину";
  }

  button.onclick = function (event) {
    event.preventDefault();
    // если уже добавлена, то удалить
    if (isBookInCart(id)) {
      if (delBookCart(id) === true) {
        button.classList.remove("btn-danger");
        button.textContent = "В корзину";
      }
    } else if (addInCart(id, 1)) {
      button.textContent = "В корзине";
      button.classList.add("btn-danger");
    }
  };

  // Добавление элементов в DOM
  priceCol.appendChild(priceText);
  buttonCol.appendChild(button);
  row.appendChild(priceCol);
  row.appendChild(buttonCol);
  container.appendChild(row);
  cardBody.appendChild(cardTitle);
  cardBody.appendChild(yearPub);
  cardBody.appendChild(authorsText);
  cardBody.appendChild(genresText);
  cardBody.appendChild(container);
  card.appendChild(image);
  card.appendChild(cardBody);
  cardCol.appendChild(card);

  // Возвращение созданной карточки
  return cardCol;
}

function createImg(imageName, id) {
  const imagePath = getImagePath(); // Фиксированный путь к изображениям
  const imgSrc = imagePath + imageName; // Полный путь к изображению

  const image = document.createElement("img");
  image.src = imgSrc;
  image.className = "card-img-top";
  image.alt = "Книга";

  image.onclick = function () {
    goToBookId(id);
  };

  return image;
}

// когда кликаешь по карте, нужно перейти на страницу конкретной книги
function goToBookId(id) {
  window.location.href = `../book/index.html?id=${id}`;
}

function authorsToCard(authors) {
  const authorsText = document.createElement("p");
  authorsText.className = "card-text";

  let resStr = "Автор: ";

  const authorNames = authors.map((author) => formatAuthorName(author));

  // если больше трех авторов, то нужно добавить "и другие"
  const maxAuthorsToShow = 3;
  const truncatedAuthorNames = authorNames.slice(0, maxAuthorsToShow);
  resStr += truncatedAuthorNames.join(", ");

  if (authorNames.length > maxAuthorsToShow) {
    resStr += " и другие";
  }

  authorsText.textContent = resStr;

  return authorsText;
}

// форматирование ФИО автора
function formatAuthorName({ lastName, firstName, middleName }) {
  let fullName = "";

  if (lastName) {
    fullName += lastName;

    if (firstName || middleName) {
      fullName += " ";
    }
  }

  if (firstName) {
    fullName += firstName.charAt(0).toUpperCase() + ".";
  }

  if (middleName) {
    fullName += middleName.charAt(0).toUpperCase() + ".";
  }

  return fullName.trim();
}

function genresToCard(genres) {
  const genresText = document.createElement("p");
  genresText.className = "card-text";
  let resStr = "Жанр: ";

  const genreTitles = genres.map((genre) => genre.title);

  const maxGenresToShow = 3;
  const firstGenre = genreTitles[0];
  const truncatedGenres = genreTitles.slice(1, maxGenresToShow);
  const otherGenresCount = genreTitles.length - 1;

  const maxLength = 20; // Максимальная длина строки жанра

  if (firstGenre.length > maxLength) {
    resStr += firstGenre + " и другие";
  } else {
    resStr += firstGenre;
    if (truncatedGenres.length > 0) {
      resStr += ", " + truncatedGenres.join(", ");
    }
  }

  if (otherGenresCount > 0 && firstGenre.length <= maxLength) {
    resStr += ` и ещё ${otherGenresCount}`;
  }

  genresText.textContent = resStr;

  return genresText;
}

// логика кнопки в корзину
// когда нажал первый раз - покрасить и поменять текст, добавить в хранилище
// когда нажал второй раз удалить из хранилища
// как мне сохранить объект книгу?
function clickInCart(btnCart, bookObject) {}

// фильтрация
export function renderFilters(
  genres,
  authors,
  publishers,
  divGenres,
  divAuthors,
  divPublishers
) {
  // Заполняем жанры
  divGenres.innerHTML = "";
  genres.forEach((genre) => {
    const checkbox = createCheckbox(genre.id, genre.title, "g");
    divGenres.appendChild(checkbox);
  });

  // Заполняем авторов
  divAuthors.innerHTML = "";
  authors.forEach((author) => {
    const checkbox = createCheckbox(
      author.id,
      formatAuthorNameForFilter(author),
      "a"
    );
    divAuthors.appendChild(checkbox);
  });

  // Заполняем издательства
  divPublishers.innerHTML = "";
  publishers.forEach((publisher, index) => {
    const checkbox = createCheckbox(index, publisher, "p");
    divPublishers.appendChild(checkbox);
  });
}

function createCheckbox(id, label, name) {
  const div = document.createElement("div");
  div.className = "form-check";

  const input = document.createElement("input");
  input.type = "checkbox";
  input.className = "form-check-input";
  input.id = id + name;

  const labelElement = document.createElement("label");
  labelElement.className = "form-check-label";
  labelElement.htmlFor = id + name;
  labelElement.textContent = label;

  div.appendChild(input);
  div.appendChild(labelElement);

  return div;
}
function formatAuthorNameForFilter(author) {
  let formattedName;
  if (author.lastName === "") {
    if (author.firstName) {
      formattedName = author.firstName; // Первая буква имени
    }
    if (author.middleName) {
      formattedName += author.middleName; // Первая буква отчества
    }
  } else {
    formattedName = author.lastName; // Фамилия всегда будет первой частью
    if (author.firstName) {
      formattedName += " " + author.firstName.substring(0, 1) + "."; // Первая буква имени
    }
    if (author.middleName) {
      formattedName += " " + author.middleName.substring(0, 1) + "."; // Первая буква отчества
    }
  }
  if (author.pseudonym) {
    formattedName += " - " + author.pseudonym;
  }
  return formattedName;
}
