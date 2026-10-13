# Repository Guidelines

## Project Identity and Current Platform

Oxidized is a Minecraft mod by Safro that adds vanilla-style uses for copper. The committed baseline is Oxidized 1.8.4 for Minecraft 1.21.1, Fabric Loader 0.16.9, Fabric API 0.107.0+1.21.1, Yarn 1.21.1+build.3, and Java 21, using Gradle 8.8 and Fabric Loom 1.7-SNAPSHOT. Read the current working tree's `gradle.properties`, `build.gradle`, and loader metadata before changing target versions.

The mod ID and resource namespace are `oxidized`, the baseline display name is `Oxidized`, the Maven group is `safro`, and Java packages start at `safro.oxidized`. The in-progress build metadata uses `Oxidized Fork` and `LemonadeSMP`; preserve upstream Safro attribution alongside fork metadata. Preserve the existing GPL v3 license. Do not introduce identities, dependencies, release workflows, or version adapters from another project.

The branch is `main-neoforge`. The working tree uses NeoGradle userdev 7.1.38, NeoForge 21.1.235 and Gradle 9.2.1. The stale Fabric descriptor and empty access widener have been removed, and NeoForge registries replace SafLib registration helpers. Gameplay parity and runtime validation remain in progress. Determine platform status from all relevant files, not the branch name or build scaffold alone. See `docs/neoforge-migration-report.md` for the conversion plan and parity requirements.

## Source Layout and Features

- `src/main/java/safro/oxidized/Oxidized.java`: common initialization, configuration, and creative group setup.
- `client/`: client-only event registration, kiln screen and recipe book, copper golem renderer and model layer. Register these through a `@Mod(dist = Dist.CLIENT)` entrypoint; common classes must not import client APIs. Declare block render types such as cutout in the NeoForge block model JSON under `assets/oxidized/models/block/`.
- `registry/`: blocks, block items, items, kiln block entity, menu, recipe type/serializer, and copper golem entity/attributes.
- `block/`, `block/entity/`, and `block/screen/`: copper rails, pan, button, trap, and furnace-derived copper kiln.
- `item/`: copper pulsar, weather barometer, and rose gold material; tools and smithing template are created in `ItemRegistry`.
- `entity/` and `entity/goal/`: common copper golem entity, oxidation, lightning behavior, and copper button AI.
- `recipe/`: kiln recipes and custom recipe type.
- `mixin/` and `util/`: minecart movement hooks, lightning-rod golem construction, closed-trap movement, weather access, and custom trap damage.
- `src/main/resources/assets/oxidized/`: models, blockstates, textures, and English, Spanish, Russian, and Simplified Chinese translations.
- `src/main/resources/data/`: recipes, recipe advancements, loot tables, damage type, vanilla block tags, and common item tags. Minecraft 1.21.1 uses singular directories such as `recipe`, `advancement`, `loot_table`, and `tags/item`.

Feature scope includes eight vertical cut copper variants (four oxidation stages and four waxed forms), copper lantern, rail, pan, button, kiln, trap, pulsar, barometer, nugget, five rose gold tools, smithing template, copper golem, and spawn egg. Preserve gameplay values, existing IDs, blockstate property names, recipes, rendering, configuration, and save compatibility when adapting platform code.

The fork also adds copper tools and armor through `neoforge/CopperGear`. `util/CopperToolMigration` and `mixin/ItemStackMigrationMixin` convert the five copper tools to matching `minecraft:` IDs during stack construction only when those destination items exist, preserving the full component map and remaining durability. Keep legacy tool registrations available for decoding saves. See `docs/copper-tool-migration.md` for scope and validation limits; armor is excluded, and the current Minecraft 1.21.1 target supplies no native copper tools.

## Dependencies and Configuration

The Fabric baseline requires SafLib 1.1.1, bundled through Loom `include`. It supplied registry helpers, creative group setup, and damage-source utilities. The NeoForge port removes SafLib and uses native deferred registers, a native creative tab, and registry-backed damage sources.

The Fabric baseline used REI 16.0.788 for optional client integration and Mod Menu 11.0.3 as a development dependency. The NeoForge tree has separate optional client adapters for JEI, REI, and EMI; their API dependencies are compile-only and their metadata dependencies are optional and client-side. Keep REI out of the JEI development runtime because the two conflict there. JEI and the required performance mods are local test dependencies, not player-facing requirements. No Mod Menu entrypoint or configuration screen is implemented.

Configuration is Gson JSON at `config/oxidized_config.json`, loaded and saved by `OxidizedConfig`. Preserve its field names and defaults. Pulsar, rose gold, and golem flags currently conditionally register some items; the golem entity itself is always registered. Registration changes require explicit consideration of existing worlds, recipes, and differing client/server configurations.

## Build and Validation

Use JDK 21 and the current wrapper. On Windows, build with `./gradlew.bat build --max-workers=16 --no-daemon`; on other systems use `./gradlew`. Development tasks are `runClient` and `runServer`. The baseline Loom profile uses `run/`; the new NeoGradle scaffold uses `run/<run-name>/` and includes data-generation and GameTest run profiles, without implemented tests. Artifacts are under `build/libs/`. The scaffold also adds `src/generated/resources/`; generated data needs review before being treated as source. No artifact validation script is configured. Verify the new wrapper/plugin combination before claiming build support.

For documentation-only edits, review the content and diff; a Minecraft build is unnecessary. For implementation work, follow the user's requested validation scope and applicable session instructions. When validation is authorized, compile/package the change and exercise relevant client, dedicated-server, resource reload, and gameplay paths. A successful build alone does not demonstrate feature parity. Report checks actually performed and unresolved behavior.

Always launch Minecraft development environments outside the sandbox. For Minecraft 1.20 and newer, include Sodium, ModernFix, FerriteCore, and JEI in the test runtime. Use Embeddium instead of Sodium for Forge 1.20.

Keep generated output, local config, mod JARs, credentials, and development worlds out of commits. Preserve populated profiles and worlds; use copies for migration checks. If using a workspace-local Gradle cache, ensure its directory is ignored before committing.

Run at most one heavy Gradle build at a time and cap it at 16 workers. Keep combined memory use below 85% of installed RAM and inspect machine load before heavy work.

## Change and Migration Workflow

Follow existing Java formatting and package organization. Use `UpperCamelCase` class names, `lowerCamelCase` members, and lowercase `snake_case` resource identifiers. Keep changes focused and explain gameplay or compatibility effects.

Perform NeoForge migration on `main-neoforge` while preserving a recoverable Fabric baseline. Start with Minecraft 1.21.1 / Java 21; additional Minecraft targets require separate scope and verified API support. Replace loader-specific initialization, mappings, registration, dependencies, metadata, and client hooks together. Use supported NeoForge APIs where they preserve behavior; retain narrowly scoped mixins when an API cannot reproduce the existing timing or physics.

Do not silently fix gameplay quirks during a loader port. Record inherited defects and distinguish necessary platform adaptations from deliberate behavior changes. Update these guidelines when the build actually changes platform, including exact dependency versions and launch directories.

Do not commit, push, or publish unless requested. If a commit is requested, use a short imperative subject and the agent identity `Codex <codex@example.invalid>` without changing the user's configured Git identity. Do not commit README changes without explicit approval. There is no checked-in release publishing workflow to invoke; publishing requires explicit authorization and verified target metadata/artifacts.
