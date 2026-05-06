# WTC CRM Chat Application

## Overview

WTC CRM Chat is a full-stack messaging application built as a FIAP academic project for the World Trade Center São Paulo (WTC). The application consists of:

- **Backend**: Java 17 + Spring Boot 3 REST API with MongoDB persistence
- **Frontend**: Android application with real-time chat capabilities
- **Features**: Authentication (JWT), CRM modules, real-time messaging, push notifications via Firebase Cloud Messaging (FCM), file upload, and audit logging

## Project Structure

```
wtc/
├── wtc-crm-backend/          # Spring Boot backend application
├── wtc-login-main/           # Android client application
├── .env.example              # Environment variables template
├── FIREBASE_SETUP.md         # Firebase configuration guide
├── SPRINT2_CHECKLIST.md      # Development progress tracking
└── README.md                 # This file
```

## Key Features

### Backend (`wtc-crm-backend`)
- **Authentication**: JWT-based login/registration with role-based access control
- **CRM Modules**: Clients, segments, campaigns, tasks, profile 360°, media management
- **Real-time Chat**: WebSocket/STOMP implementation for instant messaging
- **Push Notifications**: Firebase Cloud Messaging integration via Admin SDK
- **Audit Logging**: Comprehensive activity tracking with `@Auditable` aspect
- **API Documentation**: Swagger/OpenAPI available at `/swagger-ui.html`
- **Database**: MongoDB with collections for users, clients, conversations, messages, etc.

### Android Client (`wtc-login-main`)
- **Authentication**: Login/register with JWT token storage
- **Real-time Messaging**: STOMP client subscribed to conversation topics
- **Push Notifications**: FCM token registration and handling
- **Media Support**: Image upload/download capabilities
- **UI**: Modern Material Design interface

## Technology Stack

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3
- **Database**: MongoDB
- **Authentication**: JWT (HS256)
- **Messaging**: WebSocket/STOMP with SockJS fallback
- **Push Notifications**: Firebase Admin SDK
- **API Docs**: SpringDoc OpenAPI (Swagger)
- **Build Tool**: Maven
- **Containerization**: Docker Compose

### Android
- **Language**: Kotlin
- **Minimum SDK**: 21
- **Architecture**: MVVM
- **Dependencies**:
  - Firebase Cloud Messaging
  - STOMP over WebSocket (STOMP-Android)
  - Glide (image loading)
  - Gson (JSON parsing)
  - Material Design Components

## Setup Instructions (Local Execution)

Para garantir que o aplicativo Android e o backend se comuniquem corretamente em um ambiente local, siga estas etapas:

### 1. Configuração de Rede (Crucial para o APK)

O aplicativo Android precisa saber o endereço IP do seu computador para acessar a API.

1.  **Descubra seu IP Local**: No Windows, use `ipconfig`. No Mac/Linux, use `ifconfig` ou `ip addr`. Procure pelo endereço IPv4 (ex: `192.168.1.15`).
2.  **Atualize o arquivo `.env`**: Na raiz do projeto, edite o arquivo `.env` e altere a variável `API_BASE_URL`:
    *   Se estiver usando o **Emulador**: `API_BASE_URL=http://10.0.2.2:8080/`
    *   Se estiver usando um **Celular Real**: `API_BASE_URL=http://<SEU_IP_LOCAL>:8080/`
3.  **Mesma Rede**: Certifique-se de que seu celular e seu computador estejam conectados à **mesma rede Wi-Fi**.

### 2. Backend Setup

### Prerequisites
- JDK 17
- Maven 3.9+
- Docker & Docker Compose (for MongoDB)
- Android Studio (for client)
- Firebase project (for push notifications)

### Backend Setup

1. **Environment Configuration**
   ```bash
   cp .env.example .env
   # Edit .env with your Firebase/Google Cloud credentials
   ```

2. **MongoDB**
   ```bash
   # From wtc-crm-backend directory
   docker compose up -d
   ```

3. **Firebase Service Account**
   - Download `firebase-service-account.json` from Firebase Console
   - Place in `wtc-crm-backend/src/main/resources/`

4. **Run Application**
   ```bash
   cd wtc-crm-backend
   mvn spring-boot:run
   ```
   API available at: http://localhost:8080
   Swagger UI: http://localhost:8080/swagger-ui.html

### Android Setup

1. **Firebase Configuration**
   - Follow instructions in [FIREBASE_SETUP.md](FIREBASE_SETUP.md)
   - Add SHA-1 fingerprint to Firebase project
   - Download `google-services.json` and place in `wtc-login-main/app/`

2. **Build & Run**
   - Open project in Android Studio
   - Build and run on emulator/device

## API Endpoints Summary

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `PUT /api/auth/users/{id}/fcm-token` - Register FCM token

### Core Modules
- `GET/POST/PUT/DELETE /api/clients` - Client management
- `GET/POST/PUT/DELETE /api/segments` - Customer segmentation
- `GET/POST/PUT/DELETE /api/campaigns` - Marketing campaigns
- `GET/POST/PUT/DELETE /api/tasks` - Task management
- `GET/POST /api/profile` - User profile 360°
- `GET/POST /api/media` - File upload

### Chat
- `GET/POST /api/chat/conversations` - Conversation management
- `GET/POST /api/chat/conversations/{id}/messages` - Message handling
- WebSocket: `/ws/chat` (STOMP over SockJS)

## Development Progress

See [SPRINT2_CHECKLIST.md](SPRINT2_CHECKLIST.md) for detailed Sprint 2 objectives and completion status.

### Completed
- ✅ Backend Java 17 + Spring Boot with JWT, Swagger, and all CRM modules
- ✅ MongoDB persistence with all required collections
- ✅ Environment configuration via `.env` with DotenvBootstrap
- ✅ Firebase Admin SDK integration (conditional)
- ✅ Android BuildConfig integration with `.env` variables
- ✅ Real-time chat via WebSocket/STOMP
- ✅ FCM push notification backend implementation

### Pending
- ⏳ Refresh token endpoint (`POST /api/auth/refresh`)
- ⏳ Media upload to S3/MinIO (currently simulated)
- ⏳ Message status tracking (Sent/Delivered/Read/Failed)
- ⏳ Academic deliverables: ZIP packages, APK, PDF/PPT documentation
- ⏳ Security hardening: strong JWT_SECRET, credentials management

## Firebase Configuration

Refer to [FIREBASE_SETUP.md](FIREBASE_SETUP.md) for detailed setup instructions covering:
1. Firebase Console project setup (`wtc-chat-e9532`)
2. Android app configuration (`google-services.json`)
3. Service account for backend (`firebase-service-account.json`)
4. Cloud Messaging setup
5. SHA-1 fingerprint registration for Google Sign-In

## Important Notes

### Security
- Never commit `.env` or Firebase service account keys to version control
- Use strong JWT_SECRET in production environments
- Firebase credentials should be managed via CI/CD secrets or equivalent

### Database
- MongoDB collections are automatically created on first use
- Indexes may need to be added for production performance optimization

### Testing
- Test user credentials:
  - Email: `operator@wtc.com`
  - Password: `123456`
  - Role: `OPERATOR`

## License

Academic project developed for FIAP - Faculdade de Informática e Administração Paulista.

## Acknowledgments

- FIAP professors and mentors
- Firebase and Google Cloud platforms
- Spring Boot and MongoDB communities
- Android open-source libraries