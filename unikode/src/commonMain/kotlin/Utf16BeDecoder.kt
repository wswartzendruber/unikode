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

public class Utf16BeDecoder(callback: (Char) -> Unit) : Decoder(callback) {

    private val surrogateValidator = SurrogateValidator(callback)
    private var bufferedByte = -1

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount / 2

    public override fun input(value: Byte): Unit {
        if (bufferedByte == -1) {
            bufferedByte = value.toInt() and 0xFF
        } else {
            surrogateValidator.input(bytePairToChar(bufferedByte, value.toInt() and 0xFF))
            bufferedByte = -1
        }
    }

    public override fun flush(): Unit {
        surrogateValidator.flush()
        if (bufferedByte != -1) {
            callback(REPLACEMENT_CHAR)
            bufferedByte = -1
        }
    }

    public override fun reset(): Unit {
        surrogateValidator.reset()
        bufferedByte = -1
    }

    private companion object {

        private fun bytePairToChar(high: Int, low: Int) = (high shl 8) or low
    }
}
