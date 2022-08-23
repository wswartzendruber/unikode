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

import org.unikode.REPLACEMENT_CHAR
import org.unikode.Decoder
import org.unikode.SurrogateValidator

public class Htf7Decoder(callback: (Char) -> Unit) : Decoder(callback) {

    private val surrogateValidator = SurrogateValidator(callback)
    private var continuing = false
    private var currentChar = 0
    private var currentByteIndex = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    public override fun input(value: Byte): Unit {

        val valueInt = value.toInt() and 0xFF

        if (!continuing) {
            when {
                valueInt < 0x40 -> {
                    callback(valueInt.toChar())
                }
                valueInt and 0xF0 == 0x40 -> {
                    continuing = true
                    currentByteIndex = 1
                    currentChar = valueInt and 0x0F
                }
                else -> {
                    reset()
                    callback(REPLACEMENT_CHAR)
                }
            }
        } else {
            val prefix = valueInt and 0xF0 or currentByteIndex++
            when (prefix) {
                0x51, 0x62, 0x73 -> {
                    currentChar = (currentChar shl 4) or (valueInt and 0x0F)
                }
                else -> {
                    reset()
                    callback(REPLACEMENT_CHAR)
                }
            }
            if (currentByteIndex == 4) {
                if (currentChar > 0x3F) {
                    continuing = false
                    callback(currentChar.toChar())
                } else {
                    reset()
                    callback(REPLACEMENT_CHAR)
                }
            }
        }
    }

    public override fun flush(): Unit {
        surrogateValidator.flush()
        if (continuing) {
            callback(REPLACEMENT_CHAR)
            continuing = false
        }
    }

    public override fun reset(): Unit {
        surrogateValidator.reset()
        continuing = false
        currentByteIndex = 0
        currentChar = 0
    }
}
