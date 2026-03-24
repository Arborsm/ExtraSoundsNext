@file:Suppress("unused", "DuplicatedCode")

import dev.kikugie.fletching_table.extension.FletchingTableExtension
import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import me.modmuss50.mpp.ModPublishExtension
import me.modmuss50.mpp.ReleaseType
import org.gradle.api.DefaultTask
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.extensions.stdlib.toDefaultLowerCase
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.plugins.ide.idea.model.IdeaModel
import java.util.*
import javax.inject.Inject

fun Project.prop(name: String): String = (findProperty(name) ?: "") as String

fun Project.env(variable: String): String? = providers.environmentVariable(variable).orNull

fun Project.envTrue(variable: String): Boolean = env(variable)?.toDefaultLowerCase() == "true"

fun RepositoryHandler.strictMaven(
	url: String, vararg groups: String, configure: MavenArtifactRepository.() -> Unit = {}
) = exclusiveContent {
	forRepository { maven(url) { configure() } }
	filter { groups.forEach(::includeGroup) }
}

abstract class GenerateAccessTransformerTask : DefaultTask() {
	@get:InputFile
	abstract val inputFile: RegularFileProperty

	@get:OutputFile
	abstract val outputFile: RegularFileProperty

	@TaskAction
	fun generate() {
		val output = outputFile.get().asFile
		output.parentFile.mkdirs()

		val converted = linkedSetOf<String>()

		inputFile.get().asFile.readLines()
			.map(String::trim)
			.filter { it.isNotEmpty() && !it.startsWith("accessWidener") }
			.forEach { converted.addAll(convertAccessWidenerLine(it)) }

		output.writeText(converted.joinToString(System.lineSeparator()))
	}

	private fun convertAccessWidenerLine(line: String): List<String> {
		val parts = line.split(Regex("\\s+"))
		if (parts.isEmpty() || parts[0] != "accessible") return emptyList()

		return when (parts.getOrNull(1)) {
			"class" -> listOf("public ${parts[2].replace('/', '.')}")
			"field" -> listOf(
				"public ${parts[2].replace('/', '.')}",
				"public ${parts[2].replace('/', '.')} ${parts[3]}"
			)
			"method" -> listOf(
				"public ${parts[2].replace('/', '.')}",
				"public ${parts[2].replace('/', '.')} ${parts[3]}${parts[4]}"
			)
			else -> emptyList()
		}
	}
}

abstract class ModPlatformPlugin @Inject constructor() : Plugin<Project> {
	override fun apply(project: Project) = with(project) {
		val inferredLoader = project.buildFile.name.substringAfter('.').replace(".gradle.kts", "")
		val inferredLoaderIsFabric = inferredLoader == "fabric"

		val extension = extensions.create("platform", ModPlatformExtension::class.java).apply {
			loader.convention(inferredLoader)
			jarTask.convention(if (inferredLoaderIsFabric) "remapJar" else "jar")
			sourcesJarTask.convention(if (inferredLoaderIsFabric) "remapSourcesJar" else "sourcesJar")
		}

		listOf(
			"org.jetbrains.kotlin.jvm",
			"com.google.devtools.ksp",
			"dev.kikugie.fletching-table"
		).forEach { apply(plugin = it) }

		if (!inferredLoaderIsFabric) {
			registerAccessTransformerTask(prop("deps.minecraft"))
		}

		afterEvaluate {
			configureProject(extension)
		}
	}

	private fun Project.configureProject(extension: ModPlatformExtension) {
		val loader = extension.loader.get()
		val isFabric = loader == "fabric"
		val isNeoForge = loader == "neoforge"
		val isForge = loader == "forge"

		val modId = prop("mod.id")
		val modVersion = prop("mod.version")
		val channelTag = prop("mod.channel_tag")
		val mcVersion = prop("deps.minecraft")

		val stonecutter = extensions.getByType<StonecutterBuildExtension>()

		listOf(
			"java",
			"me.modmuss50.mod-publish-plugin",
			"idea",
		).forEach { apply(plugin = it) }

		version = "$modVersion$channelTag+$mcVersion-$loader"

		extension.requiredJava.set(
			when {
				stonecutter.eval(stonecutter.current.version, ">=1.20.6") -> JavaVersion.VERSION_21
				stonecutter.eval(stonecutter.current.version, ">=1.18") -> JavaVersion.VERSION_17
				stonecutter.eval(stonecutter.current.version, ">=1.17") -> JavaVersion.VERSION_16
				else -> JavaVersion.VERSION_1_8
			}
		)

		if (isFabric) {
			extension.dependencies { required("java") { versionRange = ">=${extension.requiredJava.get().majorVersion}" } }
		}

		configureFletchingTable(isFabric)
		configureJarTask(modId, loader)
		configureIdea()
		configureProcessResources(isFabric, isNeoForge, isForge, modId, "$modVersion$channelTag", mcVersion, extension, extension.requiredJava.get())
		configureMixinsValidation(isFabric, isNeoForge, isForge, modId)
		configureJava(stonecutter, extension.requiredJava.get())
		registerBuildAndCollectTask(extension, "$modVersion$channelTag")
		configurePublishing(extension, loader, stonecutter, "$modVersion$channelTag", channelTag, version.toString())
	}

