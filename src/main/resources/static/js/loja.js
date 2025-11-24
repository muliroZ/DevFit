document.addEventListener('DOMContentLoaded', () => {
    const container = document.getElementById('products-container')
    const searchInput = document.getElementById('search-input')
    let allProducts = []

    async function fetchProducts() {
        try {
            const token = localStorage.getItem('authToken')
            const headers = {
                'Content-Type': 'application/json'
            }

            if (token) {
                headers['Authorization'] = `Bearer ${token}`
            }

            const response = await fetch('/produtos', {
                method: 'GET',
                headers: headers
            })

            if (!response.ok) {
                throw new Error('Falha ao carregar produtos')
            }

            allProducts = await response.json()
            renderProducts(allProducts)
            
        } catch (error) {
            console.error('Erro ao buscar produtos:', error)
            container.innerHTML = '<p class="error">Erro ao carregar a loja. Tente novamente mais tarde</p>'
        }
    }

    function renderProducts(products) {
        container.innerHTML = ''

        if (products.lenght === 0) {
            container.innerHTML = '<p class="no-results">Nenhum produto encontrado.</p>'
            return
        }

        products.forEach(product => {
            const card = document.createElement('div')
            card.className = 'product-card'

            const precoFormatado = new Intl.NumberFormat('pt-BR', {
                style: 'currency',
                currency: 'BRL'
            }).format(product.preco)

            const temEstoque = product.estoque > 0
            const actionBtn = temEstoque 
                ? `<button class="buy-button" onclick="adicionarAoCarrinho(${product.id})">Comprar</button>`
                : `<button class="buy-button" disabled>Esgotado</button>`

            const imagem = product.imagemUrl

            card.innerHTML = `
                <div class="product-image-container">
                    <img src="${imagem}" alt="${product.nome}" class="product-image">
                </div>
                <div class="product-info">
                    <h3 class="product-name">${product.nome}</h3>
                    <p class="product-description">${product.descricao}</p>
                    <div class="product-footer">
                        <span class="product-price">${precoFormatado}</span>
                        ${actionBtn}
                    </div>
                </div>
            `

            container.appendChild(card)
        });
    }

    // Filtro de busca em tempo real
    searchInput.addEventListener('input', (e) => {
        const termo = e.target.value.toLowerCase()
        const produtosFiltrados = allProducts.filter(product => 
            product.nome.toLowerCase().includes(termo) ||
            product.descricao.toLowerCase().includes(termo)
        )
        renderProducts(produtosFiltrados)
    })

    // Simulação de adicionar ao carrinho
    window.adicionarAoCarrinho = (id) => {
        const token = localStorage.getItem('authToken')
        if (!token) {
            alert('Por favor, faça login para adicionar produtos ao carrinho.')
            window.location.href = '/login.html'
            return
        }
        alert(`Produto com ID: ${id}, adicionado ao carrinho! (Implementação futura)`)
    }

    // Inicializa
    fetchProducts()
})