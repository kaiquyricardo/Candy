plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.github.kaiquy"
version = "0.1-ALPHA"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://jitpack.io")
    maven("https://maven.elmakers.com/repository/")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {

    compileOnly ("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly ("com.github.azbh111:craftbukkit-1.8.8:R")

    implementation ("com.zaxxer:HikariCP:4.0.3")
    implementation("com.github.SaiintBrisson.command-framework:shared:1.2.0")
    implementation("com.github.SaiintBrisson.command-framework:bukkit:1.2.0")
    implementation("com.github.DevNatan:inventory-framework:2.0.3")
    implementation("de.tr7zw:item-nbt-api:2.8.0")

    compileOnly (fileTree("libs"))
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
}

tasks {

    java {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    compileJava { options.encoding = "UTF-8" }

    java {
        shadowJar {
            archiveFileName.set("candy-1.0-SNAPSHOT.jar")
            relocate("me.saiintbrisson.minecraft.command", "com.github.kaiquy.candy.misc.commands")
            relocate("me.saiintbrisson.minecraft", "com.github.kaiquy.candy.misc.inventory")
            relocate("me.saiintbrisson.bukkit.command", "com.github.kaiquy.candy.misc.commands")
        }
    }
}

