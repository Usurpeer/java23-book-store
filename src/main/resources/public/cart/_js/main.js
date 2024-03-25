document.addEventListener("DOMContentLoaded", () => {
  // сюда добавлять элементы после
  const divCards = document.getElementById("div-cards");
  const divLoadingSpinner = document.getElementById("div-loading");
  const divAlert = document.getElementById("div-alert");
  const alert = document.getElementById("alert");

  const divTotal = document.getElementById("div-total-and-createOrder");
  const total = document.getElementById("total");
  const totalValue = document.getElementById("id-total-amount");
  const btnCreateOrder = document.getElementById("id-btn-create-order");

  let cart = getCartInStorage();

  if (!cart) {
    console.error("getActiveOrderCustomerByOrderId failed", error);
    setAlert(divAlert, alert, "Произошла ошибка при загрузке книг заказа");
    return;
  }

  loadCartBooks();

  function loadCartBooks() {
    setLoading(divLoadingSpinner, true);

    divTotal.hidden = true;

    setTimeout(() => {
      showCart();
      showTotalValue(cart.totalAmount);
      divTotal.hidden = false;
      setLoading(divLoadingSpinner, false);
    }, 500);
  }
  function showTotalValue(value) {
    totalValue.innerText = autoFormat(value);
  }
  function showCart() {
    renderBooksCart(cart, divCards);
  }
});
