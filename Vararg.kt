package com.example.mockktest

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TestVarargClass {
    fun testVarargFun(vararg x: Int) = "never used"
}

class Varargs {
    @MockK
    lateinit var testVarargs: TestVarargClass

    @Before
    fun create() {
        MockKAnnotations.init(this)
    }

    @Test
    fun varargs() {
        every { testVarargs.testVarargFun(1, *anyIntVararg(), 1) } returns "1"
        every { testVarargs.testVarargFun(2, *varargAnyInt { it > 4 }, 2) } returns "2"
        every { testVarargs.testVarargFun(3, *varargAllInt { it == position }, 3) } returns "3"

        assertEquals("1", testVarargs.testVarargFun(1, 3, 4, 5, 1, 1))
        assertEquals("2", testVarargs.testVarargFun(2, 3, 4, 5, 1, 2))
        assertEquals("3", testVarargs.testVarargFun(3, 1, 2, 3, 4, 3))
    }
}