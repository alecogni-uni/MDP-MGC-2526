plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0" // Plugin per JavaFX
}

group = "it.unicam.cs.mpgc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JavaFX
    implementation("org.openjfx:javafx-controls:21")
    implementation("org.openjfx:javafx-fxml:21")

    implementation("com.google.code.gson:gson:2.10.1")
    // JPA API e Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")

    // Database H2
    implementation("com.h2database:h2:2.2.224")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")


    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

application {
    mainClass.set("it.unicam.cs.mpgc.rpg126161.MainGUI")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

tasks.test {
    useJUnitPlatform()
}