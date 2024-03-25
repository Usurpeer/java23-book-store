export function getTotalAmount(books) {
  return books.reduce((sum, b) => {
    return sum + b.price * b.quantity;
  }, 0);
}
