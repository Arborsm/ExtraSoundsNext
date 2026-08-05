plugins {
	id("mod-platform")
	id("net.neoforged.moddev.legacyforge")
	id("dev.kikugie.fletching-table")
}

platform {
	loader = "forge"
	dependencies {
		required("minecraft") {
			forgeVersionRange = "[${prop("deps.minecraft")}]"
		}
		required("forge") {
			forgeVersionRange = "[1,)"
		}
		optional("jei") {
			slug("jei", "jei")
			forgeVersionRange = "[${prop("deps.jei")},)"
			environment = "client"
		}
	}
}

legacyForge {
	version = "${property("deps.minecraft")}-${property("deps.forge")}"

	//validateAccessTransformers = true

	accessTransformers.from(tasks.named("generateAccessTransformer"))

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "Forge Client (${stonecutter.active?.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "Forge Server (${stonecutter.active?.version})"
		}
	}


	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
}

mixin {
	add(sourceSets.main.get(), "${prop("mod.id")}.refmap.json")
	config("${prop("mod.id")}.mixins.json")
}

apply(from = "${rootDir}/gradle/scripts/repositories.gradle.kts")

dependencies {
	annotationProcessor("org.spongepowered:mixin:${libs.versions.mixin.get()}:processor")

	annotationProcessor(libs.mixinextras.common)
	compileOnly(libs.mixinextras.common)
	implementation(libs.mixinextras.forge)
	jarJar(libs.mixinextras.forge)

	implementation(libs.moulberry.mixinconstraints)
	jarJar(libs.moulberry.mixinconstraints)

	modCompileOnly("mezz.jei:jei-${prop("deps.minecraft")}-forge-api:${prop("deps.jei")}")
	modImplementation("mezz.jei:jei-${prop("deps.minecraft")}-forge:${prop("deps.jei")}")
	modCompileOnly("maven.modrinth:rei:${prop("deps.rei")}")
	if (sc.current.parsed > "1.18.2") {
		modCompileOnly("dev.emi:emi-forge:${prop("deps.emi")}:api")
		modCompileOnly("dev.emi:emi-forge:${prop("deps.emi")}")
	}
	modImplementation("appeng:appliedenergistics2-forge:${prop("deps.ae2")}")
	if (prop("deps.guideme").isNotEmpty()) {
		modImplementation("org.appliedenergistics:guideme:${prop("deps.guideme")}")
	}
}

sourceSets.main {
	resources.srcDir("src/generated/resources")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

stonecutter {

}
