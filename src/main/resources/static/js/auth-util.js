/**
 * Decodifica o payload de um JWT sem verificar a assinatura (apenas leitura).
 * @param {string} token - O token JWT completo.
 * @returns {object|null} - O objeto JSON com os dados (claims) ou null se inválido.
 */
function parseJwt(token) {
    try {
        // O JWT é dividido em 3 partes por pontos: Header.Payload.Signature
        const base64Url = token.split('.')[1]; 
        
        // Converte Base64Url para Base64 padrão
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        
        // Decodifica a string Base64 para texto
        const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error("Erro ao decodificar token", e);
        return null;
    }
}

/**
 * Verifica se o usuário tem a permissão necessária.
 * @param {string} roleNecessaria - Ex: 'GESTOR', 'ADMIN', 'INSTRUTOR'
 * @returns {boolean} - True se permitido, False se negado.
 */
function verificarPermissao(roleNecessaria) {
    const token = localStorage.getItem("token"); // Ou onde você salva o token
    if (!token) return false;

    const dados = parseJwt(token);
    
    // Verifica se o token expirou (opcional, mas recomendado)
    if (dados.exp * 1000 < Date.now()) {
        localStorage.removeItem("token"); // Remove token expirado
        return false;
    }

    // Verifica se a role bate (backend geralmente salva como 'ROLE_GESTOR' ou 'GESTOR')
    // Ajuste o .includes conforme o padrão que seu CustomUserDetailsService usa
    return dados.role === roleNecessaria || dados.role === `ROLE_${roleNecessaria}`;
}