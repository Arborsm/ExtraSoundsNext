plugins {
	id("mod-platform")
	id("fabric-loom")
}

platform {
	loader = "fabric"
	dependencies {
		required("minecraft") {
			versionRange = prop("deps.minecraft")
		}
		required("fabric-api") {
			slug("fabric-api")
			versionRange = ">=${prop("deps.fabric-api")}"
		}
		required("fabricloader") {
			versionRange = ">=${prop("deps.fabricloader")}"
		}
		optional("modmenu") {}
		optional("jei") {
			slug("jei", "jei")
			versionRange = ">=${prop("deps.jei")}"
			environment = "client"
		}
		incompatible("jei") {
			versionRange = "<${prop("deps.jei")}"
			environment = "client"
		}
	}
}

loom {
	accessWidenerPath = rootProject.file("src/main/resources/aw/${stonecutter.current.version}.accesswidener")
	runs.named("client") {
		client()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "client"
		programArgs("--username=Dev")
		configName = "Fabric Client"
	}
	runs.named("server") {
		server()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "server"
		configName = "Fabric Server"
	}
}

fabricApi {
	configureDataGeneration {
		outputDirectory = file("${rootDir}/versions/datagen/${stonecutter.current.version.split("-")[0]}/src/main/generated")
		client = true
	}
}

apply(from = "${rootDir}/gradle/scripts/repositories.gradle.kts")

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	mappings(
		loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment")) parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		})
	modImplementation(libs.fabric.loader)
	implementation(libs.moulberry.mixinconstraints)
	include(libs.moulberry.mixinconstraints)
	modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	modImplementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}")

	modCompileOnly("mezz.jei:jei-${prop("deps.minecraft")}-fabric-api:${prop("deps.jei")}")
	modImplementation("mezz.jei:jei-${prop("deps.minecraft")}-fabric:${prop("deps.jei")}")
	modCompileOnly("maven.modrinth:rei:${prop("deps.rei")}")
	if (sc.current.parsed <= "1.18.2") {
		modCompileOnly("dev.emi:emi:${prop("deps.emi")}")
		modCompileOnly("dev.emi:emi:${prop("deps.emi")}:api")
	}
	else {
		modCompileOnly("dev.emi:emi-fabric:${prop("deps.emi")}")
		modCompileOnly("dev.emi:emi-fabric:${prop("deps.emi")}:api")
	}
	if(sc.current.parsed <= "1.20.1") {
		modCompileOnly("appeng:appliedenergistics2-fabric:${prop("deps.ae2")}") { isTransitive = false }
	}
	else {
		modImplementation("org.appliedenergistics:appliedenergistics2:${prop("deps.ae2")}")
	}
}

stonecutter {
	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
}
