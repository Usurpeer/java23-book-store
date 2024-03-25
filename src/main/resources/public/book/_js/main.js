//import api from "api.js";

document.addEventListener("DOMContentLoaded", () => {
  const idDivContainer = document.getElementById("id-div-container-book");
  const divLoadingSpinner = document.getElementById("div-loading");
  //const loadingSpinner = document.getElementById("loading");
  const divAlert = document.getElementById("div-alert");
  const alert = document.getElementById("alert");

  const idImg = document.getElementById("id-img-book");
  const idBookTitle = document.getElementById("id-book-title");
  const idBookYear = document.getElementById("id-book-year");
  const idBookPublisher = document.getElementById("id-book-publisher");
  const idBookPrice = document.getElementById("id-book-price");

  const idHForAddAuthors = document.getElementById("id-book-after-p-authors");
  const idHForAddGenres = document.getElementById("id-book-after-p-genres");

  const idPDescription = document.getElementById("id-book-description");

  const idFieldBookQuantity = document.getElementById("id-book-quantity");
  const idMinusQuantity = document.getElementById("id-btn-quantity-minus");
  const idPlusQuantity = document.getElementById("id-btn-quantity-plus");

  const idBtnGoCart = document.getElementById("id-btn-go-cart");

  let book = [];
  let page = {
    idImg: idImg,
    idBookTitle: idBookTitle,
    idBookYear: idBookYear,
    idBookPublisher: idBookPublisher,
    idBookPrice: idBookPrice,
    idHForAddAuthors: idHForAddAuthors,
    idHForAddGenres: idHForAddGenres,
    idPDescription: idPDescription,
  };
  loadBook();

  function loadBook() {
    setLoading(divLoadingSpinner, true);

    idDivContainer.hidden = true;

    setTimeout(() => {
      apiBook
        .getBook()
        .then((result) => {
          book = result;
          showBook();

          idDivContainer.hidden = false;
        })
        .catch((err) => {
          console.error("getBook failed", err);
          setAlert(divAlert, alert, "Произошла ошибка при загрузке книги");
        })
        .finally(() => {
          setLoading(divLoadingSpinner, false);
        });
    }, 500);
  }
  function showBook() {
    fillBookPage(page, book);
  }

  idMinusQuantity.addEventListener("click", () => {
    minusQuantityBook(idFieldBookQuantity);
  });
  idPlusQuantity.addEventListener("click", () => {
    let maxDigit = 100;
    plusQuantityBook(idFieldBookQuantity, maxDigit);
  });
});
