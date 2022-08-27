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

package org.unikode.stf7

import org.unikode.Encoder
import org.unikode.SurrogateComposer

public class Stf7Encoder(callback: (Byte) -> Unit) : Encoder(callback) {

    private val surrogateComposer = SurrogateComposer({ scalarValue: Int ->
        when {
            scalarValue < 0x80 && directEncoded[scalarValue] -> {
                callback(scalarValue.toByte())
            }
            scalarValue < 0x100 -> {
                callback(packedInitial[scalarValue ushr 4])
                callback(packedClosing[scalarValue and 0xF])
            }
            scalarValue < 0x1000 -> {
                callback(packedInitial[scalarValue ushr 8])
                callback(packedInitial[scalarValue ushr 4 and 0xF])
                callback(packedClosing[scalarValue and 0xF])
            }
            scalarValue < 0x10000 -> {
                callback(packedInitial[scalarValue ushr 12])
                callback(packedInitial[scalarValue ushr 8 and 0xF])
                callback(packedInitial[scalarValue ushr 4 and 0xF])
                callback(packedClosing[scalarValue and 0xF])
            }
            scalarValue < 0x100000 -> {
                callback(packedInitial[scalarValue ushr 16])
                callback(packedInitial[scalarValue ushr 12 and 0xF])
                callback(packedInitial[scalarValue ushr 8 and 0xF])
                callback(packedInitial[scalarValue ushr 4 and 0xF])
                callback(packedClosing[scalarValue and 0xF])
            }
            scalarValue < 0x110000 -> {
                callback(packedInitial[scalarValue ushr 20])
                callback(packedInitial[scalarValue ushr 16 and 0xF])
                callback(packedInitial[scalarValue ushr 12 and 0xF])
                callback(packedInitial[scalarValue ushr 8 and 0xF])
                callback(packedInitial[scalarValue ushr 4 and 0xF])
                callback(packedClosing[scalarValue and 0xF])
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

    private companion object {

        private val directEncoded = booleanArrayOf(
            true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true,
            true, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false,
            true, true, true, true, true, true, true, true,
            true, true, false, false, false, false, false, false,
            false, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true,
            true, true, true, false, false, false, false, false,
            false, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true,
            true, true, true, false, false, false, false, true,
        )
        private val packedInitial = byteArrayOf(
            0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28,
            0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x3A,
        )
        private val packedClosing = byteArrayOf(
            0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x40, 0x5B, 0x5C,
            0x5D, 0x5E, 0x5F, 0x60, 0x7B, 0x7C, 0x7D, 0x7E,
        )
    }
}
