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

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test

import org.unikode.bad.toStringCesu8
import org.unikode.bad.Cesu8Decoder

class Cesu8DecoderTests {

    @Test
    fun initial_ls() {
        assertEquals(
            " � ",
            byteArrayOf(0x20, -0x13, -0x48, -0x80, 0x20).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_1_byte_valid() {
        assertEquals(
            " � ",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, 0x20).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_2_bytes_valid() {
        assertEquals(
            " �θ",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -0x32, -0x48).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_3_bytes_valid() {
        assertEquals(
            " �漢",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -0x1A, -0x44, -0x5E).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_surrogate_pair_valid() {
        assertEquals(
            " �😀",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -19, -96, -67, -19, -72, -128)
                .toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_2_bytes_invalid() {
        assertEquals(
            " �� ",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -0x32, 0x20).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_3_bytes_invalid() {
        assertEquals(
            " �� ",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -0x1A, -0x44, 0x20).toStringCesu8(),
        )
    }

    @Test
    fun unfollowed_hs_then_surrogate_pair_invalid() {
        assertEquals(
            " ��� ",
            byteArrayOf(0x20, -0x13, -0x58, -0x80, -19, -96, -67, -19, -72, 0x20)
                .toStringCesu8(),
        )
    }
}
