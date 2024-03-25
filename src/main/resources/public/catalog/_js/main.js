import { api } from "./api.js";
import { setCheckBoxes, getSelectedValues } from "./helper.js";
import { renderCatalogBooks, renderFilters } from "./component.js";
import { setLoading, setAlert } from "../../_js/helpers.js";

document.addEventListener("DOMContentLoaded", () => {
  const divLoadingSpinner = document.getElementById("div-loading");
  const divAlert = document.getElementById("div-alert");
  const alert = document.getElementById("alert");

  // поиск
  const inputSearch = document.getElementById("input-search");
  const btnSearchApply = document.getElementById("btn-search");

  // фильтрация
  // цена, год
  const inputMinPrice = document.getElementById("input-price-from");
  const inputMaxPrice = document.getElementById("input-price-to");

  const inputMinYear = document.getElementById("input-year-from");
  const inputMaxYear = document.getElementById("input-year-to");

  //сортировка
  const linkSortByTitle = document.getElementById("id-sort-title");
  const linkSortByPrice = document.getElementById("id-sort-price");
  const linkSortByYear = document.getElementById("id-sort-year");

  // жанры
  const divGenres = document.getElementById("id-div-genres");

  // авторы
  const divAnthors = document.getElementById("id-div-authors");

  // издательства
  const divPublishers = document.getElementById("id-div-publishers");

  // кнопки:
  const btnFilterApply = document.getElementById("btn-filter-apply");
  const btnFilterReset = document.getElementById("btn-filter-reset");

  // книги
  const divBooksCards = document.getElementById("id-div-before-books");

  // пагинация
  const pagination = document.getElementById("catalog-pagination");

  // данные с api
  let books = [];
  let genres = [];
  let authors = [];
  let publishers = [];

  // поисковый запрос и сортировка
  //let ascValue = true;
  //let sortField = "title";
  let searchValue;
  let sorting = {
    btn: linkSortByTitle,
    field: "title",
    asc: true,
  };
  let filters = {
    minPrice: "",
    maxPrice: "",
    minYear: "",
    maxYear: "",
    authors: [],
    genres: [],
    publishers: [],
  };

  // стартовые данные
  let currentPage = 0;
  let pageSize = 6;

  loadCatalog();

  function loadCatalog() {
    pagination.replaceChildren();
    divBooksCards.innerHTML = "";
    setLoading(divLoadingSpinner, true);

    api
      .getCatalogPost(currentPage, pageSize, sorting, searchValue, filters)
      .then((catalog) => {
        books = catalog.books;
        genres = catalog.filters.allGenres;
        authors = catalog.filters.allAuthors;
        publishers = catalog.filters.allPublishers;

        setPagesCount(Math.ceil(catalog.booksQuantity / pageSize));
        fillFiltersFields();
        showBooks();
      })
      .catch((error) => {
        console.error("loadCatalog failed", error);
        setAlert(divAlert, alert, "Произошла ошибка при загрузке каталога");
      })
      .finally(() => {
        setLoading(divLoadingSpinner, false);
      });
  }

  function showBooks() {
    renderCatalogBooks(books, divBooksCards);
  }
  function fillFiltersFields() {
    renderFilters(
      genres,
      authors,
      publishers,
      divGenres,
      divAnthors,
      divPublishers
    );

    // тут нужно каждое поле заполнить, если значение в filters не пустое
    // для того, чтобы при кнопке применить ничего не сбрасывалось
    setFIltersValues();
  }

  function setFIltersValues() {
    if (filters.minPrice) {
      inputMinPrice.value = filters.minPrice;
    }
    if (filters.maxPrice) {
      inputMinPrice.value = filters.maxPrice;
    }
    if (filters.minYear) {
      inputMinPrice.value = filters.minYear;
    }
    if (filters.maxYear) {
      inputMinPrice.value = filters.maxYear;
    }
    setCheckBoxes(
      divGenres.querySelectorAll(".form-check-input"),
      filters.genres
    );
    setCheckBoxes(
      divAnthors.querySelectorAll(".form-check-input"),
      filters.authors
    );
    setCheckBoxes(
      divPublishers.querySelectorAll(".form-check-input"),
      filters.publishers
    );
  }

  function setPagesCount(count) {
    pagination.replaceChildren();
    if (count <= 0) {
      return;
    }
    const maxPagesToShow = 10;
    const pagesBeforeCurrent = Math.min(
      Math.max(currentPage - 1, 0),
      Math.floor(maxPagesToShow / 2)
    );
    const pagesAfterCurrent = Math.min(
      count - currentPage,
      Math.floor(maxPagesToShow / 2)
    );
    const startPage = Math.max(currentPage - pagesBeforeCurrent, 1);
    const endPage = Math.min(currentPage + pagesAfterCurrent, count - 1);

    // Добавляем стрелку "назад"
    pagination.append(createPaginationLi(currentPage - 1, "&laquo;"));
    pagination.append(createPaginationLi(0, 1, 0 == currentPage));

    // Добавляем страницы
    for (let i = startPage; i < endPage; i++) {
      pagination.append(createPaginationLi(i, i + 1, i == currentPage));
    }

    if (count != 1) {
      pagination.append(
        createPaginationLi(count - 1, count, count == currentPage - 1)
      );
    }
    // Добавляем стрелку "вперед"
    pagination.append(createPaginationLi(count - 1, "&raquo;"));

    function createPaginationLi(page, text, active) {
      const li = document.createElement("li");
      const a = document.createElement("a");
      li.append(a);
      li.classList.add("page-item");
      a.classList.add("page-link");
      if (active) {
        li.classList.add("active");
      } else {
        a.href = "#";
        a.addEventListener("click", () => {
          if (p < 0 || p > count) {
            return;
          }
          currentPage = page;
          loadCatalog();
        });
      }
      a.innerHTML = text;
      return li;
    }
  }

  // Добавление обработчика события к кнопке с указанным id и значением
  linkSortByTitle.addEventListener("click", () =>
    handleSortButtonClick(linkSortByTitle, "title")
  );
  linkSortByPrice.addEventListener("click", () =>
    handleSortButtonClick(linkSortByPrice, "price")
  );
  linkSortByYear.addEventListener("click", () =>
    handleSortButtonClick(linkSortByYear, "year")
  );
  // Функция для обработки клика на кнопке сортировки
  function handleSortButtonClick(btn, value) {
    if (sorting.field === value) {
      sorting.asc = !sorting.asc;
      const i = sorting.btn.querySelector("i");
      // сменить вид иконки
      if (i.classList.contains("bi-sort-down-alt")) {
        i.classList.remove("bi-sort-down-alt");
        i.classList.add("bi-sort-up-alt");
      } else {
        i.classList.remove("bi-sort-up-alt");
        i.classList.add("bi-sort-down-alt");
      }
    } else {
      // удалить i у старой кнопки
      sorting.btn.removeChild(sorting.btn.querySelector("i"));
      sorting.btn = btn;
      // добавить i к кнопке
      const i = document.createElement("i");
      i.classList.add("bi", "bi-sort-down-alt", "m-1");

      // Добавляем иконку к кнопке
      sorting.btn.appendChild(i);

      sorting.field = value;
    }
    currentPage = 0;
    loadCatalog();
  }

  btnFilterApply.addEventListener("click", () => {
    const strRes = getFiltersValues();
    if (strRes !== "") {
      setAlert(divAlert, alert, strRes);
    } else {
      loadCatalog();
    }
  });
  function getFiltersValues() {
    filters.genres = getSelectedValues(
      divGenres.querySelectorAll(".form-check-input")
    );
    filters.authors = getSelectedValues(
      divAnthors.querySelectorAll(".form-check-input")
    );
    filters.publishers = getSelectedValues(
      divPublishers.querySelectorAll(".form-check-input")
    );

    return validateInput();
  }
  function validateInput() {
    const minPrice = inputMinPrice.value.trim();
    const maxPrice = inputMaxPrice.value.trim();
    const maxYear = inputMinYear.value.trim();
    const minYear = inputMaxYear.value.trim();
    filters.maxPrice = "";
    filters.minYear = "";
    filters.minYear = "";
    filters.maxYear = "";

    // Попарная проверка на непустые значения и положительные числа
    // если обе цены / оба года не введены это допустимо, если хоть одно ввели то нужно второе
    // если поля пустые то пропускаются
    if (minPrice !== "" || maxPrice !== "") {
      if (parseFloat(minPrice) >= 0 && parseFloat(maxPrice) >= 0) {
        filters.minPrice = minPrice;
        filters.maxPrice = maxPrice;
      } else {
        return "Введите оба значения цены, положительные числа";
      }
    }

    if (maxYear !== "" || minYear !== "") {
      if (parseInt(maxYear) >= 0 && parseInt(minYear) >= 0) {
        filters.maxYear = maxYear ? parseInt(maxYear) : -1;
        filters.minYear = minYear ? parseInt(minYear) : -1;
      } else {
        return "Введите оба значения года выпуска, положительные целые числа";
      }
    }

    return "";
  }

  btnFilterReset.addEventListener("click", () => {
    clearChecked(divGenres.querySelectorAll(".form-check-input"));
    clearChecked(divAnthors.querySelectorAll(".form-check-input"));
    clearChecked(divPublishers.querySelectorAll(".form-check-input"));

    inputMinPrice.value = "";
    inputMaxPrice.value = "";
    inputMinYear.value = "";
    inputMaxYear.value = "";
  });
  function clearChecked(checkboxes) {
    checkboxes.forEach((checkbox) => {
      if (checkbox.checked) {
        checkbox.checked = false;
      }
    });
    filters = {
      minPrice: "",
      maxPrice: "",
      minYear: "",
      maxYear: "",
      authors: [],
      genres: [],
      publishers: [],
    };
  }
});
