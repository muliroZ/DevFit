document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("register-form");
  const messageDisplay = document.getElementById("message-display");

  const passCheck = document.getElementById("password");
  passCheck.addEventListener("input", () => {
    const pass = passCheck.value;

    updateCheck(".length", pass.length >= 8);
    updateCheck(".lower", /[a-z]/.test(pass));
    updateCheck(".upper", /[A-Z]/.test(pass));
    updateCheck(".number", /\d/.test(pass));
  });

  function updateCheck(selector, condition) {
    const el = document.querySelector(selector);
    if (!el) return;
    if (condition) {
      el.classList.add("ok");
    } else {
      el.classList.remove("ok");
    }
  }

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    messageDisplay.innerHTML = "";
    messageDisplay.classList = "message-container";

    const nome = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;

    if (senha !== confirmPassword) {
      messageDisplay.innerHTML = `<span class="error-msg">As senhas não batem</span>`;
      return;
    }

    const data = {
      nome,
      email,
      senha,
    };

    try {
      const response = await fetch("/auth/cadastro", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });

      const statusCode = response.status;

      if (!response.ok) {
        const errorText = await response.text();
        messageDisplay.innerHTML = `<span class="error-msg">Erro: ${
          errorText || "Falha no cadastro"
        } - Status: ${statusCode}</span>`;
        return;
      }

      messageDisplay.innerHTML = `<span class="success-msg">Cadastro realizado com sucesso. Redirecionando...</span>`;
      setTimeout(() => {
        window.location.href = "/login.html";
      }, 2000);
    } catch (error) {
      console.error("Erro: ", error);
      messageDisplay.innerHTML = `<span class="error-msg">Erro de conexão com o servidor</span>`;
    }
  });
});
