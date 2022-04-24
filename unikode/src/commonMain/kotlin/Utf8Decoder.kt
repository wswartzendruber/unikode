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

public class Utf8Decoder : Decoder() {

    private var continuing = false
    private val currentBytes = ByteArray(4)
    private var currentBytesExpected = 0
    private var currentByteCount = 0

    public override fun decode(
        source: ByteArray,
        destination: CharArray,
        sourceStartIndex: Int,
        sourceEndIndex: Int,
        destinationOffset: Int,
    ): Int {

        var destinationIndex = destinationOffset

        require(sourceStartIndex <= sourceEndIndex) {
            "sourceStartIndex must be equal to or less than sourceEndIndex."
        }

        val bytesToDecode = sourceEndIndex - sourceStartIndex

        require(bytesToDecode <= source.size) {
            "The number of characters to encode exceeds the number of characters in the source."
        }

        val iterator = source.iterator()
        var bytesDecoded = 0

        repeat(sourceStartIndex) {
            iterator.next()
        }

        while (bytesDecoded < bytesToDecode) {

            val currentByte = iterator.next()
            val currentByteValue = currentByte.toInt()

            if (!continuing) {
                if (currentByteValue and -0x80 == 0x00) {
                    destination[destinationIndex++] = currentByteValue.toChar()
                } else if (currentByteValue and -0x20 == -0x40) {
                    continuing = true
                    currentBytes[0] = currentByte
                    currentBytesExpected = 2
                    currentByteCount = 1
                } else if (currentByteValue and -0x10 == -0x20) {
                    continuing = true
                    currentBytes[0] = currentByte
                    currentBytesExpected = 3
                    currentByteCount = 1
                } else if (currentByteValue and -0x08 == -0x10) {
                    continuing = true
                    currentBytes[0] = currentByte
                    currentBytesExpected = 4
                    currentByteCount = 1
                } else {
                    destination[destinationIndex++] = REPLACEMENT_CHAR
                }
            } else {
                if (currentByteValue and -0x40 == -0x80) {
                    currentBytes[currentByteCount++] = currentByte
                    if (currentByteCount == currentBytesExpected) {
                        val codePoint = when (currentBytesExpected) {
                            2 -> {
                                (currentBytes[0].toInt() and 0x1F shl 6) or
                                    (currentBytes[1].toInt() and 0x3F)
                            }
                            3 -> {
                                (currentBytes[0].toInt() and 0x0F shl 12) or
                                    (currentBytes[1].toInt() and 0x3F shl 6) or
                                    (currentBytes[2].toInt() and 0x3F)
                            }
                            4 -> {
                                (currentBytes[0].toInt() and 0x07 shl 18) or
                                    (currentBytes[1].toInt() and 0x3F shl 12) or
                                    (currentBytes[2].toInt() and 0x3F shl 6) or
                                    (currentBytes[3].toInt() and 0x3F)
                            }
                            else -> {
                                throw IllegalStateException("Internal state is irrational.")
                            }
                        }
                        when (codePoint) {
                            in 0x0000..0xD7FF, in 0xE000..0xFFFF -> {
                                destination[destinationIndex++] = codePoint.toChar()
                            }
                            in 0x010000..0x10FFFF -> {
                                destination[destinationIndex++] = codePoint.highSurrogate()
                                destination[destinationIndex++] = codePoint.lowSurrogate()
                            }
                            else -> {
                                destination[destinationIndex++] = REPLACEMENT_CHAR
                            }
                        }
                        reset()
                    }
                } else {
                    destination[destinationIndex++] = REPLACEMENT_CHAR
                    reset()
                }
            }

            bytesDecoded++
        }

        return destinationIndex - destinationOffset
    }

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    public override fun reset(): Unit {
        continuing = false
        currentBytes[0] = 0x00
        currentBytes[1] = 0x00
        currentBytes[2] = 0x00
        currentBytes[3] = 0x00
        currentBytesExpected = 0
        currentByteCount = 0
    }

    private companion object {

        private const val REPLACEMENT_CHAR = '�'
    }
}
