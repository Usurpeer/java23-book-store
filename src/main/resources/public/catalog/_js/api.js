import { api as basicApi } from "../../_js/api.js";

export const api = {
  async getCatalogPost(page, pageSize, sorting, search, filters, searchType) {
    const body = {
      page: page !== undefined ? page : 0,
      pageSize: pageSize !== undefined ? pageSize : 10,
      search: search || "",
      searchType: searchType || "",
      field:
        sorting.field !== undefined && sorting.field !== null
          ? sorting.field
          : "title",
      asc:
        sorting.asc !== undefined && sorting.asc !== null ? sorting.asc : false,
      filters: filters || {},
    };

    return basicApi.post("catalog", body);
  },
  getPagesCount(pageSize, filterBy) {
    const params = new URLSearchParams();
    if (filterBy) {
      params.append("search", filterBy);
    }
    return basicApi
      .get("catalog/count", params)
      .then((result) => Math.ceil(result.count / pageSize));
  },
};
