package br.com.fiap.wtchat.api.dto

data class TaskDTO(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val clientId: String? = null,
    val operatorId: String? = null,
    val status: String? = null,
    val priority: Int = 1,
    val dueDate: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
