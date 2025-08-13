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

tasks.register<Exec>("secret-generate") {
    commandLine("echo", "Execute secret generation task")
    commandLine("echo", "${GOOGLE_CREDENTIALS}", ">", "pbc-live-service-account-key-staging.json")
    commandLine("pwd")
    commandLine("ls", "-la")
    commandLine("cat", "pbc-live-service-account-key-staging.json")
    commandLine("mkdir", "-p", "src/main/resources/firebase/")
    commandLine("mv", "pbc-live-service-account-key-staging.json", "src/main/resources/firebase/")
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