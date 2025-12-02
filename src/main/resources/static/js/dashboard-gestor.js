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

async function carregarDashboard() {
  const token = localStorage.getItem("token");
  const headers = { Authorization: `Bearer ${token}` };

  try {
    // Fazendo as requisições em paralelo para performance
    const [resFinancas, resUsuarios, resEquip] = await Promise.all([
      fetch("/admin/dashboard/financeiro/resumo", { headers }),
      fetch("/admin/dashboard/usuarios/detalhado", { headers }),
      fetch("/dashboard/equipamentos", { headers }),
    ]);

    if (resFinancas.ok) atualizarFinanceiro(await resFinancas.json());
    if (resUsuarios.ok) atualizarUsuarios(await resUsuarios.json());
    if (resEquip.ok) atualizarEquipamentos(await resEquip.json());
  } catch (error) {
    console.error("Erro ao carregar dashboard:", error);
  }
}

// --- LÓGICA DO FINANCEIRO ---
function atualizarFinanceiro(data) {
  // Preenche os Cards
  document.getElementById("kpi-receita").innerText = formatter.format(
    data.receitaTotal
  );
  document.getElementById("kpi-despesa").innerText = formatter.format(
    data.despesaTotal
  );

  const lucroEl = document.getElementById("kpi-lucro");
  lucroEl.innerText = formatter.format(data.lucroLiquido);

  // Muda a cor do lucro se for prejuízo
  if (data.lucroLiquido < 0) {
    lucroEl.classList.remove("positive");
    lucroEl.classList.add("negative");
  } else {
    lucroEl.classList.add("positive");
  }

  // Gráfico de Receita (Doughnut)
  const ctx = document.getElementById("revenueChart").getContext("2d");

  // Trata dados caso venham vazios do backend
  const labels = data.receitaPorFonte ? Object.keys(data.receitaPorFonte) : [];
  const values = data.receitaPorFonte
    ? Object.values(data.receitaPorFonte)
    : [];

  new Chart(ctx, {
    type: "doughnut",
    data: {
      labels: labels,
      datasets: [
        {
          data: values,
          backgroundColor: ["#00ff88", "#00ccff", "#ffaa00"],
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

// --- LÓGICA DE USUÁRIOS ---
function atualizarUsuarios(mapaUsuarios) {
  // mapaUsuarios vem como: { "ALUNO": [...], "INSTRUTOR": [...] }

  let totalAlunos = 0;
  const labels = [];
  const counts = [];
  const tbody = document.getElementById("table-users-body");
  tbody.innerHTML = "";

  // Itera sobre o Map retornado pelo backend
  for (const [role, lista] of Object.entries(mapaUsuarios)) {
    labels.push(role);
    counts.push(lista.length);

    if (role === "ALUNO") totalAlunos = lista.length;

    // Popula a tabela (limitando a 5 recentes por role para não poluir)
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

  // Atualiza KPI Total Alunos
  document.getElementById("kpi-alunos").innerText = totalAlunos;

  // Gráfico de Usuários (Bar)
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

// --- LÓGICA DE EQUIPAMENTOS ---
function atualizarEquipamentos(lista) {
  const tbody = document.getElementById("table-equip-body");
  tbody.innerHTML = "";

  if (!lista || lista.length === 0) {
    tbody.innerHTML =
      "<tr><td colspan='4'>Nenhum equipamento cadastrado.</td></tr>";
    return;
  }

  lista.forEach((eq) => {
    const tr = document.createElement("tr");
    const dataAq = new Date(eq.dataAquisao).toLocaleDateString("pt-BR"); // Note: no DTO está dataAquisao
    tr.innerHTML = `
            <td>${eq.nome}</td>
            <td>${eq.quantidade}</td>
            <td>${formatter.format(eq.valor)}</td>
            <td>${dataAq}</td>
        `;
    tbody.appendChild(tr);
  });
}
