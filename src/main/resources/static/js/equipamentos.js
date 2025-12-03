document.addEventListener("DOMContentLoaded", () => {
  if (!verificarPermissao("GESTOR")) window.location.href = "/index.html";
  listarEquipamentos();

  document
    .getElementById("equipamento-form")
    .addEventListener("submit", salvarEquipamento);
});

async function listarEquipamentos() {
  const token = localStorage.getItem("token");
  const tbody = document.getElementById("lista-equipamentos");

  try {
    const res = await fetch("/dashboard/equipamentos", {
      headers: { Authorization: `Bearer ${token}` },
    });
    const lista = await res.json();

    tbody.innerHTML = "";
    lista.forEach((eq) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
                <td>${eq.nome}</td>
                <td>${eq.quantidade}</td>
                <td>R$ ${eq.valor}</td>
                <td>${eq.status || "ATIVO"}</td>
            `;
      tbody.appendChild(tr);
    });
  } catch (e) {
    console.error(e);
  }
}

async function salvarEquipamento(e) {
  e.preventDefault();
  const token = localStorage.getItem("token");
  const msg = document.getElementById("msg");

  const data = {
    nome: document.getElementById("nome").value,
    quantidade: document.getElementById("quantidade").value,
    valor: document.getElementById("valor").value,
    dataAquisicao: document.getElementById("dataAquisicao").value,
    status: document.getElementById("status").value,
    descricao: document.getElementById("descricao").value,
  };

  try {
    const res = await fetch("/dashboard/equipamentos", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(data),
    });

    if (res.ok) {
      msg.innerHTML = "<span class='success-msg'>Cadastrado!</span>";
      listarEquipamentos();
      document.getElementById("equipamento-form").reset();
    } else {
      msg.innerHTML = "<span class='error-msg'>Erro ao salvar.</span>";
    }
  } catch (e) {
    msg.innerHTML = "<span class='error-msg'>Erro de conexão.</span>";
  }
}
