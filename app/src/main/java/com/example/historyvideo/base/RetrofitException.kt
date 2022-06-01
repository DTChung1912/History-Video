package com.example.historyvideo.base

import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.RuntimeException
import retrofit2.Response
import retrofit2.Retrofit

class RetrofitException internal constructor(
    message: String?,
    /**
     * The request URL which produced the error.
     */
    val url: String?,
    /**
     * Response object containing status code, headers, body, etc.
     */
    val response: Response<*>?,
    /**
     * The event kind which triggered this error.
     */
    val kind: Kind, exception: Throwable?,
    /**
     * The Retrofit this request was executed on
     */
    val retrofit: Retrofit?
) : RuntimeException(message, exception) {

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>?): T? {
        if (response == null || response.errorBody() == null) {
            return null
        }
        val converter: Converter<ResponseBody?, T> =
            retrofit!!.responseBodyConverter(type, arrayOfNulls(0))
        return converter.convert(response.errorBody())
    }

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     *
     * responseBodyの変換でエラーが発生した場合はnullを返す
     */
    fun <T> getErrorBodyAsOrNull(type: Class<T>?): T? {
        return try {
            getErrorBodyAs(type)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Identifies the event kind which triggered a [RetrofitException].
     */
    enum class Kind {
        /**
         * An [IOException] occurred while communicating to the server.
         */
        NETWORK,

        /**
         * A non-200 HTTP mStatus code was received from the server.
         */
        HTTP,

        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    companion object {
        @JvmStatic
        fun httpError(url: String?, response: Response<*>, retrofit: Retrofit?): RetrofitException {
            val message = response.code().toString() + " " + response.message()
            return RetrofitException(message, url, response, Kind.HTTP, null, retrofit)
        }

        @JvmStatic
        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(
                exception.message, null, null, Kind.NETWORK, exception,
                null
            )
        }

        @JvmStatic
        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(
                exception.message, null, null, Kind.UNEXPECTED, exception,
                null
            )
        }
    }
}