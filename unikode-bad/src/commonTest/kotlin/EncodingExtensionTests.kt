/*
 * Any copyright is dedicated to the Public Domain.
 *
 * Copyright 2022 William Swartzendruber
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.bad.test

import kotlin.test.assertTrue
import kotlin.test.Test

import org.unikode.bad.toCesu8ByteArray

class EncodingExtensionTests {

    @Test
    fun char_array_cesu8() {
        println(TEXT.toList().toCharArray().toCesu8ByteArray().joinToString(", "))
        println(textByteArrayCesu8.joinToString(", "))
        assertTrue(
            TEXT.toList().toCharArray().toCesu8ByteArray() contentEquals textByteArrayCesu8
        )
    }

    @Test
    fun char_sequence_cesu8() {
        assertTrue(TEXT.toCesu8ByteArray() contentEquals textByteArrayCesu8)
    }

    @Test
    fun char_iterable_cesu8() {
        assertTrue(TEXT.toList().toCesu8ByteArray() contentEquals textByteArrayCesu8)
    }
}
