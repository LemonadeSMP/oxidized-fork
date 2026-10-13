package safro.oxidized.client.recipe;

import java.util.Set;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class CopperKilnRecipeBook extends AbstractFurnaceRecipeBookComponent {
    private static final Component RECIPE_FILTER_TEXT = Component.translatable("gui.recipebook.toggleRecipes.kiln");

    @Override
    protected Component getRecipeFilterName() {
        return RECIPE_FILTER_TEXT;
    }

    @Override
    protected Set<Item> getFuelItems() {
        return AbstractFurnaceBlockEntity.getFuel().keySet();
    }
}
