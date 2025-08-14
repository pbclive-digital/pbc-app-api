plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.kavi.pbc.live"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.firebase.admin)
    implementation(libs.kotlin.serialization)

    implementation(project(":module-data"))

    testImplementation(libs.kotlin.test.junit5)
}

/**
 * This gradle task to generate google-firebase-credentials .json file
 * on while application build and deploy to heroku.
 *
 * This refers the credentials from Heroku Env variable `GOOGLE_CREDENTIALS` and write that into
 * `pbc-live-service-account-<application-env-name>.json` file.
 * As per the environment the file name will change as pbc-live-service-account-staging.json / pbc-live-service-account-prod.json
 */
tasks.register("secret-generate") {
    val googleServiceAccount = System.getenv("GOOGLE_CREDENTIALS")
    val pbcEnv = System.getenv("PBC_ENV")

    googleServiceAccount?.let {
        val googleServiceAccJson = project.file("pbc-live-service-account-key-$pbcEnv.json")
        val resourceDir = file("src/main/resources/firebase")
        googleServiceAccJson.createNewFile()
        googleServiceAccJson.writeText(googleServiceAccount)

        if (!resourceDir.exists()) {
            resourceDir.mkdirs()
        }

        copy {
            from(googleServiceAccJson)
            into(resourceDir)
        }

        doLast {
            googleServiceAccJson.delete()
        }
    }
}

/**
 * Make the `compileKotlin` task dependes on the new gradle task created above.
 */
tasks.named("compileKotlin") {
    dependsOn("secret-generate")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(libs.versions.jvmVersion.get().toInt())
}