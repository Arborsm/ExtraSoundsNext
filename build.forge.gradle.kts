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
	}
}

fletchingTable {
	accessConverter.register(sourceSets.main) {
		add("aw/${stonecutter.current.version}.accesswidener")
	}
}

legacyForge {
	version = "${property("deps.minecraft")}-${property("deps.forge")}"

	//validateAccessTransformers = true

	rootProject.file("versions/${stonecutter.current.version}-forge/build/resources/main/META-INF/accesstransformer.cfg").let {
		if (it.exists()) accessTransformers.from(it)
	}

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
	add(sourceSets.main.get(), "${prop("mod.id")}.mixins.refmap.json")
	config("${prop("mod.id")}.mixins.json")
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

dependencies {
	annotationProcessor("org.spongepowered:mixin:${libs.versions.mixin.get()}:processor")

	annotationProcessor(libs.mixinextras.common)
	compileOnly(libs.mixinextras.common)
	implementation(libs.mixinextras.forge)
	jarJar(libs.mixinextras.forge)

	implementation(libs.moulberry.mixinconstraints)
	jarJar(libs.moulberry.mixinconstraints)
}

sourceSets.main {
	resources.srcDir("src/generated/resources")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

stonecutter {

}
