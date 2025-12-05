document.addEventListener("DOMContentLoaded", () => {
  if (!verificarPermissao(["INSTRUTOR", "GESTOR"])) {
    alert("Acesso restrito a profissionais.");
    window.location.href = "/index.html";
    return;
  }

  carregarAlunos();
  document
    .getElementById("form-avaliacao")
    .addEventListener("submit", salvarAvaliacao);
});

async function carregarAlunos() {
  const select = document.getElementById("select-aluno");
  const token = localStorage.getItem("token");

  try {
    const res = await fetch("/usuarios/alunos", {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (res.ok) {
      const alunos = await res.json();
      select.innerHTML = '<option value="">Selecione um aluno...</option>';
      alunos.forEach((a) => {
        select.innerHTML += `<option value="${a.id}">${a.nome} (${a.email})</option>`;
      });
    } else {
      select.innerHTML = "<option>Erro ao carregar</option>";
    }
  } catch (e) {
    console.error(e);
    select.innerHTML = "<option>Erro de conexão</option>";
  }
}

async function salvarAvaliacao(e) {
  e.preventDefault();
  const token = localStorage.getItem("token");
  const msg = document.getElementById("msg");

  const alunoId = document.getElementById("select-aluno").value;
  const peso = document.getElementById("peso").value;
  const altura = document.getElementById("altura").value;

  if (!alunoId || !peso || !altura) {
    msg.innerHTML =
      '<span class="error-msg">Preencha os campos obrigatórios (Aluno, Peso, Altura).</span>';
    return;
  }

  const usuarioLogado = parseJwt(token);
  const instrutorId = usuarioLogado ? usuarioLogado.id : null;

  if (!instrutorId) {
    alert("Erro de sessão. Faça login novamente.");
    window.location.href = "/login.html";
    return;
  }

  // Monta o objeto conforme FichaAvaliacaoRequest.java
  const avaliacaoDTO = {
    idAluno: parseInt(alunoId),
    idInstrutor: instrutorId,
    pesoKg: parseFloat(peso),
    alturaCm: parseFloat(altura),
    circunferenciaCinturaCm:
      parseFloat(document.getElementById("cintura").value) || null,
    circunferenciaAbdomenCm:
      parseFloat(document.getElementById("abdomen").value) || null,
    circunferenciaQuadrilCm:
      parseFloat(document.getElementById("quadril").value) || null,
    historicoSaude: document.getElementById("historico").value,
    observacoesGerais: document.getElementById("observacoes").value,
  };

  msg.innerHTML = '<span style="color:#ccc">Salvando...</span>';

  try {
    const res = await fetch("/fichas/avaliacao/criar", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(avaliacaoDTO),
    });

    if (res.ok) {
      msg.innerHTML =
        '<span class="success-msg">Avaliação registrada com sucesso!</span>';
      setTimeout(() => {
        window.location.href = "/avaliacoes.html";
      }, 1500);
    } else {
      const erroTxt = await res.text();
      try {
        const erroJson = JSON.parse(erroTxt);
        msg.innerHTML = `<span class="error-msg">${
          erroJson.message || erroTxt
        }</span>`;
      } catch {
        msg.innerHTML = `<span class="error-msg">Erro: ${erroTxt}</span>`;
      }
    }
  } catch (e) {
    msg.innerHTML = `<span class="error-msg">Erro de conexão ao salvar.</span>`;
  }
}
