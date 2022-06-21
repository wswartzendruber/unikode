/*
 * Any copyright is dedicated to the Public Domain.
 *
 * Copyright 2022 William Swartzendruber
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.test

import kotlin.test.assertTrue
import kotlin.test.Test

import org.unikode.toUtf8ByteArray
import org.unikode.toUtf16BeByteArray
import org.unikode.toUtf16LeByteArray
import org.unikode.toUtf32BeByteArray
import org.unikode.toUtf32LeByteArray

class EncodingExtensionTests {

    @Test
    fun char_array_utf8() {
        assertTrue(
            TEXT.toList().toCharArray().toUtf8ByteArray() contentEquals textByteArrayUtf8
        )
    }

    @Test
    fun char_sequence_utf8() {
        assertTrue(TEXT.toUtf8ByteArray() contentEquals textByteArrayUtf8)
    }

    @Test
    fun char_iterable_utf8() {
        assertTrue(TEXT.toList().toUtf8ByteArray() contentEquals textByteArrayUtf8)
    }

    @Test
    fun char_array_utf32be() {
        assertTrue(
            TEXT.toList().toCharArray().toUtf32BeByteArray() contentEquals textByteArrayUtf32Be
        )
    }

    @Test
    fun char_sequence_utf32be() {
        assertTrue(TEXT.toUtf32BeByteArray() contentEquals textByteArrayUtf32Be)
    }

    @Test
    fun char_iterable_utf32be() {
        assertTrue(TEXT.toList().toUtf32BeByteArray() contentEquals textByteArrayUtf32Be)
    }

    @Test
    fun char_array_utf32le() {
        assertTrue(
            TEXT.toList().toCharArray().toUtf32LeByteArray() contentEquals textByteArrayUtf32Le
        )
    }

    @Test
    fun char_sequence_utf32le() {
        assertTrue(TEXT.toUtf32LeByteArray() contentEquals textByteArrayUtf32Le)
    }

    @Test
    fun char_iterable_utf32le() {
        assertTrue(TEXT.toList().toUtf32LeByteArray() contentEquals textByteArrayUtf32Le)
    }
}