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

package org.unikode.bored

import org.unikode.Encoder
import org.unikode.SurrogateComposer

public class Htf7Encoder(callback: (Byte) -> Unit) : Encoder(callback) {

    private val surrogateComposer = SurrogateComposer({ scalarValue: Int ->
        when {
            scalarValue < 0x40 -> {
                callback(scalarValue.toByte())
            }
            scalarValue < 0x100 -> {
                callback((0x40 or (scalarValue ushr 4)).toByte())
                callback((0x70 or (scalarValue and 0xF)).toByte())
            }
            scalarValue < 0x10000 -> {
                callback((0x50 or (scalarValue ushr 12)).toByte())
                callback((0x70 or (scalarValue ushr 8 and 0xF)).toByte())
                callback((0x70 or (scalarValue ushr 4 and 0xF)).toByte())
                callback((0x70 or (scalarValue and 0xF)).toByte())
            }
            scalarValue < 0x10FFFF -> {
                callback((0x60 or (scalarValue ushr 20)).toByte())
                callback((0x70 or (scalarValue ushr 16 and 0xF)).toByte())
                callback((0x70 or (scalarValue ushr 12 and 0xF)).toByte())
                callback((0x70 or (scalarValue ushr 8 and 0xF)).toByte())
                callback((0x70 or (scalarValue ushr 4 and 0xF)).toByte())
                callback((0x70 or (scalarValue and 0xF)).toByte())
            }
            else -> {
                throw IllegalStateException("Value too large.")
            }
        }
    })

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 4

    public override fun input(value: Char): Unit {
        surrogateComposer.input(value)
    }

    public override fun flush(): Unit {
        surrogateComposer.flush()
    }

    public override fun reset(): Unit {
        surrogateComposer.reset()
    }
}
