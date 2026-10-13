# Oxidized-Fork

Oxidized is a Minecraft mod that aims to provide more uses for vanilla copper.

This fork is being ported to NeoForge for Minecraft 1.21.1.

## Changes
- Removed REI dependency for dev environment as it caused issues with JEI
- Oxidized-Fork now provides Copper Gear if not present yet
(If vanilla Backport is added mid-save, Oxidized migrate its tools to the Vanilla namespace & disable its own)

## NeoForge migration checklist

The migration is being handled in stages. The first milestone is a NeoForge build that compiles; the unchecked items track the remaining feature and runtime work.

- [x] Compile the complete source set against NeoForge 1.21.1 (`compileJava`).
- [x] Replace the Java entrypoint, registrations, configuration loading, and damage source access with NeoForge APIs.
- [x] Remove the legacy Fabric descriptor and access widener, then verify the packaged NeoForge metadata.
- [x] Preserve all existing block, item, entity, recipe, menu, and creative tab IDs and registration behavior.
- [ ] Port copper oxidation and waxing, rail behavior, pan drops, trap timing, and golem behavior.
- [x] Port the copper kiln block entity, custom recipe type and serializer, recipe book, screen, and other integrations.
- [x] Port client rendering and keep client-only code off dedicated servers.
- [x] Verify retained Mixin targets against the NeoForge 1.21.1 runtime classes; Mojang names mean no refmap is needed.
- [x] Preserve the existing config fields and defaults, resource data, translations, and icon.
- [ ] Verify a client launch, a dedicated server launch, and the migrated gameplay paths on NeoForge 1.21.1.
- [ ] Check save compatibility and record any intentional behavior differences from the Fabric build.

## Links
* [Oxidized Curseforge](https://www.curseforge.com/minecraft/mc-mods/oxidized)
* [Oxidized Discord](https://discord.gg/muAnYRGXrq)
