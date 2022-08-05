/*
 * Copyright 2022 William Swartzendruber
 *
 * To the extent possible under law, the person who associated CC0 with this file has waived all
 * copyright and related or neighboring rights to this file.
 *
 * You should have received a copy of the CC0 legalcode along with this work. If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
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
