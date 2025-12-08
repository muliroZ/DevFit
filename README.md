# DevFit

## 🏋️ API REST para Gestão de Academia & Aplicativo Web

O DevFit é um sistema completo para gerenciamento de academias, composto por uma **API RESTful** robusta (construída com Spring Boot) e um **Front-end Web Estático** (HTML/CSS/JS puro) para dashboard, loja, treinos e avaliações. O projeto é focado em modularidade e segurança (JWT e Spring Security).

## 🚀 Tecnologias Utilizadas

| Categoria | Tecnologia | Versão/Detalhe | Fonte |
| :--- | :--- | :--- | :--- |
| **Backend** | **Java** | 21 | |
| **Framework** | **Spring Boot** | 3.5.7 | |
| **Persistência** | **Spring Data JPA** | Utilizado para acesso ao banco | |
| **Banco de Dados** | **PostgreSQL** | 16 (via Docker) | |
| **Gerenciamento DB** | **Flyway** | 11.17.1 (Migrations) | |
| **Segurança** | **Spring Security & JWT** | Autenticação baseada em Token | |
| **Build** | **Maven** | Wrapper (`mvnw`) | |
| **Contêiner** | **Docker & Docker Compose** | Para ambiente de desenvolvimento isolado | |

## 📦 Estrutura do Módulo

O projeto é modular e as funcionalidades são segmentadas por papéis (`UsuarioRole`).

| Módulo | Papéis Principais | Funções Chave |
| :--- | :--- | :--- |
| **Auth** | GESTOR, INSTRUTOR, ALUNO | Cadastro e Login via JWT |
| **Financeiro** | GESTOR | Resumo de Receitas (Mensalidades, Pedidos) e Despesas |
| **Produtos/Pedidos** | GESTOR, ALUNO | Gestão de Estoque e Processamento de Compras na Loja |
| **Treino** | GESTOR, INSTRUTOR, ALUNO | Criação e Visualização de Fichas de Treino (`FichaTreino`, `ItemTreino`) |
| **Avaliação**| GESTOR, INSTRUTOR, ALUNO | Cadastro de Medidas Corporais e cálculo de IMC |
| **Checkin** | ALUNO | Registro de Entrada/Saída e Estatísticas de Horários de Pico |

## 📋 Pré-requisitos

Certifique-se de ter instalado:

1.  **Docker & Docker Compose** (Recomendado para ambiente completo)
2.  **Git**

## 🔧 Configuração Inicial

### 1\. Clonar e Acessar

```bash
git clone https://github.com/muliroZ/devfit.git
cd DevFit
```

### 2\. Configurar Variáveis de Ambiente

Copie o arquivo `.env.example` para `.env` e edite os valores:

```bash
cp .env.example .env
```

**Exemplo de Configurações Críticas (definidas no `.env`)**:

| Variável | Valor Exemplo | Uso |
| :--- | :--- | :--- |
| `POSTGRES_PASSWORD` | `maisumavez88` | Senha do PostgreSQL para o container DB |
| `JWT_SECRET` | `qpoweijfoqiewjfqwef...` | Chave secreta para a assinatura JWT |
| `ADMIN_SECRET` | `euamojava1` | Código de segurança para criar o primeiro GESTOR |

## 🐳 Executando com Docker (Recomendado)

O `docker-compose.yml` inicia três serviços: `app` (Spring Boot), `db` (PostgreSQL) e `pgadmin`.

### Iniciar o Sistema

Execute na pasta raiz do projeto (`/DevFit`):

```bash
docker compose up -d --build
```

### Acessos à Aplicação e Ferramentas

| Serviço | URL | Porta Local |
| :--- | :--- | :--- |
| **Aplicação Web (API)**| `http://localhost:8080/index.html` | `8080` |
| **PgAdmin** | `http://localhost:5050` | `5050` |
| **PostgreSQL** | `(Acesso externo)` | `5432` |

## 👤 Usuários de Teste (Pós-Migration V5)

A base de dados é populada com usuários iniciais para testes.

**A senha padrão para todos os usuários é: `Devfit123`**.

| Perfil | Email |
| :--- | :--- |
| **GESTOR** | `admin@devfit.com` |
| **INSTRUTOR** | `instrutor@devfit.com` |
| **ALUNO** | `ana@email.com` |
| **ALUNO** | `joao@email.com` |

## 🛑 Comandos Docker Úteis

| Comando | Descrição |
| :--- | :--- |
| `docker compose logs -f app` | Acompanha os logs em tempo real |
| `docker compose down` | Desliga e remove os containers |
| `docker compose down -v` | Desliga e remove containers **e volumes** (Limpa o banco de dados) |

-----

## 💻 Executando Localmente (Sem Docker)

### 1\. Build e Execução

A aplicação utiliza o Maven Wrapper (`mvnw`) para gerenciar o build.

```bash
# Permissão (necessário em Linux/Mac)
chmod +x mvnw

# 1. Limpar e Compilar
./mvnw clean package -DskipTests

# 2. Executar
./mvnw spring-boot:run
```

### 2\. Testes

Execute todos os testes do projeto:

```bash
./mvnw test
```

## 📝 Licença

Este projeto está sob a licença **MIT**, com Copyright (c) 2025 Murilo de Andrade.