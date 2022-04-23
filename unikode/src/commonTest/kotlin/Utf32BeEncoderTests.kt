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

import org.unikode.Utf32BeEncoder

class Utf32BeEncoderTests {

    @Test
    fun encode_string_single_chunk() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf32BeEncoder()
        val byteCount = encoder.encode(TEXT, testByteArray)

        assertEquals(encodedByteArray.size, byteCount)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_half_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf32BeEncoder()
        var byteIndex = 0

        byteIndex += encoder.encode(TEXT, testByteArray, 0, 37)
        byteIndex += encoder.encode(TEXT, testByteArray, 37, TEXT.length, byteIndex)

        assertEquals(encodedByteArray.size, byteIndex)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_quarter_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf32BeEncoder()
        var byteIndex = 0

        byteIndex += encoder.encode(TEXT, testByteArray, 0, 18)
        byteIndex += encoder.encode(TEXT, testByteArray, 18, 36, byteIndex)
        byteIndex += encoder.encode(TEXT, testByteArray, 36, 54, byteIndex)
        byteIndex += encoder.encode(TEXT, testByteArray, 54, TEXT.length, byteIndex)

        assertEquals(encodedByteArray.size, byteIndex)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_eighth_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf32BeEncoder()
        var byteIndex = 0

        byteIndex += encoder.encode(TEXT, testByteArray, 0, 9)
        byteIndex += encoder.encode(TEXT, testByteArray, 9, 18, byteIndex)
        byteIndex += encoder.encode(TEXT, testByteArray, 18, 27, byteIndex)
        byteIndex += encoder.encode(TEXT, testByteArray, 27, 36, byteIndex)
        byteIndex += encoder.encode(TEXT, testByteArray, 36, 45, byteIndex)
        byteIndex += encoder.encode(TEXT, testByteArray, 45, 54, byteIndex)
        byteIndex += encoder.encode(TEXT, testByteArray, 54, 63, byteIndex)
        byteIndex += encoder.encode(TEXT, testByteArray, 63, TEXT.length, byteIndex)

        assertEquals(encodedByteArray.size, byteIndex)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_single_chars() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf32BeEncoder()
        var byteIndex = 0

        for (charIndex in 0 until TEXT.length) {
            byteIndex +=
                encoder.encode(TEXT, testByteArray, charIndex, charIndex + 1, byteIndex)
        }

        assertEquals(encodedByteArray.size, byteIndex)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    companion object {

        val encodedByteArray = byteArrayOf(
            0, 0, 0, 84, 0, 0, 0, 104, 0, 0, 0, 105, 0, 0, 0, 115, 0, 0, -56, 28, 0, 0, -84, 0,
            0, 0, 4, 28, 0, 0, 4, 62, 0, 0, 4, 61, 0, 0, 4, 51, 0, 0, 4, 62, 0, 0, 4, 59, 0, 0,
            9, 50, 0, 0, 9, 65, 0, 0, 9, 46, 0, 0, 9, 77, 0, 0, 9, 44, 0, 0, 9, 63, 0, 0, 9, 40,
            0, 0, 9, 64, 0, 0, 0, 105, 0, 0, 0, 115, 0, 0, -84, 0, 0, 0, -69, -8, 0, 0, -56, 21,
            0, 0, -59, -72, 0, 0, 4, 51, 0, 0, 4, 77, 0, 0, 4, 64, 0, 0, 9, 53, 0, 0, 9, 40, 0,
            0, 0, 97, 0, 0, -62, -35, 0, 0, -78, -7, 0, 0, -59, -48, 0, 0, 9, 40, 0, 0, 9, 71,
            0, 0, 9, 42, 0, 0, 9, 62, 0, 0, 9, 50, 0, 0, 0, 116, 0, 0, 0, 101, 0, 0, 0, 115, 0,
            0, 0, 116, 0, 0, -57, -120, 0, 0, -59, -76, 0, 0, -58, -108, 0, 0, 4, 49, 0, 0, 4,
            62, 0, 0, 4, 59, 0, 0, 9, 21, 0, 0, 9, 71, 0, 0, 0, 100, 0, 0, 0, 111, 0, 0, 0, 99,
            0, 0, 0, 117, 0, 0, 0, 109, 0, 0, 0, 101, 0, 0, 0, 110, 0, 0, 0, 116, 0, 1, -15,
            -16, 0, 1, -15, -9, 0, 0, 4, 34, 0, 0, 4, -23, 0, 0, 4, 50, 0, 0, 9, 36, 0, 0, 9,
            48, 0, 0, 9, 62, 0, 0, 9, 8, 0, 1, -10, 0
        )
    }
}
