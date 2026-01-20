# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ExtraSounds Next is a Minecraft mod that adds UI sounds (clicks, scrolls, typing, inventory interactions, etc.) to the game. It supports multiple Minecraft versions (1.18.2 through 1.21.1) and multiple mod loaders (Fabric, NeoForge, Legacy Forge) from a single codebase using Stonecutter.

## Build Commands

```bash
# Build all versions
./gradlew buildAndCollect --no-daemon

# Build specific version+platform
./gradlew :1.21.1-fabric:build
./gradlew :1.21.1-neoforge:build
./gradlew :1.20.1-forge:build

# Run Fabric client in dev
./gradlew :1.21.1-fabric:runClient

# Run data generation
./gradlew :1.21.1-fabric:runDatagen

# Publish a specific version to Modrinth
./gradlew publish1.21.1
```

## Supported Versions Matrix

| MC Version | Fabric | NeoForge | Legacy Forge |
|------------|--------|----------|--------------|
| 1.21.1     | yes    | yes      | -            |
| 1.20.1     | yes    | -        | yes          |
| 1.19.4     | yes    | -        | yes          |
| 1.19.2     | yes    | -        | yes          |
| 1.18.2     | yes    | -        | yes          |

Configured in `settings.gradle.kts`. VCS version is `1.21.1-fabric`. Active Stonecutter version is tracked in `.sc_active_version`.

## Architecture

### Stonecutter Conditional Compilation

Platform-specific code uses Stonecutter comment directives. The "active" platform's code is uncommented; others are wrapped in block comments:

```java
//? if fabric {
/*fabricCode();*/
//?} else {
neoforgeOrForgeCode();
//?}
```

Platform constants (`fabric`, `neoforge`, `forge`) are set in `stonecutter.gradle.kts` based on the project name suffix. String replacements handle API differences (e.g., `ResourceLocation` vs `Identifier` for 1.21.11+).

### Build System

- **Stonecutter 0.8.3** orchestrates multi-version/multi-loader builds
- **build-logic/** contains a custom Gradle plugin (`mod-platform`) that handles platform-specific jar naming, access transformers, dependency declaration for Modrinth/CurseForge, and resource processing
- Per-platform build scripts: `build.fabric.gradle.kts`, `build.neoforge.gradle.kts`, `build.forge.gradle.kts`
- Version-specific properties in `versions/{version}-{platform}/gradle.properties` override `[VERSIONED]` placeholders from root `gradle.properties`

### Source Structure (package: `dev.arbor.extrasoundsnext`)

- **`ExtraSoundsNext.java`** — Core entry point with platform-abstracted utilities (`getLoader()`, `isClient()`, `isModLoaded()`) via Stonecutter conditionals
- **`platform/fabric/`** — Fabric entry points using Fletching Table annotations
- **`platform/neoforge/`** — NeoForge/Forge entry point with `@Mod` annotation
- **`mapping/`** — Sound pack loading (`SoundPackLoader`), auto-generation (`DefaultAutoGenerator`), and addon framework (`SoundGenerator`)
- **`sounds/`** — Sound playback (`SoundManager`), predefined sounds (`Sounds`), categories (`Categories`), volume mixing (`Mixers`)
- **`mixin/`** — ~20 mixin classes organized by game system: `core/`, `gui/`, `hotbar/`, `inventory/`, `chat/`, `typing/`, `action/`, `misc/`, `screens/`
- **`annotation/`** — Addon discovery framework (`@SoundsGenerator`, `ISoundsGenerator`, `AddonFinder`)
- **`gui/`** — Custom sound settings screens
- **`json/`** — Sound definition serialization

### Sound Generation Framework

Third-party mods can provide custom sounds via the `@SoundsGenerator` annotation on classes implementing `ISoundsGenerator`. The `AddonFinder` discovers these at runtime. `DefaultAutoGenerator` handles vanilla items/blocks by mapping material types to sounds.

### Access Transformers

- Fabric: `src/main/resources/aw/{version}.accesswidener`
- Forge/NeoForge: autogen by accesswidener

### Mixin Configuration

Defined in `src/main/resources/extrasounds.mixins.json`. Uses Fletching Table for annotation-driven mixin registration.

## Key Files

| File | Purpose |
|------|---------|
| `settings.gradle.kts` | Version/loader matrix definition |
| `stonecutter.gradle.kts` | Stonecutter parameters, swaps, constants |
| `gradle.properties` | Mod metadata and `[VERSIONED]` dependency placeholders |
| `build-logic/` | Custom `mod-platform` Gradle plugin |
| `versions/*/gradle.properties` | Per-version dependency versions |
