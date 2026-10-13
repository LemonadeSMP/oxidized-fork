package safro.oxidized.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

/** Rose gold tool values inherited from the upstream mod. */
public final class RoseGoldMaterial {
    public static final Tier ROSE_GOLD = new SimpleTier(
            BlockTags.INCORRECT_FOR_GOLD_TOOL,
            250,
            12.0F,
            0.0F,
            22,
            () -> Ingredient.of(Items.COPPER_INGOT)
    );

    private RoseGoldMaterial() {}
}
