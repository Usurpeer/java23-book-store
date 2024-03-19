//export

const api = {
  async getBooks(page, pageSize) {
    books = getCatalogBooks();

    return books.slice(page * pageSize, (page + 1) * pageSize);
  },
  async getAuthors() {
    return getCatalogAuthors();
  },
  async getGenres() {
    return getCatalogGenres();
  },
  async getPublishers() {
    return getCatalogPublishers();
  },
  async getPagesCount(pageSize) {
    return Math.ceil(getCatalogBooks().length / pageSize);
  },
};
