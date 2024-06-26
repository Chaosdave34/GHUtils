import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml
import java.net.URI


plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.6.3"
    id("xyz.jpenilla.run-paper") version "2.2.4" // Adds runServer and runMojangMappedServer tasks for testing
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1" // Generates plugin.yml based on the Gradle config

    id("io.freefair.lombok") version "8.6"

    id("maven-publish")
}

group = "io.github.chaosdave34"
version = "0.4.0-SNAPSHOT"
description = "Utilities for Gamershub Paper Plugins"

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 21 on systems that only have JDK 11 installed for example.
    toolchain.languageVersion = JavaLanguageVersion.of(21)

    withSourcesJar()
}

// 1)
// For >=1.20.5 when you don't care about supporting spigot
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

// 2)
// For 1.20.4 or below, or when you care about supporting Spigot on >=1.20.5
// Configure reobfJar to run when invoking the build task
/*
tasks.assemble {
  dependsOn(tasks.reobfJar)
}
 */

dependencies {
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
    // paperweight.foliaDevBundle("1.20.6-R0.1-SNAPSHOT")
    // paperweight.devBundle("com.example.paperfork", "1.20.6-R0.1-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = URI.create("https://maven.pkg.github.com/Chaosdave34/GHUtils")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
    }
}

tasks {
    compileJava {
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release = 21
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    // Only relevant when going with option 2 above
    /*
    reobfJar {
      // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
      // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
      outputJar = layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar")
    }
     */
}

// Configure plugin.yml generation
// - name, version, and description are inherited from the Gradle project.
bukkitPluginYaml {
    main = "io.github.chaosdave34.ghutils.GHUtils"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    authors.add("Chaosdave34")
    apiVersion = "1.20.5"
}
