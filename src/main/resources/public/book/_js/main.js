import { api } from "./api.js";
import {
  setLoading,
  setAlert,
  minusQuantityBook,
  plusQuantityBook,
  isBookInCart,
  addInCart,
  getBookInCart,
  delBookCart,
} from "../../_js/helpers.js";
import { fillBookPage } from "./component.js";

document.addEventListener("DOMContentLoaded", () => {
  const idDivContainer = document.getElementById("id-div-container-book");
  const divLoadingSpinner = document.getElementById("div-loading");
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

  let idParams;
  let book;
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

    const urlParams = new URLSearchParams(window.location.search);
    const idParams = urlParams.get("id");

    api
      .getBook(idParams)
      .then((result) => {
        book = result;
        showBook();
        getValuesInCart();
        idDivContainer.hidden = false;
      })
      .catch((err) => {
        console.error("getBook failed", err);
        setAlert(divAlert, alert, "Произошла ошибка при загрузке книги");
      })
      .finally(() => {
        setLoading(divLoadingSpinner, false);
      });
  }
  function showBook() {
    fillBookPage(page, book);
  }

  idBtnGoCart.addEventListener("click", () => {
    const quantity = parseInt(idFieldBookQuantity.value);
    if (isBookInCart(book.id)) {
      if (delBookCart(book.id) === true) {
        idBtnGoCart.classList.remove("btn-danger");
        idBtnGoCart.textContent = "В корзину";
      }
    } else if (addInCart(book.id, quantity)) {
      idBtnGoCart.textContent = "В корзине";
      idBtnGoCart.classList.add("btn-danger");
    }
  });

  idMinusQuantity.addEventListener("click", () => {
    let res = minusQuantityBook(idFieldBookQuantity.value);
    idFieldBookQuantity.value = res;
    if (isBookInCart(book.id)) {
      addInCart(book.id, res);
      getValuesInCart();
    }
  });
  idPlusQuantity.addEventListener("click", () => {
    let maxDigit = 100;
    let res = plusQuantityBook(idFieldBookQuantity.value, maxDigit);
    idFieldBookQuantity.value = res;
    if (isBookInCart(book.id)) {
      addInCart(book.id, res);
    }
  });
  function getValuesInCart() {
    if (isBookInCart(book.id)) {
      idFieldBookQuantity.value = getBookInCart(book.id).quantity;
      idBtnGoCart.textContent = "В корзине";
      idBtnGoCart.classList.add("btn-danger");
    } else {
      idBtnGoCart.classList.remove("btn-danger");
      idBtnGoCart.textContent = "В корзину";
    }
  }
});