	private fun Project.configureJarTask(modId: String, loader: String) {
		val isForge = loader == "forge"

		tasks.withType<Jar>().configureEach {
			archiveBaseName.set(modId)
			if (isForge) {
				manifest.attributes(
					"MixinConfigs" to "${modId}.mixins.json"
				)
			}
		}
	}

	private fun Project.configureProcessResources(
		isFabric: Boolean,
		isNeoForge: Boolean,
		isForge: Boolean,
		modId: String,
		modVersion: String,
		mcVersion: String,
		extension: ModPlatformExtension,
		requiredJava: JavaVersion
	) {
		tasks.named<ProcessResources>("processResources") {
			dependsOn(tasks.named("stonecutterGenerate"))
			dependsOn("kspKotlin")
			if (!isFabric) {
				from(tasks.named("generateAccessTransformer")) {
					into("META-INF")
				}
			}

			filesMatching("*.mixins.json") {
				expand("java" to "JAVA_${requiredJava.majorVersion}")
			}

			var contributors = prop("mod.contributors")
			var authors = prop("mod.authors")
			var issuesUrl = prop("mod.issues_url")
			if (issuesUrl == "") issuesUrl = prop("mod.sources_url") + "/issues"

			if (isFabric) {
				contributors = contributors.replace(", ", "\", \"")
				authors = authors.replace(", ", "\", \"")
			}

			val dependencies = buildDependenciesBlock(isFabric, modId, extension.dependencies)

			val props = mapOf(
				"version" to modVersion,
				"minecraft" to mcVersion,
				"id" to modId,
				"name" to prop("mod.name"),
				"group" to prop("mod.group"),
				"authors" to authors,
				"contributors" to contributors,
				"license" to prop("mod.license"),
				"description" to prop("mod.description"),
				"issues_url" to issuesUrl,
				"homepage_url" to prop("mod.homepage_url"),
				"sources_url" to prop("mod.sources_url"),
				"discord_url" to prop("mod.discord_url"),
				"dependencies" to dependencies
			)

			when {
				isFabric -> {
					filesMatching("fabric.mod.json") { expand(props) }
					exclude("META-INF/mods.toml", "META-INF/neoforge.mods.toml", "aw/*.cfg", ".cache", "pack.mcmeta")
				}

				isNeoForge -> {
					filesMatching("META-INF/neoforge.mods.toml") { expand(props) }
					exclude("META-INF/mods.toml", "fabric.mod.json", ".cache", "pack.mcmeta")
				}

				isForge -> {
					filesMatching("META-INF/mods.toml") { expand(props) }
					exclude("META-INF/neoforge.mods.toml", "fabric.mod.json", ".cache")
				}
			}
		}
	}

	private fun Project.registerAccessTransformerTask(minecraftVersion: String) {
		tasks.register<GenerateAccessTransformerTask>("generateAccessTransformer") {
			group = "build"
			description = "Generates META-INF/accesstransformer.cfg from the current access widener"
			inputFile.set(rootProject.layout.projectDirectory.file("src/main/resources/aw/$minecraftVersion.accesswidener"))
			outputFile.set(layout.buildDirectory.file("generated/access-transformer/main/META-INF/accesstransformer.cfg"))
		}
	}

