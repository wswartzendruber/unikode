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

public class Cesu8Encoder(callback: (Byte) -> Unit) : Encoder(callback) {

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 3

    protected override fun inputScalarValue(value: Int, callback: (Byte) -> Unit): Unit =
        when (value) {
            in 0x00..0x7F -> {
                callback(value.toByte())
            }
            in 0x080..0x7FF -> {
                callback((0xC0 or (value ushr 6)).toByte())
                callback((0x80 or (value and 0x3F)).toByte())
            }
            in 0x0800..0xFFFF -> {
                writeTripleByte(value, callback)
            }
            in 0x010000..0x10FFFF -> {
                writeTripleByte(value.highSurrogate(), callback)
                writeTripleByte(value.lowSurrogate(), callback)
            }
            else -> {
                throw IllegalStateException("Got invalid value from superclass.")
            }
        }

    private fun writeTripleByte(value: Int, callback: (Byte) -> Unit) {
        callback((0xE0 or (value ushr 12)).toByte())
        callback((0x80 or (value ushr 6 and 0x3F)).toByte())
        callback((0x80 or (value and 0x3F)).toByte())
    }

    private fun Int.highSurrogate(): Int = ((this - 0x10000) ushr 10) + 0xD800

    private fun Int.lowSurrogate(): Int = ((this - 0x10000) and 0x3FF) + 0xDC00
}
