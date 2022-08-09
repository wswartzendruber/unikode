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

public class Utf8Encoder(callback: (Byte) -> Unit) : Encoder(callback) {

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
                callback((0xE0 or (scalarValue ushr 12)).toByte())
                callback((0x80 or (scalarValue ushr 6 and 0x3F)).toByte())
                callback((0x80 or (scalarValue and 0x3F)).toByte())
            }
            scalarValue < 0x110000 -> {
                callback((0xF0 or (scalarValue ushr 18)).toByte())
                callback((0x80 or (scalarValue ushr 12 and 0x3F)).toByte())
                callback((0x80 or (scalarValue ushr 6 and 0x3F)).toByte())
                callback((0x80 or (scalarValue and 0x3F)).toByte())
            }
            else -> {
                throw IllegalStateException("Got invalid value from superclass.")
            }
        }
    })

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 3

    public override fun input(value: Char): Unit = surrogateComposer.input(value)

    public override fun reset(): Unit = surrogateComposer.reset()
}
