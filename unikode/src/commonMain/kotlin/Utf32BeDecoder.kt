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

public class Utf32BeDecoder(callback: (Char) -> Unit) : Decoder(callback) {

    private val surrogateDecomposer = SurrogateDecomposer(callback)
    private val currentBytes = IntArray(4)
    private var currentByteIndex = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount / 2

    public override fun input(value: Byte): Unit {
        currentBytes[currentByteIndex++] = value.toInt() and 0xFF
        if (currentByteIndex == 4) {
            surrogateDecomposer.input(currentBytesToScalarValue())
            currentByteIndex = 0
        }
    }

    public override fun flush(): Unit {
        if (currentByteIndex != 0) {
            callback(REPLACEMENT_CHAR)
            currentByteIndex = 0
        }
    }

    public override fun reset(): Unit {
        currentBytes[0] = 0x00
        currentBytes[1] = 0x00
        currentBytes[2] = 0x00
        currentBytes[3] = 0x00
        currentByteIndex = 0
    }

    private fun currentBytesToScalarValue(): Int =
        (currentBytes[0] shl 24) or
        (currentBytes[1] shl 16) or
        (currentBytes[2] shl 8) or
        currentBytes[3]
}
