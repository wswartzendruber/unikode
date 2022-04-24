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

public class Utf32BeDecoder : Decoder() {

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

        require(bytesToDecode % 4 == 0) {
            "The number of UTF-32 bytes to decode must be evenly divisible by four."
        }

        val iterator = source.iterator()
        var bytesDecoded = 0

        repeat(sourceStartIndex) {
            iterator.next()
        }

        while (bytesDecoded < bytesToDecode) {

            val currentValue =
                (
                    (iterator.next().toInt() and 0xFF shl 24) or
                    (iterator.next().toInt() and 0xFF shl 16) or
                    (iterator.next().toInt() and 0xFF shl 8) or
                    (iterator.next().toInt() and 0xFF)
                )

            when (currentValue) {
                in 0x0000..0xD7FF, in 0xE000..0xFFFF -> {
                    destination[destinationIndex++] = currentValue.toChar()
                }
                in 0x010000..0x10FFFF -> {
                    destination[destinationIndex++] = currentValue.highSurrogate()
                    destination[destinationIndex++] = currentValue.lowSurrogate()
                }
                else -> {
                    destination[destinationIndex++] = REPLACEMENT_CHAR
                }
            }

            bytesDecoded += 4
        }

        return destinationIndex - destinationOffset
    }

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount / 2

    public override fun reset(): Unit { }

    private companion object {

        private const val REPLACEMENT_CHAR = '�'
    }
}
