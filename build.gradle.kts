plugins {
    id("java")
    id("maven-publish")
}

group = "com.nolmax.database"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.xerial:sqlite-jdbc:3.51.3.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("nolmax's client database connector")
                description.set("database utility library for nolmax")
                url.set("https://github.com/nolmax-works/client-database-connector")
            }
        }
    }

    repositories {
        maven {
            name = "qtpcRepo"
            url = uri("https://maven.qtpc.tech/releases")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}