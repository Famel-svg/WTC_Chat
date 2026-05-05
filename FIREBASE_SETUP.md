# Firebase — o que configurar (WTC-CHAT)
Este projeto usa **Cloud Messaging (FCM)** no Android e **Firebase Admin SDK** no backend Java para enviar pushes.
## 1. Console Firebase ([console.firebase.google.com](https://console.firebase.google.com))
1. Abra o projeto **`wtc-chat-e9532`** (ou crie com esse ID se ainda não existir).
2. **Project settings** (ícone de engrenagem) → guia **General**.
3. Confirme **Project ID** = `wtc-chat-e9532` e anote o **Project number** (deve bater com o `.env`).
### App Android
1. Em **Your apps** → **Add app** → **Android**.
2. **Android package name:** `br.com.fiap.wtchat` (igual ao `applicationId` no Gradle).
3. Baixe o arquivo **`google-services.json`** gerado pelo assistente.
4. **Substitua** o conteúdo de `wtc-login-main/app/google-services.json` pelo arquivo oficial baixado (o template do repositório só existe para não quebrar o Gradle até você fazer isso).
### Cloud Messaging
1. No menu lateral: **Build** → **Cloud Messaging**.
2. Para enviar do **backend**, use a conta de serviço (Admin SDK), não a “Server key” legada se o console já mostrar só API V1.
### Conta de serviço (backend)
1. **Project settings** → **Service accounts**.
2. **Firebase Admin SDK** → **Generate new private key** → baixa um JSON.
3. Copie para `wtc-crm-backend/src/main/resources/firebase-service-account.json` (nome pode ser esse; o código atual espera esse nome em `FirebaseConfig` / `classpath:firebase-service-account.json`).
4. **Não commite** esse JSON em repositório público (use `.gitignore`).
### API keys (Google Cloud)
1. Em [Google Cloud Console](https://console.cloud.google.com/) → APIs & Services → **Credentials**.
2. A **Web API Key** do Firebase pode ser restrita (HTTP referrers / apps Android com SHA-1) para reduzir abuso.
3. O Android usa chaves do `google-services.json`; o `.env` na raiz alinha variáveis para backend/BuildConfig.
## 2. Android — SHA-1 (recomendado para Google Sign-In / algumas APIs)
No projeto:
```bash
cd wtc-login-main
./gradlew signingReport
```
Copie o **SHA-1** do debug e registre em Firebase → Project settings → seu app Android → **Add fingerprint**.
## 3. Backend
- Subir MongoDB e API com `.env` contendo pelo menos `GOOGLE_CLOUD_PROJECT_ID=wtc-chat-e9532`.
- Colocar `firebase-service-account.json` válido no classpath (passo acima).
- Sem esse arquivo, a API sobe, mas **FCM não envia** (há log de aviso).
## 4. Teste rápido
1. Login no app → o app registra o token FCM no backend (`PUT /api/auth/users/{id}/fcm-token`).
2. Duas contas em duas conversas com **participantIds** contendo os dois `userId` → mensagem em tempo real pelo **WebSocket** (`/ws/native`).
3. Com app em segundo plano, nova mensagem pode gerar **notificação FCM** para o outro participante (se token válido e projeto Firebase correto).
