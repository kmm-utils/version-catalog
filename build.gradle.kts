import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()

        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://s01.oss.sonatype.org/content/repositories/releases/")
    }

    dependencies {
        classpath(libs.log4j.core)
    }
}

@Suppress("DSL_SCOPE_VIOLATION")  // TODO: Remove once KTIJ-19369 is fixed
plugins {
    `version-catalog`
    `maven-publish`
    alias(libs.plugins.update.versions)
    alias(libs.plugins.update.versionCatalog)
}

group = "kmm.utils"
version = "0.1"

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "version-catalog"
            from(components["versionCatalog"])

            pom {
                name.set("Kotlin Mobile Multiplatform Utils Version Catalog")
                description.set("The version catalog used on the KMM Utils projects")
                url.set("https://github.com/kmm-utils/version-catalog")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://mit-license.org/")
                    }
                }
                developers {
                    developer {
                        id.set("arlm")
                        name.set("Alexandre Rocha Lima e Marcondes")
                        email.set("alexandre.marcondes@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com:kmm-utils/version-catalog.git")
                    developerConnection.set("scm:git:ssh://git@github.com:kmm-utils/version-catalog.git")
                    url.set("https://github.com/kmm-utils/version-catalog")
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = java.net.URI("https://maven.pkg.github.com/kmm-utils/version-catalog")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: System.getenv("USERNAME")
                password = System.getenv("GITHUB_TOKEN") ?: System.getenv("TOKEN")
            }
        }
    }
}

catalog {
    versionCatalog {
        versionCatalogs.forEach { c ->
            c.versionAliases.forEach { v ->
                val value = c.findVersion(v).get().toString()
                project.logger.lifecycle("Adding version $v as '$value'")
                version(v, value)
            }
            c.libraryAliases.forEach { l ->
                val lib = c.findLibrary(l).get().get()
                val value = "${lib.group}:${lib.name}:${lib.version}"
                project.logger.lifecycle("Adding library $l as '$value'")
                library(l, lib.group!!, lib.name).version(lib.version.toString())
            }
            c.pluginAliases.forEach { p ->
                val plug = c.findPlugin(p).get().get()
                project.logger.lifecycle("Adding plugin $p as '${plug.pluginId}:${plug.version}'")
                plugin(p, plug.pluginId).version(plug.version.toString())
            }
            c.bundleAliases.forEach { b ->
                val items = c.findBundle(b).get().get()
                    .map { d ->
                        c.libraryAliases.find {
                            val l = c.findLibrary(it).get().get()
                            l.group == d.group && l.name == d.name
                        }
                    }
                project.logger.lifecycle("Adding bundle $b as '$items'")
                bundle(b, items)
            }
        }
    }
}


versionCatalogUpdate {
    // sort the catalog by key (default is true)
    sortByKey.set(true)

    // keep versions without any library or plugin reference
    val keepUnusedVersionsValue = false

    // keep all libraries that aren't used in the project
    val keepUnusedLibrariesValue = true

    // keep all plugins that aren't used in the project
    val keepUnusedPluginsValue = true

    // Referenced that are pinned are not automatically updated.
    // They are also not automatically kept however (use keep for that).
    pin {
        // pins all libraries and plugins using the given versions
        //versions.add("my-version-name")
        //versions.add("other-version")

        // pins specific libraries that are in the version catalog
        //libraries.add(libs.my.library.reference)
        //libraries.add(libs.my.other.library.reference)

        // pins specific plugins that are in the version catalog
        //plugins.add(libs.plugins.my.plugin)
        //plugins.add(libs.plugins.my.other.plugin)

        // pins all libraries (not plugins) for the given groups
        //groups.add("com.somegroup")
        //groups.add("com.someothergroup")
    }

    keep {
        // keep has the same options as pin to keep specific entries
        //versions.add("my-version-name")
        //versions.add("other-version")
        //libraries.add(libs.my.library.reference)
        //libraries.add(libs.my.other.library.reference)
        //plugins.add(libs.plugins.my.plugin)
        //plugins.add(libs.plugins.my.other.plugin)
        //groups.add("com.somegroup")
        //groups.add("com.someothergroup")

        keepUnusedVersions.set(keepUnusedVersionsValue)
        keepUnusedLibraries.set(keepUnusedLibrariesValue)
        keepUnusedPlugins.set(keepUnusedPluginsValue)
    }

    versionCatalogs {
        create("androidX") {
            catalogFile.set(file("gradle/androidxlibs.versions.toml"))
            // sorted
            sortByKey.set(true)

            // Referenced that are pinned are not automatically updated.
            // They are also not automatically kept however (use keep for that).
            pin {
                // pins all libraries and plugins using the given versions
                //versions.add("my-version-name")
                //versions.add("other-version")

                // pins specific libraries that are in the version catalog
                //libraries.add(libs.my.library.reference)
                //libraries.add(libs.my.other.library.reference)

                // pins specific plugins that are in the version catalog
                //plugins.add(libs.plugins.my.plugin)
                //plugins.add(libs.plugins.my.other.plugin)

                // pins all libraries (not plugins) for the given groups
                //groups.add("com.somegroup")
                //groups.add("com.someothergroup")
            }

            keep {
                // keep has the same options as pin to keep specific entries
                //versions.add("my-version-name")
                //versions.add("other-version")
                //libraries.add(libs.my.library.reference)
                //libraries.add(libs.my.other.library.reference)
                //plugins.add(libs.plugins.my.plugin)
                //plugins.add(libs.plugins.my.other.plugin)
                //groups.add("com.somegroup")
                //groups.add("com.someothergroup")

                keepUnusedVersions.set(keepUnusedVersionsValue)
                keepUnusedLibraries.set(keepUnusedLibrariesValue)
                keepUnusedPlugins.set(keepUnusedPluginsValue)
            }
        }

        create("kotlin") {
            catalogFile.set(file("gradle/kotlinlibs.versions.toml"))
            // sorted
            sortByKey.set(true)

            // Referenced that are pinned are not automatically updated.
            // They are also not automatically kept however (use keep for that).
            pin {
                // pins all libraries and plugins using the given versions
                //versions.add("my-version-name")
                //versions.add("other-version")

                // pins specific libraries that are in the version catalog
                //libraries.add(libs.my.library.reference)
                //libraries.add(libs.my.other.library.reference)

                // pins specific plugins that are in the version catalog
                //plugins.add(libs.plugins.my.plugin)
                //plugins.add(libs.plugins.my.other.plugin)

                // pins all libraries (not plugins) for the given groups
                //groups.add("com.somegroup")
                //groups.add("com.someothergroup")
            }

            keep {
                // keep has the same options as pin to keep specific entries
                //versions.add("my-version-name")
                //versions.add("other-version")
                //libraries.add(libs.my.library.reference)
                //libraries.add(libs.my.other.library.reference)
                //plugins.add(libs.plugins.my.plugin)
                //plugins.add(libs.plugins.my.other.plugin)
                //groups.add("com.somegroup")
                //groups.add("com.someothergroup")

                keepUnusedVersions.set(keepUnusedVersionsValue)
                keepUnusedLibraries.set(keepUnusedLibrariesValue)
                keepUnusedPlugins.set(keepUnusedPluginsValue)
            }
        }
    }
}

// https://github.com/ben-manes/gradle-versions-plugin
tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    checkConstraints = true
    checkBuildEnvironmentConstraints = true

    gradleReleaseChannel = "current"
    checkForGradleUpdate = true

    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"

    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}