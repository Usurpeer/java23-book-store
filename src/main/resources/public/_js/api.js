export const api = {
  baseUrl: "http://localhost:8080/api/",
  getUrl(path, searchParams) {
    const url = new URL(path, this.baseUrl);
    if (searchParams) {
      url.search = searchParams.toString();
    }
    return url;
  },
  getJsonResult(response) {
    if (response.ok) {
      return response.json();
    }
    throw Error(
      `Request returned code = ${response.status} text = ${response.statusText}`
    );
  },
  get(path, searchParams) {
    return fetch(this.getUrl(path, searchParams)).then((response) =>
      this.getJsonResult(response)
    );
  },
  post(path, body, searchParams) {
    return fetch(this.getUrl(path, searchParams), {
      method: "POST",
      body: typeof body === "string" ? body : JSON.stringify(body),
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
    }).then((response) => this.getJsonResult(response));
  },
  put(path, body, searchParams) {
    return fetch(this.getUrl(path, searchParams), {
      method: "PUT",
      body: typeof body === "string" ? body : JSON.stringify(body),
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
    }).then((response) => this.getJsonResult(response));
  },
  delete(path, searchParams) {
    return fetch(this.getUrl(path, searchParams)).then((response) =>
      this.getJsonResult(response)
    );
  },
  postNoBody(path, body, searchParams) {
    return fetch(this.getUrl(path, searchParams), {
      method: "POST",
      body: typeof body === "string" ? body : JSON.stringify(body),
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
    }).then((response) => {
      //this.getJsonResult(response);
      if (response.status === 200) {
        return { message: "Заказ успешно создан", success: true };
      } else {
        throw new Error("Произошла ошибка при создании заказа");
      }
    });
  },
};
