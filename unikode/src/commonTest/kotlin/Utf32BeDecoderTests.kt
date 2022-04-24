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

import org.unikode.Utf32BeDecoder

class Utf32BeDecoderTests {

    @Test
    fun decode_bytes_single_chunk() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf32BeDecoder()
        val charCount = decoder.decode(textByteArrayUtf32Be, testCharArray)

        assertEquals(TEXT.length, charCount)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_half_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf32BeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 0, 140)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 140, 280, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_quarter_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf32BeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 0, 68)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 68, 136, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 136, 204, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 204, 280, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_eighth_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf32BeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 0, 32)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 32, 64, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 64, 96, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 96, 128, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 128, 160, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 160, 192, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 192, 224, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 224, 280, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_single_step() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf32BeDecoder()
        var charIndex = 0

        for (byteIndex in 0 until textByteArrayUtf32Be.size step 4) {
            charIndex += decoder.decode(
                textByteArrayUtf32Be, testCharArray, byteIndex, byteIndex + 4, charIndex
            )
        }

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }
}
