package me.crivos.app.data.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Serializable
data class SupabaseAuthRequest(
    val email: String,
    val password: String
)

@Serializable
data class SupabaseUser(
    val id: String,
    val email: String? = null
)

@Serializable
data class SupabaseSession(
    val access_token: String,
    val token_type: String,
    val user: SupabaseUser
)

@Serializable
data class UserProgressPayload(
    val user_id: String,
    val panel_id: String,
    val state_data: JsonObject? = null,
    val last_tab: String = "home",
    val updated_at: String
)

class SupabaseRepository(
    private val supabaseUrl: String = "https://dmmtmfjlieqeqwlhtgrr.supabase.co",
    private val supabaseAnonKey: String = "sb_publishable_pRdjFhU4VWrjVOOSUcNB5Q_9ZnJwFNw"
) {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }

    var currentSession: SupabaseSession? = null
        private set

    val isAuthenticated: Boolean
        get() = currentSession != null

    suspend fun loginWithPassword(email: String, pass: String): Result<SupabaseSession> = withContext(Dispatchers.IO) {
        try {
            val bodyJson = json.encodeToString(SupabaseAuthRequest.serializer(), SupabaseAuthRequest(email, pass))
            val req = Request.Builder()
                .url("$supabaseUrl/auth/v1/token?grant_type=password")
                .addHeader("apikey", supabaseAnonKey)
                .addHeader("Content-Type", "application/json")
                .post(bodyJson.toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(req).execute().use { response ->
                val bodyStr = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("Falha no login (${response.code}): $bodyStr"))
                }
                val session = json.decodeFromString(SupabaseSession.serializer(), bodyStr)
                currentSession = session
                Result.success(session)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchUserProgress(panelId: String): Result<String> = withContext(Dispatchers.IO) {
        val session = currentSession ?: return@withContext Result.failure(Exception("Usuário não autenticado"))
        try {
            val req = Request.Builder()
                .url("$supabaseUrl/rest/v1/user_progress?user_id=eq.${session.user.id}&panel_id=eq.$panelId&order=updated_at.desc&limit=1")
                .addHeader("apikey", supabaseAnonKey)
                .addHeader("Authorization", "Bearer ${session.access_token}")
                .get()
                .build()

            client.newCall(req).execute().use { response ->
                val bodyStr = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("Erro ao buscar progresso: $bodyStr"))
                }
                Result.success(bodyStr)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveUserProgress(progress: UserProgressPayload): Result<Boolean> = withContext(Dispatchers.IO) {
        val session = currentSession ?: return@withContext Result.failure(Exception("Usuário não autenticado"))
        try {
            val bodyJson = json.encodeToString(UserProgressPayload.serializer(), progress)
            val req = Request.Builder()
                .url("$supabaseUrl/rest/v1/user_progress")
                .addHeader("apikey", supabaseAnonKey)
                .addHeader("Authorization", "Bearer ${session.access_token}")
                .addHeader("Prefer", "resolution=merge-duplicates")
                .post(bodyJson.toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(req).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("Erro ao salvar progresso (${response.code})"))
                }
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        currentSession = null
    }
}
