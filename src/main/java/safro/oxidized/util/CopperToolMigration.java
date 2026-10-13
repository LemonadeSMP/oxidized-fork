package safro.oxidized.util;

import java.util.Set;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import safro.oxidized.Oxidized;

/** Migrates legacy tools only when their exact minecraft counterpart is registered. */
public final class CopperToolMigration {
    private static final Set<String> TOOL_IDS = Set.of(
            "copper_pickaxe", "copper_axe", "copper_shovel", "copper_hoe", "copper_sword"
    );

    private CopperToolMigration() {}

    public static Item replacementFor(Item source) {
        ResourceLocation sourceId = BuiltInRegistries.ITEM.getKey(source);
        if (sourceId == null || !Oxidized.MOD_ID.equals(sourceId.getNamespace())
                || !TOOL_IDS.contains(sourceId.getPath())) {
            return source;
        }

        // Do not use Registry.get: missing entries may resolve to the default (air).
        // Do not cache misses: stacks can also be constructed during registration.
        Item replacement = BuiltInRegistries.ITEM
                .getOptional(ResourceLocation.withDefaultNamespace(sourceId.getPath()))
                .orElse(source);
        return replacement == Items.AIR ? source : replacement;
    }

    public static PatchedDataComponentMap preserveComponents(
            PatchedDataComponentMap source, Item replacement) {
        DataComponentPatch.Builder patch = DataComponentPatch.builder();
        for (TypedDataComponent<?> component : source) {
            patch.set(component);
        }
        for (DataComponentType<?> type : replacement.components().keySet()) {
            if (!source.has(type)) {
                patch.remove(type);
            }
        }
        // Preserve explicit removals as well as values, including components that
        // are currently absent from both items' defaults.
        source.asPatch().entrySet().forEach(entry -> {
            if (entry.getValue().isEmpty()) {
                patch.remove(entry.getKey());
            }
        });

        // Rebase against the new item. Copying the old map directly would keep
        // the old prototype and could lose default durability on the next save.
        return PatchedDataComponentMap.fromPatch(replacement.components(), patch.build());
    }
}
