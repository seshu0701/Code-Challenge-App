package com.example.codechallengeex.data.network

/**
 * Created by Satya Seshu on 10/04/22.
 */
data class NetworkState constructor(
    val state: Status,
    val message: String? = null
) {

    companion object {
        val LOADED =
            NetworkState(Status.SUCCESS)
        val LOADING =
            NetworkState(
                Status.RUNNING,
                "Loading..."
            )

        fun errorEmpty(emptyMessage: String?) =
            NetworkState(
                Status.EMPTY,
                emptyMessage
            )

        fun error(msg: String?) = NetworkState(
            Status.FAILED,
            msg
        )
    }

    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED,
        EMPTY
    }
}