document.addEventListener('DOMContentLoaded', () => {

    const loginForm = document.getElementById('login-form')
    const errorMsg = document.getElementById('error-message');

    loginForm.addEventListener('submit', login)

    async function login(event) {
        event.preventDefault();

        const email = document.getElementById('email').value;
        const senha = document.getElementById('password').value;

        const payload = {
            email: email,
            senha: senha
        }

        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })

        if (!response.ok) {
            const text = await response.text();
            errorMsg.textContent = 'Erro no login: ' + response.status + ' - ' + text;
            errorMsg.style.display = 'block';
            return;
        }

        const result = await response.json();
        localStorage.setItem('authToken', result.token);

        window.location.href = '/index.html'
    }
})