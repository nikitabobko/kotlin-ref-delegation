import ru.nikitabobko.kotlin.refdelegation.softRef
import ru.nikitabobko.kotlin.refdelegation.weakRef
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

var curId = 0

class SomeClass {
    val id = curId++
}

private const val CLEAN_GC_ATTEMPTS_NUMBER = 4

fun eatAllMemory() = LongArray(Int.MAX_VALUE / 10) { it.toLong() }

class Tests {
    @Test
    fun weakRefTest1() {
        val a by weakRef(::SomeClass)
        val id = a.id
        repeat(CLEAN_GC_ATTEMPTS_NUMBER) { System.gc() }
        assertEquals(id + 1, a.id)
    }

    @Test
    fun weakRefTest2() {
        val a by weakRef(::SomeClass)
        @Suppress("UNUSED_VARIABLE")
        val strongRef = a
        val id = a.id
        repeat(CLEAN_GC_ATTEMPTS_NUMBER) { System.gc() }
        assertEquals(id, a.id)
    }

    @Test
    fun softRefTest1() {
        val a by softRef(::SomeClass)
        val id = a.id
        repeat(CLEAN_GC_ATTEMPTS_NUMBER) { System.gc() }
        assertEquals(id, a.id)
    }

    @Test
    fun softRefTest2() {
        val a by softRef(::SomeClass)
        val id = a.id
        try {
            eatAllMemory()
            fail(message = "Not eat all memory :(")
        } catch (ex: OutOfMemoryError) { }
        assertEquals(id + 1, a.id)
    }
}
