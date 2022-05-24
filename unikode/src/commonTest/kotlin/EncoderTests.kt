/*
 * Any copyright is dedicated to the Public Domain.
 *
 * Copyright 2022 William Swartzendruber
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test

import org.unikode.Encoder

class EncoderTests {

    @Test
    fun nothing() {
        assertTrue(TestEncoder() scalarValuesEqual intArrayOf())
    }

    @Test
    fun single_char() {

        val callback = { _: Byte -> }
        val encoder = TestEncoder()

        encoder.inputChar(' ', callback)

        assertTrue(encoder scalarValuesEqual intArrayOf(0x20))
    }

    @Test
    fun two_single_chars() {

        val callback = { _: Byte -> }
        val encoder = TestEncoder()

        encoder.inputChar(0x20.toChar(), callback)
        encoder.inputChar(0x40.toChar(), callback)

        assertTrue(encoder scalarValuesEqual intArrayOf(0x20, 0x40))
    }

    @Test
    fun surrogate_pair() {

        val callback = { _: Byte -> }
        val encoder = TestEncoder()

        encoder.inputChar(0xD83D.toChar(), callback)
        encoder.inputChar(0xDE00.toChar(), callback)

        assertTrue(encoder scalarValuesEqual intArrayOf(0x1F600))
    }

    @Test
    fun low_surrogate() {

        val callback = { _: Byte -> }
        val encoder = TestEncoder()

        encoder.inputChar(0xDE00.toChar(), callback)

        assertTrue(encoder scalarValuesEqual intArrayOf(0xFFFD))
    }

    @Test
    fun high_surrogate_single_char() {

        val callback = { _: Byte -> }
        val encoder = TestEncoder()

        encoder.inputChar(0xD83D.toChar(), callback)
        encoder.inputChar(0x20.toChar(), callback)

        assertTrue(encoder scalarValuesEqual intArrayOf(0xFFFD, 0x20))
    }

    @Test
    fun high_surrogate_surrogate_pair() {

        val callback = { _: Byte -> }
        val encoder = TestEncoder()

        encoder.inputChar(0xD83C.toChar(), callback)
        encoder.inputChar(0xD83D.toChar(), callback)
        encoder.inputChar(0xDE00.toChar(), callback)

        assertTrue(encoder scalarValuesEqual intArrayOf(0xFFFD, 0x1F600))
    }

    @Test
    fun high_surrogate_reset_single_char() {

        val callback = { _: Byte -> }
        val encoder = TestEncoder()

        encoder.inputChar(0xD83D.toChar(), callback)
        encoder.reset()
        encoder.inputChar(' ', callback)

        assertTrue(encoder scalarValuesEqual intArrayOf(0x20))
    }

    @Test
    fun char_sequence_full() {

        val input = "Hello"
        val output = ByteArray(5)
        val encoder = TestEncoder()

        encoder.encode(input, output)

        assertTrue(encoder scalarValuesEqual intArrayOf(0x48, 0x65, 0x6C, 0x6C, 0x6F))
    }

    @Test
    fun char_sequence_slice() {

        val input = "Hello"
        val output = ByteArray(5)
        val encoder = TestEncoder()

        encoder.encode(input, output, 1, 4)

        assertTrue(encoder scalarValuesEqual intArrayOf(0x65, 0x6C, 0x6C))
    }

    @Test
    fun char_sequence_slice_offset() {

        val input = "Hello"
        val output = ByteArray(5)
        val encoder = TestEncoder()

        encoder.encode(input, output, 1, 4, 1)

        assertTrue(output contentEquals byteArrayOf(0x00, 0x01, 0x01, 0x01, 0x00))
    }

    @Test
    fun char_array_full() {

        val input = charArrayOf('H', 'e', 'l', 'l', 'o')
        val output = ByteArray(5)
        val encoder = TestEncoder()

        encoder.encode(input, output)

        assertTrue(encoder scalarValuesEqual intArrayOf(0x48, 0x65, 0x6C, 0x6C, 0x6F))
    }

    @Test
    fun char_array_slice() {

        val input = charArrayOf('H', 'e', 'l', 'l', 'o')
        val output = ByteArray(5)
        val encoder = TestEncoder()

        encoder.encode(input, output, 1, 4)

        assertTrue(encoder scalarValuesEqual intArrayOf(0x65, 0x6C, 0x6C))
    }

    @Test
    fun char_array_slice_offset() {

        val input = charArrayOf('H', 'e', 'l', 'l', 'o')
        val output = ByteArray(5)
        val encoder = TestEncoder()

        encoder.encode(input, output, 1, 4, 1)

        assertTrue(output contentEquals byteArrayOf(0x00, 0x01, 0x01, 0x01, 0x00))
    }

    @Test
    fun char_iterable() {

        val input = listOf<Char>('H', 'e', 'l', 'l', 'o')
        val output = ByteArray(5)
        val encoder = TestEncoder()

        encoder.encode(input, output)

        assertTrue(encoder scalarValuesEqual intArrayOf(0x48, 0x65, 0x6C, 0x6C, 0x6F))
    }

    @Test
    fun char_iterable_offset() {

        val input = listOf<Char>('e', 'l', 'l')
        val output = ByteArray(5)
        val encoder = TestEncoder()

        encoder.encode(input, output, 1)

        assertTrue(output contentEquals byteArrayOf(0x00, 0x01, 0x01, 0x01, 0x00))
    }

    class TestEncoder : Encoder() {

        val scalarValues = mutableListOf<Int>()

        override fun maxBytesNeeded(charCount: Int) = charCount

        protected override fun inputScalarValue(value: Int, callback: (Byte) -> Unit) {
            callback(0x01)
            scalarValues.add(value)
        }

        infix fun scalarValuesEqual(other: IntArray) =
            scalarValues.toIntArray() contentEquals other
    }
}
