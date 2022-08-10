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

package org.unikode.bad

import org.unikode.Encoder
import org.unikode.SurrogateValidator
import org.unikode.ThompsonEncoder

public class Cesu8Encoder(callback: (Byte) -> Unit) : Encoder(callback) {

    private val thompsonEncoder = ThompsonEncoder(callback)
    private val surrogateValidator = SurrogateValidator({ codeUnit: Char ->
        thompsonEncoder.input(codeUnit.code)
    })

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 3

    public override fun input(value: Char): Unit {
        surrogateValidator.input(value)
    }

    public override fun flush(): Unit {
        surrogateValidator.flush()
    }

    public override fun reset(): Unit {
        surrogateValidator.reset()
    }
}
