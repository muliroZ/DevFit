document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('register-form');
    const errorMsg = document.getElementById('error-message');

    form.addEventListener('submit', cadastro)

    async function cadastro(event) {
        event.preventDefault();
        
        const nome = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const senha = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirm-password').value;

        if (senha !== confirmPassword) {
            errorMsg.textContent = 'As senhas não batem.'
            return
        }

        const data = {
            nome,
            email,
            senha
        }

        try {
            const response = await fetch('/auth/cadastro', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })

            if (!response.ok) {
                errorMsg.textContent = 'Erro ao realizar cadastro.'
                errorMsg.style.display = 'block'
                return
            }

            window.location.href = '/login.html'
        } catch (error) {
            console.error('Erro: ', error)
            errorMsg.textContent = 'Erro ao conectar com o servidor.'
        }
    }
})