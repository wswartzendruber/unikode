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

        val testByteArray = ByteArray(textByteArrayUtf32Be.size)
        val encoder = Utf32BeEncoder()
        val byteCount = encoder.encode(TEXT, testByteArray)

        assertEquals(textByteArrayUtf32Be.size, byteCount)
        assertTrue(textByteArrayUtf32Be contentEquals testByteArray)
    }

    @Test
    fun encode_string_half_chunks() {

        val testByteArray = ByteArray(textByteArrayUtf32Be.size)
        val encoder = Utf32BeEncoder()
        var byteIndex = 0

        byteIndex += encoder.encode(TEXT, testByteArray, 0, 37)
        byteIndex += encoder.encode(TEXT, testByteArray, 37, TEXT.length, byteIndex)

        assertEquals(textByteArrayUtf32Be.size, byteIndex)
        assertTrue(textByteArrayUtf32Be contentEquals testByteArray)
    }

    @Test
    fun encode_string_quarter_chunks() {

        val testByteArray = ByteArray(textByteArrayUtf32Be.size)
        val encoder = Utf32BeEncoder()
        var byteIndex = 0

        byteIndex += encoder.encode(TEXT, testByteArray, 0, 18)
        byteIndex += encoder.encode(TEXT, testByteArray, 18, 36, byteIndex)
        byteIndex += encoder.encode(TEXT, testByteArray, 36, 54, byteIndex)
        byteIndex += encoder.encode(TEXT, testByteArray, 54, TEXT.length, byteIndex)

        assertEquals(textByteArrayUtf32Be.size, byteIndex)
        assertTrue(textByteArrayUtf32Be contentEquals testByteArray)
    }

    @Test
    fun encode_string_eighth_chunks() {

        val testByteArray = ByteArray(textByteArrayUtf32Be.size)
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

        assertEquals(textByteArrayUtf32Be.size, byteIndex)
        assertTrue(textByteArrayUtf32Be contentEquals testByteArray)
    }

    @Test
    fun encode_string_single_step() {

        val testByteArray = ByteArray(textByteArrayUtf32Be.size)
        val encoder = Utf32BeEncoder()
        var byteIndex = 0

        for (charIndex in 0 until TEXT.length) {
            byteIndex +=
                encoder.encode(TEXT, testByteArray, charIndex, charIndex + 1, byteIndex)
        }

        assertEquals(textByteArrayUtf32Be.size, byteIndex)
        assertTrue(textByteArrayUtf32Be contentEquals testByteArray)
    }
}
