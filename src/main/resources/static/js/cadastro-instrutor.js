document.addEventListener("DOMContentLoaded", () => {
  if (!verificarPermissao("GESTOR")) {
    alert("Acesso Negado! Área Restrita a Gestores");
    window.location.href = "/index.html";
  }

  // Botão de Logout
  document.getElementById("logout-btn").addEventListener("click", (e) => {
    e.preventDefault();
    localStorage.removeItem("token");
    window.location.href = "/index.html";
  });

  // 2. Envio do Formulário
  const form = document.getElementById("instructor-form");
  const messageDisplay = document.getElementById("message-display");

  const passCheck = document.getElementById("senha");
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

    const data = {
      nome: document.getElementById("nome").value,
      email: document.getElementById("email").value,
      senha: document.getElementById("senha").value,
    };

    const token = localStorage.getItem("token");

    try {
      const response = await fetch("/auth/cadastro/instrutor", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`, // Envia token do gestor
        },
        body: JSON.stringify(data),
      });

      if (response.ok) {
        messageDisplay.innerHTML = `<span class="success-msg">Instrutor cadastrado com sucesso!</span>`;
        form.reset();
      } else {
        const errorText = await response.text();
        messageDisplay.innerHTML = `<span class="error-msg">Erro: ${errorText}</span>`;
      }
    } catch (error) {
      console.error(error);
      messageDisplay.innerHTML = `<span class="error-msg">Erro de conexão.</span>`;
    }
  });
});
