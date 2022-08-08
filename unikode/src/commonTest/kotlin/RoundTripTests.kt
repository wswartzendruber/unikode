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
import kotlin.test.BeforeTest
import kotlin.test.Test

import org.unikode.*

class RoundTripTests {

    @Test
    fun utf8() =
        assertEquals(completeString, completeString.toUtf8ByteArray().toStringUtf8())

    @Test
    fun utf16le() =
        assertEquals(completeString, completeString.toUtf16LeByteArray().toStringUtf16Le())

    @Test
    fun utf16be() =
        assertEquals(completeString, completeString.toUtf16BeByteArray().toStringUtf16Be())

    @Test
    fun utf32le() =
        assertEquals(completeString, completeString.toUtf32LeByteArray().toStringUtf32Le())

    @Test
    fun utf32be() =
        assertEquals(completeString, completeString.toUtf32BeByteArray().toStringUtf32Be())

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
