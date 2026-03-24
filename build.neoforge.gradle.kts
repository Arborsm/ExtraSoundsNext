plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
}

platform {
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeVersionRange = "[${prop("deps.minecraft")}]"
		}
		required("neoforge") {
			forgeVersionRange = "[1,)"
		}
	}
}

neoForge {
	version = property("deps.neoforge") as String

	//validateAccessTransformers = true

	accessTransformers.from(tasks.named("generateAccessTransformer"))

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = (property("deps.parchment") as String).split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${stonecutter.active?.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${stonecutter.active?.version})"
		}
	}

	mods {
		register(property("mod.id") as String) {
			sourceSet(sourceSets["main"])
		}
	}
	//sourceSets["main"].resources.srcDir("${rootDir}/versions/datagen/${stonecutter.current.version.split("-")[0]}/src/main/generated")
}

apply(from = "${rootDir}/gradle/scripts/repositories.gradle.kts")

dependencies {
	implementation(libs.moulberry.mixinconstraints)
	jarJar(libs.moulberry.mixinconstraints)

	compileOnly("mezz.jei:jei-${prop("deps.minecraft")}-neoforge-api:${prop("deps.jei")}")
	implementation("mezz.jei:jei-${prop("deps.minecraft")}-neoforge:${prop("deps.jei")}")
	compileOnly("maven.modrinth:rei:${prop("deps.rei")}")
	compileOnly("dev.emi:emi-neoforge:${prop("deps.emi")}:api")
	compileOnly("dev.emi:emi-neoforge:${prop("deps.emi")}")
	implementation("org.appliedenergistics:appliedenergistics2:${prop("deps.ae2")}")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

stonecutter {
	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
}
