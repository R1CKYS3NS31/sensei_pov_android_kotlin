package com.example.remote.api.user

import com.example.remote.model.user.UserRemoteModel
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {
    @GET(value="users/{userId}")
    suspend fun getUser(
        @Path(value = "userId")
        userId: String
    ): UserRemoteModel

    @GET(value = "users")
    suspend fun getAllUsers(): List<UserRemoteModel>
}