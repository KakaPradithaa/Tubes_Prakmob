package com.example.bengkelappclient.util

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Extension function to convert a String to an OkHttp RequestBody.
 * This is useful for sending plain text parts in a multipart request.
 * It defaults to "text/plain" media type.
 */
fun String.toRequestBody(): RequestBody {
    return this.toRequestBody("text/plain".toMediaTypeOrNull())
}
