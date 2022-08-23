/*
 * Copyright 2022 William Swartzendruber
 *
 * To the extent possible under law, the person who associated CC0 with this file has waived all
 * copyright and related or neighboring rights to this file.
 *
 * You should have received a copy of the CC0 legalcode along with this work. If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.bored.test

import org.unikode.highSurrogate
import org.unikode.lowSurrogate

val completeString = StringBuilder()
    .also {
        for (i in 0x0000..0xD7FF)
            it.append(i.toChar())
        for (i in 0xE000..0xFFFF)
            it.append(i.toChar())
        for (i in 0x010000..0x10FFFF) {
            it.append(i.highSurrogate())
            it.append(i.lowSurrogate())
        }
    }
    .toString()
