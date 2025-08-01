dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/version-catalog/libs.versions.toml"))
        }
    }
}

listOf(
    "module-firebase",
    "module-data",
    "pbc-api"
).onEach {
    include(it)
}

rootProject.name = "pbc-app-api"