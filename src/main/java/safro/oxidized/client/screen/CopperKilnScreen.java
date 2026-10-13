package safro.oxidized.client.screen;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import safro.oxidized.block.screen.CopperKilnScreenHandler;
import safro.oxidized.client.recipe.CopperKilnRecipeBook;

public class CopperKilnScreen extends AbstractFurnaceScreen<CopperKilnScreenHandler> {
    private static final ResourceLocation LIT_PROGRESS_TEXTURE = ResourceLocation.withDefaultNamespace("container/furnace/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_TEXTURE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");

    public CopperKilnScreen(CopperKilnScreenHandler handler, Inventory inventory, Component title) {
        super(handler, new CopperKilnRecipeBook(), inventory, title, TEXTURE, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE);
    }
}
