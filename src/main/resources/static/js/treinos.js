document.addEventListener("DOMContentLoaded", () => {
    verificarLogin();
    carregarTreinos();
    configurarPermissoes();
});

function verificarLogin() {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "/login.html";
    }
}

function configurarPermissoes() {
    if (verificarPermissao("INSTRUTOR") || verificarPermissao("GESTOR")) {
        const btn = document.getElementById("btn-novo-treino");
        if(btn) {
            btn.style.display = "block";
            btn.addEventListener("click", () => alert("Funcionalidade de Criar Treino (Redirecionar para form)"));
        }
    }
}

async function carregarTreinos() {
    const container = document.getElementById("treinos-container");
    const token = localStorage.getItem("token");

    let url = "/fichas/treino/minhas-fichas";
    if (verificarPermissao("INSTRUTOR") || verificarPermissao("GESTOR")) {
        url = "/fichas/treino"; // Endpoint que lista todas
    }

    try {
        const response = await fetch(url, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!response.ok) throw new Error("Erro ao buscar treinos");

        const treinos = await response.json();
        renderizarTreinos(treinos);

    } catch (error) {
        console.error(error);
        container.innerHTML = `<p class="error-msg">Não foi possível carregar os treinos.</p>`;
    }
}

function renderizarTreinos(treinos) {
    const container = document.getElementById("treinos-container");
    container.innerHTML = "";

    if (treinos.length === 0) {
        container.innerHTML = `<p class="loading-msg">Nenhuma ficha de treino encontrada.</p>`;
        return;
    }

    treinos.forEach(t => {
        const card = document.createElement("div");
        card.className = "data-card";
        
        const dataVenc = new Date(t.dataVencimento).toLocaleDateString('pt-BR');
        const status = t.isAtiva ? "Ativa" : "Expirada";
        const statusClass = t.isAtiva ? "ativa" : "";

        // Se for instrutor vendo, mostra o nome do aluno. Se for aluno, mostra o do instrutor.
        const nomeSecundario = verificarPermissao("ALUNO") 
            ? `Instrutor: ${t.instrutor.nome}` 
            : `Aluno: ${t.aluno.nome}`;

        card.innerHTML = `
            <div class="card-header">
                <span class="card-date">Vence em: ${dataVenc}</span>
                <span class="status-badge ${statusClass}">${status}</span>
            </div>
            <div class="card-body">
                <p><strong>ID:</strong> #${t.id}</p>
                <p>${nomeSecundario}</p>
                <p><strong>Exercícios:</strong> ${t.ListaDeItens ? t.ListaDeItens.length : 0}</p>
            </div>
            <button class="btn-details" onclick="verDetalhesTreino(${t.id})">Ver Exercícios</button>
        `;
        container.appendChild(card);
    });
}

async function verDetalhesTreino(id) {
    const token = localStorage.getItem("token");
    const modal = document.getElementById("modal-detalhes");
    const modalBody = document.getElementById("modal-body");

    modal.style.display = "flex";
    modalBody.innerHTML = "<p>Carregando detalhes...</p>";

    try {
        const response = await fetch(`/fichas/treino/${id}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        const treino = await response.json();
        
        let htmlItens = '<ul class="info-list">';
        treino.ListaDeItens.forEach(item => {
            htmlItens += `
                <li class="info-item">
                    <h4 style="color:#fff; margin-bottom:5px;">${item.exercicio.nome}</h4>
                    <p style="font-size:0.9rem; color:#ccc;">
                       ${item.exercicio.musculoPrincipal} | 
                       <strong>${item.series}</strong> séries x <strong>${item.repeticoes}</strong> reps
                       ${item.cargaEstimadaKg ? `| Carga: ${item.cargaEstimadaKg}kg` : ''}
                    </p>
                    ${item.observacoes ? `<small style="color:#888;">Obs: ${item.observacoes}</small>` : ''}
                </li>
            `;
        });
        htmlItens += '</ul>';

        modalBody.innerHTML = htmlItens;

    } catch (error) {
        modalBody.innerHTML = "<p style='color:red'>Erro ao carregar detalhes.</p>";
    }
}

function fecharModal() {
    document.getElementById("modal-detalhes").style.display = "none";
}

// Fechar modal ao clicar fora
window.onclick = function(event) {
    const modal = document.getElementById("modal-detalhes");
    if (event.target == modal) {
        modal.style.display = "none";
    }
}