document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("admin-form");
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

    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("password").value;
    const admCode = document.getElementById("access-code").value;

    const data = {
      nome: nome,
      email: email,
      senha: senha,
      gestorCode: admCode,
    };

    try {
      const response = await fetch("/auth/cadastro/gestor", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });

      if (!response.ok) {
        const errorText = await response.text();
        messageDisplay.innerHTML = `<span class="error-msg">Erro: ${
          errorText || "Falha no cadastro"
        }`;
        return;
      }

      messageDisplay.innerHTML = `<span class="success-msg">Gestor cadastrado com sucesso! Redirecionando...</span>`;
      setTimeout(() => {
        window.location.href = "/login.html";
      }, 2000);
    } catch (error) {
      console.error("Erro na requisição: ", error);
      messageDisplay.innerHTML = `<span class="error-msg">Erro de conexão com o servidor.</span>`;
    }
  });
});
