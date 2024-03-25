import {
  getImagePath,
  plusQuantityBook,
  minusQuantityBook,
} from "../../_js/helpers.js";
import { addInCart } from "../../_js/helpers.js";
export function renderBooksCart(books, divCards) {
  divCards.innerHTML = "";

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
  quantity,
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
  priceText.textContent = "Цена: " + price;

  const inputCol = document.createElement("div");
  inputCol.className =
    "col-lg-6 d-flex justify-content-lg-end align-items-center col-md-12 justify-content-md-center align-items-md-center col-sm-6 justify-content-sm-end align-items-sm-end col-6 justify-content-end";

  const inputGroup = document.createElement("div");
  inputGroup.className = "input-group";

  const minusButton = document.createElement("button");
  minusButton.className = "btn btn-outline-secondary";
  minusButton.type = "button";
  minusButton.textContent = "-";
  minusButton.addEventListener("click", () => {
    let oldVal = input.value;
    input.value = minusQuantityBook(input.value);
    if (input.value !== oldVal) {
      addInCart(id, input.value);
    }
  });

  // Кнопка увеличения количества
  const plusButton = document.createElement("button");
  plusButton.className = "btn btn-outline-secondary";
  plusButton.type = "button";
  plusButton.textContent = "+";
  plusButton.addEventListener("click", () => {
    let oldVal = input.value;
    input.value = plusQuantityBook(input.value, 100);
    if (input.value !== oldVal) {
      addInCart(id, input.value);
    }
  });

  const input = document.createElement("input");
  input.type = "text";
  input.className = "form-control text-center";
  input.value = quantity;
  input.readOnly = true;

  // Добавление элементов в DOM
  priceCol.appendChild(priceText);
  inputGroup.appendChild(minusButton);
  inputGroup.appendChild(input);
  inputGroup.appendChild(plusButton);
  inputCol.appendChild(inputGroup);
  row.appendChild(priceCol);
  row.appendChild(inputCol);
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

// когда кликаешь по книге, нужно перейти на страницу книги
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
