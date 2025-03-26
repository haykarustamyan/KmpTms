package com.mobiclocks.kiosk
import dev.icerock.moko.resources.StringResource

expect object Platform {
    val isAndroid: Boolean
    val isIOS: Boolean
}


expect class Strings {
    fun get(id: StringResource, args: List<Any>): String
}