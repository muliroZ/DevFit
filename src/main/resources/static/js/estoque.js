document.addEventListener("DOMContentLoaded", () => {
  if (!verificarPermissao("GESTOR")) {
    alert("Acesso negado.");
    window.location.href = "/index.html";
    return;
  }
  listarProdutos();
  document
    .getElementById("produto-form")
    .addEventListener("submit", salvarProduto);
});

const formatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

async function listarProdutos() {
  const tbody = document.getElementById("lista-produtos");
  tbody.innerHTML = "<tr><td colspan='5'>Carregando...</td></tr>";

  try {
    const res = await fetch("/produtos");
    if (!res.ok) throw new Error();

    const produtos = await res.json();
    tbody.innerHTML = "";

    if (produtos.length === 0) {
      tbody.innerHTML =
        "<tr><td colspan='5'>Nenhum produto cadastrado.</td></tr>";
      return;
    }

    produtos.forEach((p) => {
      const tr = document.createElement("tr");

      const imgUrl = p.imagemUrl || "https://placehold.co/50x50?text=Sem+Img";

      tr.innerHTML = `
                <td><img src="${imgUrl}" class="thumb-img" alt="img"></td>
                <td>${p.nome}</td>
                <td style="color: ${p.estoque < 5 ? "#ff4444" : "#fff"}">${
        p.estoque
      } un.</td>
                <td>${formatter.format(p.preco)}</td>
                <td class="actions-cell">
                    <button class="btn-edit" onclick='prepararEdicao(${JSON.stringify(
                      p
                    )})'>Editar</button>
                    <button class="btn-delete" onclick="deletarProduto(${
                      p.id
                    })">Excluir</button>
                </td>
            `;
      tbody.appendChild(tr);
    });
  } catch (e) {
    tbody.innerHTML =
      "<tr><td colspan='5' style='color:red'>Erro ao carregar produtos.</td></tr>";
  }
}

async function salvarProduto(e) {
  e.preventDefault();

  const id = document.getElementById("produto-id").value;
  const isEdit = !!id;
  const token = localStorage.getItem("token");
  const msg = document.getElementById("msg");

  const data = {
    nome: document.getElementById("nome").value,
    descricao: document.getElementById("descricao").value,
    preco: parseFloat(document.getElementById("preco").value),
    estoque: parseInt(document.getElementById("estoque").value),
    imagemUrl: document.getElementById("imagemUrl").value,
  };

  const url = isEdit ? `/produtos/atualizar/${id}` : `/produtos/adicionar`;
  const method = isEdit ? "PUT" : "POST";

  try {
    const res = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(data),
    });

    if (res.ok) {
      msg.innerHTML = `<span class="success-msg">Produto ${
        isEdit ? "atualizado" : "criado"
      } com sucesso!</span>`;
      listarProdutos();
      cancelarEdicao(); // Limpa o form
    } else {
      const errorTxt = await res.text();
      msg.innerHTML = `<span class="error-msg">Erro: ${errorTxt}</span>`;
    }
  } catch (error) {
    msg.innerHTML = `<span class="error-msg">Erro de conexão.</span>`;
  }
}

function prepararEdicao(produto) {
  document.getElementById("produto-id").value = produto.id;
  document.getElementById("nome").value = produto.nome;
  document.getElementById("preco").value = produto.preco;
  document.getElementById("estoque").value = produto.estoque;
  document.getElementById("imagemUrl").value = produto.imagemUrl || "";
  document.getElementById("descricao").value = produto.descricao || "";

  document.getElementById("form-title").innerText = "Editar Produto";
  document.getElementById("btn-salvar").innerText = "Atualizar Produto";
  document.getElementById("btn-cancelar").style.display = "inline-block";

  // Scroll suave até o form
  document.querySelector(".form-hero").scrollIntoView({ behavior: "smooth" });
}

function cancelarEdicao() {
  document.getElementById("produto-form").reset();
  document.getElementById("produto-id").value = "";

  document.getElementById("form-title").innerText = "Adicionar Produto";
  document.getElementById("btn-salvar").innerText = "Salvar Produto";
  document.getElementById("btn-cancelar").style.display = "none";
  document.getElementById("msg").innerHTML = "";
}

async function deletarProduto(id) {
  if (!confirm("Tem certeza que deseja excluir este produto?")) return;

  const token = localStorage.getItem("token");
  try {
    const res = await fetch(`/produtos/excluir/${id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    });

    if (res.ok || res.status === 204) {
      listarProdutos();
    } else {
      alert("Erro ao excluir produto.");
    }
  } catch (e) {
    alert("Erro de conexão.");
  }
}
