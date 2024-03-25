import { setCustomerLogin, getCustomerLogin } from "./helpers.js";

document.addEventListener("DOMContentLoaded", () => {
  const exitLoginLink = document.getElementById("exit-login");

  // проверка авторизации пользователя на сайте, выкидывает если нет логина в хранилище
  if (!setCustomerLogin(exitLoginLink, getCustomerLogin())) {
    //alert("Вы не авторизовны");
    //window.location.href = "../index.html";
  }
});
