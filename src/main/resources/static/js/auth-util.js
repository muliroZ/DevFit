/**
 * Decodifica o payload de um JWT sem verificar a assinatura (apenas leitura).
 * @param {string} token - O token JWT completo.
 * @returns {object|null} - O objeto JSON com os dados (claims) ou null se inválido.
 */
function parseJwt(token) {
  try {
    // O JWT é dividido em 3 partes por pontos: Header.Payload.Signature
    const base64Url = token.split(".")[1];

    // Converte Base64Url para Base64 padrão
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");

    // Decodifica a string Base64 para texto
    const jsonPayload = decodeURIComponent(
      window
        .atob(base64)
        .split("")
        .map(function (c) {
          return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join("")
    );

    return JSON.parse(jsonPayload);
  } catch (e) {
    console.error("Erro ao decodificar token", e);
    return null;
  }
}

/**
 * Verifica se o usuário tem a permissão necessária.
 * @param {string} roleNecessaria - Ex: 'GESTOR', 'ADMIN', 'INSTRUTOR'
 * @returns {boolean} - True se permitido, False se negado.
 */
function verificarPermissao(roleNecessaria) {
  const token = localStorage.getItem("token");
  if (!token) return false;

  const dados = parseJwt(token);

  if (dados && dados.exp * 1000 < Date.now()) {
    localStorage.removeItem("token");
    return false;
  }

  return (
    dados &&
    (dados.role === roleNecessaria || dados.role === `ROLE_${roleNecessaria}`)
  );
}

function atualizarInterfaceAuth() {
  const token = localStorage.getItem("token");
  const authContainer = document.querySelector(".auth-container");
  const logo = document.querySelector(".logo");

  if (!authContainer || !logo) return;

  if (token) {
    const dados = parseJwt(token);

    if (dados.exp * 1000 < Date.now()) {
      logout();
      return;
    }

    if (!logo.querySelector(".user-badge")) {
      const role = dados.role.replace("ROLE_", "");
      const badge = document.createElement("span");
      badge.className = `user-badge ${role.toLowerCase()}`;
      badge.innerText = role;
      logo.appendChild(badge);
    }

    const emailUsuario = dados.sub || "Usuário";

    authContainer.innerHTML = "";

    if (verificarPermissao("GESTOR")) {
      const btnAdmin = document.createElement("button")
      btnAdmin.className = "btn-admin-toggle"
      btnAdmin.innerHTML = "Admin"
      btnAdmin.onclick = 
      authContainer.appendChild(btnAdmin)

      injetarSidebarAdmin()
    }

    const greeting = document.createElement("span");
    greeting.className = "user-greeting";
    greeting.innerHTML = `Bem vindo(a), <br>${emailUsuario}`;
    
    const btnLogout = document.createElement("button");
    btnLogout.id = "logout-btn";
    btnLogout.className = "logout-button";
    btnLogout.innerText = "Sair";
    btnLogout.addEventListener("click", (e) => {
      e.preventDefault();
      logout();
    });

    authContainer.appendChild(greeting);
    authContainer.appendChild(btnLogout);

  } else {
    authContainer.innerHTML = `
        <a href="login.html" class="login-button">Login</a>
        <a href="cadastro.html" class="signup-button">Cadastre-se</a>
    `;
  }
}

function injetarSidebarAdmin() {
    // Evita duplicidade se já foi injetado
    if (document.getElementById("admin-sidebar")) return;

    const sidebarHTML = `
        <div id="sidebar-overlay" class="sidebar-overlay" onclick="toggleAdminSidebar()"></div>
        <aside id="admin-sidebar" class="admin-sidebar">
            <div class="sidebar-header">
                <h3>Menu Gestor</h3>
                <button class="btn-close-sidebar" onclick="toggleAdminSidebar()">×</button>
            </div>
            <nav class="sidebar-nav">
                <a href="/dashboard-gestor.html">📊 Dashboard Geral</a>
                <a href="/cadastro-instrutor.html">💪 Cadastrar Instrutor</a>
                <a href="/cadastro-adm.html">👔 Novo Admin</a>
                <a href="#" onclick="alert('Ir para Gestão de Produtos')">📦 Estoque/Loja</a>
                <a href="#" onclick="alert('Ir para Relatórios Financeiros')">💰 Financeiro</a>
                <hr style="border-color:#333; width:100%">
                <a href="/treinos.html">🏋️ Ver Todos Treinos</a>
                <a href="/avaliacoes.html">medical_services Ver Avaliações</a>
            </nav>
        </aside>
    `;

    document.body.insertAdjacentHTML('beforeend', sidebarHTML);
}

function toggleAdminSidebar() {
    const sidebar = document.getElementById("admin-sidebar");
    const overlay = document.getElementById("sidebar-overlay");
    
    if (sidebar && overlay) {
        sidebar.classList.toggle("open");
        overlay.classList.toggle("active");
    }
}

function logout() {
  localStorage.removeItem("token");
  window.location.href = "/index.html";
}

document.addEventListener("DOMContentLoaded", atualizarInterfaceAuth);
