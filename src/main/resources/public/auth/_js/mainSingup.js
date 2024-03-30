document.addEventListener("DOMContentLoaded", () => {
  clearSessionStorage();
  // отслеживание клика
  const btn_singup = document.getElementById("id-btn-singup");
  const divAlert = document.getElementById("div-alert");
  const alert = document.getElementById("alert");

  btn_singup.addEventListener("click", (ev) => {
    // предотвращение отправки формы
    ev.preventDefault();

    setAlert(divAlert, alert, "Регистрация не сделана");
  });
});
