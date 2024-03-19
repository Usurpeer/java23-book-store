document.addEventListener("DOMContentLoaded", () => {
  const divLoadingSpinner = document.getElementById("div-loading");
  const divAlert = document.getElementById("div-alert");
  const alert = document.getElementById("alert");

  // поиск
  const inputSearch = document.getElementById("input-search");
  const btnSearchApply = document.getElementById("btn-search");

  // фильтрация
  // цена, год
  const inputPriceFrom = document.getElementById("input-price-from");
  const inputPriceTo = document.getElementById("input-price-to");

  const inputYearFrom = document.getElementById("input-year-from");
  const inputYearTo = document.getElementById("input-year-to");

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

  // поисковый запрос
  let searchValue;

  // стартовые данные
  let currentPage = 0;
  let pageSize = 6;

  loadCatalog();

  function loadCatalog() {
    pagination.replaceChildren();
    setLoading(divLoadingSpinner, true);

    setTimeout(() => {
      Promise.all([
        api.getBooks(currentPage, pageSize),
        api.getAuthors(),
        api.getGenres(),
        api.getPublishers(),
        api.getPagesCount(pageSize),
      ])

        .then(
          ([booksRes, authorsRes, genresRes, publishersRes, pagesCount]) => {
            books = booksRes;
            genres = genresRes;
            authors = authorsRes;
            publishers = publishersRes;

            fillFiltersFields();
            showBooks();
            setPagesCount(pagesCount);
          }
        )
        .catch((error) => {
          console.error("getActiveOrderCustomerByOrderId failed", error);
          setAlert(
            divAlert,
            alert,
            "Произошла ошибка при загрузке книг заказа"
          );
        })
        .finally(() => {
          setLoading(divLoadingSpinner, false);
        });
    }, 500);
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
  }
  function setPagesCount(count) {
    pagination.append(createPaginationLi(0, "&laquo;"));
    generateRange(count).forEach((p) => {
      pagination.append(createPaginationLi(p, p + 1, p == currentPage));
    });
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
          currentPage = page;
          loadCatalog();
        });
      }
      a.innerHTML = text;
      return li;
    }
  }
});
