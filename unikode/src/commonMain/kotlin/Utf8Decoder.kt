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

public class Utf8Decoder(callback: (Char) -> Unit) : Decoder(callback) {

    private var continuing = false
    private val currentBytes = IntArray(6)
    private var currentBytesExpected = 0
    private var currentByteCount = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    protected override fun inputByte(value: Byte, callback: (Int) -> Unit): Unit {

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
                valueInt and 0xFC == 0xF8 -> {
                    continuing = true
                    currentBytesExpected = 5
                    currentByteCount = 1
                }
                valueInt and 0xFE == 0xFC -> {
                    continuing = true
                    currentBytesExpected = 6
                    currentByteCount = 1
                }
                else -> {
                    callback(REPLACEMENT_CODE)
                }
            }
        } else {
            if (valueInt and 0xC0 == 0x80) {
                currentBytes[currentByteCount++] = valueInt
                if (currentByteCount == currentBytesExpected) {
                    val scalarValue = when (currentBytesExpected) {
                        2 -> {
                            val temp = (currentBytes[0] and 0x1F shl 6) or
                                (currentBytes[1] and 0x3F)
                            if (temp in twoByteRange)
                                temp
                            else
                                REPLACEMENT_CODE
                        }
                        3 -> {
                            val temp = (currentBytes[0] and 0x0F shl 12) or
                                (currentBytes[1] and 0x3F shl 6) or
                                (currentBytes[2] and 0x3F)
                            if (temp in threeByteRange)
                                temp
                            else
                                REPLACEMENT_CODE
                        }
                        4 -> {
                            val temp = (currentBytes[0] and 0x07 shl 18) or
                                (currentBytes[1] and 0x3F shl 12) or
                                (currentBytes[2] and 0x3F shl 6) or
                                (currentBytes[3] and 0x3F)
                            if (temp in fourByteRange)
                                temp
                            else
                                REPLACEMENT_CODE
                        }
                        5, 6 -> {
                            REPLACEMENT_CODE
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
                callback(REPLACEMENT_CODE)
                inputByte(value, callback)
            }
        }
    }

    public override fun reset(): Unit {
        continuing = false
        currentBytes[0] = 0x00
        currentBytes[1] = 0x00
        currentBytes[2] = 0x00
        currentBytes[3] = 0x00
        currentBytes[4] = 0x00
        currentBytes[5] = 0x00
        currentBytesExpected = 0
        currentByteCount = 0
    }

    private companion object {

        private val twoByteRange = 0x080..0x7FF
        private val threeByteRange = 0x0800..0xFFFF
        private val fourByteRange = 0x010000..0x10FFFF
    }
}
