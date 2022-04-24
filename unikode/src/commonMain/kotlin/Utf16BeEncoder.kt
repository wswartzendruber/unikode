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

public class Utf16BeEncoder : Encoder() {

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
                        writeCharToByteArray(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR,
                        )
                        destinationIndex += 2
                        instanceHighSurrogate = null
                    }
                    writeCharToByteArray(
                        destination, destinationIndex,
                        currentChar,
                    )
                    destinationIndex += 2
                }
                currentChar.isHighSurrogate() -> {
                    if (highSurrogate != null) {
                        writeCharToByteArray(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR,
                        )
                        destinationIndex += 2
                    }
                    instanceHighSurrogate = currentChar
                }
                currentChar.isLowSurrogate() -> {
                    if (highSurrogate != null) {
                        writeCharToByteArray(
                            destination, destinationIndex,
                            highSurrogate,
                        )
                        destinationIndex += 2
                        writeCharToByteArray(
                            destination, destinationIndex,
                            currentChar,
                        )
                        destinationIndex += 2
                        instanceHighSurrogate = null
                    } else {
                        writeCharToByteArray(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR,
                        )
                        destinationIndex += 2
                    }
                }
                else -> {
                    throw IllegalStateException("Internal state is irrational.")
                }
            }
        }

        return destinationIndex - destinationOffset
    }

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 2

    public override fun maxCharsPossible(byteCount: Int): Int = byteCount / 2

    public override fun reset(): Unit {
        instanceHighSurrogate = null
    }

    private companion object {

        private const val REPLACEMENT_CHAR = '�'

        private fun writeCharToByteArray(
            destination: ByteArray,
            index: Int,
            char: Char,
        ) {
            destination[index] = (char.code ushr 8).toByte()
            destination[index + 1] = (char.code and 0xFF).toByte()
        }
    }
}
