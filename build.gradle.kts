plugins {
    kotlin("jvm") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
   // implementation("org.mongodb:mongodb-driver-sync:4.11.0")

    implementation("org.mongodb:mongodb-driver-sync:4.11.1")
    //Logback implementation
   // implementation("ch.qos.logback:logback-classic:1.4.14")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(22)
}