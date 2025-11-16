document.addEventListener('DOMContentLoaded', () => {

    const loginButton = document.getElementById('login-btn')

    loginButton.addEventListener('click', login)

    async function login() {
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        const payload = {
            email: email,
            senha: password
        }

        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })

        if (!response.ok) {
            const text = await response.text();
            alert('Erro no login: ' + response.status + ' - ' + text);
            return;
        }

        const result = await response.json();
        localStorage.setItem('authToken', result.token);

        window.location.href = '/index.html'
    }
})