export const api = {
  async getAuthCustomer(login) {
    try {
      const response = await fetch("/login", {
        method: "POST",
        body: login,
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        if (response.status === 404) {
          return "";
        } else {
          throw new Error("Ошибка сервера");
        }
      }

      const data = await response.json();
      return data; // Возвращаем данные о пользователе
    } catch (error) {
      throw new Error(error.message);
    }
  },
};
