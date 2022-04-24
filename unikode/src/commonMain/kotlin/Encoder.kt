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

    public fun encode(
        source: CharSequence,
        destination: ByteArray,
        sourceStartIndex: Int = 0,
        sourceEndIndex: Int = source.length,
        destinationOffset: Int = 0,
    ): Int {

        require(sourceStartIndex <= sourceEndIndex) {
            "sourceStartIndex must be equal to or less than sourceEndIndex."
        }

        val charsToEncode = sourceEndIndex - sourceStartIndex

        require(sourceEndIndex <= source.length) {
            "sourceEndIndex exceeds the number of characters in the source."
        }

        val iterator = source.iterator()

        repeat(sourceStartIndex) {
            iterator.next()
        }

        val bytesEncoded = encode(iterator, charsToEncode, destination, destinationOffset)

        return bytesEncoded
    }

    public abstract fun encode(
        source: CharIterator,
        sourceCount: Int,
        destination: ByteArray,
        destinationOffset: Int = 0,
    ): Int

    public abstract fun maxBytesNeeded(charCount: Int): Int

    public abstract fun reset(): Unit
}
