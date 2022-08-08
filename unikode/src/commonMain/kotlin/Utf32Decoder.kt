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

public abstract class Utf32Decoder(callback: (Char) -> Unit) : Decoder(callback) {

    private var currentByteCount = 0

    protected val currentBytes: IntArray = IntArray(4)

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount / 2

    protected override fun inputByte(value: Byte, callback: (Int) -> Unit): Unit {
        currentBytes[currentByteCount++] = value.toInt() and 0xFF
        if (currentByteCount == 4) {
            callback(currentBytesToScalarValue())
            currentByteCount = 0
        }
    }

    public override fun reset(): Unit {
        currentBytes[0] = 0x00
        currentBytes[1] = 0x00
        currentBytes[2] = 0x00
        currentBytes[3] = 0x00
        currentByteCount = 0
    }

    protected abstract fun currentBytesToScalarValue(): Int
}
