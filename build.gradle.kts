import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
}

group = "cz.cvut.fit"
version = "0.3"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-security:2.6.2")
    implementation("org.springframework.security:spring-security-test:5.5.1")

    implementation("mysql:mysql-connector-java:8.0.25")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc:2.6.2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.2")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.6.2")


    implementation("com.auth0:java-jwt:3.18.2")
    implementation("com.auth0:jwks-rsa:0.20.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
