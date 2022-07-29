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

import org.unikode.toStringUtf8
import org.unikode.Utf8Decoder

class Utf8DecoderTests {

    @Test
    fun decode_bytes_single_chunk() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf8Decoder()
        val charCount = decoder.decode(textByteArrayUtf8, testCharArray)

        assertEquals(TEXT.length, charCount)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_half_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf8Decoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 0, 80)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 80, 160, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_quarter_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf8Decoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 0, 40)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 40, 80, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 80, 120, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 120, 160, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_eighth_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf8Decoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 0, 20)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 20, 40, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 40, 60, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 60, 80, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 80, 100, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 100, 120, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 120, 140, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 140, 160, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_single_step() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf8Decoder()
        var charIndex = 0

        for (byteIndex in 0 until textByteArrayUtf8.size) {
            charIndex += decoder.decode(
                textByteArrayUtf8, testCharArray, byteIndex, byteIndex + 1, charIndex
            )
        }

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun reject_overlongs_two_bytes() {

        val range = 0x00..0x7F
        val buffer = ByteArray(2)
        val decoder = Utf8Decoder()
        val result = mutableListOf<Char>()
        val callback = { char: Char ->
            result.add(char)
            Unit
        }

        for (value in range) {
            two_byte_value(buffer, value)
            decoder.inputByte(buffer[0], callback)
            decoder.inputByte(buffer[1], callback)
        }

        assertEquals(range.count(), result.size)
        assertTrue(result.all { char: Char -> char == '�' })
    }

    @Test
    fun reject_overlongs_three_bytes() {

        val range = 0x000..0x7FF
        val buffer = ByteArray(3)
        val decoder = Utf8Decoder()
        val result = mutableListOf<Char>()
        val callback = { char: Char ->
            result.add(char)
            Unit
        }

        for (value in range) {
            three_byte_value(buffer, value)
            decoder.inputByte(buffer[0], callback)
            decoder.inputByte(buffer[1], callback)
            decoder.inputByte(buffer[2], callback)
        }

        assertEquals(range.count(), result.size)
        assertTrue(result.all { char: Char -> char == '�' })
    }

    @Test
    fun reject_overlongs_four_bytes() {

        val range = 0x0000..0xFFFF
        val buffer = ByteArray(4)
        val decoder = Utf8Decoder()
        val result = mutableListOf<Char>()
        val callback = { char: Char ->
            result.add(char)
            Unit
        }

        for (value in range) {
            four_byte_value(buffer, value)
            decoder.inputByte(buffer[0], callback)
            decoder.inputByte(buffer[1], callback)
            decoder.inputByte(buffer[2], callback)
            decoder.inputByte(buffer[3], callback)
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
