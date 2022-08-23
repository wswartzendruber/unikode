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
import org.unikode.SurrogateValidator

public class Htf7Encoder(callback: (Byte) -> Unit) : Encoder(callback) {

    private val surrogateValidator = SurrogateValidator({ codeUnit: Char ->

        val value = codeUnit.code

        if (value < 0x40) {
            callback(value.toByte())
        } else {
            callback((0x40 or (value ushr 12 and 0xF)).toByte())
            callback((0x50 or (value ushr 8 and 0xF)).toByte())
            callback((0x60 or (value ushr 4 and 0xF)).toByte())
            callback((0x70 or (value and 0xF)).toByte())
        }
    })

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 4

    public override fun input(value: Char): Unit {
        surrogateValidator.input(value)
    }

    public override fun flush(): Unit {
        surrogateValidator.flush()
    }

    public override fun reset(): Unit {
        surrogateValidator.reset()
    }
}
