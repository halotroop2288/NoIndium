plugins {
	alias(libs.plugins.unimined)
	`maven-publish`
}

group = "me.luligabi"
base.archivesName = "NoIndium"
version = project.ext["mod_version"] as String

unimined.minecraft {
	version(libs.versions.minecraft.get())
	side("client")

	mappings { mojmap() }

	fabric {
		loader(libs.versions.fabric.loader.get())
	}
}

dependencies {
	val modImplementation: Configuration by configurations.getting
	val include: Configuration by configurations.getting
	modImplementation(fabricApi.fabricModule("fabric-lifecycle-events-v1", libs.versions.fabric.api.get()))

	implementation(libs.jankson)
    include(libs.jankson)
}

tasks.withType(ProcessResources::class).configureEach {
    inputs.property("version", project.version)

    filesMatching("*.mod.json") {
        expand("version" to project.version)
    }
}

val targetJavaVersion = 17
tasks.withType(JavaCompile::class).configureEach {
    options.release.set(targetJavaVersion)
}

java {
	toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

tasks.withType(Jar::class).configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	from("LICENSE") {
        rename { "${it}_${project.base.archivesName}" }
    }
}
