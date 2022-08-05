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

package org.unikode.bad.test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test

import org.unikode.bad.toStringCesu8
import org.unikode.bad.Cesu8Decoder

class Cesu8DecoderTests {

    @Test
    fun decode_bytes_single_chunk() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Cesu8Decoder()
        val charCount = decoder.decode(textByteArrayCesu8, testCharArray)

        assertEquals(TEXT.length, charCount)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_half_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Cesu8Decoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 0, 80)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 80, 166, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_quarter_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Cesu8Decoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 0, 40)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 40, 80, charIndex)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 80, 120, charIndex)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 120, 166, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_eighth_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Cesu8Decoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 0, 20)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 20, 40, charIndex)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 40, 60, charIndex)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 60, 80, charIndex)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 80, 100, charIndex)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 100, 120, charIndex)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 120, 140, charIndex)
        charIndex += decoder.decode(textByteArrayCesu8, testCharArray, 140, 166, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_single_step() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Cesu8Decoder()
        var charIndex = 0

        for (byteIndex in 0 until textByteArrayCesu8.size) {
            charIndex += decoder.decode(
                textByteArrayCesu8, testCharArray, byteIndex, byteIndex + 1, charIndex
            )
        }

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun reject_overlongs_two_bytes() {

        val range = 0x00..0x7F
        val buffer = ByteArray(2)
        val decoder = Cesu8Decoder()
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
        val decoder = Cesu8Decoder()
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
    fun reject_initial_continuation_byte() {

        val input = byteArrayOf(-0x80)
        val output = input.toStringCesu8()

        assertEquals("�", output)
    }

    @Test
    fun reject_incomplete_sequence_1() {

        val input = byteArrayOf(-0x13, 0x20)
        val output = input.toStringCesu8()

        assertEquals("� ", output)
    }

    @Test
    fun reject_incomplete_sequence_2() {

        val input = byteArrayOf(-0x13, -0x6B, 0x20)
        val output = input.toStringCesu8()

        assertEquals("� ", output)
    }

    @Test
    fun initial_ls() {
        assertEquals(
            " � ",
            byteArrayOf(0x20, -0x13, -0x48, -0x80, 0x20).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_1_byte_valid() {
        assertEquals(
            " � ",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, 0x20).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_2_bytes_valid() {
        assertEquals(
            " �θ",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -0x32, -0x48).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_3_bytes_valid() {
        assertEquals(
            " �漢",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -0x1A, -0x44, -0x5E).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_surrogate_pair_valid() {
        assertEquals(
            " �😀",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -19, -96, -67, -19, -72, -128)
                .toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_2_bytes_invalid() {
        assertEquals(
            " �� ",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -0x32, 0x20).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_3_bytes_invalid() {
        assertEquals(
            " �� ",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -0x1A, -0x44, 0x20).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_surrogate_pair_invalid() {
        assertEquals(
            " ��� ",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -19, -96, -67, -19, -72, 0x20)
                .toStringCesu8(),
        )
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
    }
}
