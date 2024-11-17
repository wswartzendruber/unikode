/*
 * Copyright 2024 William Swartzendruber
 *
 * To the extent possible under law, the person who associated CC0 with this file has waived all
 * copyright and related or neighboring rights to this file.
 *
 * You should have received a copy of the CC0 legalcode along with this work. If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.cf8.test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.Test

import org.unikode.REPLACEMENT_CHAR
import org.unikode.cf8.toCf8ByteArray
import org.unikode.cf8.toStringCf8
import org.unikode.cf8.Cf8Encoder
import org.unikode.cf8.Cf8Decoder

class Cf8Tests {

    @Test
    fun round_trip() =
        assertEquals(completeString, completeString.toCf8ByteArray().toStringCf8())
}
