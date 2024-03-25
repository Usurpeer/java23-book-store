import { api } from "./api.js";
import { renderBooksCart } from "./component.js";
import { getTotalAmount } from "./helper.js";
import {
  setLoading,
  setAlert,
  getCart,
  setCart,
  getCustomerLogin,
  clearCart,
} from "../../_js/helpers.js";

document.addEventListener("DOMContentLoaded", () => {
  const divCards = document.getElementById("div-cards");
  const divLoadingSpinner = document.getElementById("div-loading");
  const divAlert = document.getElementById("div-alert");
  const alertEl = document.getElementById("alert");

  const divTotal = document.getElementById("div-total-and-createOrder");
  const totalValue = document.getElementById("id-total-amount");
  const btnCreateOrder = document.getElementById("id-btn-create-order");

  let cart = getCart();
  let books = [];
  if (!cart) {
    setAlert(divAlert, alertEl, "Произошла ошибка при загрузке книг заказа");
    return;
  }
  loadCartBooks();

  function loadCartBooks() {
    setLoading(divLoadingSpinner, true);
    divTotal.hidden = true;

    api
      .getBooksPost(cart)
      .then((result) => {
        books = result.books;
        showCart();
        showTotalValue();
      })
      .catch((error) => {
        console.error("getBooksPost", error);
        setAlert(divAlert, alertEl, "Произошла ошибка при получении корзины");
      })
      .finally(() => {
        setLoading(divLoadingSpinner, false);
      });
  }
  function showTotalValue() {
    totalValue.innerText = getTotalAmount(books);
    divTotal.hidden = false;
  }
  function showCart() {
    renderBooksCart(books, divCards);
  }
  btnCreateOrder.addEventListener("click", () => {
    createOrderClick();

    function createOrderClick() {
      let login = getCustomerLogin();
      cart = getCart();
      if (cart && cart.length > 0 && login) {
        api
          .createOrderPost(cart, login)
          .then((result) => {
            alert("Заказ успешно создан");
            clearAll();
          })
          .catch((error) => {
            console.error("createOrderPost", error);
            alert("Произошла ошибка при создании заказа");
          })
          .finally(() => {
            setLoading(divLoadingSpinner, false);
          });
      }
    }
    function clearAll() {
      clearCart();
      cart = [];
      totalValue.innerText = 0;
      divCards.innerHTML = "";
    }
  });
});
