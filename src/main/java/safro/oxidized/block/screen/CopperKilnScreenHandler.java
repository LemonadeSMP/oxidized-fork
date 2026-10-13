package safro.oxidized.block.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.Container;
import safro.oxidized.registry.BlockRegistry;
import safro.oxidized.registry.RecipeRegistry;

public class CopperKilnScreenHandler extends AbstractFurnaceMenu {
    public CopperKilnScreenHandler(int containerId, Inventory playerInventory) {
        super(BlockRegistry.COPPER_KILN_SCREEN_HANDLER.get(), RecipeRegistry.COPPER_KILN_TYPE.get(), RecipeBookType.FURNACE, containerId, playerInventory);
    }

    public CopperKilnScreenHandler(int containerId, Inventory playerInventory, Container inventory, ContainerData data) {
        super(BlockRegistry.COPPER_KILN_SCREEN_HANDLER.get(), RecipeRegistry.COPPER_KILN_TYPE.get(), RecipeBookType.FURNACE, containerId, playerInventory, inventory, data);
    }
}
