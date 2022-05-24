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

public class Utf8Decoder : Decoder() {

    private var continuing = false
    private val currentBytes = IntArray(4)
    private var currentBytesExpected = 0
    private var currentByteCount = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    protected override fun inputNextByte(value: Byte, callback: (Int) -> Unit): Unit {

        val valueInt = value.toInt()

        if (!continuing) {
            when {
                valueInt and 0x80 == 0x00 -> {
                    callback(valueInt)
                }
                valueInt and 0xE0 == 0xC0 -> {
                    continuing = true
                    currentBytes[0] = valueInt
                    currentBytesExpected = 2
                    currentByteCount = 1
                }
                valueInt and 0xF0 == 0xE0 -> {
                    continuing = true
                    currentBytes[0] = valueInt
                    currentBytesExpected = 3
                    currentByteCount = 1
                }
                valueInt and 0xF8 == 0xF0 -> {
                    continuing = true
                    currentBytes[0] = valueInt
                    currentBytesExpected = 4
                    currentByteCount = 1
                }
                else -> {
                    callback(REPLACEMENT_CHAR.code)
                }
            }
        } else {
            if (valueInt and 0xC0 == 0x80) {
                currentBytes[currentByteCount++] = valueInt
                if (currentByteCount == currentBytesExpected) {
                    val scalarValue = when (currentBytesExpected) {
                        2 -> {
                            (currentBytes[0] and 0x1F shl 6) or
                                (currentBytes[1] and 0x3F)
                        }
                        3 -> {
                            (currentBytes[0] and 0x0F shl 12) or
                                (currentBytes[1] and 0x3F shl 6) or
                                (currentBytes[2] and 0x3F)
                        }
                        4 -> {
                            (currentBytes[0] and 0x07 shl 18) or
                                (currentBytes[1] and 0x3F shl 12) or
                                (currentBytes[2] and 0x3F shl 6) or
                                (currentBytes[3] and 0x3F)
                        }
                        else -> {
                            throw IllegalStateException("Internal state is irrational.")
                        }
                    }
                    reset()
                    callback(scalarValue)
                }
            } else {
                reset()
                callback(REPLACEMENT_CHAR.code)
                inputNextByte(value, callback)
            }
        }
    }

    public override fun reset(): Unit {
        continuing = false
        currentBytes[0] = 0x00
        currentBytes[1] = 0x00
        currentBytes[2] = 0x00
        currentBytes[3] = 0x00
        currentBytesExpected = 0
        currentByteCount = 0
    }
}
