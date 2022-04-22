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

import org.unikode.Utf16BeEncoder

class Utf16BeEncoderTests {

    @Test
    fun encode_string_single_chunk() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf16BeEncoder()
        val byteCount = encoder.encode(TEXT, testByteArray)

        assertEquals(encodedByteArray.size, byteCount)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_half_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf16BeEncoder()
        var byteIndex = 0

        byteIndex += encoder.encode(TEXT, testByteArray, 0, 37)
        byteIndex += encoder.encode(TEXT, testByteArray, 37, TEXT.length, byteIndex)

        assertEquals(encodedByteArray.size, byteIndex)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_quarter_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf16BeEncoder()
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
        val encoder = Utf16BeEncoder()
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
        val encoder = Utf16BeEncoder()
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
            0, 84, 0, 104, 0, 105, 0, 115, -56, 28, -84, 0, 4, 28, 4, 62, 4, 61, 4, 51, 4, 62,
            4, 59, 9, 50, 9, 65, 9, 46, 9, 77, 9, 44, 9, 63, 9, 40, 9, 64, 0, 105, 0, 115, -84,
            0, -69, -8, -56, 21, -59, -72, 4, 51, 4, 77, 4, 64, 9, 53, 9, 40, 0, 97, -62, -35,
            -78, -7, -59, -48, 9, 40, 9, 71, 9, 42, 9, 62, 9, 50, 0, 116, 0, 101, 0, 115, 0,
            116, -57, -120, -59, -76, -58, -108, 4, 49, 4, 62, 4, 59, 9, 21, 9, 71, 0, 100, 0,
            111, 0, 99, 0, 117, 0, 109, 0, 101, 0, 110, 0, 116, -40, 60, -35, -16, -40, 60, -35,
            -9, 4, 34, 4, -23, 4, 50, 9, 36, 9, 48, 9, 62, 9, 8, -40, 61, -34, 0
        )
    }
}
