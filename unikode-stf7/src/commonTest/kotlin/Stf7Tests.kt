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

package org.unikode.stf7.test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.Test

import org.unikode.REPLACEMENT_CHAR
import org.unikode.stf7.toStf7ByteArray
import org.unikode.stf7.toStringStf7
import org.unikode.stf7.Stf7Encoder
import org.unikode.stf7.Stf7Decoder

class Stf7Tests {

    @Test
    fun round_trip() =
        assertEquals(completeString, completeString.toStf7ByteArray().toStringStf7())

    @Test
    fun reject_initial_closing_chunk() =
        assertEquals(byteArrayOf(0x20, 0x5D, 0x20).toStringStf7(), " � ")
}
