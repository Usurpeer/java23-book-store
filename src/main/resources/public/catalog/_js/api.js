import { api as basicApi } from "../../_js/api.js";

export const api = {
  async getCatalog(page, pageSize, sorting, filterBy) {
    const params = new URLSearchParams();
    params.append("page", page || 0);
    params.append("pageSize", pageSize || 10);
    if (filterBy) {
      params.append("search", filterBy);
    }
    if (sorting.field) {
      params.append("sortingField", sorting.field);
    }
    if (sorting.asc !== undefined && sorting.asc !== null) {
      params.append("sortingDesc", !sorting.asc);
    }
    return basicApi.get("catalog", params);
  },
  async getCatalogPost(page, pageSize, sorting, search, filters) {
    const body = {
      page: page !== undefined ? page : 0,
      pageSize: pageSize !== undefined ? pageSize : 10,
      search: search || "",
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
