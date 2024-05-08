package com.example.remote.retrofit.di

import android.content.Context
import coil.ImageLoader
import com.example.remote.retrofit.interceptor.AuthInterceptor
import com.example.remote.util.Config
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        prettyPrint
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesOkhttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            this.setLevel(HttpLoggingInterceptor.Level.BASIC)
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }).connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)  // ricky has bugs: prevents retrofit calls
        .build()

    /* retrofit */
    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        networkJson: Json
    ): Retrofit = Retrofit.Builder().baseUrl(Config.BASE_URL)
        .client(okHttpClient).addConverterFactory(
            networkJson.asConverterFactory(contentType = "application/json".toMediaType())
        ).build() // edit for also multipart data - edit request headers

    /* image loader */
    @Provides
    @Singleton
    fun providesImageLoader(
        @ApplicationContext
        context: Context,
        okHttpClient: OkHttpClient
    ): ImageLoader = ImageLoader.Builder(context).okHttpClient(okHttpClient)
//        .components {  }
        .respectCacheHeaders(enable = false)
        .build()
}