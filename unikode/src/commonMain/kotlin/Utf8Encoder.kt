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
        source: Iterator<Char>,
        sourceCount: Int,
        destination: ByteArray,
        destinationOffset: Int,
    ): Int {

        var destinationIndex = destinationOffset

        repeat(sourceCount) {

            val highSurrogate = instanceHighSurrogate
            val currentChar = source.next()

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
                else -> {
                    throw IllegalStateException("Internal state is irrational.")
                }
            }
        }

        return destinationIndex - destinationOffset
    }

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 3

    public override fun maxCharsPossible(byteCount: Int): Int = byteCount

    public override fun reset(): Unit {
        instanceHighSurrogate = null
    }

    private companion object {

        private const val REPLACEMENT_CHAR = 0xFFFD

        private fun writeCodePointToByteArray(
            destination: ByteArray,
            index: Int,
            codePoint: Int,
        ) = when (codePoint) {
            in 0x00..0x7F -> {
                destination[index] = codePoint.toByte()
                1
            }
            in 0x080..0x7FF -> {
                destination[index] = (0xC0 or (codePoint ushr 6)).toByte()
                destination[index + 1] = (0x80 or (codePoint and 0x3F)).toByte()
                2
            }
            in 0x0800..0xFFFF -> {
                destination[index] = (0xE0 or (codePoint ushr 12)).toByte()
                destination[index + 1] = (0x80 or (codePoint ushr 6 and 0x3F)).toByte()
                destination[index + 2] = (0x80 or (codePoint and 0x3F)).toByte()
                3
            }
            in 0x010000..0x10FFFF -> {
                destination[index] = (0xF0 or (codePoint ushr 18)).toByte()
                destination[index + 1] = (0x80 or (codePoint ushr 12 and 0x3F)).toByte()
                destination[index + 2] = (0x80 or (codePoint ushr 6 and 0x3F)).toByte()
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
