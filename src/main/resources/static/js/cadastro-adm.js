document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('admin-form');
    const messageDisplay = document.getElementById('message-display');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        messageDisplay.innerHTML = '';
        messageDisplay.className = 'message-container';

        const nome = document.getElementById('nome').value;
        const email = document.getElementById('email').value;
        const senha = document.getElementById('password').value;
        const codigoDeAcesso = document.getElementById('access-code').value;

        const payload = {
            nome: nome,
            email: email,
            senha: senha,
            codigoDeAcesso: codigoDeAcesso
        };

        try {
            const response = await fetch('/auth/register/manager', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const data = await response.json();
                messageDisplay.innerHTML = `<span class="success-msg">Gestor cadastrado com sucesso! Redirecionando...</span>`;
                
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);
            } else {
                const errorText = await response.text();
                messageDisplay.innerHTML = `<span class="error-msg">Erro: ${errorText || 'Falha no cadastro.'}</span>`;
            }
        } catch (error) {
            console.error('Erro na requisição:', error);
            messageDisplay.innerHTML = `<span class="error-msg">Erro de conexão com o servidor.</span>`;
        }
    });
});