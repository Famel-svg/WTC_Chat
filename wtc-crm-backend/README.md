# WTC CRM Backend

API REST da Sprint 2 em **Java 17 + Spring Boot 3** para CRM, chat, campanhas, tarefas, perfil 360, midia, auditoria, JWT e push notifications. A persistencia principal usa **Firebase Firestore** via Firebase Admin SDK.

## Stack

- Java 17
- Spring Boot 3.3.3
- Spring Security + JWT
- Firebase Admin SDK
- Firestore
- WebSocket/STOMP
- SpringDoc OpenAPI

## Configuracao

O backend le o `.env` da raiz do repositorio por meio de `DotenvBootstrap`.

Variaveis principais:

```env
GOOGLE_CLOUD_PROJECT_ID=wtc-chat-e9532
FIREBASE_CONFIG_PATH=classpath:wtc-chat-e9532-service-account.json
JWT_SECRET=<segredo-com-pelo-menos-32-caracteres>
JWT_EXPIRATION_MS=28800000
SERVER_PORT=8082
```

Service account esperada:

```text
src/main/resources/wtc-chat-e9532-service-account.json
```

Esse arquivo deve ser gerado no Firebase Console do projeto `wtc-chat-e9532` em **Project settings > Service accounts**.

## Como Executar

```powershell
cd E:\Programacao\wtc\wtc-crm-backend
mvn spring-boot:run
```

Servidor:

```text
http://localhost:8082
```

Swagger:

```text
http://localhost:8082/swagger-ui.html
http://localhost:8082/api-docs
```

## Usuarios Seed

Nao ha credenciais fixas no codigo. Um operador inicial (papel `OPERATOR`) so e criado no Firestore,
caso ainda nao exista, quando as duas variaveis abaixo estiverem definidas no ambiente (ou no `.env`):

| Variavel | Uso |
| --- | --- |
| `WTC_SEED_OPERATOR_EMAIL` | e-mail/login do operador |
| `WTC_SEED_OPERATOR_PASSWORD` | senha do operador |

Sem elas, nenhum usuario e criado; use `POST /api/auth/register` para cadastrar.

## Colecoes Firestore

Os repositorios usam as seguintes colecoes:

- `users`
- `clients`
- `segments`
- `conversations`
- `messages`
- `campaigns`
- `tasks`
- `audit_logs`

## Endpoints

| Metodo | Caminho | Acesso |
| --- | --- | --- |
| POST | `/api/auth/login` | Publico |
| POST | `/api/auth/register` | Publico |
| PUT | `/api/auth/users/{id}/fcm-token` | Publico em dev |
| GET/POST/PUT/DELETE | `/api/clients` | `ROLE_OPERATOR` |
| GET/POST/PUT/DELETE | `/api/segments` | `ROLE_OPERATOR` |
| GET/POST/PUT/DELETE | `/api/campaigns` | `ROLE_OPERATOR` |
| GET/POST/PUT/DELETE | `/api/tasks` | Autenticado |
| GET | `/api/profile360/{clientId}` | `ROLE_OPERATOR` |
| POST | `/api/media/upload` | `ROLE_OPERATOR` |
| GET/POST | `/api/chat/conversations` | Autenticado |
| GET/POST | `/api/chat/conversations/{id}/messages` | Autenticado |
| WebSocket | `/ws/chat` | Publico em dev |

## WebSocket

- Endpoint SockJS: `/ws/chat`
- Endpoint nativo: `/ws/native`
- Broker: `/topic`
- Prefixo da aplicacao: `/app`
- Envio de mensagem: `/app/chat.send`
- Broadcast: `/topic/conversation/{conversationId}`

## Build

```powershell
mvn -DskipTests package
```

## Observacoes

- Nao versionar `.env` nem service accounts.
- `JWT_SECRET` deve ser forte em ambientes reais.
- Upload de midia ainda retorna URL simulada.
- Refresh token ainda nao foi implementado.
