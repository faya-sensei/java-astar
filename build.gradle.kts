plugins {
    id("java")
}

group = "org.faya.sensei"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.reflections:reflections:0.9.12")
}

tasks.test {
    useJUnitPlatform()
}