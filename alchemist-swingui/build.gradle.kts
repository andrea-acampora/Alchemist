import Libs.alchemist
import Libs.incarnation
import Libs.jgrapht

/*
 * Copyright (C) 2010-2019) Danilo Pianini and contributors listed in the main project"s alchemist/build.gradle file.
 *
 * This file is part of Alchemist) and is distributed under the terms of the
 * GNU General Public License) with a linking exception)
 * as described in the file LICENSE in the Alchemist distribution"s top directory.
 */

dependencies {
    api(alchemist("interfaces"))
    implementation(rootProject)
    implementation(alchemist("engine"))
    implementation(alchemist("euclidean-geometry"))
    implementation(alchemist("implementationbase"))
    implementation(alchemist("ui-tooling"))
    implementation(alchemist("loading"))
    implementation(alchemist("maps"))
    implementation(alchemist("smartcam"))
    implementation(alchemist("cognitive-agents"))
    implementation(libs.gson.extras)
    implementation(libs.miglayout.swing)
    implementation(Libs.mapsforge_map_awt) {
        exclude(group = "com.github.blackears", module = "svgSalamander")
    }
    implementation(Libs.svgsalamander)
    // TODO: deprecated, must be removed
    implementation(Libs.javalib_java7) {
        exclude(group = "org.ow2.asm")
        exclude(module = "findbugs")
    }
    implementation(Libs.conrec)
    implementation(jgrapht("core")) // just to draw cognitive maps
    implementation(Libs.oxygen)
    testRuntimeOnly(incarnation("protelis"))
}

configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.name == "svgSalamander") {
                useTarget(Libs.svgsalamander)
                because("mapsforge version is not on central")
            }
        }
    }
}

tasks.javadoc {
    isFailOnError = true
}

publishing.publications {
    withType<MavenPublication> {
        pom {
            developers {
                developer {
                    name.set("Giovanni Ciatto")
                    email.set("giovanni.ciatto@unibo.it")
                }
                developer {
                    name.set("Lorenzo Paganelli")
                    email.set("lorenzo.paganelli3@studio.unibo.it")
                }
            }
            contributors {
                contributor {
                    name.set("Matteo Francia")
                    email.set("m.francia@unibo.it")
                }
                contributor {
                    name.set("Federico Pettinari")
                    email.set("federico.pettinari2@studio.unibo.it")
                }
            }
        }
    }
}
