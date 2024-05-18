package com.example.data.common.dispatcher

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

enum class PoVDispatchers {
    Default,
    IO
}

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(
    val povDispatchers: PoVDispatchers
)