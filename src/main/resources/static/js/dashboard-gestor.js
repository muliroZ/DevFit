document.addEventListener("DOMContentLoaded", () => {
  if (!verificarPermissao("GESTOR")) {
    alert("Acesso restrito a Gestores.");
    window.location.href = "/index.html";
    return;
  }

  carregarDashboard();
});

const formatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

const percentFormatter = new Intl.NumberFormat("pt-BR", {
  style: "percent",
  minimumFractionDigits: 1,
  maximumFractionDigits: 1,
});

async function carregarDashboard() {
  const token = localStorage.getItem("token");
  const headers = { Authorization: `Bearer ${token}` };

  try {
    // Agora buscamos o endpoint geral de stats + listas detalhadas para tabelas/gráficos
    const [resStats, resUsuarios, resEquip] = await Promise.all([
      fetch("/admin/dashboard/stats", { headers }), // Endpoint principal do AdminStatsDTO
      fetch("/admin/dashboard/usuarios/detalhado", { headers }),
      fetch("/dashboard/equipamentos", { headers }),
    ]);

    if (resStats.ok) {
        const statsData = await resStats.json();
        atualizarKPIs(statsData);
        atualizarGraficoFinanceiro(statsData.financeiroDashboardDTO);
    }
    
    if (resUsuarios.ok) atualizarGraficoUsuariosETabela(await resUsuarios.json());
    if (resEquip.ok) atualizarTabelaEquipamentos(await resEquip.json());

  } catch (error) {
    console.error("Erro ao carregar dashboard:", error);
  }
}

// --- PREENCHIMENTO DOS CARDS (KPIs) ---
function atualizarKPIs(stats) {
    const fin = stats.financeiroDashboardDTO;

    // 1. Financeiro
    document.getElementById("kpi-receita").innerText = formatter.format(fin.receitaTotal);
    document.getElementById("kpi-despesa").innerText = formatter.format(fin.despesaTotal);
    document.getElementById("kpi-faturamento-previsto").innerText = formatter.format(stats.faturamentoMensalPrevisto);
    
    const lucroEl = document.getElementById("kpi-lucro");
    lucroEl.innerText = formatter.format(fin.lucroLiquido);
    if (fin.lucroLiquido < 0) {
        lucroEl.classList.remove("positive");
        lucroEl.classList.add("negative");
    } else {
        lucroEl.classList.add("positive");
        lucroEl.classList.remove("negative");
    }

    // 2. Membros & Retenção
    document.getElementById("kpi-alunos-ativos").innerText = stats.totalAlunosAtivos;
    document.getElementById("kpi-alunos-inativos").innerText = stats.totalAlunosInativos;
    document.getElementById("kpi-total-users").innerText = stats.totalUsuariosCadastrados;
    
    // Taxa de retenção vem como double (ex: 0.85 para 85%)
    document.getElementById("kpi-taxa-retencao").innerText = percentFormatter.format(stats.taxaRetencao);

    // 3. Operacional
    document.getElementById("kpi-checkins").innerText = stats.checkinsHoje;
    document.getElementById("txt-capacidade").innerText = stats.capacidadeMaxima;
    
    // Calculo simples de ocupação baseado nos checkins de hoje vs capacidade
    // (Num cenário real, seria checkins simultâneos, mas usaremos o total do dia como proxy para o exemplo)
    const ocupacao = stats.capacidadeMaxima > 0 ? (stats.checkinsHoje / stats.capacidadeMaxima) : 0;
    document.getElementById("kpi-ocupacao").innerText = percentFormatter.format(ocupacao);

    document.getElementById("kpi-equip-total").innerText = stats.equipamentosTotais;
    document.getElementById("kpi-equip-manutencao").innerText = stats.equipamentosEmManutencao;
}

// --- GRÁFICO FINANCEIRO ---
function atualizarGraficoFinanceiro(finData) {
  const ctx = document.getElementById("revenueChart").getContext("2d");
  
  // Dados seguros
  const receitas = finData.receitaPorFonte || {};
  const labels = Object.keys(receitas);
  const values = Object.values(receitas);

  // Se não houver dados, mostra placeholder
  if (labels.length === 0) {
      labels.push("Sem dados");
      values.push(1);
  }

  new Chart(ctx, {
    type: "doughnut",
    data: {
      labels: labels,
      datasets: [
        {
          data: values,
          backgroundColor: ["#00ff88", "#00ccff", "#ffaa00", "#ff4444"],
          borderWidth: 0,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        legend: { position: "bottom", labels: { color: "#fff" } },
      },
    },
  });
}

// --- TABELA E GRÁFICO DE USUÁRIOS ---
function atualizarGraficoUsuariosETabela(mapaUsuarios) {
  const labels = [];
  const counts = [];
  const tbody = document.getElementById("table-users-body");
  tbody.innerHTML = "";

  for (const [role, lista] of Object.entries(mapaUsuarios)) {
    labels.push(role);
    counts.push(lista.length);

    // Adiciona na tabela (apenas os 5 primeiros de cada grupo para não ficar gigante)
    lista.slice(0, 5).forEach((u) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
                <td>${u.nome}</td>
                <td>${u.email}</td>
                <td><span class="user-badge ${role.toLowerCase()}">${role}</span></td>
                <td>${u.planoNome || "-"}</td>
            `;
      tbody.appendChild(tr);
    });
  }

  const ctx = document.getElementById("usersChart").getContext("2d");
  new Chart(ctx, {
    type: "bar",
    data: {
      labels: labels,
      datasets: [
        {
          label: "Quantidade",
          data: counts,
          backgroundColor: "#00ff88",
          borderRadius: 5,
        },
      ],
    },
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true,
          ticks: { color: "#fff" },
          grid: { color: "#444" },
        },
        x: { ticks: { color: "#fff" }, grid: { display: false } },
      },
      plugins: { legend: { display: false } },
    },
  });
}

// --- TABELA DE EQUIPAMENTOS ---
function atualizarTabelaEquipamentos(lista) {
  const tbody = document.getElementById("table-equip-body");
  tbody.innerHTML = "";

  if (!lista || lista.length === 0) {
    tbody.innerHTML = "<tr><td colspan='4'>Nenhum equipamento cadastrado.</td></tr>";
    return;
  }

  lista.forEach((eq) => {
    const tr = document.createElement("tr");
    // Verifica status (se não existir no DTO, assume 'Ativo')
    // No seu backend o EquipamentoDashboardDTO não tem o campo 'status' explicitamente mapeado no construtor
    // mas o AdminStatsDTO usa o countByStatus. Vou deixar genérico aqui.
    const status = eq.status || "Ativo"; 
    const statusColor = status === "MANUTENCAO" ? "#ff4444" : "#00ff88";

    tr.innerHTML = `
            <td>${eq.nome}</td>
            <td>${eq.quantidade}</td>
            <td>${formatter.format(eq.valor)}</td>
            <td style="color:${statusColor}">${status}</td>
        `;
    tbody.appendChild(tr);
  });
}