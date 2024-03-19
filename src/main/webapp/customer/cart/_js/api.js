//export
const getCartInStorage = () => {
  // убрать, когда будет создание по клику в корзину
  mockCartToJson();

  const cart = JSON.parse(sessionStorage.getItem("cart"));

  const cartTotalAmount = cart.reduce(
    (sum, book) => sum + book.quantity * book.price,
    0
  );

  cart.totalAmount = cartTotalAmount;
  return cart;
};
