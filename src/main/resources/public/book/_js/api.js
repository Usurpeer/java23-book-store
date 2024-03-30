import { api as basicApi } from "../../_js/api.js";

export const api = {
  async getBook(bookId) {
    const params = new URLSearchParams();
    params.append("id", parseInt(bookId));
    return basicApi.get("book", params);
  },
};
