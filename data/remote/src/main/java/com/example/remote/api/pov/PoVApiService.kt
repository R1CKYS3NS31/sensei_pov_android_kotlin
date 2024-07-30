package com.example.remote.api.pov

import com.example.remote.model.pov.NewPoVRemoteModel
import com.example.remote.model.pov.PoVRemoteModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PoVApiService {
    @POST(value = "povs/")
    suspend fun createPoV(
        @Body
        newPoVRemoteModel: NewPoVRemoteModel
    ): PoVRemoteModel

    @GET(value = "povs")
   suspend fun readPoVs(): List<PoVRemoteModel>

    @GET(value = "povs/{povId}")
   suspend fun readPoV(
        @Path(value = "povId")
        povId: String
    ): PoVRemoteModel

    @PUT(value = "povs/{povId}")
   suspend fun updatePoV(
        @Path(value = "povId")
        povId: String,
        @Body
        poVRemoteModel: PoVRemoteModel
    ): PoVRemoteModel

    @DELETE(value = "povs/{povId}")
    suspend fun deletePoV(
        @Path(value = "povId")
        povId: String
    ):PoVRemoteModel
}
