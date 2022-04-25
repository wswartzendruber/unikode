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

public class Utf16BeEncoder : Encoder() {

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 2

    public override fun maxCharsPossible(byteCount: Int): Int = byteCount / 2

    protected override fun writeNextCodePoint(
        destination: ByteArray,
        offset: Int,
        value: Int,
    ): Int = if (value <= 0xFFFF) {
        destination[offset] = (value ushr 8).toByte()
        destination[offset + 1] = (value and 0xFF).toByte()
        2
    } else {
        val highSurrogate = value.highSurrogate()
        val lowSurrogate = value.lowSurrogate()
        destination[offset] = (highSurrogate ushr 8).toByte()
        destination[offset + 1] = (highSurrogate and 0xFF).toByte()
        destination[offset + 2] = (lowSurrogate ushr 8).toByte()
        destination[offset + 3] = (lowSurrogate and 0xFF).toByte()
        4
    }

    private companion object {

        private fun Int.highSurrogate() = ((this - 0x10000) ushr 10) + 0xD800

        private fun Int.lowSurrogate() = ((this - 0x10000) and 0x3FF) + 0xDC00
    }
}
