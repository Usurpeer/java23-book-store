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

export function setLoading(spinner, isLoading) {
  spinner.hidden = !isLoading;
}

export function setAlert(divAlert, alert, message) {
  if (!message || message === "") {
    divAlert.hidden = true;
    return;
  }

  alert.innerText = message;
  divAlert.hidden = false;
}

// метод, который добавит к Выйти логин пользователя
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

export function getImagePath() {
  return "../img/";
}

export function plusQuantityBook(value, maxDigit) {
  if (
    value == "" ||
    isNaN(value) ||
    parseInt(value) < 1 ||
    parseInt(value) > maxDigit
  ) {
    return 1;
  } else {
    return parseInt(value) + 1;
  }
}

export function minusQuantityBook(value) {
  if (value == "" || isNaN(value) || parseInt(value) <= 1) {
    return 0;
  } else {
    return parseInt(value) - 1;
  }
}

// создание sessionStorage
export function setCart(cart) {
  if (!cart || cart.length === 0) {
    sessionStorage.removeItem("cart");
    return false;
  }

  sessionStorage.setItem("cart", cart);
  return true;
}

export function getCart() {
  let cart = sessionStorage.getItem("cart");
  if (!cart) {
    setCart(JSON.stringify([])); // Инициализация как пустой массив JSON
    return [];
  }

  return JSON.parse(cart);
}

// метод, по нажатию на кнопку добавить
export function addInCart(id, quantity) {
  let book = {
    id: +id,
    quantity: +quantity,
  };
  let cart = getCart();

  // Ищем книгу в корзине
  let existingBook = cart.find((cartBook) => cartBook.id === book.id);

  // Если книга уже есть в корзине, обновляем количество книги
  if (existingBook) {
    // Если количество книги равно 0, удаляем ее из корзины
    if (book.quantity === 0) {
      cart = cart.filter((cartBook) => cartBook.id !== book.id);
    } else {
      // Иначе обновляем количество книги в корзине
      existingBook.quantity = book.quantity;
    }
  } else {
    // Если книги нет в корзине и ее количество не равно 0, добавляем ее
    if (book.quantity !== 0) {
      cart.push({ id: book.id, quantity: book.quantity });
    }
  }

  // Сохраняем обновленную корзину в sessionStorage
  return setCart(JSON.stringify(cart));
}

export function isBookInCart(id) {
  let cart = getCart();

  return cart.some((cartBook) => cartBook.id === id);
}
export function delBookCart(id) {
  let cart = getCart();
  cart = cart.filter((item) => item.id !== id);
  return setCart(JSON.stringify(cart));
}
export function getBookInCart(bookId) {
  const cart = getCart();
  return cart.find((item) => item.id === bookId);
}
export function clearCart() {
  sessionStorage.removeItem("cart");
  sessionStorage.setItem("cart", JSON.stringify([]));
}
