import { api } from "./api.js";
import * as helper from "./helper.js";
import { setAlert } from "../../_js/helpers.js";

document.addEventListener("DOMContentLoaded", () => {
  helper.clearSessionStorage();
  // отслеживание клика
  const btn_auth = document.getElementById("id-btn-auth");
  const input_login = document.getElementById("id-login");
  const divAlert = document.getElementById("div-alert");
  const alert = document.getElementById("alert");

  btn_auth.addEventListener("click", (ev) => {
    // предотвращение отправки формы
    ev.preventDefault();

    var customerlogin = input_login.value;

    if (!helper.validateOnEmpty(customerlogin)) {
      console.error("Логин пустой!");
      setAlert(divAlert, alert, "Введите логин");
      return;
    }

    const apiResult = api.getAuthCustomer(customerlogin);

    apiResult
      .then((result) => {
        if (!result) {
          console.error("Покупатель не найден");
          setAlert(divAlert, alert, "Покупатель не найден");
          return;
        }

        if (helper.setSessionStorage(result)) {
          window.location.href = "catalog/index.html";
        }
      })
      .catch((err) => {
        console.error("getAuthCustomer failed", err);
        setAlert(divAlert, alert, "Покупатель не найден");
      });
  });
});
