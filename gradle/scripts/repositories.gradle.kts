repositories {
	flatDir { dir("libs") }
	mavenLocal()
	mavenCentral()

	exclusiveContent {
		forRepository {
			maven("https://maven.createmod.net") // Create, Ponder
		}
		filter {
			includeGroup("net.createmod.ponder")
			includeGroup("com.simibubi.create")
			includeGroup("dev.engine-room.flywheel")
		}
	}

	maven("https://api.modrinth.com/maven") // LazyDFU, Jade, Modrinth
	maven("https://maven.terraformersmc.com/releases/") // Mod Menu, EMI
	maven("https://maven.shedaniel.me/") // Cloth Config, REI
	maven("https://maven.blamejared.com/") // JEI

	maven("https://cursemaven.com") {
		content { includeGroup("curse.maven") }
	}

	maven("https://maven.parchmentmc.org") // Parchment
	maven("https://maven.quiltmc.org/repository/release") // Quilt
	maven("https://maven.gtceu.com/") // GTCEu
	maven("https://maven.firstdarkdev.xyz/snapshots") // LDLib
	maven("https://maven.theillusivec4.top/") // Curios
	maven("https://jitpack.io") // Mixin Extras
	maven("https://modmaven.dev") // JEI Mirror
	maven("https://maven.k-4u.nl") // TOP
	maven("https://maven.jamieswhiteshirt.com/libs-release") // Reach Entity Attributes
	maven("https://maven.su5ed.dev/releases")
	maven("https://modmaven.dev") // AE2 legacy (appeng namespace)
	maven("https://maven.resourcefulbees.com/repository/maven-public/")
	maven("https://maven.impactdev.net/repository/development/")

	maven("https://maven.saps.dev/minecraft") {
		content { includeGroup("dev.latvian.mods") } // KubeJS
	}
	maven("https://squiddev.cc/maven") {
		content { includeGroup("cc.tweaked.cobalt") }
	}
	maven("https://thedarkcolour.github.io/KotlinForForge/") {
		content { includeGroup("thedarkcolour") }
	}
	maven("https://maven.tterrag.com/") {
		content {
			includeGroup("com.tterrag.registrate")
			includeGroup("com.jozufozu.flywheel")
		}
	}
}
