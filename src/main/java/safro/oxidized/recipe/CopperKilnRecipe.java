package safro.oxidized.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import safro.oxidized.registry.BlockRegistry;
import safro.oxidized.registry.RecipeRegistry;

public class CopperKilnRecipe extends AbstractCookingRecipe {
    public CopperKilnRecipe(String group, CookingBookCategory category, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(RecipeRegistry.COPPER_KILN_TYPE.get(), group, category, input, output, experience, cookTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.COPPER_KILN.get());
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.COPPER_KILN_TYPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.COPPER_KILN_SERIALIZER.get();
    }
}
