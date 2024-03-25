import { api } from "./api.js";
import { renderOrderBooks } from "./component.js";
import { setLoading, setAlert, autoFormat } from "../../../_js/helpers.js";
document.addEventListener("DOMContentLoaded", () => {
  const idNumOrder = document.getElementById("id-num-order");
  const divCards = document.getElementById("div-cards");
  const divLoadingSpinner = document.getElementById("div-loading");
  const divAlert = document.getElementById("div-alert");
  const alert = document.getElementById("alert");

  const divTotal = document.getElementById("div-total");
  const total = document.getElementById("total");
  const totalValue = document.getElementById("id-total-amount");

  let books = [];
  const urlParams = new URLSearchParams(window.location.search);
  const idParams = urlParams.get("id");

  idNumOrder.innerHTML = "Заказ № " + idParams;

  loadOrderBooks();

  function loadOrderBooks() {
    setLoading(divLoadingSpinner, true);

    divTotal.hidden = true;

    api
      .getOrder(idParams)
      .then((result) => {
        books = result.books.map((o) => ({
          id: o.book.id,
          title: o.book.title,
          year: o.book.year,
          authors: o.book.authors,
          genres: o.book.genres,
          imageName: o.book.imageName,
          price: o.price,
          quantity: o.quantity,
        }));

        showBooks();
        showTotalValue(result.totalAmount);

        divTotal.hidden = false;
      })
      .catch((error) => {
        console.error("getActiveOrderCustomerByOrderId failed", error);
        setAlert(divAlert, alert, "Произошла ошибка при загрузке книг заказа");
      })
      .finally(() => {
        setLoading(divLoadingSpinner, false);
      });
  }
  function showBooks() {
    renderOrderBooks(books, divCards);
  }
  function showTotalValue(value) {
    totalValue.innerText = autoFormat(value);
  }
});
