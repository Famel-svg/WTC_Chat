package br.com.fiap.wtchat.auth

data class SessionState(
    val token: String,
    val userId: String,
    val role: String,
    val name: String
)

object SessionManager {
    @Volatile
    var session: SessionState? = null
        private set

    fun setSession(newSession: SessionState) {
        session = newSession
    }

    fun clear() {
        session = null
    }
}

