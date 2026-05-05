/**
 * Cliente HTTP singleton: Retrofit + Gson + OkHttp (log de requisições em debug).
 *
 * **Importante:** altere [BASE_URL] conforme o ambiente (emulador `10.0.2.2`, device físico = IP da máquina, produção = HTTPS).
 */
package br.com.fiap.wtchat.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Emulador Android -> localhost do seu PC: 10.0.2.2
    private const val BASE_URL = "http://10.0.2.2:8080/"

    /** Base URL do WebSocket STOMP (Spring `/ws/native`). */
    fun wsNativeUrl(): String {
        val b = BASE_URL.trimEnd('/')
        val ws = when {
            b.startsWith("https://") -> "wss://" + b.removePrefix("https://")
            b.startsWith("http://") -> "ws://" + b.removePrefix("http://")
            else -> b
        }
        return "$ws/ws/native"
    }

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: WtcApiService by lazy {
        retrofit.create(WtcApiService::class.java)
    }
}

