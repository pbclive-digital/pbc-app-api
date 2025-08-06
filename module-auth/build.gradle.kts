plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.kavi.pbc.live"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.serialization)

    implementation(project(":module-remote-datastore"))
    implementation(project(":module-data"))

    testImplementation(libs.kotlin.test.junit5)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(libs.versions.jvmVersion.get().toInt())
}