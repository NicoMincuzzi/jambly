package com.lefc.jambly

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CheckClassTest {
    @Test
    fun checkInvalidSequenceWithThreeModifiers() {
        val result = CheckClass().checkModifiers(listOf("private", "public", "static"))
        assertEquals(true, result)
    }

    @Test
    fun checkInvalidSequenceWithTwoModifiers() {
        val result = CheckClass().checkModifiers(listOf("private", "public"))
        assertEquals(true, result)
    }

    @Test
    fun checkValidModifiersWithPrivateAndStatic() {
        val result = CheckClass().checkModifiers(listOf("private", "static"))
        assertEquals(false, result)
    }

    @Test
    fun checkValidModifiersWithPublicAndStatic() {
        val result = CheckClass().checkModifiers(listOf("private", "static"))
        assertEquals(false, result)
    }
}