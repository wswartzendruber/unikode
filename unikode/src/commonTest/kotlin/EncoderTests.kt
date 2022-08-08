/*
 * Copyright 2022 William Swartzendruber
 *
 * To the extent possible under law, the person who associated CC0 with this file has waived all
 * copyright and related or neighboring rights to this file.
 *
 * You should have received a copy of the CC0 legalcode along with this work. If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
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
        assertTrue(TestEncoder({ _: Byte -> }) scalarValuesEqual intArrayOf())
    }

    @Test
    fun single_char() {

        val encoder = TestEncoder({ _: Byte -> })

        encoder.input(' ')

        assertTrue(encoder scalarValuesEqual intArrayOf(0x20))
    }

    @Test
    fun two_single_chars() {

        val encoder = TestEncoder({ _: Byte -> })

        encoder.input(0x20.toChar())
        encoder.input(0x40.toChar())

        assertTrue(encoder scalarValuesEqual intArrayOf(0x20, 0x40))
    }

    @Test
    fun surrogate_pair() {

        val encoder = TestEncoder({ _: Byte -> })

        encoder.input(0xD83D.toChar())
        encoder.input(0xDE00.toChar())

        assertTrue(encoder scalarValuesEqual intArrayOf(0x1F600))
    }

    @Test
    fun low_surrogate() {

        val encoder = TestEncoder({ _: Byte -> })

        encoder.input(0xDE00.toChar())

        assertTrue(encoder scalarValuesEqual intArrayOf(0xFFFD))
    }

    @Test
    fun high_surrogate_single_char() {

        val encoder = TestEncoder({ _: Byte -> })

        encoder.input(0xD83D.toChar())
        encoder.input(0x20.toChar())

        assertTrue(encoder scalarValuesEqual intArrayOf(0xFFFD, 0x20))
    }

    @Test
    fun high_surrogate_surrogate_pair() {

        val encoder = TestEncoder({ _: Byte -> })

        encoder.input(0xD83C.toChar())
        encoder.input(0xD83D.toChar())
        encoder.input(0xDE00.toChar())

        assertTrue(encoder scalarValuesEqual intArrayOf(0xFFFD, 0x1F600))
    }

    @Test
    fun high_surrogate_reset_single_char() {

        val encoder = TestEncoder({ _: Byte -> })

        encoder.input(0xD83D.toChar())
        encoder.reset()
        encoder.input(' ')

        assertTrue(encoder scalarValuesEqual intArrayOf(0x20))
    }

    class TestEncoder(callback: (Byte) -> Unit) : Encoder(callback) {

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
