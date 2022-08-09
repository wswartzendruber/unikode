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

package org.unikode.bad

import org.unikode.REPLACEMENT_CODE
import org.unikode.Decoder
import org.unikode.SurrogateValidator

public class Cesu8Decoder(callback: (Char) -> Unit) : Decoder(callback) {

    private val surrogateValidator = SurrogateValidator(callback)
    private var continuing = false
    private val currentBytes = IntArray(6)
    private var currentBytesExpected = 0
    private var currentByteIndex = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    public override fun input(value: Byte): Unit {

        val valueInt = value.toInt()

        if (!continuing) {
            when {
                valueInt and 0x80 == 0x00 -> {
                    surrogateValidator.input(valueInt)
                }
                valueInt and 0xE0 == 0xC0 -> {
                    continuing = true
                    currentBytes[0] = valueInt
                    currentBytesExpected = 2
                    currentByteIndex = 1
                }
                valueInt and 0xF0 == 0xE0 -> {
                    continuing = true
                    currentBytes[0] = valueInt
                    currentBytesExpected = 3
                    currentByteIndex = 1
                }
                valueInt and 0xF8 == 0xF0 -> {
                    continuing = true
                    currentBytes[0] = valueInt
                    currentBytesExpected = 4
                    currentByteIndex = 1
                }
                valueInt and 0xFC == 0xF8 -> {
                    continuing = true
                    currentBytesExpected = 5
                    currentByteIndex = 1
                }
                valueInt and 0xFE == 0xFC -> {
                    continuing = true
                    currentBytesExpected = 6
                    currentByteIndex = 1
                }
                else -> {
                    reset()
                    surrogateValidator.input(REPLACEMENT_CODE)
                }
            }
        } else {
            if (valueInt and 0xC0 == 0x80) {
                currentBytes[currentByteIndex++] = valueInt
                if (currentByteIndex == currentBytesExpected) {
                    val codeUnit = when (currentBytesExpected) {
                        2 -> {
                            val temp = (currentBytes[0] and 0x1F shl 6) or
                                (currentBytes[1] and 0x3F)
                            if (temp > 0x7F) {
                                temp
                            } else {
                                reset()
                                REPLACEMENT_CODE
                            }
                        }
                        3 -> {
                            val temp = (currentBytes[0] and 0x0F shl 12) or
                                (currentBytes[1] and 0x3F shl 6) or
                                (currentBytes[2] and 0x3F)
                            if (temp > 0x7FF) {
                                temp
                            } else {
                                reset()
                                REPLACEMENT_CODE
                            }
                        }
                        4, 5, 6 -> {
                            reset()
                            REPLACEMENT_CODE
                        }
                        else -> {
                            throw IllegalStateException("Internal state is irrational.")
                        }
                    }
                    continuing = false
                    surrogateValidator.input(codeUnit)
                }
            } else {
                reset()
                surrogateValidator.input(REPLACEMENT_CODE)
                input(value)
            }
        }
    }

    public override fun reset(): Unit {
        surrogateValidator.flush()
        resetUtf8()
    }

    private fun resetUtf8() {
        continuing = false
        currentBytes[0] = 0x00
        currentBytes[1] = 0x00
        currentBytes[2] = 0x00
        currentBytes[3] = 0x00
        currentBytes[4] = 0x00
        currentBytes[5] = 0x00
        currentBytesExpected = 0
        currentByteIndex = 0
    }
}
