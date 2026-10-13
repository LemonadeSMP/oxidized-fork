package safro.oxidized.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import safro.oxidized.recipe.CopperKilnRecipe;
import safro.oxidized.registry.BlockRegistry;
import safro.oxidized.registry.RecipeRegistry;

@EmiEntrypoint
public class OxidizedEmiPlugin implements EmiPlugin {
    static final EmiRecipeCategory KILN_SMELTING = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath("oxidized", "kiln_smelting"),
            EmiStack.of(BlockRegistry.COPPER_KILN.get()),
            EmiStack.of(BlockRegistry.COPPER_KILN.get()));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(KILN_SMELTING);
        registry.addWorkstation(KILN_SMELTING, EmiStack.of(BlockRegistry.COPPER_KILN.get()));
        for (var recipe : registry.getRecipeManager().getAllRecipesFor(RecipeRegistry.COPPER_KILN_TYPE.get())) {
            registry.addRecipe(new CopperKilnEmiRecipe(recipe));
        }
    }
}
