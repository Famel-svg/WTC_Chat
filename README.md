# WTC CRM Chat Application

Projeto acadêmico FIAP Sprint 2 para uma plataforma de comunicação e CRM do World Trade Center Sao Paulo. O sistema e composto por uma API REST em Spring Boot e um aplicativo Android nativo em Kotlin/Jetpack Compose.

## Visao Geral

- **Backend:** Java 17, Spring Boot 3, Spring Security, JWT, WebSocket/STOMP e Firebase Admin SDK.
- **Banco principal:** Firebase Firestore, usando colecoes NoSQL para usuarios, clientes, segmentos, conversas, mensagens, campanhas, tarefas e auditoria.
- **App Android:** Kotlin, Jetpack Compose, Retrofit, Firebase Cloud Messaging e Firebase Realtime Database listener para chat.
- **Integracao:** O app consome a API REST do backend e envia o token JWT nas chamadas autenticadas.

## Estrutura

```text
wtc/
├── wtc-crm-backend/          # API Spring Boot
├── wtc-login-main/           # App Android Kotlin/Compose
├── .env.example              # Modelo de variaveis locais
└── README.md
```

## Requisitos

- JDK 17+
- Maven 3.9+ ou Maven embutido no IntelliJ
- Android Studio
- Projeto Firebase `wtc-chat-e9532`
- Service account do Firebase para o backend
- `google-services.json` do Firebase para o app Android

## Configuracao Local

Crie um `.env` na raiz do repositorio a partir de `.env.example`.

Valores esperados para desenvolvimento local com emulador Android:

```env
GOOGLE_CLOUD_PROJECT_ID=wtc-chat-e9532
GOOGLE_CLOUD_PROJECT_NUMBER=<numero-do-projeto>
FIREBASE_WEB_API_KEY=<web-api-key>
FIREBASE_CONFIG_PATH=classpath:wtc-chat-e9532-service-account.json
JWT_SECRET=<segredo-com-pelo-menos-32-caracteres>
JWT_EXPIRATION_MS=28800000
API_BASE_URL=http://10.0.2.2:8082/
```

> O arquivo `.env` e as chaves Firebase nao devem ser versionados.

## Firebase

O app Android usa:

```text
wtc-login-main/app/google-services.json
```

O backend usa uma service account do mesmo projeto Firebase:

```text
wtc-crm-backend/src/main/resources/wtc-chat-e9532-service-account.json
```

Para gerar:

1. Abra o Firebase Console.
2. Entre no projeto `wtc-chat-e9532`.
3. Va em **Project settings > Service accounts**.
4. Gere uma nova chave privada.
5. Salve com o nome `wtc-chat-e9532-service-account.json` em `wtc-crm-backend/src/main/resources/`.

## Executando o Backend

```powershell
cd E:\Programacao\wtc\wtc-crm-backend
mvn spring-boot:run
```

Porta padrao:

```text
http://localhost:8082
```

Swagger/OpenAPI:

```text
http://localhost:8082/swagger-ui.html
http://localhost:8082/api-docs
```

## Executando o App Android

1. Abra `wtc-login-main` no Android Studio.
2. Sincronize o Gradle.
3. Rode em um emulador Android.
4. Mantenha o backend rodando em `localhost:8082`.

No emulador, o app usa:

```text
http://10.0.2.2:8082/
```

Em celular fisico, altere `API_BASE_URL` no `.env` para:

```text
http://<IP_DO_PC>:8082/
```

Depois recompile o app.

## Credenciais de Teste

Conta administrativa criada automaticamente no Firestore:

```text
Usuario: admin
Senha: admin
Role: OPERATOR
```

Conta adicional:

```text
Usuario: operator@wtc.com
Senha: 123456
Role: OPERATOR
```

## Modulos da API

- `POST /api/auth/login`
- `POST /api/auth/register`
- `PUT /api/auth/users/{id}/fcm-token`
- `GET/POST/PUT/DELETE /api/clients`
- `GET/POST/PUT/DELETE /api/segments`
- `GET/POST/PUT/DELETE /api/campaigns`
- `GET/POST/PUT/DELETE /api/tasks`
- `GET /api/profile360/{clientId}`
- `POST /api/media/upload`
- `GET/POST /api/chat/conversations`
- `GET/POST /api/chat/conversations/{id}/messages`
- WebSocket/STOMP: `/ws/chat`

## Build

Backend:

```powershell
cd wtc-crm-backend
mvn -DskipTests package
```

Android:

```powershell
cd wtc-login-main
.\gradlew.bat :app:assembleDebug
```

## Observacoes

- Firestore e o banco NoSQL principal.
- Firebase Cloud Messaging e usado para notificacoes push.
- A URL de upload de midia ainda e simulada.
- O endpoint de refresh token ainda nao foi implementado.
- Para producao, troque `JWT_SECRET`, proteja as credenciais e use HTTPS.

## Licenca

Projeto academico desenvolvido para FIAP.
