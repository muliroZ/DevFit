document.addEventListener("DOMContentLoaded", () => {
  const container = document.getElementById("products-container");
  const searchInput = document.getElementById("search-input");
  let allProducts = [];

  let carrinho = JSON.parse(localStorage.getItem("devfit_carrinho")) || [];

  async function fetchProducts() {
    try {
      const token = localStorage.getItem("token");
      const headers = { "Content-Type": "application/json" };
      if (token) headers["Authorization"] = `Bearer ${token}`;

      const response = await fetch("/produtos", {
        method: "GET",
        headers: headers,
      });

      if (!response.ok) throw new Error("Falha ao carregar produtos");

      allProducts = await response.json();
      renderProducts(allProducts);
    } catch (error) {
      console.error("Erro ao buscar produtos:", error);
      container.innerHTML = '<p class="error">Erro ao carregar a loja.</p>';
    }
  }

  function renderProducts(products) {
    container.innerHTML = "";
    if (!products || products.length === 0) {
      container.innerHTML =
        '<p class="no-results">Nenhum produto encontrado.</p>';
      return;
    }

    products.forEach((product) => {
      const card = document.createElement("div");
      card.className = "product-card";

      const precoFormatado = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
      }).format(product.preco);

      const temEstoque = product.estoque > 0;
      const btnHtml = temEstoque
        ? `<button class="buy-button" onclick="window.adicionarAoCarrinho(${product.id})">Comprar</button>`
        : `<button class="buy-button" disabled style="background:#555; cursor:not-allowed">Esgotado</button>`;

      const imagem =
        product.imagemUrl || "https://placehold.co/300x200?text=Sem+Imagem";

      card.innerHTML = `
                <div class="product-image-container">
                    <img src="${imagem}" alt="${
        product.nome
      }" class="product-image">
                </div>
                <div class="product-info">
                    <h3 class="product-name">${product.nome}</h3>
                    <p class="product-description">${
                      product.descricao || ""
                    }</p>
                    <div class="product-footer">
                        <span class="product-price">${precoFormatado}</span>
                        ${btnHtml}
                    </div>
                </div>
            `;
      container.appendChild(card);
    });
  }

  if (searchInput) {
    searchInput.addEventListener("input", (e) => {
      const termo = e.target.value.toLowerCase();
      const produtosFiltrados = allProducts.filter(
        (p) =>
          p.nome.toLowerCase().includes(termo) ||
          (p.descricao && p.descricao.toLowerCase().includes(termo))
      );
      renderProducts(produtosFiltrados);
    });
  }

  window.adicionarAoCarrinho = (id) => {
    const token = localStorage.getItem("token");
    if (!token) {
      alert("Faça login para adicionar itens ao carrinho.");
      window.location.href = "/login.html";
      return;
    }

    const produto = allProducts.find((p) => p.id === id);
    if (!produto) return;

    const itemExistente = carrinho.find((item) => item.produtoId === id);

    if (itemExistente) {
      if (itemExistente.quantidade < produto.estoque) {
        itemExistente.quantidade++;
      } else {
        alert("Estoque máximo atingido para este item!");
        return;
      }
    } else {
      carrinho.push({
        produtoId: id,
        nome: produto.nome,
        quantidade: 1,
        preco: produto.preco,
      });
    }

    salvarCarrinho();
    renderizarCarrinhoFlutuante();
    const btn = document.activeElement;
    if (btn) {
      const originalText = btn.innerText;
      btn.innerText = "Adicionado!";
      setTimeout(() => (btn.innerText = originalText), 1000);
    }
  };

  window.removerDoCarrinho = (index) => {
    carrinho.splice(index, 1);
    salvarCarrinho();
    renderizarCarrinhoFlutuante();
  };

  function salvarCarrinho() {
    localStorage.setItem("devfit_carrinho", JSON.stringify(carrinho));
  }

  function renderizarCarrinhoFlutuante() {
    let cartDiv = document.getElementById("cart-floating");

    if (carrinho.length === 0) {
      if (cartDiv) cartDiv.style.display = "none";
      return;
    }

    if (!cartDiv) {
      cartDiv = document.createElement("div");
      cartDiv.id = "cart-floating";
      cartDiv.style.cssText = `
                position: fixed; bottom: 20px; right: 20px;
                background: #1a1a1a; border: 2px solid #00ff88;
                border-radius: 10px; padding: 15px; width: 300px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.8); z-index: 9999;
                color: #fff; font-family: 'Roboto', sans-serif;
            `;
      document.body.appendChild(cartDiv);
    } else {
      cartDiv.style.display = "block";
    }

    const total = carrinho.reduce(
      (acc, item) => acc + item.preco * item.quantidade,
      0
    );
    const totalFmt = new Intl.NumberFormat("pt-BR", {
      style: "currency",
      currency: "BRL",
    }).format(total);

    let htmlItens = carrinho
      .map(
        (item, index) => `
            <div style="display:flex; justify-content:space-between; margin-bottom:5px; border-bottom:1px solid #333; padding-bottom:5px;">
                <div>
                    <div style="font-weight:bold; font-size:0.9rem;">${
                      item.nome
                    }</div>
                    <div style="font-size:0.8rem; color:#ccc;">${
                      item.quantidade
                    }x ${new Intl.NumberFormat("pt-BR", {
          style: "currency",
          currency: "BRL",
        }).format(item.preco)}</div>
                </div>
                <button onclick="window.removerDoCarrinho(${index})" style="background:none; border:none; color:#ff4444; cursor:pointer; font-weight:bold;">X</button>
            </div>
        `
      )
      .join("");

    cartDiv.innerHTML = `
            <h4 style="color:#00ff88; margin-bottom:10px; display:flex; justify-content:space-between;">
                Carrinho <span>${carrinho.length} item(s)</span>
            </h4>
            <div style="max-height: 200px; overflow-y: auto; margin-bottom: 10px;">
                ${htmlItens}
            </div>
            <div style="display:flex; justify-content:space-between; font-weight:bold; margin-bottom:10px; font-size:1.1rem;">
                <span>Total:</span> <span>${totalFmt}</span>
            </div>
            <button id="btn-finalizar" style="width:100%; background:#00ff88; color:#000; padding:10px; border:none; border-radius:5px; font-weight:bold; cursor:pointer;">
                FINALIZAR PEDIDO
            </button>
        `;

    document.getElementById("btn-finalizar").onclick = finalizarCompra;
  }

  async function finalizarCompra() {
    const token = localStorage.getItem("token");

    if (!token) {
      alert("Faça login para finalizar a compra.");
      window.location.href = "/login.html";
      return;
    }

    if (carrinho.length === 0) {
      alert("Seu carrinho está vazio.");
      return;
    }

    const usuarioDados = parseJwt(token);

    if (!usuarioDados || !usuarioDados.id) {
      alert("Erro de autenticação: ID do usuário não encontrado no token.");
      return;
    }

    const usuarioId = usuarioDados.id;

    const pedidoDTO = {
      usuarioId: usuarioId,
      itens: carrinho.map((item) => ({
        produtoId: item.produtoId,
        quantidade: item.quantidade,
      })),
    };

    const btn = document.getElementById("btn-finalizar");
    btn.innerText = "Processando...";
    btn.disabled = true;

    try {
      const response = await fetch("/pedidos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(pedidoDTO),
      });

      if (response.ok) {
        alert("Compra realizada com sucesso!");
        carrinho = [];
        localStorage.removeItem("devfit_carrinho");
        renderizarCarrinhoFlutuante();
        fetchProducts();
      } else {
        const erroTxt = await response.text();
        alert("Erro ao finalizar pedido: " + erroTxt);
      }
    } catch (error) {
      console.error(error);
      alert("Erro de conexão ao finalizar compra.");
    } finally {
      if (btn) {
        btn.innerText = "FINALIZAR PEDIDO";
        btn.disabled = false;
      }
    }
  }

  fetchProducts();
  renderizarCarrinhoFlutuante();
});
