dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/version-catalog/libs.versions.toml"))
        }
    }
}

listOf(
    "pbc-api"
).onEach {
    include(it)
}

rootProject.name = "pbc-app-api"
