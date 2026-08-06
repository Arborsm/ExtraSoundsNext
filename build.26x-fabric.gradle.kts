plugins {
	id("mod-platform")
	id("net.fabricmc.fabric-loom")
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

apply(from = "${rootDir}/gradle/scripts/repositories.gradle.kts")

dependencies {
	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	// 26.1+ 是不混淆版本，无需 mappings 层
	implementation(libs.fabric.loader)
	implementation(libs.moulberry.mixinconstraints)
	include(libs.moulberry.mixinconstraints)
	implementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	implementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}")

	compileOnly("mezz.jei:jei-${prop("deps.minecraft")}-fabric-api:${prop("deps.jei")}")
	implementation("mezz.jei:jei-${prop("deps.minecraft")}-fabric:${prop("deps.jei")}")
	compileOnly("maven.modrinth:rei:${prop("deps.rei")}")
}

stonecutter {
	// 26.1+ 无混淆，ResourceLocation 已改名 Identifier
	replacements.string(true) {
		replace("ResourceLocation", "Identifier")
	}
	replacements.string(true) {
		replace("ClickType", "ContainerInput")
	}
}
