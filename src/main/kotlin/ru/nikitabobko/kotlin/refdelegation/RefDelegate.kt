package ru.nikitabobko.kotlin.refdelegation

import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface RefDelegate<R : Reference<T>, T> : ReadWriteProperty<Any?, T> {
    val ref: R
}

private class SynchronizedRefDelegate<R : Reference<T>, T>(private val initRef: (T) -> R,
                                                           private val init: () -> T) : RefDelegate<R, T> {
    override var ref: R = initRef(init())
        private set

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return ref.get() ?: synchronized(this) { ref.get() ?: init().also { ref = initRef(it) } }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = synchronized(this) {
        ref = initRef(value)
    }
}

fun <T> weakRef(init: () -> T): RefDelegate<WeakReference<T>, T> = SynchronizedRefDelegate(::WeakReference, init)

fun <T> softRef(init: () -> T): RefDelegate<SoftReference<T>, T> = SynchronizedRefDelegate(::SoftReference, init)
