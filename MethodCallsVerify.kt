package com.example.mockktest

import io.mockk.*
import io.mockk.impl.annotations.SpyK
import org.junit.Before
import org.junit.Test

class TestMethodVerifyClass {
    fun test(a: Int) = a + a
}

class MethodCallsVerify {
    @SpyK
    var testMethodVerifyClass = TestMethodVerifyClass()

    @Before
    fun create() {
        MockKAnnotations.init(this)
    }

    @Test
    fun defaultVerify() {
        for (i in 0..5) testMethodVerifyClass.test(i)
        for (i in 0..5) testMethodVerifyClass.test(i)

        verify(ordering = Ordering.SEQUENCE) {
            for (i in 0..5) testMethodVerifyClass.test(i)
            for (i in 0..5) testMethodVerifyClass.test(i)
        }

        verify(ordering = Ordering.ORDERED) {
            testMethodVerifyClass.test(0)
            testMethodVerifyClass.test(2)
            testMethodVerifyClass.test(5)
        }

        verify(ordering = Ordering.ALL)  {
            for (i in 0..5) testMethodVerifyClass.test(i)
        }

        verify {
            testMethodVerifyClass.test(5)
            testMethodVerifyClass.test(2)
            testMethodVerifyClass.test(0)
        }
    }

    @Test
    fun specificVerify() {
        for (i in 0..5) testMethodVerifyClass.test(i)
        for (i in 0..5) testMethodVerifyClass.test(i)

        verifySequence {
            for (i in 0..5) testMethodVerifyClass.test(i)
            for (i in 0..5) testMethodVerifyClass.test(i)
        }

        verifyOrder {
            testMethodVerifyClass.test(0)
            testMethodVerifyClass.test(2)
            testMethodVerifyClass.test(5)
        }

        verifyAll  {
            for (i in 0..5) testMethodVerifyClass.test(i)
        }
    }

    @Test
    fun verifyParams() {
        for (i in 0..5) testMethodVerifyClass.test(i)

        verify(atLeast = 3) { testMethodVerifyClass.test(less(3))}
        verify(atMost = 2) { testMethodVerifyClass.test(more(3))}
        verify(exactly = 1) {testMethodVerifyClass.test(eq(3))}
    }

    @Test
    fun wasNotCalled() {
        val mock = mockk<TestMethodVerifyClass>()

        verify {
            mock wasNot Called
        }
    }

    @Test
    fun excludeRecords() {
        for (i in 0..5) testMethodVerifyClass.test(i)

        excludeRecords {
            testMethodVerifyClass.test(0)
            testMethodVerifyClass.test(3)
        }

        verifyAll {
            testMethodVerifyClass.test(1)
            testMethodVerifyClass.test(2)
            testMethodVerifyClass.test(4)
            testMethodVerifyClass.test(5)
        }
    }
}