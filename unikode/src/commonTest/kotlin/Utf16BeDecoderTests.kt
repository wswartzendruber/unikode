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

import org.unikode.Utf16BeDecoder

class Utf16BeDecoderTests {

    @Test
    fun decode_bytes_single_chunk() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16BeDecoder()
        val charCount = decoder.decode(textByteArrayUtf16Be, testCharArray)

        assertEquals(TEXT.length, charCount)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_half_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16BeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 0, 74)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 74, 146, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_quarter_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16BeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 0, 36)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 36, 72, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 72, 108, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 108, 146, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_eighth_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16BeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 0, 18)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 18, 36, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 36, 54, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 54, 72, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 72, 90, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 90, 108, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 108, 126, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 126, 146, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_single_step() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16BeDecoder()
        var charIndex = 0

        for (byteIndex in 0 until textByteArrayUtf16Be.size step 2) {
            charIndex += decoder.decode(
                textByteArrayUtf16Be, testCharArray, byteIndex, byteIndex + 2, charIndex
            )
        }

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }
}
