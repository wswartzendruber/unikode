/*
 * Copyright 2022 William Swartzendruber
 *
 * Any copyright is dedicated to the Public Domain.
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.test

import kotlin.test.assertEquals
import kotlin.test.Test

import org.unikode.scalarValues

class ScalarValuesTests {

    @Test
    fun valid() {
        assertEquals(TEXT.scalarValues().toList(), validText)
    }

    @Test
    fun nothing() {
        assertEquals("".scalarValues().toList(), emptyList<Int>())
    }

    @Test
    fun single_char() {
        assertEquals("\u0020".scalarValues().toList(), listOf(0x20))
    }

    @Test
    fun two_single_chars() {
        assertEquals("\u0020\u0040".scalarValues().toList(), listOf(0x20, 0x40))
    }

    @Test
    fun surrogate_pair() {
        assertEquals("\uD83D\uDE00".scalarValues().toList(), listOf(0x1F600))
    }

    @Test
    fun low_surrogate() {
        assertEquals("\uDE00".scalarValues().toList(), listOf(0xFFFD))
    }

    @Test
    fun high_surrogate_single_char() {
        assertEquals("\uD83D\u0020".scalarValues().toList(), listOf(0xFFFD, 0x20))
    }

    @Test
    fun high_surrogate_surrogate_pair() {
        assertEquals("\uD83C\uD83D\uDE00".scalarValues().toList(), listOf(0xFFFD, 0x1F600))
    }

    companion object {

        val validText = listOf(
            0x000054, 0x000068, 0x000069, 0x000073, 0x00c81c, 0x00ac00, 0x00041c, 0x00043e,
            0x00043d, 0x000433, 0x00043e, 0x00043b, 0x000932, 0x000941, 0x00092e, 0x00094d,
            0x00092c, 0x00093f, 0x000928, 0x000940, 0x000069, 0x000073, 0x00ac00, 0x00bbf8,
            0x00c815, 0x00c5b8, 0x000433, 0x00044d, 0x000440, 0x000935, 0x000928, 0x000061,
            0x00c2dd, 0x00b2f9, 0x00c5d0, 0x000928, 0x000947, 0x00092a, 0x00093e, 0x000932,
            0x000074, 0x000065, 0x000073, 0x000074, 0x00c788, 0x00c5b4, 0x00c694, 0x000431,
            0x00043e, 0x00043b, 0x000915, 0x000947, 0x000064, 0x00006f, 0x000063, 0x000075,
            0x00006d, 0x000065, 0x00006e, 0x000074, 0x01f1f0, 0x01f1f7, 0x000422, 0x0004e9,
            0x000432, 0x000924, 0x000930, 0x00093e, 0x000908, 0x01f600,
        )
    }
}
