/*
 * Copyright 2022 William Swartzendruber
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.unikode

public class Utf8Encoder : Encoder() {

    private var instanceHighSurrogate: Char? = null

    public override fun encode(
        destination: ByteArray,
        destinationOffset: Int,
        source: CharSequence,
        sourceStartIndex: Int,
        sourceEndIndex: Int,
    ): Int {

        var destinationIndex = destinationOffset

        require(sourceStartIndex <= sourceEndIndex) {
            "sourceStartIndex must be equal to or less than sourceEndIndex."
        }

        val charsToEncode = sourceEndIndex - sourceStartIndex

        require(charsToEncode <= source.length) {
            "The number of characters to encode exceeds the number of characters in the source."
        }

        val iterator = source.iterator()
        var charsEncoded = 0

        repeat(sourceStartIndex) {
            iterator.next()
        }

        while (charsEncoded < charsToEncode) {

            val highSurrogate = instanceHighSurrogate
            val currentChar = iterator.next()

            when {
                !currentChar.isSurrogate() -> {
                    if (highSurrogate != null) {
                        destinationIndex += writeCodePointToByteArray(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR,
                        )
                        instanceHighSurrogate = null
                    }
                    destinationIndex += writeCodePointToByteArray(
                        destination, destinationIndex,
                        currentChar.code,
                    )
                }
                currentChar.isHighSurrogate() -> {
                    if (highSurrogate != null) {
                        destinationIndex += writeCodePointToByteArray(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR,
                        )
                    }
                    instanceHighSurrogate = currentChar
                }
                currentChar.isLowSurrogate() -> {
                    if (highSurrogate != null) {
                        destinationIndex += writeCodePointToByteArray(
                            destination, destinationIndex,
                            codePoint(highSurrogate, currentChar),
                        )
                        instanceHighSurrogate = null
                    } else {
                        destinationIndex += writeCodePointToByteArray(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR,
                        )
                    }
                }
            }

            charsEncoded++
        }

        return charsEncoded
    }

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 4

    public override fun reset(): Unit {
        instanceHighSurrogate = null
    }

    private companion object {

        private const val REPLACEMENT_CHAR = 0xFFFD

        private fun writeCodePointToByteArray(
            destination: ByteArray,
            index: Int,
            codePoint: Int,
        ): Int =
            when {
                codePoint in 0x00..0x7F -> {
                    destination[index] = codePoint.toByte()
                    1
                }
                codePoint in 0x080..0x7FF -> {
                    destination[index] = (0xC0 or (codePoint shl 6)).toByte()
                    destination[index + 1] = (0x80 or (codePoint and 0x3F)).toByte()
                    2
                }
                codePoint in 0x0800..0xFFFF -> {
                    destination[index] = (0xE0 or (codePoint shl 12)).toByte()
                    destination[index + 1] = (0x80 or (codePoint shr 6 and 0x3F)).toByte()
                    destination[index + 2] = (0x80 or (codePoint and 0x3F)).toByte()
                    3
                }
                codePoint in 0x010000..0x10FFFF -> {
                    destination[index] = (0xF0 or (codePoint shl 18)).toByte()
                    destination[index + 1] = (0x80 or (codePoint shr 12 and 0x3F)).toByte()
                    destination[index + 2] = (0x80 or (codePoint shr 6 and 0x3F)).toByte()
                    destination[index + 3] = (0x80 or (codePoint and 0x3F)).toByte()
                    4
                }
                else -> {
                    destination[index] = -17
                    destination[index + 1] = -65
                    destination[index + 2] = -67
                    3
                }
            }
    }
}
