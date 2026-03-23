# WTC Chat (Android) — Integração com API CRM

Aplicativo **Kotlin** + **Jetpack Compose** para login, navegação principal, conversas e chat, consumindo a API do backend **WTC CRM** (`wtc-crm-backend`) via **Retrofit** e sessão JWT em memória.

## Requisitos

| Item | Versão |
|------|--------|
| Android Studio | Ladybug ou superior recomendado |
| JDK | 11+ (conforme `app/build.gradle.kts`) |
| Android SDK | Instalado via Android Studio |
| Backend | Spring Boot rodando (ver README do `wtc-crm-backend`) |

## Configurar URL da API

O cliente HTTP está em `app/src/main/java/br/com/fiap/wtchat/api/RetrofitInstance.kt`:

| Ambiente | `BASE_URL` típica |
|----------|-------------------|
| **Emulador Android** | `http://10.0.2.2:8080/` (mapeia para `localhost` da máquina host) |
| **Dispositivo físico (mesma rede Wi‑Fi)** | `http://<IP_DO_PC>:8080/` |
| **Produção (HTTPS)** | URL do deploy (Railway, Render, etc.) |

Altere a constante `BASE_URL` e sincronize o Gradle.

> Em HTTP local, o app usa `usesCleartextTraffic` no `AndroidManifest.xml` **apenas para desenvolvimento**. Em produção, prefira HTTPS e remova cleartext.

## Fluxo de autenticação

1. `LoginScreen` chama `POST /api/auth/login` com e-mail e senha.  
2. A resposta (`token`, `userId`, `role`, `name`) é guardada em `SessionManager` (`auth/SessionState.kt`).  
3. Chamadas autenticadas enviam o header `Authorization: Bearer <token>` (ex.: `WtcApiService`, `ChatViewModel`).

Usuário de teste no backend seed: `operator@wtc.com` / `123456`.

## Módulos principais no código

| Caminho | Função |
|---------|--------|
| `api/RetrofitInstance.kt` | Instância singleton do Retrofit + OkHttp (log em debug). |
| `api/WtcApiService.kt` | Interface Retrofit: login, conversas, mensagens. |
| `api/dto/` | DTOs alinhados ao JSON do backend (Gson). |
| `auth/SessionState.kt` | Estado de sessão e `SessionManager`. |
| `screens/LoginScreen.kt` | UI de login + corrotina de login na API. |
| `screens/ConversasScreen.kt` | Lista de conversas (`GET /api/chat/conversations`). |
| `screens/ChatopenScreen.kt` | Tela de chat + `LaunchedEffect` para `conversationId`. |
| `viewmodel/ChatViewModel.kt` | Carrega e envia mensagens via API. |
| `navigation/AppNavigation.kt` | Rotas Compose, inclusive `Chat/{conversationId}`. |

## Navegação

Rotas registradas em `AppNavigation.kt`: `Login`, `Principal`, `Conversas`, `Chat/{conversationId}`, `criar_lista`, `Ajustes`, `Tarefas` (placeholder).

## Como compilar e instalar

1. Abra a pasta `wtc-login-main` no Android Studio.  
2. Crie/edite `local.properties` com `sdk.dir=...` (o Android Studio costuma gerar automaticamente).  
3. **Run** no emulador ou dispositivo.  

Build via linha de comando (com SDK configurado):

```bash
cd wtc-login-main
gradlew.bat assembleDebug
```

APK de debug: `app/build/outputs/apk/debug/`

## Observações

- Se **não houver conversas** no backend, a lista em `Conversas` pode ficar vazia até criar conversas pela API (`POST /api/chat/conversations`).  
- WebSocket STOMP pode ser adicionado depois no `ChatViewModel` para tempo real; hoje o chat usa **REST** para listar e enviar mensagens.

## Licença / projeto acadêmico

Projeto FIAP — integração com WTC CRM Platform Sprint 2.
