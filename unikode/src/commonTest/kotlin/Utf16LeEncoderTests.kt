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

import org.unikode.Utf16LeEncoder

class Utf16LeEncoderTests {

    @Test
    fun encode_string_single_chunk() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf16LeEncoder()
        val byteCount = encoder.encode(TEXT, testByteArray)

        assertEquals(encodedByteArray.size, byteCount)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_half_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf16LeEncoder()
        var byteIndex = 0

        byteIndex += encoder.encode(TEXT, testByteArray, 0, 37)
        byteIndex += encoder.encode(TEXT, testByteArray, 37, TEXT.length, byteIndex)

        assertEquals(encodedByteArray.size, byteIndex)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_quarter_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf16LeEncoder()
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
        val encoder = Utf16LeEncoder()
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
        val encoder = Utf16LeEncoder()
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
            84, 0, 104, 0, 105, 0, 115, 0, 28, -56, 0, -84, 28, 4, 62, 4, 61, 4, 51, 4, 62, 4,
            59, 4, 50, 9, 65, 9, 46, 9, 77, 9, 44, 9, 63, 9, 40, 9, 64, 9, 105, 0, 115, 0, 0,
            -84, -8, -69, 21, -56, -72, -59, 51, 4, 77, 4, 64, 4, 53, 9, 40, 9, 97, 0, -35, -62,
            -7, -78, -48, -59, 40, 9, 71, 9, 42, 9, 62, 9, 50, 9, 116, 0, 101, 0, 115, 0, 116,
            0, -120, -57, -76, -59, -108, -58, 49, 4, 62, 4, 59, 4, 21, 9, 71, 9, 100, 0, 111,
            0, 99, 0, 117, 0, 109, 0, 101, 0, 110, 0, 116, 0, 60, -40, -16, -35, 60, -40, -9,
            -35, 34, 4, -23, 4, 50, 4, 36, 9, 48, 9, 62, 9, 8, 9, 61, -40, 0, -34
        )
    }
}
