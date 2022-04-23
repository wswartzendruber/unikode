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

public abstract class Decoder {

    public abstract fun decode(
        source: ByteArray,
        destination: CharArray,
        sourceStartIndex: Int = 0,
        sourceEndIndex: Int = source.size,
        destinationOffset: Int = 0,
    ): Int

    public abstract fun maxCharsNeeded(byteCount: Int): Int

    public abstract fun reset(): Unit
}
