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

import org.unikode.toStringUtf8
import org.unikode.Utf8Decoder

class Utf8DecoderTests {

    @Test
    fun reject_overlongs_two_bytes() {

        val range = 0x00..0x7F
        val result = mutableListOf<Char>()
        val callback = { char: Char ->
            result.add(char)
            Unit
        }
        val decoder = Utf8Decoder(callback)
        val buffer = ByteArray(2)

        for (value in range) {
            two_byte_value(buffer, value)
            decoder.input(buffer[0])
            decoder.input(buffer[1])
        }

        assertEquals(range.count(), result.size)
        assertTrue(result.all { char: Char -> char == '�' })
    }

    @Test
    fun reject_overlongs_three_bytes() {

        val range = 0x000..0x7FF
        val result = mutableListOf<Char>()
        val callback = { char: Char ->
            result.add(char)
            Unit
        }
        val decoder = Utf8Decoder(callback)
        val buffer = ByteArray(3)

        for (value in range) {
            three_byte_value(buffer, value)
            decoder.input(buffer[0])
            decoder.input(buffer[1])
            decoder.input(buffer[2])
        }

        assertEquals(range.count(), result.size)
        assertTrue(result.all { char: Char -> char == '�' })
    }

    @Test
    fun reject_overlongs_four_bytes() {

        val range = 0x0000..0xFFFF
        val result = mutableListOf<Char>()
        val callback = { char: Char ->
            result.add(char)
            Unit
        }
        val decoder = Utf8Decoder(callback)
        val buffer = ByteArray(4)

        for (value in range) {
            four_byte_value(buffer, value)
            decoder.input(buffer[0])
            decoder.input(buffer[1])
            decoder.input(buffer[2])
            decoder.input(buffer[3])
        }

        assertEquals(range.count(), result.size)
        assertTrue(result.all { char: Char -> char == '�' })
    }

    @Test
    fun reject_initial_continuation_byte() {

        val input = byteArrayOf(-0x80)
        val output = input.toStringUtf8()

        assertEquals("�", output)
    }

    @Test
    fun reject_incomplete_sequence_1() {

        val input = byteArrayOf(-0x13, 0x20)
        val output = input.toStringUtf8()

        assertEquals("� ", output)
    }

    @Test
    fun reject_incomplete_sequence_2() {

        val input = byteArrayOf(-0x13, -0x6B, 0x20)
        val output = input.toStringUtf8()

        assertEquals("� ", output)
    }

    @Test
    fun flush_pending_sequence_1() {

        val input = byteArrayOf(-0x13)
        val output = input.toStringUtf8()

        assertEquals("�", output)
    }

    @Test
    fun flush_pending_sequence_2() {

        val input = byteArrayOf(-0x13, -0x6B)
        val output = input.toStringUtf8()

        assertEquals("�", output)
    }

    companion object {

        fun two_byte_value(destination: ByteArray, value: Int) {
            destination[0] = (0xC0 or (value ushr 6)).toByte()
            destination[1] = (0x80 or (value and 0x3F)).toByte()
        }

        fun three_byte_value(destination: ByteArray, value: Int) {
            destination[0] = (0xE0 or (value ushr 12)).toByte()
            destination[1] = (0x80 or (value ushr 6 and 0x3F)).toByte()
            destination[2] = (0x80 or (value and 0x3F)).toByte()
        }

        fun four_byte_value(destination: ByteArray, value: Int) {
            destination[0] = (0xF0 or (value ushr 18)).toByte()
            destination[1] = (0x80 or (value ushr 12 and 0x3F)).toByte()
            destination[2] = (0x80 or (value ushr 6 and 0x3F)).toByte()
            destination[3] = (0x80 or (value and 0x3F)).toByte()
        }
    }
}
