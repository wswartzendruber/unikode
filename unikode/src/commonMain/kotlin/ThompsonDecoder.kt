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

public class ThompsonDecoder(private val callback: (Int) -> Unit) {

    private var continuing = false
    private var currentScalarValue = 0
    private var currentBytesExpected = 0
    private var currentByteIndex = 0
    private var minimumScalarValue = 0

    public fun input(value: Byte): Unit {

        val valueInt = value.toInt()

        if (!continuing) {
            if (valueInt and 0x80 == 0x00) {
                callback(valueInt)
            } else {
                continuing = true
                currentByteIndex = 1
                when {
                    valueInt and 0xE0 == 0xC0 -> {
                        currentScalarValue = valueInt and 0x1F
                        currentBytesExpected = 2
                        minimumScalarValue = 0x7F
                    }
                    valueInt and 0xF0 == 0xE0 -> {
                        currentScalarValue = valueInt and 0x0F
                        currentBytesExpected = 3
                        minimumScalarValue = 0x7FF
                    }
                    valueInt and 0xF8 == 0xF0 -> {
                        currentScalarValue = valueInt and 0x07
                        currentBytesExpected = 4
                        minimumScalarValue = 0xFFFF
                    }
                    valueInt and 0xFC == 0xF8 -> {
                        currentScalarValue = valueInt and 0x03
                        currentBytesExpected = 5
                        minimumScalarValue = 0x1FFFFF
                    }
                    valueInt and 0xFE == 0xFC -> {
                        currentScalarValue = valueInt and 0x01
                        currentBytesExpected = 6
                        minimumScalarValue = 0x3FFFFFF
                    }
                    else -> {
                        reset()
                        callback(REPLACEMENT_CODE)
                    }
                }
            }
        } else {
            if (valueInt and 0xC0 == 0x80) {
                currentScalarValue = (currentScalarValue shl 6) or (valueInt and 0x3F)
                if (++currentByteIndex == currentBytesExpected) {
                    if (currentScalarValue > minimumScalarValue) {
                        continuing = false
                        callback(currentScalarValue)
                    } else {
                        reset()
                        callback(REPLACEMENT_CODE)
                    }
                }
            } else {
                reset()
                callback(REPLACEMENT_CODE)
                input(value)
            }
        }
    }

    public fun flush(): Unit {
        if (continuing) {
            callback(REPLACEMENT_CODE)
            continuing = false
        }
    }

    public fun reset(): Unit {
        continuing = false
        currentScalarValue = 0
        currentBytesExpected = 0
        currentByteIndex = 0
        minimumScalarValue = 0
    }
}
