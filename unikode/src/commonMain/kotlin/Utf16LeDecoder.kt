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

public class Utf16LeDecoder : Decoder() {

    private var continuing = false
    private var currentByte: Byte = 0x00
    private var instanceHighSurrogate: Char? = null

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
            "The number of bytes to decode exceeds the number of bytes in the source."
        }

        val iterator = source.iterator()
        var bytesDecoded = 0

        repeat(sourceStartIndex) {
            iterator.next()
        }

        while (bytesDecoded < bytesToDecode) {

            if (!continuing) {
                currentByte = iterator.next()
                continuing = true
            } else {
                val highSurrogate = instanceHighSurrogate
                val currentChar =
                    (
                        (currentByte.toInt() and 0xFF) or
                        (iterator.next().toInt() and 0xFF shl 8)
                    )
                    .toChar()
                when {
                    !currentChar.isSurrogate() -> {
                        if (highSurrogate != null) {
                            destination[destinationIndex++] = REPLACEMENT_CHAR
                            instanceHighSurrogate = null
                        }
                        destination[destinationIndex++] = currentChar
                    }
                    currentChar.isHighSurrogate() -> {
                        if (highSurrogate != null)
                            destination[destinationIndex++] = REPLACEMENT_CHAR
                        instanceHighSurrogate = currentChar
                    }
                    currentChar.isLowSurrogate() -> {
                        if (highSurrogate != null) {
                            destination[destinationIndex++] = highSurrogate
                            destination[destinationIndex++] = currentChar
                            instanceHighSurrogate = null
                        } else {
                            destination[destinationIndex++] = REPLACEMENT_CHAR
                        }
                    }
                    else -> {
                        throw IllegalStateException("Internal state is irrational.")
                    }
                }
                continuing = false
            }

            bytesDecoded++
        }

        return destinationIndex - destinationOffset
    }

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount / 2

    public override fun reset(): Unit {
        continuing = false
        currentByte = 0x00
        instanceHighSurrogate = null
    }

    private companion object {

        private const val REPLACEMENT_CHAR = '�'
    }
}
