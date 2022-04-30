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

public class Utf16BeDecoder : Decoder() {

    private var instanceBufferedByte: Byte? = null
    private var instanceHighSurrogate: Char? = null

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount / 2

    protected override fun nextByte(value: Byte): Int {

        val bufferedByte = instanceBufferedByte
        val highSurrogate = instanceHighSurrogate

        return if (highSurrogate == null) {
            if (bufferedByte == null) {
                instanceBufferedByte = value
                -1
            } else {
                val char = bytePairToChar(bufferedByte, value)
                when {
                    !char.isSurrogate() -> {
                        reset()
                        char.code
                    }
                    char.isHighSurrogate() -> {
                        instanceHighSurrogate = char
                        instanceBufferedByte = null
                        -1
                    }
                    char.isLowSurrogate() -> {
                        reset()
                        REPLACEMENT_CHAR.code
                    }
                    else -> {
                        throw IllegalStateException("Internal state is irrational.")
                    }
                }
            }
        } else {
            if (bufferedByte == null) {
                instanceBufferedByte = value
                -1
            } else {
                val char = bytePairToChar(bufferedByte, value)
                if (char.isLowSurrogate()) {
                    val codePoint = codePoint(highSurrogate, char)
                    reset()
                    codePoint
                } else {
                    reset()
                    REPLACEMENT_CHAR.code
                }
            }
        }
    }

    public override fun reset(): Unit {
        instanceBufferedByte =  null
        instanceHighSurrogate = null
    }

    private companion object {

        private fun bytePairToChar(high: Byte, low: Byte) =
            ((high.toInt() shl 8) or (low.toInt() and 0xFF)).toChar()
    }
}
