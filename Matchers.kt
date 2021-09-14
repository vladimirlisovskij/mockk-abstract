package com.example.mockktest

import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TestMatcherClass(
    val param1: Int,
) {
    fun testMatherFun(
        param1: Int,
    ) = "never used"
}

fun MockKMatcherScope.customMatch() = match<Int> {
    it == 20
}

class CustomMatcher: Matcher<Int> {
    override fun match(arg: Int?): Boolean {
        return arg?.let { it == 30 } ?: false
    }
}

class Matchers {
    @MockK
    lateinit var testMatcherClass: TestMatcherClass

    @Before
    fun create() {
        MockKAnnotations.init(this)
    }

    @Test
    fun method() {
        every { testMatcherClass.testMatherFun(any()) } returns "1"
        every { testMatcherClass.testMatherFun(eq(1)) } returns "2"
        every { testMatcherClass.testMatherFun(match { it == 10 }) } returns "3"
        every { testMatcherClass.testMatherFun(customMatch()) } returns "4"
        every { testMatcherClass.testMatherFun(match(CustomMatcher())) } returns "5"

        assertEquals("1", testMatcherClass.testMatherFun(-1))
        assertEquals("2", testMatcherClass.testMatherFun(1))
        assertEquals("3", testMatcherClass.testMatherFun(10))
        assertEquals("4", testMatcherClass.testMatherFun(20))
        assertEquals("5", testMatcherClass.testMatherFun(30))
    }

    @Test
    fun constructor() {
        mockkConstructor(TestMatcherClass::class)

        every { constructedWith<TestMatcherClass>(CustomMatcher()).param1 } returns 1
        every { constructedWith<TestMatcherClass>(EqMatcher(1)).param1 } returns 2

        assertEquals(1, TestMatcherClass(30).param1)
        assertEquals(2, TestMatcherClass(1).param1)

        unmockkConstructor(TestMatcherClass::class)
    }
}