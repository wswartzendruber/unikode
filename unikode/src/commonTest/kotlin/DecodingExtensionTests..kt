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

import org.unikode.toStringUtf8

class DecodingExtensionTests {

    @Test
    fun byte_array_utf8() {
        assertTrue(textByteArrayUtf8.toStringUtf8() == TEXT)
    }

    @Test
    fun byte_iterable_utf8() {
        assertTrue(textByteArrayUtf8.toList().toStringUtf8() == TEXT)
    }
}
