import { api } from "./api.js";
import { setRows, showSortBy } from "./component.js";
import { changeSorting, sorted } from "./helper.js";
import {
  setLoading,
  setAlert,
  autoFormat,
  getCustomerLogin,
} from "../../_js/helpers.js";

document.addEventListener("DOMContentLoaded", () => {
  const table = document.getElementById("table-active_orders_customer");
  const divLoadingSpinner = document.getElementById("div-loading");
  //const loadingSpinner = document.getElementById("loading");
  const divAlert = document.getElementById("div-alert");
  const alert = document.getElementById("alert");
  const divTotal = document.getElementById("div-total");
  const total = document.getElementById("total");
  const totalValue = document.getElementById("id-total-amount");
  const sortOptions = document.querySelectorAll("[sortBy]");

  let orders = [];
  const sorting = {};

  sortOptions.forEach((s) => {
    s.addEventListener("click", (ev) => {
      setSorting(ev.target.attributes["sortBy"].value);
    });
  });

  setSorting("id");
  loadAcriveOrders();

  function loadAcriveOrders() {
    setLoading(divLoadingSpinner, true);

    table.hidden = true;
    divTotal.hidden = true;

    api
      .getOrders(getCustomerLogin())
      .then((result) => {
        orders = result.orders.map((o) => ({
          id: o.id,
          date: o.created,
          price: o.amount,
          quantity: o.quantityBooks,
          status: o.status,
        }));
        showSortedOrders();
        showTotalValue(result.totalAmount);
        table.hidden = false;
        divTotal.hidden = false;
      })
      .catch((err) => {
        console.error("getActiveOrdersCustomer failed", err);
        setAlert(
          divAlert,
          alert,
          "Произошла ошибка при загрузке активных заказов"
        );
      })
      .finally(() => {
        setLoading(divLoadingSpinner, false);
      });
  }

  function setSorting(field) {
    changeSorting(sorting, field);
    showSortBy(sortOptions, sorting);
    showSortedOrders();
  }

  function showSortedOrders() {
    orders = sorted(orders, sorting);
    setRows(table, orders);
  }

  function showTotalValue(value) {
    totalValue.innerText = autoFormat(value);
  }
});
