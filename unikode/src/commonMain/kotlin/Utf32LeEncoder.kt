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

public class Utf32LeEncoder : Encoder() {

    private var instanceHighSurrogate: Char? = null

    public override fun encode(
        source: CharIterator,
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
                        writeCodePointToByteArray(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR,
                        )
                        destinationIndex += 4
                        instanceHighSurrogate = null
                    }
                    writeCodePointToByteArray(
                        destination, destinationIndex,
                        currentChar.code,
                    )
                    destinationIndex += 4
                }
                currentChar.isHighSurrogate() -> {
                    if (highSurrogate != null) {
                        writeCodePointToByteArray(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR,
                        )
                        destinationIndex += 4
                    }
                    instanceHighSurrogate = currentChar
                }
                currentChar.isLowSurrogate() -> {
                    if (highSurrogate != null) {
                        writeCodePointToByteArray(
                            destination, destinationIndex,
                            codePoint(highSurrogate, currentChar),
                        )
                        destinationIndex += 4
                        instanceHighSurrogate = null
                    } else {
                        writeCodePointToByteArray(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR,
                        )
                        destinationIndex += 4
                    }
                }
                else -> {
                    throw IllegalStateException("Internal state is irrational.")
                }
            }
        }

        return destinationIndex - destinationOffset
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
        ) {
            destination[index] = (codePoint and 0xFF).toByte()
            destination[index + 1] = (codePoint and 0xFF00 ushr 8).toByte()
            destination[index + 2] = (codePoint and 0xFF0000 ushr 16).toByte()
            destination[index + 3] = 0x0
        }
    }
}
