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

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test

import org.unikode.toStringUtf16Le
import org.unikode.Utf16LeDecoder

class Utf16LeDecoderTests {

    @Test
    fun initial_ls() {
        assertEquals(
            " � ",
            byteArrayOf(0x20, 0x00, 0x00, -0x22, 0x20, 0x00).toStringUtf16Le(),
        )
    }

    @Test
    fun unfollowed_hs_then_bmp() {
        assertEquals(
            " � ",
            byteArrayOf(0x20, 0x00, 0x00, -0x26, 0x20, 0x00).toStringUtf16Le(),
        )
    }

    @Test
    fun unfollowed_hs_then_smp() {
        assertEquals(
            " �😀",
            byteArrayOf(0x20, 0x00, 0x00, -0x26, 0x3D, -0x28, 0x00, -0x22).toStringUtf16Le(),
        )
    }
}
