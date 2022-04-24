/*
 * Any copyright is dedicated to the Public Domain.
 *
 * Copyright 2022 William Swartzendruber
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.test

import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test

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

class RoundTripTests {

    @Test
    fun utf8() = cycle(Utf8Encoder(), Utf8Decoder())

    @Test
    fun utf16le() = cycle(Utf16LeEncoder(), Utf16LeDecoder())

    @Test
    fun utf16be() = cycle(Utf16BeEncoder(), Utf16BeDecoder())

    @Test
    fun utf32le() = cycle(Utf32LeEncoder(), Utf32LeDecoder())

    fun cycle(encoder: Encoder, decoder: Decoder) {

        val testByteArray = ByteArray(encoder.maxBytesNeeded(completeString.length))
        val bytesUsed = encoder.encode(completeString, testByteArray)
        val testCharArray = CharArray(decoder.maxCharsNeeded(bytesUsed))
        val charsUsed = decoder.decode(testByteArray, testCharArray, 0, bytesUsed)
        val endString = testCharArray.copyOfRange(0, charsUsed).concatToString()

        assertTrue(completeString == endString)
    }
}
