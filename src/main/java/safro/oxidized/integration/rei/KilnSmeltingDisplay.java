package safro.oxidized.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultCookingDisplay;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.RecipeHolder;
import safro.oxidized.recipe.CopperKilnRecipe;

import java.util.List;

public class KilnSmeltingDisplay extends DefaultCookingDisplay {
    public KilnSmeltingDisplay(RecipeHolder<CopperKilnRecipe> recipe) {
        super(recipe);
    }

    public KilnSmeltingDisplay(List<EntryIngredient> input, List<EntryIngredient> output, CompoundTag tag) {
        super(input, output, tag);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return OxidizedReiPlugin.KILN_SMELTING;
    }
}
