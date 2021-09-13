package com.example.mockktest

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TestClass {
    fun testIntFun() = 10
    fun testUnitFun() {}
    fun testThrowFun() = "never used"
    fun testMultipleReturn() = "never used"
}

class TestClassWithPrivateField {
    private var privateField: Int = 10
        private set(value) {
            field = value
        } // требуется геттер и сеттер
        get() = field

    fun getPrivateFieldValue() = privateField // вызываем геттер
    fun setPrivateFieldValue(param: Int) {
        privateField = param
    } // вызываем сеттер

    private fun privateFun() = 10
    fun getPrivateFunValue() = privateFun()
}

class TestConstructor(
    val param1: Int,
    val param2: String?
) {
    constructor(param: Int) : this(param, null)
    constructor() : this(0, null)
}

class MockBehavior {
    @MockK
    lateinit var testClass: TestClass

    @SpyK
    var testPrivateClass = TestClassWithPrivateField()

    @Before
    fun create() {
        MockKAnnotations.init(this)
    }

    @Test
    fun publicBehavior() {
        every { testClass.testIntFun() } returns 1
        justRun { testClass.testUnitFun() }
        every { testClass.testThrowFun() } throws Exception("123")
        every { testClass.testMultipleReturn() } returnsMany listOf("1", "2")

        assertEquals(1, testClass.testIntFun())

        assertThrows(Exception::class.java) { testClass.testThrowFun() }.also {
            assertTrue(it.message?.contains("123") ?: false)
        }

        testClass.testUnitFun()
        verify { testClass.testUnitFun() }

        assertEquals("1", testClass.testMultipleReturn())
        assertEquals("2", testClass.testMultipleReturn())
    }

    @Test
    fun privateBehavior() {
        every { testPrivateClass invoke "privateFun" withArguments listOf() } returns 30
//        every { testPrivateClass["privateFun"]() } returns 30 // эквивалент предыдущей строки

        assertEquals(30, testPrivateClass.getPrivateFunValue())
        verify { testPrivateClass["privateFun"]() }

        every { testPrivateClass getProperty "privateField" } propertyType Int::class answers {
            fieldValue = 55; fieldValue
        }
        every { testPrivateClass setProperty "privateField" value any<Int>() } propertyType Int::class answers {
            fieldValue = 11
        }

        assertEquals(55, testPrivateClass.getPrivateFieldValue())
    }

    @Test
    fun constructorMock() {
        mockkConstructor(TestConstructor::class)

        every { constructedWith<TestConstructor>().param1 } returns 1
        every { constructedWith<TestConstructor>(EqMatcher(1)).param1 } returns 2
        every { constructedWith<TestConstructor>(EqMatcher(1), EqMatcher("1")).param1 } returns 3

        assertEquals(1, TestConstructor().param1)
        assertEquals(2, TestConstructor(1).param1)
        assertEquals(3, TestConstructor(1, "1").param1)

        unmockkConstructor(TestConstructor::class)
    }
}