// import api from "./api.js";

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

    //задержка 0.5 сек
    setTimeout(() => {
      // тут достать id из куки
      let id = 1;
      api
        .getActiveOrdersCustomer(id)
        .then((result) => {
          orders = result.orders.map((o) => ({
            id: o.id,
            date: o.created,
            price: o.totalAmount,
            quantity: o.totalQuantity,
          }));
          showSortedOrders();
          showTotalValue(result.ordersTotalAmount);
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
          // на всякий случай, проверка, что таблица загружена
          if (divLoadingSpinner.hidden == true) {
            /*const rows = table.getElementsByTagName("tr");

            // Перебираем все строки и добавляем обработчик события клика
            for (const row of rows) {
              row.addEventListener("click", () => {
                // считать id tr, по этому id перейти на ссылку orders{id}
                // но, сейчас костыль в виде статичного перехода на страницу mockЗаказа
                window.location.href = "order/order.html";
              });
            }*/
          }
        });
    }, 500);
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
