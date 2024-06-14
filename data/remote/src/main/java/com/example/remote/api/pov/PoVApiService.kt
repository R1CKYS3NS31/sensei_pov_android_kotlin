package com.example.remote.api.pov

import com.example.remote.model.pov.PoVRemoteModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PoVApiService {
    @POST(value = "povs/")
    suspend fun createPoV(poVRemoteModel: PoVRemoteModel): PoVRemoteModel

    @GET(value = "povs")
    fun readPoVs():List<PoVRemoteModel>

    @GET(value = "povs/{povId}")
    fun readPoV(
        @Path(value = "povId")
        povId:String
    ):PoVRemoteModel


}
