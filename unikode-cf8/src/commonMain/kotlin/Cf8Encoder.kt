/*
 * Copyright 2024 William Swartzendruber
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

package org.unikode.cf8

import org.unikode.Encoder
import org.unikode.SurrogateValidator

public class Cf8Encoder(callback: (Byte) -> Unit) : Encoder(callback) {

    private val surrogateValidator = SurrogateValidator({ codeUnit: Char ->

        val code = codeUnit.code

        when {
            codeUnit < '\u00A0' -> {
                callback(code.toByte())
            }
            codeUnit < '\u0400' -> {
                callback(0xE0.or(code.ushr(6)).toByte())
                callback(0xA0.plus(code.and(0x3F)).toByte())
            }
            else -> {
                callback(0xF0.or(code.ushr(12)).toByte())
                callback(0xA0.plus(code.ushr(6).and(0x3F)).toByte())
                callback(0xA0.plus(code.and(0x3F)).toByte())
            }
        }
    })

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 3

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
