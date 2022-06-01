package com.example.historyvideo.base

class AppEvent<E, V> @JvmOverloads constructor(
    private var event: E, payload: String? = null
) {
    var payload: String? = payload
        private set

    private var hasBeenHandled = false

    val eventIfNotHandled: E?
        get() = if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            event
        }

    fun peekContent(): E = event

    companion object {
        const val API_ERROR = "API_ERROR"
        const val LOCAL_ERROR = "LOCAL_ERROR"
        const val INTERNET_ERROR = "INTERNET_ERROR"
        const val SHOW_ERROR_DIALOG = "SHOW_ERROR_DIALOG"
        const val SHOW_TITLED_ERROR_DIALOG = "SHOW_TITLED_ERROR_DIALOG"
    }
}
