package com.rowatk.waje.utills

import org.slf4j.Logger
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject

interface Logging {
}

fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
    return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
    } ?: javaClass
}

fun <T : Logging> T.logger(): Logger = getLogger(javaClass)

class LoggerDelegate<in R : Any> : ReadOnlyProperty<R, Logger> {
    override fun getValue(thisRef: R, property: KProperty<*>)
            = getLogger(getClassForLogging(thisRef.javaClass))
}
