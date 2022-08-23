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
import org.unikode.SurrogateDecomposer

public class Htf7Decoder(callback: (Char) -> Unit) : Decoder(callback) {

    private val surrogateDecomposer = SurrogateDecomposer(callback)
    private var continuing = false
    private var currentScalarValue = 0
    private var currentBytesExpected = 0
    private var currentByteIndex = 0
    private var minimumScalarValue = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    public override fun input(value: Byte): Unit {

        val valueInt = value.toInt()

        if (!continuing) {
            if (valueInt and 0xC0 == 0x00) {
                surrogateDecomposer.input(valueInt)
            } else {
                continuing = true
                currentByteIndex = 1
                when {
                    valueInt and 0xF0 == 0x40 -> {
                        currentScalarValue = valueInt and 0x0F
                        currentBytesExpected = 2
                        minimumScalarValue = 0x39
                    }
                    valueInt and 0xF0 == 0x50 -> {
                        currentScalarValue = valueInt and 0x0F
                        currentBytesExpected = 4
                        minimumScalarValue = 0xFF
                    }
                    valueInt and 0xF0 == 0x60 -> {
                        currentScalarValue = valueInt and 0x0F
                        currentBytesExpected = 6
                        minimumScalarValue = 0xFFFF
                    }
                    else -> {
                        reset()
                        callback(REPLACEMENT_CHAR)
                    }
                }
            }
        } else {
            if (valueInt and 0xF0 == 0x70) {
                currentScalarValue = (currentScalarValue shl 4) or (valueInt and 0x0F)
                if (++currentByteIndex == currentBytesExpected) {
                    if (currentScalarValue > minimumScalarValue) {
                        continuing = false
                        surrogateDecomposer.input(currentScalarValue)
                    } else {
                        reset()
                        callback(REPLACEMENT_CHAR)
                    }
                }
            } else {
                reset()
                callback(REPLACEMENT_CHAR)
                input(value)
            }
        }
    }

    public override fun flush(): Unit {
        if (continuing) {
            callback(REPLACEMENT_CHAR)
            continuing = false
        }
    }

    public override fun reset(): Unit {
        continuing = false
        currentScalarValue = 0
        currentBytesExpected = 0
        currentByteIndex = 0
        minimumScalarValue = 0
    }
}
