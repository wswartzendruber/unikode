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

public class Utf32BeEncoder(callback: (Byte) -> Unit) : Encoder(callback) {

    private val surrogateComposer = SurrogateComposer({ scalarValue: Int ->
        callback(0x0)
        callback((scalarValue and 0xFF0000 ushr 16).toByte())
        callback((scalarValue and 0xFF00 ushr 8).toByte())
        callback((scalarValue and 0xFF).toByte())
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
