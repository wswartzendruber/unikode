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

package org.unikode.test

import kotlin.test.assertEquals
import kotlin.test.Test

import org.unikode.toStringUtf16Le
import org.unikode.toUtf16LeByteArray

class Utf16LeTests {

    @Test
    fun round_trip() =
        assertEquals(completeString, completeString.toUtf16LeByteArray().toStringUtf16Le())
}
