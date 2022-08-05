/*
 * Copyright 2022 William Swartzendruber
 *
 * Any copyright is dedicated to the Public Domain.
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test

import org.unikode.toStringUtf16Be
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

        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 0, 73)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 73, 146, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_quarter_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16BeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 0, 37)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 37, 73, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 73, 110, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 110, 146, charIndex)

        assertEquals(TEXT.length, charIndex)
        assertTrue(TEXT.toCharArray() contentEquals testCharArray)
    }

    @Test
    fun decode_bytes_eighth_chunks() {

        val testCharArray = CharArray(TEXT.length)
        val decoder = Utf16BeDecoder()
        var charIndex = 0

        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 0, 18)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 18, 37, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 37, 55, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 55, 73, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 73, 91, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 91, 110, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 110, 128, charIndex)
        charIndex += decoder.decode(textByteArrayUtf16Be, testCharArray, 128, 146, charIndex)

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

    @Test
    fun initial_ls() {
        assertEquals(
            " � ",
            byteArrayOf(0x00, 0x20, -0x22, 0x00, 0x00, 0x20).toStringUtf16Be(),
        )
    }

    @Test
    fun unfollowed_hs_then_bmp() {
        assertEquals(
            " � ",
            byteArrayOf(0x00, 0x20, -0x26, 0x00, 0x00, 0x20).toStringUtf16Be(),
        )
    }

    @Test
    fun unfollowed_hs_then_smp() {
        assertEquals(
            " �😀",
            byteArrayOf(0x00, 0x20, -0x26, 0x00, -0x28, 0x3D, -0x22, 0x00).toStringUtf16Be(),
        )
    }
}
