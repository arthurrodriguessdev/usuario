# API de Gerenciamento de Usuários

API REST para cadastro e gerenciamento de usuários, endereços e telefones. Oferece autenticação stateless com JWT e consulta de CEP pelo ViaCEP.

## Tecnologias

- Java 21 e Spring Boot
- Spring MVC, Spring Security e Spring Data JPA
- PostgreSQL
- Spring Cloud OpenFeign e ViaCEP
- JJWT e BCrypt
- Gradle e Docker

## Requisitos

- JDK 21
- PostgreSQL acessível pela aplicação
- Docker, opcional, para executar o banco ou criar a imagem da API

## Configuração

A aplicação lê as configurações por variáveis de ambiente:

| Variável | Obrigatória | Padrão | Descrição |
| --- | --- | --- | --- |
| `SPRING_DATASOURCE_URL_USUARIO` | Não | `jdbc:postgresql://postgres:5432/db_usuario` | URL JDBC do PostgreSQL |
| `SPRING_DATASOURCE_USERNAME_USUARIO` | Não | `postgres` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD_USUARIO` | Sim | — | Senha do banco |
| `SECRET_KEY_JWT` | Sim | — | Chave JWT em Base64; deve decodificar para pelo menos 32 bytes |
| `PORTA_USUARIO` | Não | `8081` | Porta HTTP |
| `URL_API_VIACEP` | Não | `https://viacep.com.br` | URL base do ViaCEP |
| `SPRING_JPA_SHOW_SQL` | Não | `false` | Habilita log de SQL |

Gere uma chave JWT segura com `openssl rand -base64 32`. Não versione credenciais nem use a senha padrão do PostgreSQL em produção.

## Executar localmente

Inicie um PostgreSQL local, crie o banco `db_usuario` e configure as variáveis no PowerShell:

```powershell
$env:SPRING_DATASOURCE_URL_USUARIO = "jdbc:postgresql://localhost:5432/db_usuario"
$env:SPRING_DATASOURCE_USERNAME_USUARIO = "postgres"
$env:SPRING_DATASOURCE_PASSWORD_USUARIO = "sua-senha-local"
$env:SECRET_KEY_JWT = "chave-base64-gerada"
.\gradlew.bat bootRun
```

Para compilar e executar os testes:

```powershell
.\gradlew.bat build
```

## Executar com Docker

Construa a imagem na raiz do repositório:

```powershell
docker build -t usuario-api .
```

Execute-a conectando a um PostgreSQL acessível pelo container:

```powershell
docker run --rm -p 8081:8081 `
  -e SPRING_DATASOURCE_URL_USUARIO="jdbc:postgresql://host.docker.internal:5432/db_usuario" `
  -e SPRING_DATASOURCE_USERNAME_USUARIO="postgres" `
  -e SPRING_DATASOURCE_PASSWORD_USUARIO="sua-senha-local" `
  -e SECRET_KEY_JWT="chave-base64-gerada" `
  usuario-api
```

## Endpoints

As rotas públicas são cadastro, login e consulta ViaCEP. As demais rotas exigem autenticação via JWT, enviado em um header HTTP com esquema Bearer. Usuários autenticados só podem consultar, alterar e excluir os próprios dados; recursos de terceiros são tratados como inexistentes.

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/usuarios` | Público | Cadastra usuário |
| `POST` | `/usuarios/login` | Público | Autentica e retorna um JWT |
| `GET` | `/usuarios/{id}` | JWT | Busca usuário pelo identificador |
| `GET` | `/usuarios?email={email}` | JWT | Busca usuário pelo e-mail |
| `PUT` | `/usuarios/{id}` | JWT | Atualiza dados do usuário |
| `DELETE` | `/usuarios/{id}` | JWT | Remove usuário |
| `POST` | `/usuarios/enderecos` | JWT | Cadastra endereço para o usuário autenticado |
| `PUT` | `/usuarios/enderecos/{id}` | JWT | Atualiza endereço |
| `POST` | `/usuarios/telefones` | JWT | Cadastra telefone para o usuário autenticado |
| `PUT` | `/usuarios/telefones/{id}` | JWT | Atualiza telefone |
| `GET` | `/usuarios/enderecos/cep?cep={cep}` | Público | Consulta CEP no ViaCEP |

Campos omitidos nas atualizações permanecem inalterados. A API retorna `409 Conflict` quando o cadastro usa um e-mail existente, `404 Not Found` para recursos indisponíveis, `401 Unauthorized` para falhas de login e `400 Bad Request` para CEP inválido.

### Cadastro de usuário

```http
POST /usuarios
Content-Type: application/json
```

```json
{
  "nome": "Ana Silva",
  "email": "ana@example.com",
  "senha": "uma-senha-segura",
  "enderecos": [
    {
      "rua": "Rua das Flores",
      "bairro": "Centro",
      "cep": "01001-000",
      "cidade": "São Paulo"
    }
  ],
  "telefones": [
    {
      "ddd": "11",
      "numero": "999999999"
    }
  ]
}
```

As respostas de usuário contêm nome, e-mail, endereços e telefones; nunca retornam a senha.

### Login

```http
POST /usuarios/login
Content-Type: application/json
```

```json
{
  "email": "ana@example.com",
  "senha": "uma-senha-segura"
}
```

Envie o token retornado no login no header de autenticação das chamadas protegidas, usando o esquema Bearer.

## Estrutura do projeto

```text
src/main/java/com/projeto/usuario/usuario/
├── UsuarioApplication.java
├── business/
│   ├── UsuarioService.java
│   ├── ViaCepService.java
│   ├── converter/
│   └── dto/
├── controller/
├── exception/
└── infraestructure/
    ├── client/
    ├── entity/
    ├── handler/
    ├── repository/
    └── security/
```

Controllers expõem a API HTTP, serviços concentram as regras de negócio, conversores mapeiam DTOs e entidades, e repositórios acessam o banco via JPA. A integração externa de CEP é encapsulada pelo cliente Feign.

## Autor

- [Arthur Rodrigues](https://www.linkedin.com/in/arthur-rodriguesx/)
