document.addEventListener("DOMContentLoaded", () => {
  if (!verificarPermissao(["INSTRUTOR", "GESTOR"])) {
    alert("Acesso negado.");
    window.location.href = "/index.html";
    return;
  }

  carregarAlunos();
  carregarExercicios();

  document
    .getElementById("form-item")
    .addEventListener("submit", adicionarItemNaLista);
  document
    .getElementById("btn-salvar-ficha")
    .addEventListener("click", salvarFichaNoBackend);
});

// Estado local da lista de itens
let itensTreino = [];
let mapExercicios = {}; // Para mostrar o nome do exercício na lista visual

async function carregarAlunos() {
  const select = document.getElementById("select-aluno");
  const token = localStorage.getItem("token");
  try {
    const res = await fetch("/usuarios/alunos", {
      headers: { Authorization: `Bearer ${token}` },
    });
    const alunos = await res.json();

    select.innerHTML = '<option value="">Selecione um aluno...</option>';
    alunos.forEach((a) => {
      select.innerHTML += `<option value="${a.id}">${a.nome} (${a.email})</option>`;
    });
  } catch (e) {
    select.innerHTML = "<option>Erro ao carregar</option>";
  }
}

async function carregarExercicios() {
  const select = document.getElementById("select-exercicio");
  const token = localStorage.getItem("token");
  try {
    const res = await fetch("/exercicios", {
      headers: { Authorization: `Bearer ${token}` },
    });
    const exercicios = await res.json();

    select.innerHTML = '<option value="">Selecione...</option>';
    exercicios.forEach((ex) => {
      mapExercicios[ex.id] = ex.nome;
      select.innerHTML += `<option value="${ex.id}">${ex.nome} - ${ex.musculoPrincipal}</option>`;
    });
  } catch (e) {
    select.innerHTML = "<option>Erro ao carregar</option>";
  }
}

function adicionarItemNaLista(e) {
  e.preventDefault();

  const exercicioId = document.getElementById("select-exercicio").value;
  const series = document.getElementById("series").value;
  const repeticoes = document.getElementById("repeticoes").value;
  const carga = document.getElementById("carga").value;
  const obs = document.getElementById("obs-exercicio").value;

  if (!exercicioId) {
    alert("Selecione um exercício.");
    return;
  }

  // Objeto ItemTreinoRequest
  const item = {
    exercicioId: parseInt(exercicioId),
    series: parseInt(series),
    repeticoes: parseInt(repeticoes),
    cargaEstimadaKg: carga ? parseFloat(carga) : null,
    observacoes: obs,
  };

  itensTreino.push(item);
  renderizarLista();

  document.getElementById("carga").value = "";
  document.getElementById("obs-exercicio").value = "";
}

function renderizarLista() {
  const container = document.getElementById("lista-itens-visual");

  if (itensTreino.length === 0) {
    container.innerHTML =
      '<p style="color:#888; text-align:center; margin-top:2rem;">Nenhum exercício adicionado.</p>';
    return;
  }

  container.innerHTML = "";
  itensTreino.forEach((item, index) => {
    const nomeExercicio =
      mapExercicios[item.exercicioId] || "Exercício ID " + item.exercicioId;

    const div = document.createElement("div");
    div.className = "item-card";
    div.innerHTML = `
            <div>
                <strong style="color:#fff">${nomeExercicio}</strong>
                <div style="font-size:0.9rem; color:#ccc;">
                    ${item.series} x ${item.repeticoes} 
                    ${item.cargaEstimadaKg ? `| ${item.cargaEstimadaKg}kg` : ""}
                </div>
                ${
                  item.observacoes
                    ? `<small style="color:#888">${item.observacoes}</small>`
                    : ""
                }
            </div>
            <button class="btn-remove" onclick="removerItem(${index})">X</button>
        `;
    container.appendChild(div);
  });
}

// Função global para o onclick funcionar
window.removerItem = function (index) {
  itensTreino.splice(index, 1);
  renderizarLista();
};

async function salvarFichaNoBackend() {
  const alunoId = document.getElementById("select-aluno").value;
  const dataVencimento = document.getElementById("data-vencimento").value;
  const msg = document.getElementById("msg-final");
  const token = localStorage.getItem("token");

  if (!alunoId || !dataVencimento) {
    alert("Preencha o Aluno e a Data de Validade.");
    return;
  }
  if (itensTreino.length === 0) {
    alert("Adicione pelo menos um exercício à ficha.");
    return;
  }

  const usuarioLogado = parseJwt(token);
  const instrutorId = usuarioLogado.id;

  const fichaDTO = {
    idAluno: parseInt(alunoId),
    idInstrutor: instrutorId,
    dataVencimento: dataVencimento,
    listaDeItens: itensTreino,
  };

  msg.innerHTML = '<span style="color:#fff">Salvando...</span>';

  try {
    const res = await fetch("/fichas/treino", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(fichaDTO),
    });

    if (res.ok) {
      msg.innerHTML =
        '<span class="success-msg">Ficha criada com sucesso!</span>';
      setTimeout(() => {
        window.location.href = "/treinos.html";
      }, 1500);
    } else {
      const erro = await res.text();
      msg.innerHTML = `<span class="error-msg">Erro: ${erro}</span>`;
    }
  } catch (e) {
    msg.innerHTML = `<span class="error-msg">Erro de conexão.</span>`;
  }
}
