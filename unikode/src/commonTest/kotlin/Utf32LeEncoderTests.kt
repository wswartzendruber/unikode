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

import org.unikode.Utf32LeEncoder

class Utf32LeEncoderTests {

    @Test
    fun encode_string_single_chunk() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf32LeEncoder()
        val byteCount = encoder.encode(TEXT, testByteArray)

        assertEquals(encodedByteArray.size, byteCount)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_half_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf32LeEncoder()
        var byteIndex = 0

        byteIndex += encoder.encode(TEXT, testByteArray, 0, 37)
        byteIndex += encoder.encode(TEXT, testByteArray, 37, TEXT.length, byteIndex)

        assertEquals(encodedByteArray.size, byteIndex)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_quarter_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf32LeEncoder()
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
        val encoder = Utf32LeEncoder()
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
        val encoder = Utf32LeEncoder()
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
            84, 0, 0, 0, 104, 0, 0, 0, 105, 0, 0, 0, 115, 0, 0, 0, 28, -56, 0, 0, 0, -84, 0, 0,
            28, 4, 0, 0, 62, 4, 0, 0, 61, 4, 0, 0, 51, 4, 0, 0, 62, 4, 0, 0, 59, 4, 0, 0, 50, 9,
            0, 0, 65, 9, 0, 0, 46, 9, 0, 0, 77, 9, 0, 0, 44, 9, 0, 0, 63, 9, 0, 0, 40, 9, 0, 0,
            64, 9, 0, 0, 105, 0, 0, 0, 115, 0, 0, 0, 0, -84, 0, 0, -8, -69, 0, 0, 21, -56, 0, 0,
            -72, -59, 0, 0, 51, 4, 0, 0, 77, 4, 0, 0, 64, 4, 0, 0, 53, 9, 0, 0, 40, 9, 0, 0, 97,
            0, 0, 0, -35, -62, 0, 0, -7, -78, 0, 0, -48, -59, 0, 0, 40, 9, 0, 0, 71, 9, 0, 0,
            42, 9, 0, 0, 62, 9, 0, 0, 50, 9, 0, 0, 116, 0, 0, 0, 101, 0, 0, 0, 115, 0, 0, 0,
            116, 0, 0, 0, -120, -57, 0, 0, -76, -59, 0, 0, -108, -58, 0, 0, 49, 4, 0, 0, 62, 4,
            0, 0, 59, 4, 0, 0, 21, 9, 0, 0, 71, 9, 0, 0, 100, 0, 0, 0, 111, 0, 0, 0, 99, 0, 0,
            0, 117, 0, 0, 0, 109, 0, 0, 0, 101, 0, 0, 0, 110, 0, 0, 0, 116, 0, 0, 0, -16, -15,
            1, 0, -9, -15, 1, 0, 34, 4, 0, 0, -23, 4, 0, 0, 50, 4, 0, 0, 36, 9, 0, 0, 48, 9, 0,
            0, 62, 9, 0, 0, 8, 9, 0, 0, 0, -10, 1, 0
        )
    }
}
