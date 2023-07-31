pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()

        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://s01.oss.sonatype.org/content/repositories/releases/")
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()

        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://s01.oss.sonatype.org/content/repositories/releases/")
    }

    versionCatalogs {
        create("kotlinLibs") { from(files("gradle/kotlinlibs.versions.toml")) }
        create("androidXLibs") { from(files("gradle/androidxlibs.versions.toml")) }
    }
}

rootProject.name = "version-catalog"