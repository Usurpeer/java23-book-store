export function mockCartToJson() {
  sessionStorage.setItem("cart", JSON.stringify(cart));
}

const cart = [
  {
    id: 1,
    quantity: 1,
  },
  {
    id: 3,
    quantity: 5,
  },
  {
    id: 4,
    quantity: 2,
  },
  {
    id: 5,
    quantity: 1,
  },
  {
    id: 47,
    quantity: 1,
  },
];
