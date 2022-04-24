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

        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 0, 70)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 70, 140, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 140, 210, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 210, 280, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_eighth_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf32BeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 0, 35)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 35, 70, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 70, 105, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 105, 140, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 140, 175, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 175, 210, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 210, 245, charIndex)
        charIndex += decoder.decode(textByteArrayUtf32Be, testCharArray, 245, 280, charIndex)

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
