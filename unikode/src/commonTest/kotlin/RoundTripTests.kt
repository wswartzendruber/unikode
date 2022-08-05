/*
 * Copyright 2022 William Swartzendruber
 *
 * Any copyright is dedicated to the Public Domain.
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.test

import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test

import org.unikode.highSurrogate
import org.unikode.lowSurrogate
import org.unikode.Encoder
import org.unikode.Decoder
import org.unikode.Utf8Encoder
import org.unikode.Utf8Decoder
import org.unikode.Utf16LeEncoder
import org.unikode.Utf16LeDecoder
import org.unikode.Utf16BeEncoder
import org.unikode.Utf16BeDecoder
import org.unikode.Utf32LeEncoder
import org.unikode.Utf32LeDecoder
import org.unikode.Utf32BeEncoder
import org.unikode.Utf32BeDecoder

class RoundTripTests {

    @Test
    fun utf8() = cycle(Utf8Encoder(), Utf8Decoder())

    @Test
    fun utf16le() = cycle(Utf16LeEncoder(), Utf16LeDecoder())

    @Test
    fun utf16be() = cycle(Utf16BeEncoder(), Utf16BeDecoder())

    @Test
    fun utf32le() = cycle(Utf32LeEncoder(), Utf32LeDecoder())

    @Test
    fun utf32be() = cycle(Utf32BeEncoder(), Utf32BeDecoder())

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
