dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/version-catalog/libs.versions.toml"))
        }
    }
}

listOf(
    "module-auth",
    "module-remote-integration",
    "module-data",
    "pbc-api"
).onEach {
    include(it)
}

rootProject.name = "pbc-app-api"