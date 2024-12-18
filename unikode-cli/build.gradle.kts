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

plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":unikode"))
    implementation(project(":unikode-bad"))
    implementation(project(":unikode-cf8"))
    implementation(project(":unikode-stf7"))
    implementation(platform(kotlin("bom")))
}

application {
    mainClass.set("org.unikode.cli.ApplicationKt")
}
