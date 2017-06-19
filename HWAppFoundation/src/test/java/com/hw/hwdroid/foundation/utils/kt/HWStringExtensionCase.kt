package com.hw.hwdroid.foundation.utils.kt

import org.junit.Test

/**
 * Created by ChenJ on 2017/6/19.
 */

class HWStringExtensionCase {

    @Test
    fun byteToHexString() {
        println("----")
        println("".isBlank())
        println(" ".isBlank())
        println("".isNullOrBlank())
        println("s ".isNullOrBlank())
        println(" s ".padEnd(1, 'X')+"-")

        println("----")
        println(String())
        println(String() == null)
        println(String() == "")
        println(String() == " ")
        println(String().isNullOrBlank())
        println(String().isNullOrEmpty())
        println(String().isEmpty())

    }

}
