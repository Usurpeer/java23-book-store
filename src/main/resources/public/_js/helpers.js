// заказы, заказ, корзина
export function autoFormat(value, format) {
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
export function setLoading(spinner, isLoading) {
  spinner.hidden = !isLoading;
}

//export
export function setAlert(divAlert, alert, message) {
  if (!message || message === "") {
    divAlert.hidden = true;
    return;
  }

  alert.innerText = message;
  divAlert.hidden = false;
}

// метод, который добавит к Выйти логин пользователя
// export
export function setCustomerLogin(element, login) {
  if (element && login) {
    element.textContent = `Выйти (${login})`;
    return true;
  }
  return false;
}

export function getCustomerLogin() {
  return sessionStorage.getItem("login");
}
export function getCustomerId() {
  return sessionStorage.getItem("id");
}

export function getImagePath() {
  return "../img/";
}

// метод, который формирует storage Корзины, то есть order

// создание sessionStorage
export function setOrderStorage(orderStr) {
  if (!orderStr) {
    return false;
  }

  sessionStorage.removeItem("order");
  sessionStorage.setItem("order", orderStr);

  return true;
}

export function getOrderStorage() {
  return sessionStorage.getItem("order");
}

export function plusQuantityBook(input, maxDigit) {
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

export function minusQuantityBook(input) {
  if (input.value == "" || isNaN(input.value) || parseInt(input.value) <= 1) {
    input.value = 1;
  } else {
    input.value = parseInt(input.value) - 1;
  }
}
// написать логику кнопки в корзину, как там проверяется, и тп
