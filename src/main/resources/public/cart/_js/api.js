import { api as basicApi } from "../../_js/api.js";

// отправка на сервер для оформления заказа
// books - id quantity
export const api = {
  async getBooksPost(cart) {
    const body = {
      books: cart !== undefined ? cart : [],
    };

    return basicApi.post("cart/getBooks", body);
  },
  async createOrderPost(cart, login) {
    const body = {
      books: cart !== undefined ? cart : [],
      login: login !== undefined ? login : "",
    };

    return basicApi.postNoBody("cart/createOrder", body);
  },
};
