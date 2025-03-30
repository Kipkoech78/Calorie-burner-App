package com.fitnessapp.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object EventBus {
    private val _event = Channel<Any> ()
    val events = _event.receiveAsFlow()

    suspend fun sendEvent(event: Any){
        _event.send(event)
    }
}

sealed interface Event{
    data class Toast(val message: String): Event
}