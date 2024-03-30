import { api as basicApi } from "../../../_js/api.js";

export const api = {
  async getOrder(id) {
    const params = new URLSearchParams();
    params.append("id", parseInt(id));
    return basicApi.get("orders/order", params);
  },
};
