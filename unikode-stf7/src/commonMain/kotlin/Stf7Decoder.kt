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

package org.unikode.stf7

import org.unikode.REPLACEMENT_CHAR
import org.unikode.REPLACEMENT_CODE
import org.unikode.Decoder
import org.unikode.SurrogateDecomposer

public class Stf7Decoder(callback: (Char) -> Unit) : Decoder(callback) {

    private val surrogateDecomposer = SurrogateDecomposer(callback)
    private var bytesUsed = 0
    private var currentScalarValue = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    public override fun input(value: Byte): Unit {

        val valueInt = value.toInt() and 0xFF

        when {
            valueInt < 0x80 && encodedMapping[valueInt] == -1 -> {
                if (bytesUsed > 0) {
                    bytesUsed = 0
                    currentScalarValue = 0
                    callback(REPLACEMENT_CHAR)
                }
                callback(valueInt.toChar())
            }
            valueInt in encodedRangeContinuing -> {
                bytesUsed++
                currentScalarValue = (currentScalarValue shl 4) or encodedMapping[valueInt]
            }
            valueInt in encodedRangeClosing -> {
                bytesUsed++
                currentScalarValue = (currentScalarValue shl 4) or encodedMapping[valueInt]
                surrogateDecomposer.input(
                    when (bytesUsed) {
                        2 -> {
                            if (
                                currentScalarValue > 127 ||
                                encodedMapping[currentScalarValue] != -1
                            )
                                currentScalarValue
                            else
                                REPLACEMENT_CODE
                        }
                        3 -> {
                            if (currentScalarValue > 0xFF)
                                currentScalarValue
                            else
                                REPLACEMENT_CODE
                        }
                        4 -> {
                            if (currentScalarValue > 0xFFF)
                                currentScalarValue
                            else
                                REPLACEMENT_CODE
                        }
                        5 -> {
                            if (currentScalarValue > 0xFFFF)
                                currentScalarValue
                            else
                                REPLACEMENT_CODE
                        }
                        6 -> {
                            if (currentScalarValue > 0xFFFFF && currentScalarValue < 0x110000)
                                currentScalarValue
                            else
                                REPLACEMENT_CODE
                        }
                        else -> {
                            REPLACEMENT_CODE
                        }
                    }
                )
                bytesUsed = 0
                currentScalarValue = 0
            }
            else -> {
                reset()
                callback(REPLACEMENT_CHAR)
            }
        }
    }

    public override fun flush(): Unit {
        if (currentScalarValue != 0)
            callback(REPLACEMENT_CHAR)
        bytesUsed = 0
        currentScalarValue = 0
    }

    public override fun reset(): Unit {
        bytesUsed = 0
        currentScalarValue = 0
    }

    private companion object {

        private val encodedRangeContinuing = 0x21..0x3A
        private val encodedRangeClosing = 0x3B..0x7E
        private val encodedMapping = intArrayOf(
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 15, 0, 1, 2, 3, 4, 5, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 6, 7, 8, 9, 10, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 13, 14, 15, -1,
        )
    }
}
