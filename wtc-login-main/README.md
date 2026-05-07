# WTC Chat Android

Aplicativo Android em **Kotlin + Jetpack Compose** integrado ao backend `wtc-crm-backend` por Retrofit. O app realiza login JWT, navega pelas telas principais, lista conversas e envia mensagens.

## Requisitos

- Android Studio
- Android SDK configurado
- Backend rodando em `localhost:8082`
- `google-services.json` do projeto Firebase `wtc-chat-e9532` em `app/google-services.json`

## URL da API

O Gradle le a variavel `API_BASE_URL` do `.env` localizado na raiz do repositorio.

Para emulador Android:

```env
API_BASE_URL=http://10.0.2.2:8082/
```

Para celular fisico na mesma rede:

```env
API_BASE_URL=http://<IP_DO_PC>:8082/
```

Depois de alterar o `.env`, sincronize/recompile o app no Android Studio.

## Login de Teste

```text
Usuario: admin
Senha: admin
```

Tambem existe:

```text
Usuario: operator@wtc.com
Senha: 123456
```

## Fluxo de Autenticacao

1. `LoginScreen` envia `POST /api/auth/login`.
2. O backend retorna `token`, `userId`, `role` e `name`.
3. `SessionManager` guarda a sessao em memoria.
4. Chamadas autenticadas enviam `Authorization: Bearer <token>`.
5. `FcmRegistrar` envia o token FCM do dispositivo ao backend.

## Modulos Principais

| Caminho | Responsabilidade |
| --- | --- |
| `api/RetrofitInstance.kt` | Configura Retrofit, OkHttp e `API_BASE_URL`. |
| `api/WtcApiService.kt` | Contrato REST com o backend. |
| `api/dto/` | DTOs de login, conversas e mensagens. |
| `auth/SessionState.kt` | Estado de sessao JWT. |
| `fcm/` | Registro e recebimento de notificacoes FCM. |
| `navigation/AppNavigation.kt` | Rotas Compose. |
| `screens/LoginScreen.kt` | Tela de login. |
| `screens/ConversasScreen.kt` | Lista de conversas via API. |
| `screens/ChatopenScreen.kt` | Tela de chat. |
| `viewmodel/ChatViewModel.kt` | Carrega e envia mensagens. |

## Rotas do App

- `Login`
- `Principal`
- `Conversas`
- `Chat/{conversationId}`
- `criar_lista`
- `Ajustes`
- `Tarefas`

## Build

Pelo Android Studio, abra esta pasta e clique em **Run**.

Via terminal:

```powershell
cd E:\Programacao\wtc\wtc-login-main
.\gradlew.bat :app:assembleDebug
```

APK de debug:

```text
app/build/outputs/apk/debug/
```

## Observacoes

- Em emulador, `10.0.2.2` aponta para o `localhost` do PC.
- Em dispositivo fisico, use o IP local do PC.
- O app usa HTTP claro (`usesCleartextTraffic=true`) apenas para desenvolvimento local.
- Se nao houver conversas no Firestore/backend, a tela de conversas pode aparecer vazia.
