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

public abstract class Encoder {

    private var instanceHighSurrogate = -1

    public abstract fun maxBytesNeeded(charCount: Int): Int

    public abstract fun maxCharsPossible(byteCount: Int): Int

    public fun encode(
        source: CharSequence,
        destination: ByteArray,
        sourceStartIndex: Int = 0,
        sourceEndIndex: Int = source.length,
        destinationOffset: Int = 0,
    ): Int {

        require(sourceStartIndex >= 0 && sourceStartIndex <= sourceEndIndex) {
            "sourceStartIndex must be between zero and sourceEndIndex, inclusive."
        }
        require(sourceEndIndex <= source.length) {
            "sourceEndIndex exceeds the number of characters in the source."
        }

        val subSource = source.subSequence(sourceStartIndex, sourceEndIndex)
        var destinationIndex = destinationOffset
        val writeNextByte = { value: Byte ->
            destination[destinationIndex++] = value
        }

        for (char in subSource)
            inputChar(char, writeNextByte)

        return destinationIndex - destinationOffset
    }

    public fun encode(
        source: CharArray,
        destination: ByteArray,
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

        val subSource = source.slice(sourceStartIndex until sourceEndIndex)
        var destinationIndex = destinationOffset
        val writeNextByte = { value: Byte ->
            destination[destinationIndex++] = value
        }

        for (char in subSource)
            inputChar(char, writeNextByte)

        return destinationIndex - destinationOffset
    }

    public fun encode(
        source: Iterable<Char>,
        destination: ByteArray,
        destinationOffset: Int = 0,
    ): Int {

        var destinationIndex = destinationOffset
        val writeNextByte = { value: Byte ->
            destination[destinationIndex++] = value
        }

        for (char in source)
            inputChar(char, writeNextByte)

        return destinationIndex - destinationOffset
    }

    public fun inputChar(value: Char, callback: (Byte) -> Unit): Unit {

        val highSurrogate = instanceHighSurrogate
        val valueInt = value.code

        when {
            !value.isSurrogate() -> {
                if (highSurrogate != -1) {
                    inputCodePoint(REPLACEMENT_CHAR.code, callback)
                    instanceHighSurrogate = -1
                }
                inputCodePoint(valueInt, callback)
            }
            value.isHighSurrogate() -> {
                if (highSurrogate != -1)
                    inputCodePoint(REPLACEMENT_CHAR.code, callback)
                instanceHighSurrogate = valueInt
            }
            value.isLowSurrogate() -> {
                if (highSurrogate != -1) {
                    inputCodePoint(codePoint(highSurrogate, valueInt), callback)
                    instanceHighSurrogate = -1
                } else {
                    inputCodePoint(REPLACEMENT_CHAR.code, callback)
                }
            }
            else -> {
                throw IllegalStateException("Internal state is irrational.")
            }
        }
    }

    protected abstract fun inputCodePoint(value: Int, callback: (Byte) -> Unit): Unit

    public fun reset(): Unit {
        instanceHighSurrogate = -1
        resetState()
    }

    protected open fun resetState(): Unit { }
}
