package examples

import ru.nikitabobko.kotlin.refdelegation.softRef

fun eatAllMemory() = LongArray(Int.MAX_VALUE / 10) { it.toLong() }

fun main(args: Array<String>) {
    // a variable is SoftReference to SomeClass object
    val a by softRef { SomeClass() }

    println(a.id)  // prints: "0"

    // Invoke Java's garbage collector
    System.gc()

    println(a.id)  // still prints: "0" because we have enough memory

    try {
        eatAllMemory()
    } catch (ex: OutOfMemoryError) {
        println("Now we have lack of memory so Java's gc destroyed our object")
    }

    println(a.id)  // prints: "1" It's new object!
}
