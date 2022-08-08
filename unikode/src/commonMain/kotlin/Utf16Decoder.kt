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

public abstract class Utf16Decoder(callback: (Char) -> Unit) : Decoder(callback) {

    private val surrogateChecker = SurrogateChecker(callback)
    private var bufferedByte = -1

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount / 2

    protected override fun inputByte(value: Byte, callback: (Int) -> Unit): Unit {

        val valueInt = value.toInt() and 0xFF

        if (bufferedByte == -1) {
            bufferedByte = valueInt
        } else {
            surrogateChecker.input(bytePairToChar(bufferedByte, valueInt).toChar())
            bufferedByte = -1
        }
    }

    public override fun reset(): Unit {
        surrogateChecker.reset()
        bufferedByte = -1
    }

    protected abstract fun bytePairToChar(high: Int, low: Int): Int
}
