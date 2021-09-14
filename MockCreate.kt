package com.example.mockktest

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class TestMockClass(
    val param1: Int,
    val param2: String,
)

class TestSpyClass(
    val param1: Int,
    val param2: String,
)

object TestObject {
    fun test() { }
}

enum class TestEnum(val param: Int) {
    FIRST(10)
}

fun TestMockClass.testExtension() { }

fun testTopLevelFunction() { }

open class TestClassForInterface( // должени быть открытым
    val param: Int
)

interface TestInterface {
    fun testInterfaceFun()
}

class CreateMockTest {
    @MockK
    lateinit var testMockClass: TestMockClass

    @RelaxedMockK
    lateinit var testRelaxedMockClass: TestMockClass

    @SpyK
    var testSpyClass =  TestSpyClass(10, "10")

    @Before
    fun create() {
        MockKAnnotations.init(this)
    }

    @Test
    fun annotationMock() {
        every { testMockClass.param1 } returns 10 // mockk требует установить желаемое поведение
        assertEquals(10, testMockClass.param1)

        assertEquals(0, testRelaxedMockClass.param1)  // relaxedMockk возвращает моки своих полей
        assertEquals("", testRelaxedMockClass.param2) // или простые значения примитивовов

        assertEquals(10, testSpyClass.param1)  // spy копирует поля настоящего объекта
        every { testSpyClass.param1 } returns 0
        assertEquals(0, testSpyClass.param1)
    }

    @Test
    fun functionMock() {
        val mockk1 = mockk<TestMockClass>()
        val mockk2 = mockkClass(TestMockClass::class)
        every { mockk1.param1 } returns 10  // mockk требует установить желаемое поведение
        every { mockk2.param1 } returns 10
        assertEquals(10, mockk1.param1)
        assertEquals(10, mockk2.param1)

        val relaxedMockk = mockk<TestMockClass>(
            relaxed = true, // можно использовать relaxUnitFun для автоматического создания моков только для Unit-функций
        )
        assertEquals(0, relaxedMockk.param1)  // relaxedMockk возвращает моки своих полей
        assertEquals("", relaxedMockk.param2) // или простые значения примитивовов

        val spyMock = spyk(TestSpyClass(10, "10"))
        assertEquals(10, spyMock.param1)  // spy копирует поля настоящего объекта
        every { spyMock.param1 } returns 0
        assertEquals(0, spyMock.param1)
    }

    @Test
    fun objectMock() {
        mockkObject(TestEnum.FIRST)
        every { TestEnum.FIRST.param } returns -1
        assertEquals(-1, TestEnum.FIRST.param)
        unmockkObject(TestEnum.FIRST)

        mockkObject(TestObject)
        justRun { TestObject.test() } // эквивалент every { ... } returns Unit
        TestObject.test()
        verify { TestObject.test() } // проверка вызова
        unmockkObject(TestObject)
    }

    @Test
    fun extensionMock() {
        mockkStatic(TestMockClass::testExtension) // только для JVM
//        mockkStatic("com.example.mockktest.MockCreateKt") // эквивалент предыдущего вызова
        justRun { testMockClass.testExtension() } // эквивалент every { ... } returns Unit
        justRun { testTopLevelFunction() }
        testMockClass.testExtension()
        testTopLevelFunction()
        verify { testMockClass.testExtension() } // проверка вызова
        verify { testTopLevelFunction() }
        unmockkStatic(TestMockClass::testExtension) // только для JVM
//        unmockkStatic("com.example.mockktest.MockCreateKt") // эквивалент предыдущего вызова
    }

    @Test
    fun interfaceMock() {
        val mock = mockk<TestClassForInterface>(moreInterfaces = arrayOf(TestInterface::class))

        every { mock.param } returns 10 // mockk требует установить желаемое поведение
        justRun { (mock as TestInterface).testInterfaceFun() } // эквивалент every { ... } returns Unit
        (mock as TestInterface).testInterfaceFun()

        assertEquals(10, mock.param)
        verify { (mock as TestInterface).testInterfaceFun() } // проверка вызова
    }
}
