package examples

import ru.nikitabobko.kotlin.refdelegation.weakRef

var curId = 0
class SomeClass {
    val id = curId++
}

fun main(args: Array<String>) {
    // a variable is WeakReference to SomeClass object
    val a by weakRef { SomeClass() }

    println(a.id)  // prints: "0"

    // Invoke Java's garbage collector
    System.gc()

    println(a.id)  // prints: "1"
}
