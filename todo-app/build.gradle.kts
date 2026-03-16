plugins {
    kotlin("jvm") version "1.9.23"
    id("io.qameta.allure") version "2.11.2"
}

group = "com.todo"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.xerial:sqlite-jdbc:3.45.3.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    testImplementation("io.qameta.allure:allure-junit5:2.27.0")
}

allure {
    adapter {
        autoconfigureListeners.set(true)
    }
}

tasks.test {
    useJUnitPlatform()
    systemProperty("allure.results.directory", "$buildDir/allure-results")
}