package com.example.data.common.result

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException

const val TAG = "PoVResult"

sealed interface PoVResult<out T> {
    @Serializable
    data class Success<T>(val data: T) : PoVResult<T>
    data class Error(
        val throwable: Throwable? = null,
        val responseErrorMessage: ErrorResponse? = null
    ) : PoVResult<Nothing>

    data object Loading : PoVResult<Nothing>
}

@Serializable
data class ErrorResponse(
    val errorCode: Int = -1,
    @SerialName("error")
    val errorMessage: String? = null
)

fun Throwable.asPoVError(): PoVResult.Error {
    return when (val cause = this) {
        /* api endpoint connection error */
        is ConnectException -> {
            PoVResult.Error(
                throwable = cause,
                responseErrorMessage = ErrorResponse(errorMessage = cause.localizedMessage)
            )
        }
        /* network errors */
        is IOException -> {
            Log.e(TAG, cause.message, cause)
            PoVResult.Error(
                throwable = cause,
                responseErrorMessage = ErrorResponse(errorMessage = cause.localizedMessage)
            )
        }
        /* http errors */
        is HttpException -> {
            val errorCode = cause.code()
            val errorBody =
                cause.response()?.errorBody()?.string() // error body/message from server
            val message = cause.response()?.message().toString() // http status
            val errorMessage = try {
                Json.decodeFromString<ErrorResponse>(errorBody.orEmpty())
            } catch (throwable: Throwable) {
                ErrorResponse(errorCode, message)
            }
            Log.e(TAG, cause.response()?.errorBody()?.string().orEmpty(), cause)
            PoVResult.Error(
                throwable = cause,
                responseErrorMessage = ErrorResponse(errorMessage = errorMessage.errorMessage)
            )
        }
        /* unknown error */
        else -> {
            Log.e(TAG, cause.message, cause)
            PoVResult.Error(
                throwable = cause,
                responseErrorMessage = ErrorResponse(errorMessage = cause.localizedMessage)
            )
        }
    }
}

fun <T> Flow<T>.asPoVResult(): Flow<PoVResult<T>> {
    return this
        .map<T, PoVResult<T>> {
            Log.d(TAG, "successful results")
            PoVResult.Success(it)
        }
        .onStart {
            Log.d(TAG, "Loading...")
            emit(PoVResult.Loading)
        }.catch { cause ->
            cause.asPoVError()
        }
}