document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("login-form");
  const messageDisplay = document.getElementById("message-display");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    messageDisplay.innerHTML = "";
    messageDisplay.classList = "message-container";

    const email = document.getElementById("email").value;
    const senha = document.getElementById("password").value;

    const data = {
      email: email,
      senha: senha,
    };

    try {
      const response = await fetch("/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });

      const statusCode = response.status;

      if (!response.ok) {
        const errorText = await response.text();
        messageDisplay.innerHTML = `<span class="error-msg">Erro: ${
          errorText || "Credenciais Inválidas"
        } - Status: ${statusCode}</span>`;
        return;
      }

      const result = await response.json();
      localStorage.setItem("token", result.token);
      messageDisplay.innerHTML = `<span class=success-msg>Login realizado com sucesso. Redirecionando...</span>`;

      setTimeout(() => {
        window.location.href = "/index.html";
      }, 2000);
    } catch (error) {
      console.error("Erro: ", error);
    }
  });
});
