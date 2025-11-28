document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('register-form');
    const errorMsg = document.getElementById('error-message');

    const passCheck = document.getElementById('password');
    passCheck.addEventListener('input', () => {
        const pass = passCheck.value;

        updateCheck(".length", pass.length >= 8);
        updateCheck(".lower", /[a-z]/.test(pass));
        updateCheck(".upper", /[A-Z]/.test(pass));
        updateCheck(".number", /\d/.test(pass));
    });

    function updateCheck(selector, condition) {
        const el = document.querySelector(selector);
        if (!el) return;
        if (condition) {
            el.classList.add("ok");
        } else {
            el.classList.remove("ok");
        }
    }

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