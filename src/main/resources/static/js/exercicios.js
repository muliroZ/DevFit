document.addEventListener("DOMContentLoaded", () => {
  if (!verificarPermissao(["INSTRUTOR", "GESTOR"])) {
    alert("Acesso restrito.");
    window.location.href = "/index.html";
    return;
  }
  listarExercicios();
  document
    .getElementById("form-exercicio")
    .addEventListener("submit", salvarExercicio);
});

async function listarExercicios() {
  const tbody = document.getElementById("lista-exercicios");
  const token = localStorage.getItem("token");
  tbody.innerHTML = "<tr><td colspan='3'>Carregando...</td></tr>";

  try {
    const res = await fetch("/exercicios", {
      headers: { Authorization: `Bearer ${token}` },
    });
    const lista = await res.json();

    tbody.innerHTML = "";
    if (lista.length === 0) {
      tbody.innerHTML =
        "<tr><td colspan='3'>Nenhum exercício cadastrado.</td></tr>";
      return;
    }

    lista.forEach((ex) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
                <td>${ex.nome}</td>
                <td>${ex.musculoPrincipal}</td>
                <td>
                    <button class="btn-delete" onclick="deletarExercicio(${ex.id})">Excluir</button>
                </td>
            `;
      tbody.appendChild(tr);
    });
  } catch (e) {
    tbody.innerHTML = "<tr><td colspan='3'>Erro ao carregar.</td></tr>";
  }
}

async function salvarExercicio(e) {
  e.preventDefault();
  const token = localStorage.getItem("token");
  const msg = document.getElementById("msg");

  const data = {
    nome: document.getElementById("nome").value,
    musculoPrincipal: document.getElementById("musculo").value,
    descricao: document.getElementById("descricao").value,
  };

  try {
    const res = await fetch("/exercicios", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(data),
    });

    if (res.ok) {
      msg.innerHTML = "<span class='success-msg'>Exercício salvo!</span>";
      document.getElementById("form-exercicio").reset();
      listarExercicios();
    } else {
      msg.innerHTML = "<span class='error-msg'>Erro ao salvar.</span>";
    }
  } catch (e) {
    msg.innerHTML = "<span class='error-msg'>Erro de conexão.</span>";
  }
}

async function deletarExercicio(id) {
  if (!confirm("Excluir este exercício?")) return;
  const token = localStorage.getItem("token");

  try {
    const res = await fetch(`/exercicios/${id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    });
    if (res.ok) listarExercicios();
    else alert("Não foi possível excluir (pode estar em uso numa ficha).");
  } catch (e) {
    alert("Erro de conexão.");
  }
}
