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

import java.net.URI

import org.jetbrains.dokka.Platform

val mavenUrl: String? by project
val mavenUsername: String? by project
val mavenPassword: String? by project

repositories {
    google()
}

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("signing")
    id("maven-publish")
}

tasks {

    dokkaHtml {
        dokkaSourceSets {
            named("commonMain") {
                displayName.set("Common")
                platform.set(Platform.common)
            }
            named("jvmMain") {
                displayName.set("JVM")
                platform.set(Platform.jvm)
            }
            named("jsMain") {
                displayName.set("JS")
                platform.set(Platform.js)
            }
        }
    }

    register<Jar>("dokkaHtmlJar") {
        group = "Build"
        description = "Packages dokkaHtml output into a JAR."
        archiveClassifier = "dokka"
        from(dokkaHtml)
    }
}

kotlin {
    explicitApi()
    metadata {
        mavenPublication {
            artifact(tasks["dokkaHtmlJar"])
        }
    }
    jvm { }
    js {
        browser {
            testTask {
                enabled = false
            }
        }
        nodejs { }
    }
}

dependencies {
    // Common
    commonMainImplementation(platform(kotlin("bom")))
    commonTestImplementation(kotlin("test"))
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks["dokkaHtmlJar"])
            pom {
                name.set("Unikode")
                description.set("Unicode processing library for Kotlin Multiplatform")
                url.set("https://github.com/wswartzendruber/unikode")
                developers {
                    developer {
                        id.set("wswartzendruber")
                        name.set("William Swartzendruber")
                        email.set("wswartzendruber@gmail.com")
                    }
                }
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/wswartzendruber/unikode.git")
                    developerConnection.set(
                        "scm:git:git://github.com/wswartzendruber/unikode.git"
                    )
                    url.set("https://github.com/wswartzendruber/unikode")
                }
            }
        }
    }
    repositories {
        maven {
            url = URI(mavenUrl.toString())
            credentials {
                username = mavenUsername
                password = mavenPassword
            }
        }
    }
}
