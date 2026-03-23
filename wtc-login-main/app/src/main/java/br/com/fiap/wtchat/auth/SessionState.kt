/**
 * Sessão e singleton de armazenamento em memória para o token JWT do CRM.
 */
package br.com.fiap.wtchat.auth

/**
 * Dados retornados pelo login que precisam ser reutilizados nas chamadas à API.
 */
data class SessionState(
    val token: String,
    val userId: String,
    val role: String,
    val name: String
)

/**
 * Ponto central para ler o JWT após login; usado por [br.com.fiap.wtchat.viewmodel.ChatViewModel] e telas autenticadas.
 */
object SessionManager {
    @Volatile
    var session: SessionState? = null
        private set

    /** Salva token e identidade após login bem-sucedido. */
    fun setSession(newSession: SessionState) {
        session = newSession
    }

    /** Limpa sessão (ex.: logout futuro). */
    fun clear() {
        session = null
    }
}

