document.addEventListener("DOMContentLoaded", () => {
  carregarPlanos();
});

async function carregarPlanos() {
  const container = document.getElementById("planos-container");
  try {
    const response = await fetch("/planos");
    const planos = await response.json();

    container.innerHTML = "";

    if (planos.length === 0) {
      container.innerHTML = "<p>Nenhum plano disponível no momento.</p>";
      return;
    }

    planos.forEach((plano) => {
      const preco = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
      }).format(plano.valor);

      const card = document.createElement("div");
      card.className = "plan-card";
      card.innerHTML = `
                <h3>${plano.nome}</h3>
                <div class="plan-price">${preco}<small>/${plano.duracaoMeses} mês(es)</small></div>
                <p style="color:#ccc; margin-bottom:1rem">Acesso total à academia e app exclusivo.</p>
                <button class="btn-subscribe" onclick="assinarPlano(${plano.id})">Assinar Agora</button>
            `;
      container.appendChild(card);
    });
  } catch (error) {
    container.innerHTML = "<p class='error'>Erro ao carregar planos.</p>";
  }
}

async function assinarPlano(id) {
  const token = localStorage.getItem("token");
  if (!token) {
    alert("Você precisa fazer login para assinar um plano.");
    window.location.href = "/login.html";
    return;
  }

  if (!confirm("Deseja confirmar a assinatura deste plano?")) return;

  try {
    const response = await fetch(`/matricula/assinar/${id}`, {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    });

    if (response.ok) {
      alert("Assinatura realizada com sucesso! Bem-vindo ao time.");
      window.location.href = "/treinos.html";
    } else {
      alert(
        "Erro ao assinar plano. Verifique se já possui uma matrícula ativa."
      );
    }
  } catch (e) {
    alert("Erro de conexão.");
  }
}
