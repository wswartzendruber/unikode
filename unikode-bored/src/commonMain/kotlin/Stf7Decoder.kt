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

public class Stf7Decoder(callback: (Char) -> Unit) : Decoder(callback) {

    private val surrogateDecomposer = SurrogateDecomposer(callback)
    private var continuing = false
    private var currentScalarValue = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    public override fun input(value: Byte): Unit {

        val valueInt = value.toInt() and 0xFF

        when (valueInt) {
            in directRange1, in directRange2, in directRange3, in directRange4 -> {
                if (continuing) {
                    continuing = false
                    surrogateDecomposer.input(currentScalarValue)
                }
                surrogateDecomposer.input(valueInt)
            }
            in encodedRangeInitial -> {
                if (continuing)
                    surrogateDecomposer.input(currentScalarValue)
                continuing = true
                currentScalarValue = encodedMapping[valueInt]
            }
            in encodedRangeContinuing -> {
                currentScalarValue = (currentScalarValue shl 4) or encodedMapping[valueInt]
            }
            else -> {
                reset()
                callback(REPLACEMENT_CHAR)
            }
        }
    }

    public override fun flush(): Unit {
        if (continuing)
            surrogateDecomposer.input(currentScalarValue)
        continuing = false
    }

    public override fun reset(): Unit {
        continuing = false
        currentScalarValue = 0
    }

    private companion object {

        private val directRange1 = 0x00..0x20
        private val directRange2 = 0x30..0x39
        private val directRange3 = 0x41..0x5A
        private val directRange4 = 0x61..0x7A
        private val encodedRangeInitial = 0x21..0x3A
        private val encodedRangeContinuing = 0x3B..0x7E
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
