# kotlin-ref-delegation
This is really small library (just 29 lines of code in [one file](src/main/kotlin/ru/nikitabobko/kotlin/refdelegation/RefDelegate.kt)) which allows you to use Kotlin's property 
delegation feature for accessing Java's [WeakReference](https://docs.oracle.com/javase/8/docs/api/java/lang/ref/WeakReference.html)
and [SoftReference](https://docs.oracle.com/javase/8/docs/api/java/lang/ref/SoftReference.html) classes.

# Simple example 1
```kotlin
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

```

# Simple example 2
```kotlin
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
```

# More examples?
Two simple examples above is all you need to know. You just have two 
functions: `weakRef` and `softRef` which are used for delegation.


# Usage
In Gradle projects add this in your `build.gradle`:
```gradle
// ...
repositories { 
    // ...
    maven {
        url 'https://dl.bintray.com/bobko/kotlin-ref-delegation'
    }
}
// ...
dependencies {
    // ...
    compile "ru.nikitabobko.kotlin.refdelegation:kotlin-ref-delegation:1.1.2"
}
```
