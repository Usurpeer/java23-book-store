//import api from ".api.js";

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

  loadOrderBooks();

  function loadOrderBooks() {
    setLoading(divLoadingSpinner, true);

    divTotal.hidden = true;

    setTimeout(() => {
      apiOrder
        .getActiveOrderCustomerByOrderId(1, getCustomerId())
        .then((result) => {
          books = result.order.books.map((b) => ({
            title: b.book.title,
            authors: b.book.authors,
            genres: b.book.genres,
            price: b.price,
            quantity: b.quantity,
            imageName: b.book.imageName,
          }));
          setNumberOrder(result.order.id);
          showBooks();
          showTotalValue(result.order.totalAmount);
          divTotal.hidden = false;
        })
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
    renderOrderBooks(books, divCards);
  }
  function showTotalValue(value) {
    totalValue.innerText = autoFormat(value);
  }
  function setNumberOrder(id){
    idNumOrder.innerHTML = "Заказ № " + id;
  }
});
