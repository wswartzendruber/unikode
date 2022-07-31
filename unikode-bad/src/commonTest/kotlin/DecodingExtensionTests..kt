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

import org.unikode.bad.toStringCesu8

class DecodingExtensionTests {

    @Test
    fun byte_array_cesu8() {
        assertTrue(textByteArrayCesu8.toStringCesu8() == TEXT)
    }

    @Test
    fun byte_iterable_cesu8() {
        assertTrue(textByteArrayCesu8.toList().toStringCesu8() == TEXT)
    }
}
