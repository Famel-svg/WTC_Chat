# WTC CRM Chat

Plataforma de CRM e comunicação em tempo real desenvolvida para o **World Trade Center São Paulo** como projeto acadêmico FIAP Sprint 2. O sistema unifica gestão de clientes, campanhas e chat corporativo em uma única solução — eliminando a dependência de ferramentas externas desconectadas para equipes de relacionamento.

> **Stack principal:** Java 17 · Spring Boot 3 · Kotlin · Jetpack Compose · Firebase Firestore · WebSocket/STOMP · JWT

---

## O Problema

Equipes de relacionamento do WTC SP operavam com ferramentas fragmentadas: CRM em uma plataforma, comunicação interna em outra, sem histórico unificado de interações com clientes. O objetivo do projeto foi projetar e implementar uma solução integrada que centralizasse essas operações com rastreabilidade completa.

---

## Arquitetura

O sistema é composto por dois módulos independentes que se comunicam via API REST e WebSocket:

```
┌─────────────────────────────┐        ┌──────────────────────────┐
│     wtc-crm-backend         │        │     wtc-login-main       │
│  Java 17 + Spring Boot 3    │◄──────►│  Android (Kotlin/Compose)│
│  REST API + WebSocket/STOMP │  JWT   │  Retrofit + Firebase SDK │
│  Firebase Admin SDK         │        │  Firebase Cloud Messaging│
└─────────────────────────────┘        └──────────────────────────┘
             │
             ▼
     Firebase Firestore
     (NoSQL — coleções: users,
      clients, segments, campaigns,
      conversations, messages, tasks)
```

**Decisões de arquitetura relevantes:**

- **Firebase Firestore como banco principal** — escolha orientada à natureza em tempo real do chat: listeners nativos do Firestore eliminam polling e reduzem latência de entrega de mensagens.
- **WebSocket/STOMP no backend** — canal bidirecional persistente para o chat corporativo, desacoplado dos endpoints REST de CRM.
- **JWT stateless** — autenticação sem estado para suportar múltiplos clientes (Android + potenciais clientes web) sem sessão no servidor.
- **Backend em Java, app em Kotlin** — decisão deliberada para demonstrar interoperabilidade na JVM: o backend utiliza padrões Java/Spring consolidados, enquanto o app Android adota Kotlin idiomático com Jetpack Compose.

---

## Funcionalidades

**CRM**
- Cadastro e segmentação de clientes com perfil 360° (`GET /api/profile360/{clientId}`)
- Criação e disparo de campanhas por segmento
- Gerenciamento de tarefas vinculadas a clientes

**Chat em tempo real**
- Conversas via WebSocket/STOMP (`/ws/chat`)
- Notificações push com Firebase Cloud Messaging (FCM)
- Histórico persistido no Firestore com rastreabilidade de leitura

**Segurança**
- Autenticação JWT com expiração configurável (padrão: 8h)
- Roles por operador com controle de acesso por endpoint
- Variáveis sensíveis isoladas em `.env` (service account e chaves Firebase fora do versionamento)

---

## API Reference

A documentação interativa completa está disponível via Swagger/OpenAPI:

```
http://localhost:8082/swagger-ui.html
http://localhost:8082/api-docs
```

Endpoints principais:

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/login` | Autenticação e emissão de JWT |
| GET/POST/PUT/DELETE | `/api/clients` | CRUD de clientes |
| GET/POST/PUT/DELETE | `/api/segments` | Segmentação |
| GET/POST/PUT/DELETE | `/api/campaigns` | Campanhas |
| GET | `/api/profile360/{clientId}` | Visão 360° do cliente |
| GET/POST | `/api/chat/conversations` | Conversas |
| WS | `/ws/chat` | Canal WebSocket/STOMP |

---

## Como Executar

**Pré-requisitos:** JDK 17+, Maven 3.9+, Android Studio, projeto Firebase `wtc-chat-e9532` configurado.

**1. Configure as variáveis de ambiente**
```bash
cp .env.example .env
# Preencha GOOGLE_CLOUD_PROJECT_ID, FIREBASE_WEB_API_KEY, JWT_SECRET e demais campos
```

**2. Adicione a service account do Firebase**
```
wtc-crm-backend/src/main/resources/wtc-chat-e9532-service-account.json
```
> Gere em: Firebase Console → Project Settings → Service Accounts → Generate new private key

**3. Suba o backend**
```bash
cd wtc-crm-backend
mvn spring-boot:run
# API disponível em http://localhost:8082
```

**4. Execute o app Android**

Abra `wtc-login-main` no Android Studio, sincronize o Gradle e rode em um emulador. O app aponta para `http://10.0.2.2:8082/` por padrão.

**Credenciais de teste**
```
Usuário: admin  |  Senha: admin  |  Role: OPERATOR
```

---

## Build

```bash
# Backend — gerar JAR
cd wtc-crm-backend && mvn -DskipTests package

# Android — gerar APK debug
cd wtc-login-main && ./gradlew :app:assembleDebug
```

O APK de demonstração está disponível em [`WTChat.apk`](./WTChat.apk).

---

## Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 17, Spring Boot 3, Spring Security, Spring Data |
| Autenticação | JWT (jjwt), Firebase Auth |
| Banco de dados | Firebase Firestore (NoSQL) |
| Mensageria | WebSocket/STOMP, Firebase Cloud Messaging |
| App Android | Kotlin, Jetpack Compose, Retrofit2 |
| Documentação | Swagger / OpenAPI 3 |
| Build | Maven (backend), Gradle (Android) |

---

## Limitações conhecidas

- Upload de mídia ainda retorna URL simulada — integração com Storage em roadmap
- Endpoint de refresh token não implementado na versão atual
- Para produção: substituir `JWT_SECRET`, habilitar HTTPS e rever regras de segurança do Firestore

---

## Sobre o Projeto

Desenvolvido por **Rafael Andrade Tanimoto** como parte do Sprint 2 da FIAP — ADS.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-rafaeltanimoto-blue?style=flat&logo=linkedin)](https://linkedin.com/in/rafaeltanimoto)
[![GitHub](https://img.shields.io/badge/GitHub-Famel--svg-black?style=flat&logo=github)](https://github.com/Famel-svg)
