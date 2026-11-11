package me.crivos.app.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

@Serializable
data class SyncPayload(
    val userId: String,
    val stateJson: String,
    val updatedAt: Long
)

@Serializable
data class SyncResponse(
    val success: Boolean,
    val syncedAt: Long
)

interface CrivosApiService {
    @POST("api/sync")
    suspend fun pushSync(
        @Header("Authorization") token: String,
        @Body payload: SyncPayload
    ): SyncResponse

    @GET("api/sync")
    suspend fun fetchRemote(
        @Header("Authorization") token: String
    ): SyncPayload
}
