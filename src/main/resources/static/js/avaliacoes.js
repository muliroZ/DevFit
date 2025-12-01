document.addEventListener("DOMContentLoaded", () => {
    if (!localStorage.getItem("token")) window.location.href = "/login.html";
    carregarAvaliacoes();
    
    if (verificarPermissao("INSTRUTOR") || verificarPermissao("GESTOR")) {
        const btn = document.getElementById("btn-nova-avaliacao");
        if(btn) {
            btn.style.display = "block";
            btn.addEventListener("click", () => alert("Implementar formulário de criação"));
        }
    }
});

async function carregarAvaliacoes() {
    const container = document.getElementById("avaliacoes-container");
    const token = localStorage.getItem("token");
    
    // Lógica similar: Aluno vê as suas, Instrutor vê todas (ou busca por aluno específico via input de busca, implementação futura)
    let url = "/fichas/avaliacao/minhas-avaliacoes";
    if (verificarPermissao("INSTRUTOR") || verificarPermissao("GESTOR")) {
        url = "/fichas/avaliacao"; 
    }

    try {
        const response = await fetch(url, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (!response.ok) throw new Error("Erro na requisição");
        const lista = await response.json();
        
        renderizarAvaliacoes(lista);

    } catch (error) {
        container.innerHTML = `<p class="error-msg">Erro ao carregar dados.</p>`;
    }
}

function renderizarAvaliacoes(lista) {
    const container = document.getElementById("avaliacoes-container");
    container.innerHTML = "";

    if (!lista || lista.length === 0) {
        container.innerHTML = `<p class="loading-msg">Nenhuma avaliação encontrada.</p>`;
        return;
    }

    lista.forEach(av => {
        const card = document.createElement("div");
        card.className = "data-card";
        const dataAv = new Date(av.dataAvaliacao).toLocaleDateString('pt-BR');
        
        // Formatar IMC
        const imc = av.imc ? av.imc.toFixed(2) : "--";

        card.innerHTML = `
            <div class="card-header">
                <span class="card-date">${dataAv}</span>
                <span class="status-badge" style="background:#00ff88; color:#000;">IMC: ${imc}</span>
            </div>
            <div class="card-body">
                <p><strong>Aluno:</strong> ${av.aluno.nome}</p>
                <p><strong>Peso:</strong> ${av.pesoKg}kg</p>
                <p><strong>Altura:</strong> ${av.alturaM}m</p>
            </div>
            <button class="btn-details" onclick='abrirDetalhes(${JSON.stringify(av)})'>Ver Medidas</button>
        `;
        container.appendChild(card);
    });
}

function abrirDetalhes(av) {
    const modal = document.getElementById("modal-avaliacao");
    const body = document.getElementById("modal-body-avaliacao");
    modal.style.display = "flex";

    body.innerHTML = `
        <ul class="info-list">
            <li class="info-item">
                <strong>Instrutor Responsável:</strong> ${av.instrutor.nome}
            </li>
            <li class="info-item">
                <strong>Circunferências:</strong><br>
                Cintura: ${av.circunferenciaCinturaCm || '--'} cm<br>
                Abdômen: ${av.circunferenciaAbdomenCm || '--'} cm<br>
                Quadril: ${av.circunferenciaQuadrilCm || '--'} cm
            </li>
            <li class="info-item">
                <strong>Histórico de Saúde:</strong><br>
                <span style="color:#ccc; font-size:0.9rem">${av.historicoSaude || "Nenhum registro."}</span>
            </li>
            <li class="info-item">
                <strong>Observações:</strong><br>
                <span style="color:#ccc; font-size:0.9rem">${av.observacoesGerais || "Nenhuma observação."}</span>
            </li>
        </ul>
    `;
}

function fecharModal() {
    document.getElementById("modal-avaliacao").style.display = "none";
}

window.onclick = function(e) {
    if(e.target == document.getElementById("modal-avaliacao")) fecharModal();
}