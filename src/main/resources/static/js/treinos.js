document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("token");
  if (!token) {
    window.location.href = "/login.html";
    return;
  }

  configurarInterfacePorRole();
  carregarTreinos();
});

function configurarInterfacePorRole() {
  const isProfissional = verificarPermissao(["INSTRUTOR", "GESTOR"]);
  const btnNovo = document.getElementById("btn-novo-treino");
  const titulo = document.querySelector(".dashboard-hero h2");
  const subTitulo = document.querySelector(".dashboard-hero p");

  if (isProfissional) {
    if (btnNovo) {
      btnNovo.style.display = "block";
      btnNovo.onclick = () => (window.location.href = "/criar-treino.html");
    }
    titulo.innerText = "Gestão de Fichas";
    subTitulo.innerText = "Visualize e gerencie os treinos dos alunos";
  } else {
    if (btnNovo) btnNovo.style.display = "none";
    titulo.innerText = "Meus Treinos";
    subTitulo.innerText = "Acompanhe sua evolução";
  }
}

async function carregarTreinos() {
  const container = document.getElementById("treinos-container");
  const token = localStorage.getItem("token");

  const url = verificarPermissao(["INSTRUTOR", "GESTOR"])
    ? "/fichas/treino"
    : "/fichas/treino/minhas-fichas";

  try {
    const response = await fetch(url, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) throw new Error("Falha na requisição");

    const treinos = await response.json();
    container.innerHTML = "";

    if (treinos.length === 0) {
      container.innerHTML = `<p class="loading-msg">Nenhuma ficha encontrada.</p>`;
      return;
    }

    renderizarCards(treinos, container);
  } catch (error) {
    container.innerHTML = `<p class="error-msg">Erro ao carregar treinos.</p>`;
  }
}

function renderizarCards(lista, container) {
  const isAluno = verificarPermissao("ALUNO");

  lista.forEach((t) => {
    const card = document.createElement("div");
    card.className = "data-card";

    const dataVenc = new Date(t.dataVencimento).toLocaleDateString("pt-BR");
    const statusClass = t.isAtiva ? "ativa" : "inativa";

    // Se for aluno, mostra quem montou. Se for prof, mostra de quem é o treino.
    const infoPessoa = isAluno
      ? `Instrutor: ${t.instrutor.nome}`
      : `<strong>Aluno: ${t.aluno.nome}</strong>`;

    card.innerHTML = `
            <div class="card-header">
                <span class="card-date">Vence: ${dataVenc}</span>
                <span class="status-badge ${statusClass}">${
      t.isAtiva ? "ATIVA" : "EXPIRADA"
    }</span>
            </div>
            <div class="card-body">
                <p>${infoPessoa}</p>
                <p>Exercícios: ${t.ListaDeItens ? t.ListaDeItens.length : 0}</p>
            </div>
            <button class="btn-details" onclick="verDetalhesTreino(${
              t.id
            })">Ver Exercícios</button>
        `;
    container.appendChild(card);
  });
}

async function verDetalhesTreino(id) {
  const token = localStorage.getItem("token");
  const modal = document.getElementById("modal-detalhes");
  const modalBody = document.getElementById("modal-body");
  const tituloModal = document.getElementById("modal-titulo");

  modal.style.display = "flex";
  modalBody.innerHTML = "<p>Carregando...</p>";
  tituloModal.innerText = "Carregando...";

  try {
    const res = await fetch(`/fichas/treino/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    const treino = await res.json();

    tituloModal.innerText = `Treino de ${treino.aluno.nome}`;

    let html = '<ul class="info-list">';
    treino.ListaDeItens.forEach((item) => {
      html += `
                <li class="info-item">
                    <h4 style="color:#fff">${item.exercicio.nome}</h4>
                    <p style="color:#ccc; font-size:0.9rem">
                        ${item.series} séries x ${item.repeticoes} reps 
                        ${
                          item.cargaEstimadaKg
                            ? `(${item.cargaEstimadaKg}kg)`
                            : ""
                        }
                    </p>
                    ${
                      item.observacoes
                        ? `<small style="color:#888">${item.observacoes}</small>`
                        : ""
                    }
                </li>`;
    });
    html += "</ul>";
    modalBody.innerHTML = html;
  } catch (e) {
    modalBody.innerHTML = "Erro ao carregar detalhes.";
  }
}

function fecharModal() {
  document.getElementById("modal-detalhes").style.display = "none";
}
window.onclick = function (e) {
  if (e.target == document.getElementById("modal-detalhes")) fecharModal();
};
