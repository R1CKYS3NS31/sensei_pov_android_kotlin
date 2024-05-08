package com.example.remote.api.account

import com.example.remote.model.account.AuthResponseModel
import com.example.remote.model.account.UserAccountRemoteModel
import com.example.remote.model.account.UserAccountSignInRemoteModel
import com.example.remote.model.account.UserAccountSignUpRemoteModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserAccountApiService {
    @POST(value = "auth/signup")
    suspend fun signup(
        @Body
        userAccountSignUp: UserAccountSignUpRemoteModel
    ): AuthResponseModel

    @POST(value = "auth/signin")
    suspend fun signIn(
        @Body
        userAccountSignIn: UserAccountSignInRemoteModel
    ): AuthResponseModel

    @GET(value = "auth/account")
    suspend fun getUserAccount(): UserAccountRemoteModel

    @PUT(value = "auth/account")
    suspend fun updateUserAccount(
//        @Path(value = "userId")
//        userId: String,
        @Body
        userAccountRemoteModel: UserAccountRemoteModel
    ): UserAccountRemoteModel

    @DELETE(value = "auth/account")
    suspend fun deleteUserAccount()

    @GET(value = "auth/signout")
    suspend fun signOut()
}