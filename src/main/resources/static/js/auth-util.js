/**
 * Decodifica o payload de um JWT sem verificar a assinatura.
 */
function parseJwt(token) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      window
        .atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    return null;
  }
}

/**
 * Verifica se o usuário possui a role necessária.
 */
function verificarPermissao(rolesNecessarias) {
  const token = localStorage.getItem("token");
  if (!token) return false;

  const dados = parseJwt(token);
  if (dados && dados.exp * 1000 < Date.now()) {
    logout();
    return false;
  }

  if (!dados || !dados.role) return false;

  const listaPermissoes = Array.isArray(rolesNecessarias)
    ? rolesNecessarias
    : [rolesNecessarias];
  return listaPermissoes.some(
    (role) => dados.role === role || dados.role === `ROLE_${role}`
  );
}

/**
 * Função Principal executada ao carregar a página.
 * Gerencia o Header (Links) e a Autenticação.
 */
function atualizarInterfaceAuth() {
  const token = localStorage.getItem("token");
  const authContainer = document.querySelector(".auth-container");
  const navLinksContainer = document.querySelector(".nav-links");
  const logo = document.querySelector(".logo");

  if (!authContainer || !navLinksContainer) return;

  // 1. Definição dos menus por Perfil
  const menus = {
    PUBLICO: [
      { texto: "Início", link: "index.html" },
      { texto: "Planos", link: "planos.html" },
      { texto: "Loja", link: "loja.html" },
    ],
    ALUNO: [
      { texto: "Início", link: "index.html" },
      { texto: "Meus Treinos", link: "treinos.html" },
      { texto: "Minhas Avaliações", link: "avaliacoes.html" },
      { texto: "Loja", link: "loja.html" },
    ],
    INSTRUTOR: [
      { texto: "Início", link: "index.html" },
      { texto: "Gestão de Treinos", link: "treinos.html" },
      { texto: "Gestão de Avaliações", link: "avaliacoes.html" },
      { texto: "Loja", link: "loja.html" },
    ],
    GESTOR: [
      { texto: "Dashboard", link: "dashboard-gestor.html" },
      { texto: "Treinos", link: "treinos.html" },
      { texto: "Avaliações", link: "avaliacoes.html" },
      { texto: "Loja", link: "loja.html" },
    ],
  };

  let perfilAtual = "PUBLICO";
  let dadosUsuario = null;

  if (token) {
    dadosUsuario = parseJwt(token);

    // Verifica expiração
    if (dadosUsuario.exp * 1000 < Date.now()) {
      logout();
      return;
    }

    const role = dadosUsuario.role.replace("ROLE_", "");
    perfilAtual = role; // ALUNO, INSTRUTOR ou GESTOR

    // Adiciona Badge na Logo
    if (logo && !logo.querySelector(".user-badge")) {
      const badge = document.createElement("span");
      badge.className = `user-badge ${role.toLowerCase()}`;
      badge.innerText = role;
      logo.appendChild(badge);
    }
  }

  // 2. Renderiza os Links de Navegação
  navLinksContainer.innerHTML = "";
  const linksParaRenderizar = menus[perfilAtual] || menus.PUBLICO;

  linksParaRenderizar.forEach((item) => {
    const a = document.createElement("a");
    a.href = item.link;
    a.innerText = item.texto;

    // Marca como ativo se a URL atual contiver o link
    if (window.location.pathname.includes(item.link)) {
      a.classList.add("active");
    }
    navLinksContainer.appendChild(a);
  });

  // 3. Renderiza Área de Login/Logout/Admin
  authContainer.innerHTML = "";

  if (token && dadosUsuario) {
    // Se for GESTOR, adiciona botão da Sidebar
    if (perfilAtual === "GESTOR") {
      const btnAdmin = document.createElement("button");
      btnAdmin.className = "btn-admin-toggle";
      btnAdmin.innerHTML = "⚙️ Menu Gestor";
      btnAdmin.onclick = toggleAdminSidebar;
      authContainer.appendChild(btnAdmin);

      injetarSidebarAdmin(); // Cria o HTML da sidebar se não existir
    }

    const greeting = document.createElement("span");
    greeting.className = "user-greeting";
    greeting.innerHTML = `Olá, ${dadosUsuario.sub.split("@")[0]}`; // Pega só a parte antes do @

    const btnLogout = document.createElement("button");
    btnLogout.className = "logout-button";
    btnLogout.innerText = "Sair";
    btnLogout.onclick = logout;

    authContainer.appendChild(greeting);
    authContainer.appendChild(btnLogout);
  } else {
    authContainer.innerHTML = `
            <a href="login.html" class="login-button">Entrar</a>
            <a href="cadastro.html" class="signup-button">Criar Conta</a>
        `;
  }
}

/**
 * Injeta a Sidebar do Administrador no DOM
 */
function injetarSidebarAdmin() {
  if (document.getElementById("admin-sidebar")) return;

  const sidebarHTML = `
        <div id="sidebar-overlay" class="sidebar-overlay" onclick="toggleAdminSidebar()"></div>
        <aside id="admin-sidebar" class="admin-sidebar">
            <div class="sidebar-header">
                <h3>Painel Gestor</h3>
                <button class="btn-close-sidebar" onclick="toggleAdminSidebar()">×</button>
            </div>
            <nav class="sidebar-nav">
                <a href="/dashboard-gestor.html">📊 Visão Geral</a>
                <a href="/financeiro.html">💰 Relatório Financeiro</a>
                <a href="/estoque.html">📦 Gestão de Estoque</a>
                <a href="/equipamentos.html">🛠️ Equipamentos</a>
                <a href="/exercicios.html">💪 Banco de Exercícios</a> 
                <hr style="border-color:#333; width:100%; margin: 10px 0;">
                <a href="/cadastro-instrutor.html">💪 Novo Instrutor</a>
                <a href="/cadastro-adm.html">👔 Novo Administrador</a>
            </nav>
        </aside>
    `;
  document.body.insertAdjacentHTML("beforeend", sidebarHTML);
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

// Inicializa assim que o DOM estiver pronto
document.addEventListener("DOMContentLoaded", atualizarInterfaceAuth);
