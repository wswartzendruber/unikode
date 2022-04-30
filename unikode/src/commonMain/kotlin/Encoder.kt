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

    private var instanceHighSurrogate: Char? = null

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

        val charsToEncode = sourceEndIndex - sourceStartIndex

        require(sourceEndIndex <= source.length) {
            "sourceEndIndex exceeds the number of characters in the source."
        }

        val subSource = source.subSequence(sourceStartIndex, sourceEndIndex)
        val iterator = subSource.iterator()
        val bytesEncoded = encode(iterator, charsToEncode, destination, destinationOffset)

        return bytesEncoded
    }

    public fun encode(
        source: Iterator<Char>,
        sourceLimit: Int,
        destination: ByteArray,
        destinationOffset: Int = 0,
    ): Int {

        var sourceCount = 0
        var destinationIndex = destinationOffset

        while (source.hasNext() && sourceCount < sourceLimit) {

            val highSurrogate = instanceHighSurrogate
            val currentChar = source.next()

            when {
                !currentChar.isSurrogate() -> {
                    if (highSurrogate != null) {
                        destinationIndex += writeNextCodePoint(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR.code,
                        )
                        instanceHighSurrogate = null
                    }
                    destinationIndex += writeNextCodePoint(
                        destination, destinationIndex,
                        currentChar.code,
                    )
                }
                currentChar.isHighSurrogate() -> {
                    if (highSurrogate != null) {
                        destinationIndex += writeNextCodePoint(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR.code,
                        )
                    }
                    instanceHighSurrogate = currentChar
                }
                currentChar.isLowSurrogate() -> {
                    if (highSurrogate != null) {
                        destinationIndex += writeNextCodePoint(
                            destination, destinationIndex,
                            codePoint(highSurrogate, currentChar),
                        )
                        instanceHighSurrogate = null
                    } else {
                        destinationIndex += writeNextCodePoint(
                            destination, destinationIndex,
                            REPLACEMENT_CHAR.code,
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

    protected abstract fun writeNextCodePoint(
        destination: ByteArray,
        offset: Int,
        value: Int,
    ): Int

    public fun reset(): Unit {
        resetState()
    }

    protected open fun resetState(): Unit { }
}
