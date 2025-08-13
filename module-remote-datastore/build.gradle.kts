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

tasks.register("secret-generate") {
    val googleServiceAccount = System.getenv("GOOGLE_CREDENTIALS")

    println("READ Google Service Account credentials v2: \n$googleServiceAccount")

    googleServiceAccount?.let {
        val googleServiceAccJson = project.file("pbc-live-service-account-key-staging.json")
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

tasks.named("compileKotlin") {
    dependsOn("secret-generate")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(libs.versions.jvmVersion.get().toInt())
}