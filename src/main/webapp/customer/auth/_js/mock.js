//export
// получить пользователя по id
const mockCustomer = (login) => {
  return mockCustomers.find((c) => c.login === login);
};

const mockCustomers = [
  {
    id: 1,
    firstName: "Имя1",
    lastName: "Фамилия1",
    middleName: null,
    login: "user1",
  },
  {
    id: 2,
    firstName: "Имя2",
    lastName: "Фамилия2",
    middleName: "Отчество",
    login: "user2",
  },
  {
    id: 3,
    firstName: "Имя3",
    lastName: "Фамилия3",
    middleName: "Отчество3",
    login: "login",
  },
];
