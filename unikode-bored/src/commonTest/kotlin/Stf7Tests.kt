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

package org.unikode.bored.test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.Test

import org.unikode.REPLACEMENT_CHAR
import org.unikode.bored.toStf7ByteArray
import org.unikode.bored.toStringStf7
import org.unikode.bored.Stf7Encoder
import org.unikode.bored.Stf7Decoder

class Stf7Tests {

    @Test
    fun round_trip() =
        assertEquals(completeString, completeString.toStf7ByteArray().toStringStf7())
}
