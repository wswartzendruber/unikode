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
}
