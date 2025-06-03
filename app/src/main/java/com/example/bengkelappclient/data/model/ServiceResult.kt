package com.example.bengkelappclient.data.model

/**
 * A sealed class representing the different states of a service operation result.
 * This helps in managing UI states (loading, success, error) in a structured way.
 *
 * @param T The type of data on success.
 */
sealed class ServiceResult<out T> {
    /**
     * Represents the loading state of an operation.
     */
    object Loading : ServiceResult<Nothing>()

    /**
     * Represents the success state of an operation with data.
     * @param data The successful result data.
     */
    data class Success<out T>(val data: T) : ServiceResult<T>()

    /**
     * Represents the error state of an operation.
     * @param exception The Throwable that caused the error.
     * @param message An optional error message.
     */
    data class Error(val exception: Throwable, val message: String? = null) : ServiceResult<Nothing>()
}
