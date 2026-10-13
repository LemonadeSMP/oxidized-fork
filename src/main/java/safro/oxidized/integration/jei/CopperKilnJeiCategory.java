package safro.oxidized.integration.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import safro.oxidized.recipe.CopperKilnRecipe;
import safro.oxidized.registry.BlockRegistry;

public final class CopperKilnJeiCategory implements IRecipeCategory<RecipeHolder<CopperKilnRecipe>> {
    private static final int WIDTH = 82;
    private static final int HEIGHT = 54;

    private final IDrawable icon;

    public CopperKilnJeiCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemLike(BlockRegistry.COPPER_KILN.get());
    }

    @Override
    public RecipeType<RecipeHolder<CopperKilnRecipe>> getRecipeType() {
        return OxidizedJeiPlugin.KILN_SMELTING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.oxidized.kiln_smelting");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CopperKilnRecipe> recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, 1, 18);
        if (!recipe.value().getIngredients().isEmpty()) {
            input.addIngredients(recipe.value().getIngredients().get(0));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 18)
                .addItemStack(recipe.value().getResultItem(RegistryAccess.EMPTY));
    }
}
