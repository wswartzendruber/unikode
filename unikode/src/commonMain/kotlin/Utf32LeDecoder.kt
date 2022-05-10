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

public class Utf32LeDecoder : Decoder() {

    private val currentBytes = ByteArray(3)
    private var currentByteCount = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount / 2

    protected override fun nextByte(value: Byte, callback: (Int) -> Unit): Unit =
        if (currentByteCount < 3) {
            currentBytes[currentByteCount++] = value
        } else {
            currentByteCount = 0
            callback(
                (currentBytes[0].toInt() and 0xFF) or
                (currentBytes[1].toInt() and 0xFF shl 8) or
                (currentBytes[2].toInt() and 0xFF shl 16) or
                (value.toInt() and 0xFF shl 24)
            )
        }

    public override fun reset(): Unit {
        currentBytes[0] = 0x00
        currentBytes[1] = 0x00
        currentBytes[2] = 0x00
        currentByteCount = 0
    }
}
