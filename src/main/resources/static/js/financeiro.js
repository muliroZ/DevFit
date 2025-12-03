document.addEventListener("DOMContentLoaded", () => {
  if (!verificarPermissao("GESTOR")) {
    alert("Acesso restrito.");
    window.location.href = "/index.html";
    return;
  }
  carregarFinanceiro();
});

const moneyFmt = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

async function carregarFinanceiro() {
  const token = localStorage.getItem("token");
  try {
    const response = await fetch("/admin/dashboard/financeiro/resumo", {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (response.ok) {
      const dados = await response.json();
      atualizarTela(dados);
    }
  } catch (error) {
    console.error("Erro financeiro:", error);
  }
}

function atualizarTela(dados) {
  document.getElementById("fin-receita").innerText = moneyFmt.format(
    dados.receitaTotal
  );
  document.getElementById("fin-despesa").innerText = moneyFmt.format(
    dados.despesaTotal
  );

  const lucroEl = document.getElementById("fin-lucro");
  lucroEl.innerText = moneyFmt.format(dados.lucroLiquido);
  lucroEl.className = `kpi-value ${
    dados.lucroLiquido >= 0 ? "positive" : "negative"
  }`;

  gerarGrafico("receitaChart", dados.receitaPorFonte, ["#00ff88", "#00ccff"]);
  gerarGrafico("despesaChart", dados.despesaPorCategoria, [
    "#ff4444",
    "#ffaa00",
    "#ff00ff",
  ]);
}

function gerarGrafico(canvasId, mapDados, cores) {
  const ctx = document.getElementById(canvasId).getContext("2d");
  const labels = Object.keys(mapDados || {});
  const data = Object.values(mapDados || {});

  if (labels.length === 0) {
    labels.push("Sem dados");
    data.push(1);
    cores = ["#333"];
  }

  new Chart(ctx, {
    type: "pie",
    data: {
      labels: labels,
      datasets: [{ data: data, backgroundColor: cores, borderWidth: 0 }],
    },
    options: {
      responsive: true,
      plugins: { legend: { position: "bottom", labels: { color: "#fff" } } },
    },
  });
}
