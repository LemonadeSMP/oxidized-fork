package safro.oxidized.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import safro.oxidized.client.screen.CopperKilnScreen;
import safro.oxidized.recipe.CopperKilnRecipe;
import safro.oxidized.registry.BlockRegistry;
import safro.oxidized.registry.RecipeRegistry;

@JeiPlugin
public class OxidizedJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath("oxidized", "jei_plugin");
    static final RecipeType<RecipeHolder<CopperKilnRecipe>> KILN_SMELTING =
            RecipeType.createFromVanilla(RecipeRegistry.COPPER_KILN_TYPE.get());

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CopperKilnJeiCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            registration.addRecipes(KILN_SMELTING,
                    minecraft.level.getRecipeManager().getAllRecipesFor(RecipeRegistry.COPPER_KILN_TYPE.get()));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.COPPER_KILN.get()), KILN_SMELTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CopperKilnScreen.class, 78, 32, 28, 23, KILN_SMELTING);
    }
}