	private fun buildDependenciesBlock(
		isFabric: Boolean, modId: String, deps: DependenciesConfig
	): String = if (isFabric) {
		buildString {
			fun joinGroup(
				name: String, container: NamedDomainObjectContainer<Dependency>
			): String? {
				if (container.isEmpty()) return null
				val entries = container.joinToString(",\n    ") {
					"\"${it.modid.get()}\": \"${it.versionRange.get()}\""
				}
				return "\n  \"$name\": {\n    $entries\n  }"
			}

			val groups = listOfNotNull(
				joinGroup("depends", deps.required),
				joinGroup("recommends", deps.optional),
				joinGroup("breaks", deps.incompatible)
			)

			append(groups.joinToString(","))
		}
	} else {
		buildString {
			fun appendBlock(container: NamedDomainObjectContainer<Dependency>, type: String) {
				container.forEach {
					appendLine(
						"""

						[[dependencies.$modId]]
						modId = "${it.modid.get()}"
						side = "${it.environment.get().uppercase(Locale.getDefault())}"
                        versionRange = "${it.forgeVersionRange.get()}"
						mandatory = ${if (type == "required") "true" else "false"}
                        type = "$type"
						""".replace("                  ", "").trimIndent()
					)
				}
			}

			appendBlock(deps.required, "required")
			appendBlock(deps.optional, "optional")
			appendBlock(deps.incompatible, "incompatible")
		}
	}

	private fun Project.configureJava(stonecutter: StonecutterBuildExtension, requiredJava: JavaVersion) {
		extensions.configure<JavaPluginExtension>("java") {
			withSourcesJar()
			//withJavadocJar()
			sourceCompatibility = requiredJava
			targetCompatibility = requiredJava
		}
	}

	private fun Project.configureIdea() {
		extensions.configure<IdeaModel>("idea") {
			module {
				isDownloadJavadoc = true
				isDownloadSources = true
			}
		}
	}

	private fun Project.configureFletchingTable(isFabric: Boolean) {
		extensions.configure<FletchingTableExtension> {
			mixins.create("main").apply {
				mixin("default", "${prop("mod.id")}.mixins.json") {
					env("CLIENT")
				}
			}
		}

	}

	private fun Project.registerBuildAndCollectTask(extension: ModPlatformExtension, modVersion: String) {
		tasks.register<Copy>("buildAndCollect") {
			group = "build"
			from(
				tasks.named(extension.jarTask.get()),
				tasks.named(extension.sourcesJarTask.get()),
				//tasks.named("javadocJar").get()
			)
			into(rootProject.layout.buildDirectory.file("libs/$modVersion"))
			dependsOn("build")
		}
	}

	private fun Project.configurePublishing(
		ext: ModPlatformExtension,
		loader: String,
		stonecutter: StonecutterBuildExtension,
		modVersion: String,
		channelTag: String,
		fullVersion: String,
	) {
		val additionalVersions = (findProperty("publish.additionalVersions") as String?)?.split(',')?.map(String::trim)
			?.filter(String::isNotEmpty).orEmpty()

		val releaseType = ReleaseType.of(
			channelTag.substringAfter('-').substringBefore('.').ifEmpty { "stable" })

		extensions.configure<ModPublishExtension>("publishMods") {
			val mrStaging = envTrue("TEST_PUBLISHING_WITH_MR_STAGING")

			val modrinthAccessToken = env("MODRINTH_API_TOKEN")
			val curseforgeAccessToken = env("CURSEFORGE_API_TOKEN")
			if (!envTrue("ENABLE_PUBLISHING")) {
				dryRun = true
			}

			val jarTask = tasks.named(ext.jarTask.get()).map { it as Jar }
			val srcJarTask = tasks.named(ext.sourcesJarTask.get()).map { it as Jar }
			val currentVersion = stonecutter.current.version
			val deps = ext.dependencies

			file.set(jarTask.flatMap(Jar::getArchiveFile))
			additionalFiles.from(srcJarTask.flatMap(Jar::getArchiveFile))
			type = releaseType
			version = fullVersion
			changelog.set(rootProject.file("CHANGELOG.md").readText())
			modLoaders.add(loader)

			displayName = "${prop("mod.name")} $modVersion ${loader.replaceFirstChar(Char::titlecase)} $currentVersion"

			modrinth(deps, currentVersion, additionalVersions, mrStaging, modrinthAccessToken)
			if (!mrStaging) curseforge(deps, currentVersion, additionalVersions, false, curseforgeAccessToken)
		}
	}

	fun whenNotNull(stringProp: Property<String>, action: (String) -> Unit) {
		if (!stringProp.orNull.isNullOrBlank()) action(stringProp.get())
	}

