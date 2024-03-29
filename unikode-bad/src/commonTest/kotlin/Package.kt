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

package org.unikode.bad.test

const val TEXT = "This제가Монголलुम्बिनीis가미정언гэрवनa식당에नेपालtest있어요болकेdocument🇰🇷Төвतराई😀"

val textByteArrayCesu8 = byteArrayOf(
    84, 104, 105, 115, -20, -96, -100, -22, -80, -128, -48, -100, -48, -66, -48, -67, -48, -77,
    -48, -66, -48, -69, -32, -92, -78, -32, -91, -127, -32, -92, -82, -32, -91, -115, -32, -92,
    -84, -32, -92, -65, -32, -92, -88, -32, -91, -128, 105, 115, -22, -80, -128, -21, -81, -72,
    -20, -96, -107, -20, -106, -72, -48, -77, -47, -115, -47, -128, -32, -92, -75, -32, -92,
    -88, 97, -20, -117, -99, -21, -117, -71, -20, -105, -112, -32, -92, -88, -32, -91, -121,
    -32, -92, -86, -32, -92, -66, -32, -92, -78, 116, 101, 115, 116, -20, -98, -120, -20, -106,
    -76, -20, -102, -108, -48, -79, -48, -66, -48, -69, -32, -92, -107, -32, -91, -121, 100,
    111, 99, 117, 109, 101, 110, 116, -19, -96, -68, -19, -73, -80, -19, -96, -68, -19, -73,
    -73, -48, -94, -45, -87, -48, -78, -32, -92, -92, -32, -92, -80, -32, -92, -66, -32, -92,
    -120, -19, -96, -67, -19, -72, -128
)
