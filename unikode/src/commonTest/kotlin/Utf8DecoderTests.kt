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

import org.unikode.Utf8Decoder

class Utf8DecoderTests {

    @Test
    fun decode_bytes_single_chunk() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf8Decoder()
        val charCount = decoder.decode(textByteArrayUtf8, testCharArray)

        assertEquals(TEXT.length, charCount)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_half_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf8Decoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 0, 80)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 80, 160, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_quarter_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf8Decoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 0, 40)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 40, 80, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 80, 120, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 120, 160, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_eighth_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf8Decoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 0, 20)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 20, 40, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 40, 60, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 60, 80, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 80, 100, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 100, 120, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 120, 140, charIndex)
        charIndex += decoder.decode(textByteArrayUtf8, testCharArray, 140, 160, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_single_chars() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf8Decoder()
        var charIndex = 0

        for (byteIndex in 0 until textByteArrayUtf8.size) {
            charIndex += decoder.decode(
                textByteArrayUtf8, testCharArray, byteIndex, byteIndex + 1, charIndex
            )
        }

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }
}
