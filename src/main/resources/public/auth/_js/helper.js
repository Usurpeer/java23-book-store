export function setSessionStorage({ id, login }) {
  if (!id || !login) {
    return false;
  }
  sessionStorage.setItem("id", id);
  sessionStorage.setItem("login", login);

  return true;
}

export function clearSessionStorage() {
  sessionStorage.clear();
}

export function validateOnEmpty(value) {
  if (!value) {
    return false;
  }
  return true;
}
