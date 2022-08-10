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

public class SurrogateValidator(private val callback: (Char) -> Unit) {

    private var highSurrogate = -1

    public fun input(value: Char): Unit =
        if (highSurrogate == -1) {
            when {
                !value.isSurrogate() -> {
                    callback(value)
                }
                value.isHighSurrogate() -> {
                    highSurrogate = value.code
                }
                value.isLowSurrogate() -> {
                    callback(REPLACEMENT_CHAR)
                }
                else -> {
                    throw IllegalStateException("Internal state is irrational.")
                }
            }
        } else {
            when {
                value.isLowSurrogate() -> {
                    callback(highSurrogate.toChar())
                    callback(value)
                    highSurrogate = -1
                }
                !value.isSurrogate() -> {
                    callback(REPLACEMENT_CHAR)
                    highSurrogate = -1
                    callback(value)
                }
                value.isHighSurrogate() -> {
                    callback(REPLACEMENT_CHAR)
                    highSurrogate = value.code
                }
                else -> {
                    throw IllegalStateException("Internal state is irrational.")
                }
            }
        }

    public fun input(value: Int): Unit =
        if (highSurrogate == -1) {
            when {
                !value.isSurrogate() -> {
                    callback(value.toChar())
                }
                value.isHighSurrogate() -> {
                    highSurrogate = value
                }
                value.isLowSurrogate() -> {
                    callback(REPLACEMENT_CHAR)
                }
                else -> {
                    throw IllegalStateException("Internal state is irrational.")
                }
            }
        } else {
            when {
                value.isLowSurrogate() -> {
                    callback(highSurrogate.toChar())
                    callback(value.toChar())
                    highSurrogate = -1
                }
                !value.isSurrogate() -> {
                    callback(REPLACEMENT_CHAR)
                    highSurrogate = -1
                    callback(value.toChar())
                }
                value.isHighSurrogate() -> {
                    callback(REPLACEMENT_CHAR)
                    highSurrogate = value
                }
                else -> {
                    throw IllegalStateException("Internal state is irrational.")
                }
            }
        }

    public fun flush(): Unit {
        if (highSurrogate != -1) {
            callback(REPLACEMENT_CHAR)
            highSurrogate = -1
        }
    }

    public fun reset(): Unit {
        highSurrogate = -1
    }
}
