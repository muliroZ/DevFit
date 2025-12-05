document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("token");
  if (!token) {
    window.location.href = "/login.html";
    return;
  }

  configurarInterface();
  carregarAvaliacoes();
});

function configurarInterface() {
  const isProfissional = verificarPermissao(["INSTRUTOR", "GESTOR"]);
  const btnNovo = document.getElementById("btn-nova-avaliacao");

  if (isProfissional && btnNovo) {
    btnNovo.style.display = "block";
    btnNovo.onclick = () => (window.location.href = "/criar-avaliacao.html");
  } else if (btnNovo) {
    btnNovo.style.display = "none";
  }
}

async function carregarAvaliacoes() {
  const container = document.getElementById("avaliacoes-container");
  const token = localStorage.getItem("token");

  const url = verificarPermissao(["INSTRUTOR", "GESTOR"])
    ? "/fichas/avaliacao"
    : "/fichas/avaliacao/minhas-avaliacoes";

  try {
    const response = await fetch(url, {
      headers: { Authorization: `Bearer ${token}` },
    });

    const lista = await response.json();
    container.innerHTML = "";

    if (!lista || lista.length === 0) {
      container.innerHTML = `<p class="loading-msg">Nenhuma avaliação registrada.</p>`;
      return;
    }

    renderizarAvaliacoes(lista, container);
  } catch (error) {
    container.innerHTML = `<p class="error-msg">Erro ao carregar dados.</p>`;
  }
}

function renderizarAvaliacoes(lista, container) {
  const isAluno = verificarPermissao("ALUNO");

  lista.forEach((av) => {
    const card = document.createElement("div");
    card.className = "data-card";
    const dataAv = new Date(av.dataAvaliacao).toLocaleDateString("pt-BR");
    const imc = av.imc ? av.imc.toFixed(2) : "--";

    const linhaNome = isAluno
      ? `<p><strong>Instrutor:</strong> ${av.instrutor.nome}</p>`
      : `<p><strong>Aluno:</strong> ${av.aluno.nome}</p>`;

    card.innerHTML = `
            <div class="card-header">
                <span class="card-date">${dataAv}</span>
                <span class="status-badge" style="background:#00ff88; color:#000;">IMC: ${imc}</span>
            </div>
            <div class="card-body">
                ${linhaNome}
                <p>Peso: ${av.pesoKg}kg | Altura: ${av.alturaM}m</p>
            </div>
            <button class="btn-details" onclick='abrirDetalhes(${JSON.stringify(
              av
            )})'>Ver Medidas</button>
        `;
    container.appendChild(card);
  });
}

function abrirDetalhes(av) {
  const modal = document.getElementById("modal-avaliacao");
  const body = document.getElementById("modal-body-avaliacao");
  modal.style.display = "flex";

  body.innerHTML = `
        <ul class="info-list">
            <li class="info-item">
                <strong>Medidas Corporais:</strong><br>
                Cintura: ${av.circunferenciaCinturaCm || "--"} cm<br>
                Abdômen: ${av.circunferenciaAbdomenCm || "--"} cm<br>
                Quadril: ${av.circunferenciaQuadrilCm || "--"} cm
            </li>
            <li class="info-item">
                <strong>Histórico de Saúde:</strong><br>
                <span style="color:#ccc;">${
                  av.historicoSaude || "Nenhum registro."
                }</span>
            </li>
            <li class="info-item">
                <strong>Observações:</strong><br>
                <span style="color:#ccc;">${
                  av.observacoesGerais || "Nenhuma observação."
                }</span>
            </li>
        </ul>
    `;
}

function fecharModal() {
  document.getElementById("modal-avaliacao").style.display = "none";
}
window.onclick = (e) => {
  if (e.target == document.getElementById("modal-avaliacao")) fecharModal();
};
