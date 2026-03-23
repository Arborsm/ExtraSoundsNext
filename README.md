# ExtraSounds Next

A multi-platform Minecraft mod that adds UI sounds to the game,
supporting **Fabric**, **NeoForge**, and **Legacy Forge** across
multiple Minecraft versions from a single codebase using
[Stonecutter](https://stonecutter.kikugie.dev/).

ExtraSounds Next enhances your Minecraft experience by adding
custom sounds for UI interactions like clicks, scrolls, typing,
inventory management, and more.

## Features

* **Rich Sound System**: Custom sounds for clicks, scrolls, typing, inventory interactions, and more
* **Multi-Platform Support**: Fabric, NeoForge, and Legacy Forge from a single codebase
* **Multi-Version Support**: Minecraft 1.18.2 through 1.21.1
* **Volume Control**: Custom sound settings screens with per-category volume control
* **Add-on Framework**: Third-party mods can provide custom sounds via the `@SoundsGenerator` annotation
* **Auto-Generation**: Automatically generates sounds for vanilla items and blocks based on material types
* **Sound Pack System**: Load custom sound definitions from JSON files

## Supported Versions

| Minecraft Version | Fabric | NeoForge | Legacy Forge |
|-------------------|--------|----------|--------------|
| 1.21.1            | ✅     | ✅       | ❌           |
| 1.20.1            | ✅     | ❌       | ✅           |
| 1.19.4            | ✅     | ❌       | ✅           |
| 1.19.2            | ✅     | ❌       | ✅           |
| 1.18.2            | ✅     | ❌       | ✅           |

The active version is tracked in `.sc_active_version`. Configure the version matrix
in `settings.gradle.kts`.

## Getting Started

### Prerequisites

* **Java 21** or higher
* Suitable IDE (IntelliJ IDEA recommended)
* Git
* Basic knowledge of Minecraft modding (Fabric/NeoForge)

### Building from Source

#### 1. **Clone the repository**

```bash
git clone https://github.com/Arborsm/ExtraSoundsNext.git
cd ExtraSoundsNext
```

#### 2. **Open in your IDE**

Import the project as a Gradle project in IntelliJ IDEA or Eclipse.

#### 3. **Stonecutter IntelliJ Plugin (Recommended)**

Install the [Stonecutter IntelliJ plugin](https://plugins.jetbrains.com/plugin/18700-stonecutter/)
for:
- Comment syntax highlighting and completion
- One-click version/platform switching
- Enhanced development experience

#### 4. **Build the project**

```bash
# Build all versions
./gradlew buildAndCollect --no-daemon

# Build specific version+platform
./gradlew :1.21.1-fabric:build
./gradlew :1.21.1-neoforge:build
./gradlew :1.20.1-forge:build
```

#### 5. **Run in development**

```bash
# Run Fabric client
./gradlew :1.21.1-fabric:runClient

# Run NeoForge client
./gradlew :1.21.1-neoforge:runClient
```

#### 4. **Mod Configuration**

ExtraSounds Next is configured in `gradle.properties` with the following metadata:

| Property           | Current Value                                |
|--------------------|----------------------------------------------|
| `mod.id`           | `extrasounds`                                |
| `mod.name`         | `ExtraSounds Next`                           |
| `mod.group`        | `dev.arbor`                                  |
| `mod.version`      | `1.4`                                        |
| `mod.authors`      | `Arborsm, stashymane`                        |
| `mod.license`      | `MIT`                                        |
| `mod.description`  | `Improve your clicks, scrolls and more with Extra Sounds.` |
| `mod.sources_url`  | `https://github.com/Arborsm/ExtraSoundsNext` |
| `mod.homepage_url` | `https://github.com/Arborsm/ExtraSoundsNext` |
| `mod.issues_url`   | `https://github.com/Arborsm/ExtraSoundsNext/issues` |

Version-specific dependencies are defined using the `[VERSIONED]` placeholder
in `gradle.properties` and set in `versions/{version}-{loader}/gradle.properties`.

#### 5. **Project Structure**

ExtraSounds Next uses a flattened package structure (refactored in v0.9-alpha.3):

* **`dev.arbor.extrasoundsnext`** - Main package (no platform abstraction layer)
* **`dev.arbor.extrasoundsnext.platform.{fabric|neoforge}`** - Platform-specific entry points
* **`dev.arbor.extrasoundsnext.annotation`** - Add-on discovery framework
* **`dev.arbor.extrasoundsnext.mapping`** - Sound pack loading and auto-generation
* **`dev.arbor.extrasoundsnext.sounds`** - Sound playback and management
* **`dev.arbor.extrasoundsnext.mixin`** - Mixin classes organized by game system
* **`dev.arbor.extrasoundsnext.gui`** - Custom sound settings screens

#### 6. **Resource Files**

Key resource files:
* `src/main/resources/extrasounds.mixins.json` - Mixin configuration (uses Fletching Table)
* `src/main/resources/aw/{version}.accesswidener` - Access transformers per version
* `src/main/resources/assets/extrasounds/sounds.json` - Sound definitions
* `src/main/resources/assets/extrasounds/lang/*.json` - Localization files

## Development

### Stonecutter Configuration

ExtraSounds Next uses [Stonecutter 0.9-alpha.3](https://stonecutter.kikugie.dev/) for
multi-version and multi-loader support. Configure Stonecitter in
`stonecutter.gradle.kts` and `settings.gradle.kts`.

**Platform-specific code** using Stonecutter conditionals:

```java
//? if fabric {
/*fabricOnlyCode();*/
//?} else {
neoforgeOrForgeCode();
//?}
```

**Version-specific code** works similarly:

```java
//? if 1.21.1 {
LOGGER.info("hello 1.21.1!");
//?} else {
/*LOGGER.info("hello from other versions!");
 *///?}
```

**String replacements** handle API differences between versions (e.g., `ResourceLocation` vs `Identifier` for 1.21.1+).

For more details, read the [Stonecutter documentation](https://stonecutter.kikugie.dev/wiki/).

### Running in Development

The Gradle plugins of the respective platform should provide run configurations.
If not, you can run the server and client with the respective Gradle tasks.
Be careful to run the correct task for the selected Stonecutter platform and Minecraft version.

### Platform-Specific Entry Points

ExtraSounds Next uses platform-specific entry points:

* **Fabric**: `ExtraSoundsNextFabric` and `ExtraSoundsNextFabricClient` (uses Fletching Table annotations)
* **NeoForge/Legacy Forge**: `ExtraSoundsNextForge` (uses `@Mod` annotation)

Shared code resides in the main `dev.arbor.extrasoundsnext` package and uses
Stonecutter conditionals for platform-specific behavior where necessary.

### Adding Dependencies

Dependencies are managed through the custom `mod-platform` Gradle plugin in `build-logic/`.
For platform-specific dependencies, modify the `platform` block in the respective
`build.{platform}.gradle.kts` file:

```kotlin
platform {
  loader = "fabric" // or "neoforge" or "forge"
  dependencies {
    required("my-lib") {
      slug("my-lib")
      versionRange = ">=${prop("deps.my-lib")}"
      forgeVersionRange = "[${prop("deps.my-lib")},)"
    }
  }
}
```

This automatically updates metadata files (`fabric.mod.json`, `neoforge.mods.toml`)
and mod hosting platform configurations (Modrinth, CurseForge).

**Version-specific dependencies** are defined in `versions/{version}-{loader}/gradle.properties`
using the `[VERSIONED]` placeholder from root `gradle.properties`.

### Sound System Architecture

**Sound Definition**: Sounds are defined in `src/main/resources/assets/extrasounds/sounds.json`
and organized by categories (clicks, scrolls, typing, inventory, etc.).

**Sound Pack Loading**: `SoundPackLoader` loads custom sound packs from resource packs,
allowing users and other mods to override or extend the default sounds.

**Auto-Generation**: `DefaultAutoGenerator` automatically generates appropriate sounds
for vanilla items and blocks by mapping material types to sound categories.

**Add-on Framework**: Third-party mods can provide custom sounds via the
`@SoundsGenerator` annotation on classes implementing `ISoundsGenerator`.
The `AddonFinder` discovers these at runtime.

**Volume Control**: Custom sound settings screens (`VolumeScreen`, `VolumeSlider`)
provide per-category volume control with master volume adjustment.

**Mixin Integration**: ~20 mixin classes intercept game events at key points:
- `core/` - Client initialization and player interaction
- `gui/` - Sound settings and game options
- `hotbar/` - Hotbar slot selection and mouse interactions
- `inventory/` - Inventory management and container screens
- `chat/` - Chat input and message handling
- `typing/` - Text field input and sign editing
- `action/` - Player actions and entity interactions
- `misc/` - Sound manager and other utilities

## Resources and Links

**Documentation:**
- [Stonecutter Documentation](https://stonecutter.kikugie.dev/wiki/)
- [Fletching Table Documentation](https://github.com/LlamaLad7/MixinExtras/wiki)
- [NeoForge Documentation](https://docs.neoforged.net/docs/gettingstarted/)
- [Fabric Documentation](https://docs.fabricmc.net/develop/)

**Development Tools:**
- [Pre-commit](https://pre-commit.com/)
- [Git Source Control](https://git-scm.com/doc)
- [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)
- [Semantic Versioning](https://semver.org/)
- [Gradle Documentation](https://docs.gradle.org/current/userguide/userguide.html)

**Publishing:**
- [Modrinth PAT](https://modrinth.com/settings/pats)
- [CurseForge API Tokens](https://legacy.curseforge.com/account/api-tokens)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

### Help and Support

For help and support:
- [GitHub Issues](https://github.com/Arborsm/ExtraSoundsNext/issues) - Bug reports and feature requests
- ["Kiku's Realm" Discord](https://discord.kikugie.dev/) - Stonecutter-related questions
- ["The Fabric Project" Discord](https://discord.gg/v6v4pMv) - Fabric modding support
- ["The NeoForge Project" Discord](https://github.com/neoforged) - NeoForge/Forge modding support

## License and Credits

**License**: MIT License. See [`LICENSE`](LICENSE) for details.

**Credits**:
- **Arborsm** - Lead developer
- **stashymane** - Contributor
- Based on [Stonecutter Mod Template](https://github.com/rotgruengelb/stonecutter-mod-template) by murderspagurder
  - Which was adapted from [KikuGie's Elystra Trims](https://github.com/kikugie/elytra-trims) setup
- Uses [Stonecutter](https://stonecutter.kikugie.dev/) by KikuGie for multi-version/multi-loader support
- Uses [Fletching Table](https://github.com/LlamaLad7/MixinExtras) for annotation-driven mixin registration

**Version History**:
- **1.4-test version** - Major refactor: Migrated to Fletching Table, implemented multi-platform addon loader, flattened package structure, removed platform abstraction layer
- **v1.5.0** - Current release with full multi-version/multi-loader support
