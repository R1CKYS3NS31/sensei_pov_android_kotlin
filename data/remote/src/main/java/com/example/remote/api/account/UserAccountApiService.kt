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
    @POST(value = "account/signup")
    suspend fun signup(
        @Body
        userAccountSignUp: UserAccountSignUpRemoteModel
    ): AuthResponseModel

    @POST(value = "account/signin")
    suspend fun signIn(
        @Body
        userAccountSignIn: UserAccountSignInRemoteModel
    ): AuthResponseModel

    @GET(value = "account/account")
    suspend fun getUserAccount(): UserAccountRemoteModel

    @PUT(value = "account/account")
    suspend fun updateUserAccount(
//        @Path(value = "userId")
//        userId: String,
        @Body
        userAccountRemoteModel: UserAccountRemoteModel
    ): UserAccountRemoteModel

    @DELETE(value = "account/account")
    suspend fun deleteUserAccount()

    @GET(value = "account/signout")
    suspend fun signOut()
}