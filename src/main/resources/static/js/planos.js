document.addEventListener("DOMContentLoaded", () => {
  carregarPlanos();
});

const formatadorMoeda = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

async function carregarPlanos() {
  const container = document.getElementById("planos-grid");
  const loading = document.getElementById("loading");
  const token = localStorage.getItem("token");
  const usuarioLogado = token ? parseJwt(token) : null;

  try {
    const response = await fetch("/planos/listar");
    if (!response.ok) throw new Error("Erro ao carregar planos");

    const planos = await response.json();
    loading.style.display = "none";
    container.innerHTML = "";

    if (planos.length === 0) {
      container.innerHTML =
        "<p style='color:#fff; text-align:center; width:100%'>Nenhum plano disponível.</p>";
      return;
    }

    planos.forEach((plano) => {
      let btnTexto = "Assinar Agora";
      let btnAcao = `abrirModalConfirmacao(${plano.id}, '${plano.nome}', ${plano.valor})`;
      let btnClass = "btn-assinar";

      if (!usuarioLogado) {
        btnTexto = "Fazer Login para Assinar";
        btnAcao = "window.location.href='/login.html'";
        btnClass = "btn-assinar secondary";
      }

      const destaque =
        plano.duracaoMeses >= 6
          ? "border: 2px solid #ffaa00; box-shadow: 0 0 15px rgba(255, 170, 0, 0.2);"
          : "";
      const seloVip =
        plano.duracaoMeses >= 12
          ? "<span style='background:#ffaa00; color:#000; padding:2px 8px; border-radius:4px; font-size:0.8rem; font-weight:bold; position:absolute; top:10px; right:10px;'>VIP</span>"
          : "";

      const card = document.createElement("div");
      card.className = "plano-card";
      if (destaque) card.style.cssText = destaque;

      card.innerHTML = `
                ${seloVip}
                <div>
                    <h3 class="plano-nome">${plano.nome}</h3>
                    <div class="plano-preco">${formatadorMoeda.format(
                      plano.valor
                    )}</div>
                    <p class="plano-duracao">Cobrado a cada ${
                      plano.duracaoMeses
                    } mês(es)</p>
                    <hr style="border-color:#333; margin: 15px 0;">
                    <ul class="plano-beneficios">
                        <li>✅ Acesso Ilimitado à Academia</li>
                        <li>✅ App DevFit Exclusivo</li>
                        ${
                          plano.duracaoMeses >= 3
                            ? "<li>✅ Avaliação Física Trimestral</li>"
                            : ""
                        }
                        ${
                          plano.duracaoMeses >= 12
                            ? "<li>✅ Acesso à Área VIP e Sauna</li>"
                            : ""
                        }
                        ${
                          plano.duracaoMeses >= 12
                            ? "<li>✅ Personal Trainer 1x/mês</li>"
                            : ""
                        }
                    </ul>
                </div>
                <button class="${btnClass}" onclick="${btnAcao}" style="margin-top:auto">
                    ${btnTexto}
                </button>
            `;
      container.appendChild(card);
    });
  } catch (error) {
    console.error(error);
    loading.innerHTML =
      "<p class='error-msg'>Não foi possível carregar os planos.</p>";
  }
}

// --- Lógica do Modal ---
let planoSelecionadoId = null;

function abrirModalConfirmacao(id, nome, valor) {
  const modal = document.getElementById("modal-confirmacao");
  const desc = document.getElementById("modal-desc");
  const btnConfirmar = document.getElementById("btn-confirmar-final");

  planoSelecionadoId = id;
  desc.innerHTML = `Deseja confirmar sua matrícula no <strong>${nome}</strong>?<br>Valor: <strong>${formatadorMoeda.format(
    valor
  )}</strong>`;

  // Configura o clique do botão de confirmação
  btnConfirmar.onclick = () => realizarMatricula(planoSelecionadoId);

  modal.style.display = "flex";
}

function fecharModal() {
  document.getElementById("modal-confirmacao").style.display = "none";
  planoSelecionadoId = null;
}

// --- Integração com Backend ---
async function realizarMatricula(planoId) {
  const token = localStorage.getItem("token");
  const btn = document.getElementById("btn-confirmar-final");

  btn.innerText = "Processando...";
  btn.disabled = true;

  try {
    const response = await fetch(`/matricula/assinar/${planoId}`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      alert("🎉 Matrícula realizada com sucesso! Bem-vindo(a) à DevFit.");
      window.location.href = "/treinos.html"; // Redireciona para área logada
    } else {
      const erro = await response.text();
      alert("Erro ao realizar matrícula: " + erro);
      fecharModal();
    }
  } catch (e) {
    alert("Erro de conexão.");
    fecharModal();
  } finally {
    btn.innerText = "Confirmar e Assinar";
    btn.disabled = false;
  }
}

// Fecha modal ao clicar fora
window.onclick = function (event) {
  const modal = document.getElementById("modal-confirmacao");
  if (event.target == modal) {
    fecharModal();
  }
};

window.abrirModalConfirmacao = abrirModalConfirmacao;
window.fecharModal = fecharModal;
