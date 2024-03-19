function setOrderStorage({ id, login }) {
  if (!id || !login) {
    return false;
  }
  sessionStorage.setItem("id", id);
  sessionStorage.setItem("login", login);

  return true;
}

function clearSessionStorage() {
  sessionStorage.clear();
}

function validateOnEmpty(value) {
  if (!value) {
    return false;
  }
  return true;
}
