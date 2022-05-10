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
    private val currentBytes = ByteArray(4)
    private var currentBytesExpected = 0
    private var currentByteCount = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    protected override fun nextByte(value: Byte, callback: (Int) -> Unit): Unit {
        if (!continuing) {
            when (value) {
                in 0x00..0x7F -> {
                    callback(value.toInt())
                }
                in -0x40..-0x21 -> {
                    continuing = true
                    currentBytes[0] = value
                    currentBytesExpected = 2
                    currentByteCount = 1
                }
                in -0x20..-0x11 -> {
                    continuing = true
                    currentBytes[0] = value
                    currentBytesExpected = 3
                    currentByteCount = 1
                }
                in -0x10..-0x09 -> {
                    continuing = true
                    currentBytes[0] = value
                    currentBytesExpected = 4
                    currentByteCount = 1
                }
                else -> {
                    callback(REPLACEMENT_CHAR.code)
                }
            }
        } else {
            if (value in -0x80..-0x41) {
                currentBytes[currentByteCount++] = value
                if (currentByteCount == currentBytesExpected) {
                    val codePoint = when (currentBytesExpected) {
                        2 -> {
                            (currentBytes[0].toInt() and 0x1F shl 6) or
                                (currentBytes[1].toInt() and 0x3F)
                        }
                        3 -> {
                            (currentBytes[0].toInt() and 0x0F shl 12) or
                                (currentBytes[1].toInt() and 0x3F shl 6) or
                                (currentBytes[2].toInt() and 0x3F)
                        }
                        4 -> {
                            (currentBytes[0].toInt() and 0x07 shl 18) or
                                (currentBytes[1].toInt() and 0x3F shl 12) or
                                (currentBytes[2].toInt() and 0x3F shl 6) or
                                (currentBytes[3].toInt() and 0x3F)
                        }
                        else -> {
                            throw IllegalStateException("Internal state is irrational.")
                        }
                    }
                    reset()
                    callback(codePoint)
                }
            } else {
                reset()
                callback(REPLACEMENT_CHAR.code)
                nextByte(value, callback)
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
