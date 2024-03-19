// заказы, заказ, корзина
function autoFormat(value, format) {
  switch (
    format ||
    typeof value // если формат неопределен, то тип value
  ) {
    case "string":
      return value;
    //case "date":
    case "year":
      return value.toLocaleString("ru-Ru", {
        maximumFractionDigits: 0, // удаление дробной части
        userGrouping: false, // отключение группировки разрядов
      });
    case "integer":
      return value.toLocaleString("ru-Ru", {
        maximumFractionDigits: 0,
        userGrouping: true,
      });
    case "number":
    case "currency":
      return value.toLocaleString("ru-Ru", {
        maximumFractionDigits: 2,
        userGrouping: true,
      });
    case "boolean":
      return value ? "Да" : "Нет";
    default:
      if (value instanceof Date) {
        return value.toLocaleString();
      }
      return value?.toString();
  }
}

//export
function setLoading(spinner, isLoading) {
  spinner.hidden = !isLoading;
}

//export
function setAlert(divAlert, alert, message) {
  if (!message) {
    divAlert.hidden = true;
    return;
  }

  alert.innerText = message;
  divAlert.hidden = false;
}

// метод, который добавит к Выйти логин пользователя
// export
function setCustomerLogin(element, login) {
  if (element && login) {
    element.textContent = `Выйти (${login})`;
    return true;
  }
  return false;
}

function getCustomerLogin() {
  return sessionStorage.getItem("login");
}
function getCustomerId() {
  return sessionStorage.getItem("id");
}

function getImagePath() {
  return "../../../resources/img/";
}

// метод, который формирует storage Корзины, то есть order

// создание sessionStorage
function setOrderStorage(orderStr) {
  if (!orderStr) {
    return false;
  }

  sessionStorage.removeItem("order");
  sessionStorage.setItem("order", orderStr);

  return true;
}

function getOrderStorage() {
  return sessionStorage.getItem("order");
}

function plusQuantityBook(input, maxDigit) {
  if (
    input.value == "" ||
    isNaN(input.value) ||
    parseInt(input.value) < 1 ||
    parseInt(input.value) >= maxDigit
  ) {
    input.value = 1;
  } else {
    input.value = parseInt(input.value) + 1;
  }
}

function minusQuantityBook(input) {
  if (input.value == "" || isNaN(input.value) || parseInt(input.value) <= 1) {
    input.value = 1;
  } else {
    input.value = parseInt(input.value) - 1;
  }
}
// написать логику кнопки в корзину, как там проверяется, и тп
