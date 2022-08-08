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

public abstract class Encoder(private val callback: (Byte) -> Unit) {

    private val scalarValueCallback = { scalarValue: Int ->
        inputScalarValue(scalarValue, callback)
    }
    private val surrogateComposer = SurrogateComposer(scalarValueCallback)

    public abstract fun maxBytesNeeded(charCount: Int): Int

    public fun input(value: Char): Unit = surrogateComposer.input(value)

    protected abstract fun inputScalarValue(value: Int, callback: (Byte) -> Unit): Unit

    public fun reset(): Unit {
        surrogateComposer.reset()
        resetState()
    }

    protected open fun resetState(): Unit { }
}
