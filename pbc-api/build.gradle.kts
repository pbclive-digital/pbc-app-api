plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.kotlin.serialization)
}

group = "com.kavi.pbc.live"
version = "0.0.1-SNAPSHOT"

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
	//implementation(libs.spring.boot.starter.security)
	implementation(libs.spring.boot.starter.thymeleaf)

    implementation(libs.kotlin.stdlib.jdk8)
	implementation(libs.kotlin.reflect)
	implementation(libs.kotlin.serialization)

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

tasks.bootRun {
    systemProperties["spring.profiles.active"] = "qa"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
