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

public class Utf32BeEncoder : Utf32Encoder() {

    protected override fun writeNextCodePoint(
        destination: ByteArray,
        offset: Int,
        value: Int,
    ): Int {

        destination[offset] = 0x0
        destination[offset + 1] = (value and 0xFF0000 ushr 16).toByte()
        destination[offset + 2] = (value and 0xFF00 ushr 8).toByte()
        destination[offset + 3] = (value and 0xFF).toByte()

        return 4
    }
}
