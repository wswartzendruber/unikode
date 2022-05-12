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

public abstract class Utf16Decoder : Decoder() {

    private var instanceBufferedByte: Int = -1
    private var instanceHighSurrogate: Int = -1

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount / 2

    protected override fun nextByte(value: Int, callback: (Int) -> Unit): Unit {

        val bufferedByte = instanceBufferedByte
        val highSurrogate = instanceHighSurrogate

        if (highSurrogate == -1) {
            if (bufferedByte == -1) {
                instanceBufferedByte = value
            } else {
                val char = bytePairToChar(bufferedByte, value)
                when {
                    !char.isSurrogate() -> {
                        reset()
                        callback(char)
                    }
                    char.isHighSurrogate() -> {
                        instanceHighSurrogate = char
                        instanceBufferedByte = -1
                    }
                    char.isLowSurrogate() -> {
                        reset()
                        callback(REPLACEMENT_CHAR.code)
                    }
                    else -> {
                        throw IllegalStateException("Internal state is irrational.")
                    }
                }
            }
        } else {
            if (bufferedByte == -1) {
                instanceBufferedByte = value
            } else {
                val char = bytePairToChar(bufferedByte, value)
                if (char.isLowSurrogate()) {
                    val codePoint = codePoint(highSurrogate, char)
                    reset()
                    callback(codePoint)
                } else {
                    reset()
                    callback(REPLACEMENT_CHAR.code)
                }
            }
        }
    }

    public override fun reset(): Unit {
        instanceBufferedByte = -1
        instanceHighSurrogate = -1
    }

    protected abstract fun bytePairToChar(high: Int, low: Int): Int

    protected companion object {

        protected fun Int.isSurrogate(): Boolean = this in 0xD800..0xDFFF

        protected fun Int.isHighSurrogate(): Boolean = this in 0xD800..0xDBFF

        protected fun Int.isLowSurrogate(): Boolean = this in 0xDC00..0xDFFF
    }
}