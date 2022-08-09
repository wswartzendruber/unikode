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

package org.unikode.bad

import org.unikode.Encoder
import org.unikode.SurrogateComposer

public class Cesu8Encoder(callback: (Byte) -> Unit) : Encoder(callback) {

    private val surrogateComposer = SurrogateComposer({ scalarValue: Int ->
        when {
            scalarValue < 0x80 -> {
                callback(scalarValue.toByte())
            }
            scalarValue < 0x800 -> {
                callback((0xC0 or (scalarValue ushr 6)).toByte())
                callback((0x80 or (scalarValue and 0x3F)).toByte())
            }
            scalarValue < 0x10000 -> {
                writeTripleByte(scalarValue)
            }
            scalarValue < 0x110000 -> {
                writeTripleByte(scalarValue.highSurrogate())
                writeTripleByte(scalarValue.lowSurrogate())
            }
            else -> {
                throw IllegalStateException("Got invalid value from superclass.")
            }
        }
    })

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 3

    public override fun input(value: Char): Unit = surrogateComposer.input(value)

    public override fun reset(): Unit = surrogateComposer.reset()

    private fun writeTripleByte(value: Int) {
        callback((0xE0 or (value ushr 12)).toByte())
        callback((0x80 or (value ushr 6 and 0x3F)).toByte())
        callback((0x80 or (value and 0x3F)).toByte())
    }

    private companion object {

        private fun Int.highSurrogate(): Int = ((this - 0x10000) ushr 10) + 0xD800

        private fun Int.lowSurrogate(): Int = ((this - 0x10000) and 0x3FF) + 0xDC00
    }
}
