# DevFit 
*O README ainda está em construção...*

#### API REST para gestão de academia desenvolvida com Spring Boot, utilizando PostgreSQL e Docker.

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **Spring Security + JWT**
- **PostgreSQL 18**
- **Docker & Docker Compose**
- **Lombok**
- **Maven**

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [Docker](https://docs.docker.com/get-docker/) (versão 20.10 ou superior)
- [Docker Compose](https://docs.docker.com/compose/install/) (versão 2.0 ou superior)

**OU**

- [Java 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [PostgreSQL 18](https://www.postgresql.org/download/)

## 🔧 Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/muliroZ/devfit.git
cd devfit
```

### 2. Configure as variáveis de ambiente

Copie o arquivo `.env.example` para `.env`:

```bash 
cp .env.example .env
``` 

Edite o arquivo `.env` com suas configurações:

```env
# POSTGRESQL
POSTGRES_USER=admin 
POSTGRES_PASSWORD=sua_senha_segura 
POSTGRES_DB=devfitdb

# CONEXÃO COM O BANCO DE DADOS
DB_HOST=db 
DB_USER=admin 
DB_PASSWORD=sua_senha_segura 
DB_NAME=devfitdb

# CHAVE SECRETA PARA JWT (gere uma chave segura)
JWT_SECRET=sua_chave_secreta_jwt_aqui
```

> ⚠️ **Importante**: Nunca commite o arquivo `.env` no Git! Ele contém informações sensíveis.

## 🐳 Executando com Docker (Recomendado)

### Iniciar a aplicação

```bash 
docker-compose up -d
``` 

Este comando irá:
- Baixar as imagens necessárias
- Criar e iniciar o container do PostgreSQL
- Compilar e executar a aplicação Spring Boot

### Verificar os logs

```bash 
docker-compose logs -f app
```

### Parar a aplicação

```bash 
docker-compose down
``` 

### Parar e remover volumes (limpar banco de dados)

```bash 
docker-compose down -v
```

## 💻 Executando localmente (sem Docker)

### 1. Configure o PostgreSQL

Certifique-se de que o PostgreSQL está rodando e crie o banco de dados:

```sql 
CREATE DATABASE devfitdb;
       
CREATE USER admin WITH PASSWORD 'sua_senha'; 

GRANT ALL PRIVILEGES ON DATABASE devfitdb TO admin;
``` 

### 2. Atualize o arquivo `.env`

Altere `DB_HOST=db` para `DB_HOST=localhost`:

```
env DB_HOST=localhost
```

### 3. Compile e execute

```bash
Compilar o projeto
./mvnw clean package -DskipTests
Executar a aplicação
./mvnw spring-boot:run
``` 

Ou no Windows:

```cmd 
mvnw.cmd clean package -DskipTests mvnw.cmd spring-boot:run
```

## 🌐 Acessando a aplicação

Após iniciar a aplicação, ela estará disponível em:

```
http://localhost:8080
``` 

### Endpoints disponíveis

A documentação completa da API estará disponível em:

```
http://localhost:8080/swagger-ui.html
```

(Se você adicionar o Swagger/OpenAPI posteriormente)

## 🧪 Executando testes

```
bash ./mvnw test
``` 

## 📦 Build para produção

```
bash ./mvnw clean package -DskipTests
```

O arquivo JAR será gerado em `target/DevFit-0.0.1-SNAPSHOT.jar`

## 🔍 Verificação de saúde

Para verificar se a aplicação está rodando corretamente:

```bash 
curl http://localhost:8080/actuator/health
``` 

## 🛠️ Comandos úteis do Docker

```bash
# Ver containers em execução
docker-compose ps

# Acessar o container da aplicação
docker exec -it devfit_app bash

# Acessar o PostgreSQL
docker exec -it postgres_db psql -U admin -d devfitdb

# Reconstruir as imagens
docker-compose build --no-cache

# Ver logs do banco de dados
docker-compose logs -f db
```

## ❗ Solução de problemas

### Porta 8080 já em uso

Se a porta 8080 estiver em uso, você pode alterá-la no arquivo `.env`:

```env 
SERVER_PORT=3000
``` 

E no `docker-compose.yml`, altere o mapeamento de portas:

```yaml 
ports:
  "3000:8080"
```

### Problema de permissão com Maven Wrapper

No Linux/Mac, dê permissão de execução:

```bash 
chmod +x mvnw
``` 

### Container não inicia

```bash
# Limpar containers e volumes
docker-compose down -v

# Remover imagens antigas
docker-compose build --no-cache

# Iniciar novamente
docker-compose up -d
```

## 📝 Licença

Este projeto está sob a licença especificada no arquivo [LICENSE](LICENSE).

## 👥 Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.

## 📧 Contato

Para dúvidas ou sugestões, entre em contato através das issues do projeto.
