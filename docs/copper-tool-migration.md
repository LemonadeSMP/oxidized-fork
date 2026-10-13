# Automatic copper tool migration

Copper pickaxes, axes, shovels, hoes and swords migrate from `oxidized:<tool>`
to `minecraft:<tool>` when the exact destination item is registered. Armor,
nuggets and other copper content are outside this migration.

The old item registrations remain available so saved IDs can still be decoded.
An alias cannot replace those IDs while they are registered. Instead,
`ItemStackMigrationMixin` migrates stacks during construction, after their
components have been decoded. This covers ordinary player/container saves,
dropped items, nested item containers, recipe results, stack copies and network
decoding. Stored items in unloaded chunks or offline player data migrate when
loaded; the mod does not scan or rewrite world files in the background.

`CopperToolMigration` checks each exact destination ID, rather than testing
whether the `minecraft` namespace exists. A missing counterpart leaves the
original stack untouched. Registry misses are not cached during startup.
Minecraft 1.21.1 has no native copper tools, so the normal outcome on this target
is no conversion unless another mod supplies those IDs. This implementation is
specific to the current 1.21.1 APIs; upgrading Minecraft still requires porting
the mod and checking the constructor hook.

The conversion keeps stack count and the complete effective component map,
including damage, maximum damage, enchantments, names, lore, repair cost, custom
data, attribute modifiers and explicit component removals. The map is rebased
against the destination item so differences are persisted on subsequent saves.
Keeping both damage and maximum damage preserves remaining durability exactly,
even if the destination has a different default durability. Existing component
values take precedence over destination defaults; migrated tools retain their
component-defined legacy stats. The destination item's Java behavior is used.

No recipe, advancement or tag IDs are rewritten by this mechanism. Integrations
that compare directly against the old item must recognize the destination too;
for example, a recipe ingredient containing only `oxidized:copper_pickaxe` will
not match a converted `minecraft:copper_pickaxe` stack. Migrated saves require
the destination item to remain available thereafter.

Validation should cover each tool with and without its destination registered,
an enchanted/named damaged stack, differing source/destination maximum damage,
removed components, player/chest/dropped/nested stacks, and a second save/load.
These runtime checks have not been performed as part of this change. The new
helper and mixin compiled with Java 21 against the cached NeoForge 21.1.235
classes. This was a scoped compilation with annotation processing disabled,
not a full mod build or proof that the mixin applies in a running game. The
constructor descriptor and component APIs were checked against the locally
resolved, NeoForge-patched Minecraft sources. Mixin configuration JSON parsed
successfully.
