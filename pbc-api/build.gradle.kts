import java.util.Properties

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.kotlin.serialization)
}

group = "${project.properties["groupName"]}"
version = "${project.properties["versionName"]}"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(libs.versions.jvmVersion.get().toInt())
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter)
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.security)
	implementation(libs.spring.boot.starter.thymeleaf)
	implementation(libs.spring.boot.starter.mail)

    implementation(libs.kotlin.stdlib.jdk8)
	implementation(libs.kotlin.reflect)
	implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.coroutines.core)

    implementation(libs.json)
    implementation(libs.kotlin.logging.jvm)

    implementation(project(":module-auth"))
    implementation(project(":module-remote-integration"))
    implementation(project(":module-data"))
    implementation(project(":module-csv"))

	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.kotlin.test.junit5)
}

/**
 * Fix the issue in running the app from java -jar
 * https://codeutility.org/spring-boot-kotlin-gradle-error-main-method-not-found-in-class-stack-overflow/
 */
tasks.jar {
    manifest {
        attributes("Main-Class" to "com.kavi.pbc.live.api.PbcApiApplication")
        attributes("Start-Class" to "com.kavi.pbc.live.api.PbcApiApplication")
    }
}

tasks.register("stage") {
    dependsOn("bootJar")
}

springBoot {
    mainClass.set("com.kavi.pbc.live.api.PbcApiApplicationKt")
}

/**
 * This is for set default environment
 */
tasks.bootRun {
    systemProperties["spring.profiles.active"] = "staging"
}

/**
 * This is for set environmental variables from local.properties when app runs in local
 */
tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    val localProps = Properties()
    val localPropsFile = project.rootProject.file("local.properties")
    if (localPropsFile.exists()) {
        localProps.load(localPropsFile.inputStream())
        localProps.forEach { (key, value) ->
            environment(key.toString(), value.toString())
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.test {
    useJUnitPlatform()
}
