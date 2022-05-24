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

public class Utf16BeEncoder : Utf16Encoder() {

    protected override fun inputScalarValue(value: Int, callback: (Byte) -> Unit): Unit =
        if (value <= 0xFFFF) {
            callback((value ushr 8).toByte())
            callback((value and 0xFF).toByte())
        } else {
            val highSurrogate = value.highSurrogate()
            val lowSurrogate = value.lowSurrogate()
            callback((highSurrogate ushr 8).toByte())
            callback((highSurrogate and 0xFF).toByte())
            callback((lowSurrogate ushr 8).toByte())
            callback((lowSurrogate and 0xFF).toByte())
        }
}
