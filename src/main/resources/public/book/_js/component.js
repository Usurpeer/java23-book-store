function fillBookPage(
  {
    idImg,
    idBookTitle,
    idBookYear,
    idBookPublisher,
    idBookPrice,
    idHForAddAuthors,
    idHForAddGenres,
    idPDescription,
  },
  book
) {
  idImg.src = getImagePath() + book.imageName;

  idBookTitle.textContent = book.title;

  idBookYear.textContent = book.year;

  idBookPublisher.textContent = book.publisher;

  idBookPrice.textContent = book.price;

  idPDescription.textContent = book.description;

  idHForAddAuthors.insertAdjacentHTML("afterend", createAuthors(book.authors));

  idHForAddGenres.insertAdjacentHTML("afterend", createGenres(book.genres));
}

function createAuthors(authors) {
  const authorElements = authors.map((author) => {
    let authorName = `${author.lastName} ${author.firstName}`;
    if (author.middleName) {
      authorName += ` ${author.middleName}`;
    }
    if (author.pseudonym) {
      authorName += ` (${author.pseudonym})`;
    }
    return `<p>${authorName}</p>`;
  });
  return authorElements.join("");
}

function createGenres(genres) {
  const genreElements = genres.map((genre) => `<p>${genre.title}</p>`);
  return genreElements.join("");
}
