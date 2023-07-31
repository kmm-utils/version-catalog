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

group = "kmm.utils.version_catalog"
version = "1.0-SNAPSHOT"

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])
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