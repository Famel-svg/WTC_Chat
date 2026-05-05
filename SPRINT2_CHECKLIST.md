# Sprint 2 — checklist atualizado (WTC)

**Progresso Geral: ~53% concluído** (10 de 19 itens concluídos)

Última revisão após: MongoDB, `.env` + integração backend/Android, Firebase Admin com `projectId`.

## Concluído

- [x] Backend Java 17 + Spring Boot (JWT, Swagger, módulos CRM/chat/campanhas/tarefas/perfil 360°/mídia/auditoria).
- [x] Persistência **MongoDB** (coleções users, clients, segments, conversations, messages, campaigns, tasks, audit_logs).
- [x] Arquivo **`.env` na raiz** com projeto Firebase/Google; **`.gitignore`** ignora `.env`.
- [x] **DotenvBootstrap** no backend: carrega `.env` antes do Spring; propriedades `GOOGLE_CLOUD_PROJECT_ID`, `MONGODB_URI`, `JWT_SECRET`, etc.
- [x] **Firebase Admin**: `projectId` opcional alinhado ao `.env` quando existe `firebase-service-account.json` no classpath.
- [x] Android: **BuildConfig** preenchido a partir do `../.env` + objeto **`FirebaseEnv`** para uso futuro (SDK cliente / APIs).

## Ainda falta (prioridade sugerida)

### Funcional / integração (nota alto peso)

- [ ] **`POST /api/auth/refresh`** — refresh token (brief PDF); hoje só login/register.
- [x] **Push FCM** — backend usa **Firebase Admin** (`FirebaseMessaging.send`); campanha envia para usuários com mesmo e-mail que clientes do segmento; chat notifica outros participantes com token FCM. App registra token após login. **Exige** `firebase-service-account.json` + `google-services.json` reais (ver `FIREBASE_SETUP.md`). **Observação**: Funcional somente quando os arquivos de credencial estão presentes no classpath/local.
- [x] **Android + tempo real** — cliente **STOMP** (`/ws/native`) + inscrição em `/topic/conversation/{id}`; REST mantém envio/histórico. **Observação**: Verificar se conexão WebSocket está estabelecida e mensagens em tempo real funcionam adequadamente.
- [ ] **Upload tipo desafio** — **S3 ou MinIO** com URL pré-assinada (hoje `MediaService` retorna URL simulada).
- [ ] **Estados de mensagem ponta a ponta** — backend tem status; confirmar **Enviado / Entregue / Lido / Falha** no app e callbacks se necessário.

### Opcionais do PDF (exemplos)

- [ ] Rotas extras de campanha (`schedule`, `abtest`) — não obrigatório se o escopo da disciplina aceitar só envio imediato.
- [ ] Tags em cliente — se o professor exigir além de segment/score/status.

### Credenciais e ambiente

- [x] **`firebase-service-account.json`** do projeto **`wtc-chat-e9532`** em `wtc-crm-backend/src/main/resources/` (substituir JSON antigo de outro projeto; não commitar em repositório público).
- [x] **`google-services.json`** no app Android (Firebase Console) quando ativar FCM/analytics — complementar ao `.env`.

### Entrega acadêmica

- [ ] ZIP backend + ZIP app + **APK**.
- [ ] **PDF/PPT**: RM/nomes, diagrama de arquitetura, especificação de endpoints (rota, método, payload, resposta).
- [ ] **Modelo de dados NoSQL** (diagrama ou descrição das coleções).

### Segurança

- [ ] **JWT_SECRET** forte em produção e variáveis sensíveis só em `.env` / CI, nunca no Git.
