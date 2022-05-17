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

public abstract class Decoder {

    public abstract fun maxCharsNeeded(byteCount: Int): Int

    public fun decode(
        source: ByteArray,
        destination: CharArray,
        sourceStartIndex: Int = 0,
        sourceEndIndex: Int = source.size,
        destinationOffset: Int = 0,
    ): Int {

        require(sourceStartIndex >= 0 && sourceStartIndex <= sourceEndIndex) {
            "sourceStartIndex must be between zero and sourceEndIndex, inclusive."
        }
        require(sourceEndIndex <= source.size) {
            "sourceEndIndex exceeds the number of characters in the source."
        }

        var sourceIndex = sourceStartIndex
        var destinationIndex = destinationOffset
        val writeNextChar = { value: Char ->
            destination[destinationIndex++] = value
        }

        while (sourceIndex < sourceEndIndex)
            inputByte(source[sourceIndex++], writeNextChar)

        return destinationIndex - destinationOffset
    }

    public fun decode(
        source: Iterable<Byte>,
        destination: CharArray,
        destinationOffset: Int = 0,
    ): Int {

        var destinationIndex = destinationOffset
        val writeNextChar = { value: Char ->
            destination[destinationIndex++] = value
        }

        for (byte in source)
            inputByte(byte, writeNextChar)

        return destinationIndex - destinationOffset
    }

    public fun inputByte(value: Byte, callback: (Char) -> Unit): Unit {

        val writeChars = { valueInt: Int ->
            when (valueInt) {
                in 0x0000..0xD7FF, in 0xE000..0xFFFF -> {
                    callback(valueInt.toChar())
                }
                in 0x010000..0x10FFFF -> {
                    callback(valueInt.highSurrogate())
                    callback(valueInt.lowSurrogate())
                }
                else -> {
                    callback(REPLACEMENT_CHAR)
                }
            }
        }

        inputNextByte(value, writeChars)
    }

    protected abstract fun inputNextByte(value: Byte, callback: (Int) -> Unit): Unit

    public abstract fun reset(): Unit
}
