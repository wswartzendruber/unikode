/*
 * Copyright 2024 William Swartzendruber
 *
 * To the extent possible under law, the person who associated CC0 with this file has waived all
 * copyright and related or neighboring rights to this file.
 *
 * You should have received a copy of the CC0 legalcode along with this work. If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
 *
 * SPDX-License-Identifier: CC0-1.0
 */

plugins {
    kotlin("multiplatform").version("2.0.21").apply(false)
    kotlin("jvm").version("2.0.21").apply(false)
    kotlin("js").version("2.0.21").apply(false)
    id("org.jetbrains.dokka").version("1.9.20").apply(false)
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}
