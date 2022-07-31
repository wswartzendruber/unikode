/*
 * Any copyright is dedicated to the Public Domain.
 *
 * Copyright 2022 William Swartzendruber
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.bad.test

import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test

import org.unikode.highSurrogate
import org.unikode.lowSurrogate
import org.unikode.Encoder
import org.unikode.Decoder
import org.unikode.bad.Cesu8Encoder
import org.unikode.bad.Cesu8Decoder

class RoundTripTests {

    @Test
    fun cesu8() = cycle(Cesu8Encoder(), Cesu8Decoder())

    fun cycle(encoder: Encoder, decoder: Decoder) {

        val testByteArray = ByteArray(encoder.maxBytesNeeded(completeString.length))
        val bytesUsed = encoder.encode(completeString, testByteArray)
        val testCharArray = CharArray(decoder.maxCharsNeeded(bytesUsed))
        val charsUsed = decoder.decode(testByteArray, testCharArray, 0, bytesUsed)
        val endString = testCharArray.copyOfRange(0, charsUsed).concatToString()

        assertEquals(completeString, endString)
    }

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