	private fun ModPublishExtension.modrinth(
		deps: DependenciesConfig,
		currentVersion: String,
		additionalVersions: List<String>,
		staging: Boolean,
		acesssToken: String?
	) = modrinth {
		if (staging) apiEndpoint = "https://staging-api.modrinth.com/v2"
		projectId = project.prop("publish.modrinth")
		accessToken = acesssToken
		minecraftVersions.addAll(listOf(currentVersion) + additionalVersions)

		if (!staging) {
			deps.required.forEach { dep -> whenNotNull(dep.modrinth) { requires(it) } }
			deps.optional.forEach { dep -> whenNotNull(dep.modrinth) { optional(it) } }
			deps.incompatible.forEach { dep -> whenNotNull(dep.modrinth) { incompatible(it) } }
			deps.embeds.forEach { dep -> whenNotNull(dep.modrinth) { embeds(it) } }
		}
	}

	private fun ModPublishExtension.curseforge(
		deps: DependenciesConfig,
		currentVersion: String,
		additionalVersions: List<String>,
		staging: Boolean,
		acesssToken: String?
	) = curseforge {
		projectId = project.prop("publish.curseforge")
		accessToken = acesssToken
		minecraftVersions.addAll(listOf(currentVersion) + additionalVersions)
		clientRequired.set(true)
		serverRequired.set(false)

		deps.required.forEach { dep -> whenNotNull(dep.curseforge) { requires(it) } }
		deps.optional.forEach { dep -> whenNotNull(dep.curseforge) { optional(it) } }
		deps.incompatible.forEach { dep -> whenNotNull(dep.curseforge) { incompatible(it) } }
		deps.embeds.forEach { dep -> whenNotNull(dep.curseforge) { embeds(it) } }
	}

	private fun Project.configureMixinsValidation(
		isFabric: Boolean,
		isNeoForge: Boolean,
		isForge: Boolean,
		modId: String
	) {
		val version = project.name.substringBefore('-')

		// 确定要对比的兄弟平台
		val siblingLoader = when {
			isFabric -> if (rootProject.subprojects.any { it.name == "$version-neoforge" }) "neoforge" else "forge"
			isNeoForge || isForge -> "fabric"
			else -> return
		}

		val siblingProject = rootProject.subprojects.find { it.name == "$version-$siblingLoader" } ?: return

		tasks.register("validateMixinsJson") {
			group = "verification"
			description = "验证 $modId.mixins.json 与 $siblingLoader 版本是否一致"

			dependsOn(tasks.named("processResources"))
			dependsOn(siblingProject.tasks.named("processResources"))

			doLast {
				val currentFile = file("${project.layout.buildDirectory.get()}/resources/main/$modId.mixins.json")
				val siblingFile = file("${siblingProject.layout.buildDirectory.get()}/resources/main/$modId.mixins.json")

				if (!currentFile.exists()) throw IllegalStateException("当前项目的 mixins.json 不存在: ${currentFile.absolutePath}")
				if (!siblingFile.exists()) throw IllegalStateException("$siblingLoader 项目的 mixins.json 不存在: ${siblingFile.absolutePath}")

				val currentContent = currentFile.readText()
				val siblingContent = siblingFile.readText()

				if (currentContent != siblingContent) {
					logger.warn("========================================")
					logger.warn("mixins.json 与 $siblingLoader 版本不一致")
					logger.warn("当前项目 (${project.name}): ${currentFile.absolutePath} (${currentContent.length} 字符)")
					logger.warn("兄弟项目 ($siblingLoader): ${siblingFile.absolutePath} (${siblingContent.length} 字符)")
					logger.warn("========================================")

					val shorterProject = if (currentContent.length < siblingContent.length) project else siblingProject

					logger.warn("较短的 mixins.json 来自: ${shorterProject.name}")
					logger.warn("正在删除 ${shorterProject.name} 的 build 文件夹...")

					val buildDir = shorterProject.layout.buildDirectory.asFile.get()
					if (buildDir.deleteRecursively()) {
						logger.warn("✓ 已删除: ${buildDir.absolutePath}")
					} else {
						logger.error("✗ 删除失败: ${buildDir.absolutePath}")
					}

					logger.error("请重新运行构建以重新生成资源文件")
				} else {
					logger.quiet("✓ mixins.json 验证通过")
				}
			}
		}

		tasks.named("build") { dependsOn("validateMixinsJson") }
	}
}
