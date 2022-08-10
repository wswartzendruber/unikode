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
