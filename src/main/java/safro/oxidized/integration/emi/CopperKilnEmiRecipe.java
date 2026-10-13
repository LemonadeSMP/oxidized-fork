package safro.oxidized.integration.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;
import safro.oxidized.recipe.CopperKilnRecipe;

public final class CopperKilnEmiRecipe extends BasicEmiRecipe {
    public CopperKilnEmiRecipe(RecipeHolder<CopperKilnRecipe> holder) {
        super(OxidizedEmiPlugin.KILN_SMELTING, holder.id(), 82, 54);
        CopperKilnRecipe recipe = holder.value();
        if (!recipe.getIngredients().isEmpty()) {
            inputs.add(EmiIngredient.of(recipe.getIngredients().get(0)));
        }
        outputs.add(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 27, 18);
        if (!inputs.isEmpty()) {
            widgets.addSlot(inputs.get(0), 1, 18);
        }
        widgets.addSlot(outputs.get(0), 61, 18).recipeContext(this);
    }
}
