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

import org.unikode.Utf8Encoder

class Utf8EncoderTests {

    @Test
    fun encode_string_single_chunk() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf8Encoder()
        val byteCount = encoder.encode(TEXT, testByteArray)

        assertEquals(encodedByteArray.size, byteCount)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_half_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf8Encoder()
        var byteIndex = 0

        byteIndex += encoder.encode(TEXT, testByteArray, 0, 37)
        byteIndex += encoder.encode(TEXT, testByteArray, 37, TEXT.length, byteIndex)

        assertEquals(encodedByteArray.size, byteIndex)
        assertTrue(encodedByteArray contentEquals testByteArray)
    }

    @Test
    fun encode_string_quarter_chunks() {

        val testByteArray = ByteArray(encodedByteArray.size)
        val encoder = Utf8Encoder()
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
        val encoder = Utf8Encoder()
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
        val encoder = Utf8Encoder()
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
            84, 104, 105, 115, -20, -96, -100, -22, -80, -128, -48, -100, -48, -66, -48, -67,
            -48, -77, -48, -66, -48, -69, -32, -92, -78, -32, -91, -127, -32, -92, -82, -32,
            -91, -115, -32, -92, -84, -32, -92, -65, -32, -92, -88, -32, -91, -128, 105, 115,
            -22, -80, -128, -21, -81, -72, -20, -96, -107, -20, -106, -72, -48, -77, -47, -115,
            -47, -128, -32, -92, -75, -32, -92, -88, 97, -20, -117, -99, -21, -117, -71, -20,
            -105, -112, -32, -92, -88, -32, -91, -121, -32, -92, -86, -32, -92, -66, -32, -92,
            -78, 116, 101, 115, 116, -20, -98, -120, -20, -106, -76, -20, -102, -108, -48, -79,
            -48, -66, -48, -69, -32, -92, -107, -32, -91, -121, 100, 111, 99, 117, 109, 101,
            110, 116, -16, -97, -121, -80, -16, -97, -121, -73, -48, -94, -45, -87, -48, -78,
            -32, -92, -92, -32, -92, -80, -32, -92, -66, -32, -92, -120, -16, -97, -104, -128
        )
    }
}
