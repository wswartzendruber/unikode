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

package org.unikode.test

import kotlin.test.assertTrue
import kotlin.test.Test

import org.unikode.toStringUtf8
import org.unikode.toStringUtf16Be
import org.unikode.toStringUtf16Le
import org.unikode.toStringUtf32Be
import org.unikode.toStringUtf32Le

class DecodingExtensionTests {

    @Test
    fun byte_array_utf8() {
        assertTrue(textByteArrayUtf8.toStringUtf8() == TEXT)
    }

    @Test
    fun byte_iterable_utf8() {
        assertTrue(textByteArrayUtf8.toList().toStringUtf8() == TEXT)
    }

    @Test
    fun byte_array_utf16be() {
        assertTrue(textByteArrayUtf16Be.toStringUtf16Be() == TEXT)
    }

    @Test
    fun byte_iterable_utf16be() {
        assertTrue(textByteArrayUtf16Be.toList().toStringUtf16Be() == TEXT)
    }

    @Test
    fun byte_array_utf16le() {
        assertTrue(textByteArrayUtf16Le.toStringUtf16Le() == TEXT)
    }

    @Test
    fun byte_iterable_utf16le() {
        assertTrue(textByteArrayUtf16Le.toList().toStringUtf16Le() == TEXT)
    }

    @Test
    fun byte_array_utf32be() {
        assertTrue(textByteArrayUtf32Be.toStringUtf32Be() == TEXT)
    }

    @Test
    fun byte_iterable_utf32be() {
        assertTrue(textByteArrayUtf32Be.toList().toStringUtf32Be() == TEXT)
    }

    @Test
    fun byte_array_utf32le() {
        assertTrue(textByteArrayUtf32Le.toStringUtf32Le() == TEXT)
    }

    @Test
    fun byte_iterable_utf32le() {
        assertTrue(textByteArrayUtf32Le.toList().toStringUtf32Le() == TEXT)
    }
}
