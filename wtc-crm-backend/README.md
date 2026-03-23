# WTC CRM Backend — Sprint 2

API REST em **Java 17** + **Spring Boot 3** para o CRM WTC (World Trade Center São Paulo): autenticação JWT, clientes, segmentos, chat, campanhas, tarefas, perfil 360°, mídia, auditoria e integração preparada para **Firebase** (Firestore / Storage / FCM).

## Requisitos

| Item | Versão / observação |
|------|---------------------|
| JDK | **17** |
| Maven | 3.9+ |
| Firebase (opcional em dev) | Arquivo `firebase-service-account.json` em `src/main/resources/` |

## Como executar

```bash
cd wtc-crm-backend
mvn spring-boot:run
```

Servidor padrão: **http://localhost:8080**

### Variáveis e configuração

Edite `src/main/resources/application.properties`:

| Propriedade | Descrição |
|-------------|-----------|
| `jwt.secret` | Chave secreta para assinatura HS256 (use valor forte em produção). |
| `jwt.expiration` | Tempo de vida do token em ms (ex.: `28800000` = 8h). |
| `firebase.config.path` | Caminho do JSON da service account (classpath). |
| `springdoc.*` | Caminhos do Swagger/OpenAPI. |

## Documentação da API (Swagger)

Após subir a aplicação:

- **Swagger UI:** http://localhost:8080/swagger-ui.html  
- **OpenAPI JSON:** http://localhost:8080/api-docs  

## Usuário de teste (seed)

O `AuthService` cria um operador inicial na memória:

| Campo | Valor |
|-------|--------|
| E-mail | `operator@wtc.com` |
| Senha | `123456` |
| Papel | `OPERATOR` |

> **Nota:** A persistência de usuários hoje é **em memória** no backend de exemplo; em produção deve apontar para a coleção Firestore `users` conforme o desenho do Sprint 2.

## Estrutura de pacotes (`br.com.fiap.wtccrm`)

| Pacote | Responsabilidade |
|--------|------------------|
| `config/` | `SecurityConfig`, `WebSocketConfig`, `SwaggerConfig`, `FirebaseConfig` |
| `auth/` | Login, registro, JWT (`JwtUtil`, filtro), DTOs |
| `clients/` | CRUD de clientes CRM |
| `segments/` | Segmentos e listagem de clientes por segmento |
| `chat/` | Conversas, mensagens REST + STOMP em `/ws/chat` |
| `campaigns/` | Campanhas e disparo |
| `tasks/` | Tarefas por cliente/operador |
| `profile/` | Agregação perfil 360° |
| `media/` | Upload validado (tipos/tamanho) e URL simulada |
| `notifications/` | Stub de FCM |
| `audit/` | Aspecto `@Auditable` e serviço de log |
| `exception/` | `GlobalExceptionHandler` e DTO de erro |

## Endpoints principais (resumo)

| Método | Caminho | Autenticação |
|--------|---------|--------------|
| POST | `/api/auth/login` | Público |
| POST | `/api/auth/register` | Público |
| PUT | `/api/auth/users/{id}/fcm-token` | Público (ajustar em produção) |
| GET/POST/PUT/DELETE | `/api/clients`, `/api/segments`, … | JWT + papel conforme `SecurityConfig` |
| GET/POST | `/api/chat/conversations`, `/api/chat/conversations/{id}/messages` | JWT |
| WebSocket | `/ws/chat` (SockJS + STOMP) | Configurar segurança em produção |

Lista completa e schemas no Swagger.

## WebSocket (chat em tempo real)

- **Endpoint SockJS:** `/ws/chat`  
- **Broker:** tópicos em `/topic`  
- **Destino da app:** prefixo `/app`  
- **Envio STOMP:** mapeamento `/app/chat.send` → broadcast em `/topic/conversation/{conversationId}`  

## Firebase

1. Baixe a **service account** no console Firebase (Projeto → Configurações → Contas de serviço).  
2. Salve como `src/main/resources/firebase-service-account.json`.  
3. Se o arquivo não existir, a aplicação ainda sobe; inicialização Firebase é condicional.

## Build e testes

```bash
mvn -DskipTests package
```

## Deploy

Exemplos: **Railway**, **Render**, **Azure**, etc. Defina `JWT_SECRET` (ou equivalente) e variáveis de ambiente conforme o provedor; mantenha o JSON da service account fora do repositório (use secrets).

## Licença / projeto acadêmico

Projeto FIAP — WTC CRM Platform Sprint 2.
