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
import kotlin.test.BeforeTest
import kotlin.test.Test

import org.unikode.highSurrogate
import org.unikode.lowSurrogate
import org.unikode.bad.toStringCesu8
import org.unikode.bad.toCesu8ByteArray

class RoundTripTests {

    // TODO: Set timeout.
    // @Test
    fun cesu8() =
        assertEquals(completeString, completeString.toCesu8ByteArray().toStringCesu8())

    companion object {

        val completeString = generateCompleteString()

        fun generateCompleteString(): String {

            val returnValue = mutableListOf<Char>()

            for (i in 0x0000..0xD7FF)
                returnValue.add(i.toChar())
            for (i in 0xE000..0xFFFF)
                returnValue.add(i.toChar())
            for (i in 0x010000..0x10FFFF) {
                returnValue.add(i.highSurrogate())
                returnValue.add(i.lowSurrogate())
            }

            return returnValue.toCharArray().concatToString()
        }
    }
}
