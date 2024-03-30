import { api as basicApi } from "../../_js/api.js";

export const api = {
  async getOrders(login) {
    const params = new URLSearchParams();
    params.append("login", login);
    return basicApi.get("orders", params);
  },
};
