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

import org.unikode.Utf16LeDecoder

class Utf16LeDecoderTests {

    @Test
    fun decode_bytes_single_chunk() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16LeDecoder()
        val charCount = decoder.decode(textByteArrayUtf16Le, testCharArray)

        assertEquals(TEXT.length, charCount)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_half_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16LeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 0, 73)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 73, 146, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_quarter_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16LeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 0, 37)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 37, 73, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 73, 110, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 110, 146, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_eighth_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16LeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 0, 18)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 18, 37, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 37, 55, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 55, 73, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 73, 91, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 91, 110, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 110, 128, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Le, testCharArray, 128, 146, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_single_step() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16LeDecoder()
        var charIndex = 0

        for (byteIndex in 0 until textByteArrayUtf16Le.size step 2) {
            charIndex += decoder.decode(
                textByteArrayUtf16Le, testCharArray, byteIndex, byteIndex + 2, charIndex
            )
        }

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }
}
