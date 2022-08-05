/*
 * Copyright 2022 William Swartzendruber
 *
 * Any copyright is dedicated to the Public Domain.
 *
 * SPDX-License-Identifier: CC0-1.0
 */

plugins {
    kotlin("multiplatform").version("1.6.20").apply(false)
    kotlin("jvm").version("1.6.20").apply(false)
    kotlin("js").version("1.6.20").apply(false)
    id("org.jetbrains.dokka").version("1.6.10").apply(false)
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}
