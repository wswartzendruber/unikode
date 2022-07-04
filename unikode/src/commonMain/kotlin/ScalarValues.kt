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

public fun CharSequence.scalarValues(): Iterable<Int> = ScalarValueIterable(this.asIterable())

public fun Iterable<Char>.scalarValues(): Iterable<Int> = ScalarValueIterable(this)

public class ScalarValueIterable(private val chars: Iterable<Char>) : Iterable<Int> {

    public override operator fun iterator(): Iterator<Int> =
        ScalarValueIterator(chars.iterator())
}

public class ScalarValueIterator(private val chars: Iterator<Char>) : Iterator<Int> {

    private var bufferedChar = -1

    public override operator fun hasNext(): Boolean = bufferedChar != -1 || chars.hasNext()

    public override operator fun next(): Int {

        val nextChar =
            if (bufferedChar == -1) {
                chars.next()
            } else {
                val tempChar = bufferedChar
                bufferedChar = -1
                tempChar.toChar()
            }

        return when {
            !nextChar.isSurrogate() -> {
                nextChar.code
            }
            nextChar.isHighSurrogate() -> {
                if (chars.hasNext()) {
                    val nextNextChar = chars.next()
                    if (nextNextChar.isLowSurrogate()) {
                        scalarValue(nextChar, nextNextChar)
                    } else {
                        bufferedChar = nextNextChar.code
                        REPLACEMENT_CODE
                    }
                } else {
                    REPLACEMENT_CODE
                }
            }
            nextChar.isLowSurrogate() -> {
                REPLACEMENT_CODE
            }
            else -> {
                throw IllegalStateException("Internal state is irrational.")
            }
        }
    }
}
