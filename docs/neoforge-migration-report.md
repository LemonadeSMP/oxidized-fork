# Oxidized: Fabric to NeoForge with 1:1 feature parity

Prepared 2026-10-13 from the repository's Fabric baseline and inspected Java/resources. Only AGENTS.md and this report were written for this task. During the review, concurrent working-tree changes added a NeoGradle build scaffold; this report distinguishes those changes from the baseline and from a completed port. External API guidance uses official documentation for Minecraft 1.21.1 wherever available.

## Recommendation and parity contract

Port Oxidized 1.8.4 to **NeoForge on Minecraft 1.21.1, using Java 21**, before considering another Minecraft version. Keeping the game version fixed distinguishes loader changes from Minecraft changes. Use an official 1.21.1 MDK matching the selected build plugin as the reference, and pin the plugin, wrapper and NeoForge versions together. The concurrent scaffold selects NeoGradle userdev 7.1.38, NeoForge 21.1.235 and Gradle 9.2.1; their compatibility was not resolved or built in this documentation task. The official setup supports both NeoGradle and ModDevGradle. [NeoForge setup documentation](https://docs.neoforged.net/docs/1.21.1/gettingstarted/).

Parity must cover all content and resource IDs; recipes, loot, advancements and tags; text, models, textures, animations, sounds and particles; creative group entries; gameplay rules and timing; item attributes; configuration; client rendering; the kiln recipe book; optional REI integration; dedicated server and multiplayer behavior; and save/load compatibility. A copied Fabric world on the same Minecraft version should preserve Oxidized content, subject to the other installed mods also having compatible replacements.

Treat the Fabric build as the reference. Compilation and equal item counts are insufficient evidence. Defects already present need a recorded decision: preserve observed behavior for a strict port, or approve a correction and identify it as a deviation. Necessary platform adaptations should also be documented.

## Repository baseline

The baseline `gradle.properties` selects Minecraft 1.21.1, Yarn 1.21.1+build.3, Fabric Loader 0.16.9, Fabric API 0.107.0+1.21.1, REI 16.0.788, Mod Menu 11.0.3, and SafLib 1.1.1. Its `build.gradle` uses Loom 1.7-SNAPSHOT, includes SafLib inside the artifact, and targets Java 21. Its wrapper is Gradle 8.8. `fabric.mod.json` advertises `1.21.x`, broader than the single version selected by the build; initially advertise only the verified Minecraft target in the port.

The working-tree scaffold now selects Minecraft range `[1.21.1]`, REI 16.0.799 with NeoForge coordinates, display name `Oxidized Fork`, and authors `LemonadeSMP`, while preserving `oxidized` and `safro`. Preserve upstream Safro attribution alongside fork authorship. It removes Fabric/SafLib/Mod Menu build dependencies and adds per-run directories and generated resources; Java still requires loader/mapping/helper conversion. These selections are observed work in progress, not verified recommendations.

There are 33 Java files, 53 recipe JSONs, and 33 recipes of type `oxidized:kiln_smelting`. Entrypoints are `Oxidized`, `OxidizedClient`, and the Fabric `rei_client` plugin. Five mixins are required. The access widener contains only its header and no access rules. No automated test source set or CI/release workflow is checked in. Despite its name, `main-neoforge` currently contains a Fabric project.

## Feature inventory

| Feature and source | Behavior to preserve | Conversion work |
| --- | --- | --- |
| Vertical cut copper, `BlockRegistry` | Four oxidation stages, four waxed forms, vanilla oxidation/scraping/waxing, hardness 3 and resistance 6 | Preserve eight IDs; connect three oxidation transitions and four waxing pairs |
| Copper lantern, `BlockRegistry` | Standing/hanging lantern, light 15, existing geometry/textures | Vanilla lantern equivalent, registration and cutout rendering |
| Copper rail, `CopperRailBlock`, minecart mixins, `MinecartHandler` | Propagation limit 50 by default, slopes, redstone, waterlogging; unpowered horizontal velocity multiplier 0.25; powered pre-movement velocity changes | Translate propagation and physics, including subsequent vanilla/NeoForge processing |
| Copper pan, `CopperPanBlock` | Waterlogged over sand-tag blocks or gravel; `hatch` stages 0–2; repeated production at stage 2; configurable sound/drop checks | Preserve random ticks, sequential probability checks, spawn offsets and pickup delay |
| Copper button, `CopperButtonBlock` | Iron block-set behavior, 20-tick press, golem interaction | Preserve properties, pulse length, sounds and mounting |
| Copper kiln, block/entity/menu/screen and recipe classes | Custom recipe type, 100-tick packaged recipes, half vanilla fuel duration, XP, furnace inventory/UI/book, light 13, smoke/sound | Translate full furnace inheritance chain and recipe serialization |
| Copper trap, block and `LivingEntityMixin` | Movement-triggered damage 5, closed state, 60-tick reopening, redstone/manual changes, movement restriction | Reproduce collision/tick timing and damage-source lookup |
| Copper pulsar, `CopperPulsarItem` | Active by default; inventory-wide item/XP pickup in an expanded box, reach 10; non-sneaking use toggles; `Powered` custom data | Preserve item component representation, pickup and messages |
| Barometer, `BarometerItem`, `ServerWorldAccessor` | Server weather timer / 20, translated gold action-bar message, snow classification by biome temperature | Translate weather-data access and messages |
| Copper nugget, `ItemRegistry` and data | Nugget/ingot conversion and common tag membership | Preserve recipes and compatible tags |
| Rose gold tools/template, `ItemRegistry`, `RoseGoldMaterial` | Five gold-tool upgrades with copper; durability 250, speed 12, material attack bonus 0, enchantability 22, gold mining restrictions, copper-ingot repairs; per-tool attack modifiers | Preserve smithing data, template UI and attributes |
| Copper golem, entity/goals/model/renderer and rod mixin | Construction, egg, button AI, thunderstorm rod seeking, oxidation/statue, axe scraping, lightning, four textures, pressing animation | Translate entity, attributes, tracked data, AI, rendering and construction |
| JEI, REI and EMI, `integration/` | Separate optional kiln categories, recipe displays and kiln workstations; JEI and REI screen click areas `(78,32,28,23)` | Compile/runtime compatibility and viewer-present/absent behavior remain unverified |

## Build, mappings and initialization

Complete the build conversion using the official NeoForge MDK matching the scaffold's NeoGradle setup, with Mojang mappings. Check the chosen wrapper/plugin compatibility and all dependency coordinates before relying on the scaffold. Keep `maven-publish` if needed for the existing publication use case. Set Java toolchain and release to 21; preserve source JAR generation and LICENSE packaging.

Translate Yarn classes, members and signatures throughout the source. Starting equivalents include `World` → `Level`, `ServerWorld` → `ServerLevel`, `Identifier` → `ResourceLocation`, `Text` → `Component`, `ScreenHandler` → `AbstractContainerMenu`, `AbstractMinecartEntity` → `AbstractMinecart`, and `DataTracker` → `SynchedEntityData`. Verify overloads and NeoForge patches in the resolved target sources rather than relying on mechanical replacements.

Create an `@Mod("oxidized")` entrypoint and replace `fabric.mod.json` with `META-INF/neoforge.mods.toml`. Preserve identity, description, attribution, license, links and version expansion. Declare target requirements and the mixin configuration using NeoForge metadata. The empty access widener needs no access transformer replacement. Confirm mixin build tooling against the selected MDK. [NeoForge mod files](https://docs.neoforged.net/docs/1.21.1/gettingstarted/modfiles/).

Register renderers/model layers through `EntityRenderersEvent.RegisterRenderers` and `RegisterLayerDefinitions`, and screens through `RegisterMenuScreensEvent`. Preserve cutout rendering for rail, kiln, lantern and trap through the target render-layer API. Isolate these client classes from common initialization. World mutation and authoritative gameplay belong on the logical server. [NeoForge side rules](https://docs.neoforged.net/docs/1.21.1/concepts/sides/).

## Replace SafLib with native code

SafLib's project page lists Fabric support; a compatible NeoForge binary has not been established. Replacing the helpers used here is the recommended path. Audit the actual 1.1.1 source or resolved artifact before implementation, especially creative group ordering and hidden item-property defaults. [SafLib project](https://modrinth.com/mod/saflib).

| Current helper | Native replacement |
| --- | --- |
| `BaseBlockItemRegistry.register` overloads | Deferred block, block-item, item and block-entity registrations, preserving IDs/settings |
| `settings()` | Explicit item properties after checking helper defaults |
| `SafLib.createGroup` / `registerAll` | Creative mode tab with the same identity, icon and ordered content |
| `DamageSourceUtil.register` | Resource key for datapack damage type `oxidized:trap` |
| `DamageSourceUtil.create` | Damage source from the current level's damage-type registry holder |

Use `DeferredRegister` on the mod event bus for blocks, items, entity types, block entities, menus, recipe types/serializers and creative tab. Resolve holders only once the corresponding registry is populated, or inside suppliers with valid ordering. Replace Fabric attribute registration with `EntityAttributeCreationEvent`. Do not reproduce eager static registration mechanically. [NeoForge registries](https://docs.neoforged.net/docs/1.21.1/concepts/registries/).

## Configuration and optional integrations

Retain Gson and `oxidized_config.json`, using `FMLPaths.CONFIGDIR` in place of Fabric's config directory. Load the JSON before deciding which deferred item registrations to declare. Ordinary NeoForge TOML configuration would change both format and registration timing.

Preserve defaults: rail range 50; pulsar enabled/reach 10; iron/gold chances 0.15; sand 0.24; gravel 0.12; emerald 0.02; pan sound enabled; golem button chance 30; golem enabled; rose gold enabled.

Disabling pulsar or rose gold currently omits item registrations. Disabling golems omits egg, construction and client renderer but leaves the entity type registered. Recipe data remains unconditional. Capture the Fabric behavior under these flags. Always registering content while gating availability improves stability but changes the contract and needs a deliberate parity decision. Define multiplayer handling of differing client/server flags; a config handshake would be new work, not behavior already implemented here.

JEI, REI and EMI each have a separate optional client adapter. Their APIs are compile-only and their metadata dependencies are optional and client-side. JEI remains the only viewer in the local development runtime; do not add REI there because it conflicts with the required JEI test runtime. The adapter APIs, recipe displays, and behavior with each viewer installed or absent still need build and runtime validation. [REI upstream](https://github.com/shedaniel/RoughlyEnoughItems), [EMI upstream](https://github.com/emilyploszaj/emi).

Mod Menu has no implemented entrypoint/configuration screen here. Remove its Fabric development dependency. A NeoForge config screen remains additional scope.

## Copper transitions, resources and kiln

Replace `OxidizableBlocksRegistry` with NeoForge data maps: `data/neoforge/data_maps/block/oxidizables.json` using `next_oxidation_stage` for three transitions, and `data/neoforge/data_maps/block/waxables.json` using `waxed` for four pairs. Use vanilla weathering copper behavior for natural oxidation. The maps also supply reverse scraping/dewaxing relationships. Verify reload and lightning behavior in the selected runtime. [1.21.1 built-in data maps](https://docs.neoforged.net/docs/1.21.1/resources/server/datamaps/builtin/).

Keep singular 1.21.1 resource directories and preserve assets, all four translations, recipe advancement criteria, loot, vanilla mining/rail tags and `oxidized:trap` metadata. Preserve even `oxidized:coppper_ingot_from_nuggets`: fixing its spelling renames a resource.

Recipes use `c:ingots/copper` and `c:copper_nuggets`; the latter is locally defined. Preserve this alias and consider adding `c:nuggets/copper` after inspecting the target common tags. Compare resolved ingredient membership and outputs before changing namespaces.

Keep type and serializer `oxidized:kiln_smelting`, block entity `oxidized:copper_kiln`, and menu `oxidized:copper_kiln_screen_handler`. Translate to the target cooking classes and preserve JSON plus network serialization of ingredient, result, category/group, XP and time. Reuse the target cooking serializer if its signature supports the custom recipe; otherwise implement equivalent `MapCodec` and `StreamCodec`. [NeoForge recipe serialization](https://docs.neoforged.net/docs/1.21.1/resources/server/recipes/).

Translate the furnace block entity, menu, screen and recipe book together. Preserve inherited slot order, progress values, shift-click, sided automation, output XP, names and saves. Adapt the fuel override and maintain integer halving. With the packaged 100-tick recipes, per-fuel output counts must match the reference. Verify NeoForge recipe-book classification and discovery of custom kiln recipes instead of assuming the existing furnace category suffices. Preserve furnace sprites, kiln toggle text and REI click area. [NeoForge menus](https://docs.neoforged.net/docs/1.21.1/gui/menus/).

## Vanilla hooks and inherited quirks

| Current mixin | Conversion strategy |
| --- | --- |
| `MinecartRailMixin` | Retain HEAD injection in mapped `AbstractMinecart.moveAlongTrack` if no target rail hook preserves its phase; verify descriptor |
| `MinecartEntityAccessor` | Map block-hit and maximum-speed invokers; verify semantics in patched code |
| `LightningRodBlockMixin` | Retain mapped placement callback unless an event covers the identical paths/timing |
| `LivingEntityMixin` | Preserve living tick HEAD restriction; compare event ordering before replacing it |
| `ServerWorldAccessor` | Map weather-data accessor or use a public API returning the same per-world data |

The rail hook is cancellable but never cancels vanilla movement. Its powered branch sets velocity to normalized full velocity times the maximum-speed accessor times ten, then vanilla processing continues. This expression does not establish a final tenfold speed. Cancelling movement or changing the speed cap would alter behavior. Compare displacement on slopes, occupied/empty carts, and mixed rail transitions in both runtimes.

Panning performs fresh random draws in sequence: iron, gold, sand, emerald, gravel, then clay. Configured chances are conditional probabilities. Approximate unconditional defaults are 15%, 12.75%, 17.34%, 1.10%, 6.46%, and 47.35%. Preserve draw order/comparisons; a weighted loot table using raw values changes the distribution. Stage 2 does not reset after producing an item.

The pulsar stores boolean `Powered` in `minecraft:custom_data`; missing data means active. It uses player-collision pickup methods, including their restrictions. A new component would require migration. The existing inventory tick casts its owner to a server player, and toggling replaces the custom-data compound. Record these edge cases before adding guards or merging data. Preserve its existing toggle message; localization cleanup is a separate change.

Golem construction occurs on adding a lightning rod above carved pumpkin and copper block or cut copper. It consumes the pattern, spawns the golem, notifies neighbors and triggers the summoned-entity criterion for nearby server players. Preserve eligible blocks and placement order.

Golem attributes are health 15, speed 0.27, knockback resistance 0.75; dimensions 0.5 × 0.9, fire immunity, tracking range 10. Goals prioritize thunderstorm rods (range 10), copper buttons (range 8), wandering, player watching and looking around. At a reached button, the integer draw from 1 through the configured chance must equal 2. Oxidation thresholds are exactly 400,000, 800,000 and 1,200,000 ticks; final oxidation disables AI. Axe scraping changes stage without resetting the timer; lightning resets both. Preserve geometry, 32×32 textures, shadows and pressing animation.

The golem has **no custom NBT persistence of oxidation/timer and no tracked oxidation state**; only pressing is tracked. Its stage starts uninitialized until the oxidation tick. Client ticking, late joins and save/load can expose differences. Adding persistence or synchronization changes observable behavior: capture reference outcomes and record any correction. Also inspect lightning damage ordering: `super.damage` runs before the attacker check, so the returned rejection alone does not prove damage was prevented.

Trap collision uses render-position fields to detect movement; closed-trap occupants are teleported to their current position at living tick HEAD. Compare the actual restriction for players/mobs, jumping, trap-avoiding entities, reopening and redstone. A generic slowness effect is not automatically equivalent.

## Implementation sequence

1. **Capture the reference:** preserve a Fabric commit/artifact, copy representative saves, record registries/resources and creative order, exercise default/disabled flags, and inspect SafLib helpers. Record inherited failures separately.
2. **Establish the build:** replace tooling/metadata and translate mappings. Gate: resolved dependencies, compilation, packaging, valid metadata and unchanged IDs; inspect for leftover Fabric dependencies/bundled SafLib.
3. **Restore common content:** deferred registries, early JSON config, tab, attributes, damage and copper maps. Gate: matching manifests/defaults, resource reload, client and dedicated-server loading.
4. **Restore gameplay/client:** kiln/menu/book, hooks, items, golem/AI/rendering and cutout layers. Gate: feature comparisons below with deviations recorded.
5. **Validate optional recipe viewers:** check JEI, REI and EMI separately when installed and absent, including server loading without client dependencies.
6. **Validate saves/package:** migrate copied Fabric worlds, save/restart on NeoForge, compare data and review dependency ranges. Gate: no unexplained content or behavior loss; update AGENTS.md with the implemented build.

## Acceptance evidence required

| Comparison | Required evidence |
| --- | --- |
| Content/data | Same IDs and creative order; 53 recipes/33 kiln recipes; matching loot, advancements, assets, text and ingredient sets; conditional-registration behavior recorded |
| Rail | Range boundary checks around 50 and changed values, power removal, slopes, mixed rails, waterlogging; matched cart position/velocity traces |
| Pan | Fluid/substrate gating, hatch progression, repeated drops, probability behavior, sound flag and pickup |
| Kiln | All recipes and matching XP/time/fuel; slot transfer, hopper access, recipe book, synchronized progress, names, save/reload and break drops |
| Golem | Egg/construction, AI/chance, oxidation boundaries, scraping/lightning/statue, model/animation; late join/save outcomes and inherited defects |
| Trap/items/tools | Trap damage/death message/timing/movement/redstone; pulsar state/pickup/custom-data saves; barometer messages; tool attributes, mining, repair and smithing |
| Platform/config | Client/server loading, multiplayer joins/reconnects, every flag and changed numeric values, differing config policy, JEI/REI/EMI installed/absent, resource reload |
| Migrated world | Placed blocks/properties, kiln inventory/progress/name, tool damage/enchantments, pulsar data, entity existence, second NeoForge save/load |

These are proposed checks, not completed results. Use deterministic comparisons or controlled distributions for random/tick-dependent features. Preserve original worlds. Other mods and loader-specific world data prevent an unqualified promise that every Fabric modpack save transfers.

Unresolved work includes verifying the selected NeoForge/MDK toolchain and viewer artifacts/APIs, auditing SafLib helper semantics, and deciding how to handle inherited config/golem defects. Each feature has a native API or targeted-mixin path, but 1:1 parity can only be claimed after those decisions and acceptance comparisons are complete.
