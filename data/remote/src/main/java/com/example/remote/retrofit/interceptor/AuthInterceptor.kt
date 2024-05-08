package com.example.remote.retrofit.interceptor

import com.example.datastore.UserAccountPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userAccountPreferencesRepository: UserAccountPreferencesRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val tokenString = runBlocking {
            userAccountPreferencesRepository.getToken().first()
        }
        return if (tokenString.isNullOrEmpty()) {
            chain.proceed(originalRequest)
        } else {
            val authenticatedRequest: Request =
                originalRequest.newBuilder().header("Authorization", "Bearer ${tokenString}")
                    .build()
            chain.proceed(authenticatedRequest)
        }
    }

}