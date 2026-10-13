package safro.oxidized.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import safro.oxidized.block.screen.CopperKilnScreenHandler;
import safro.oxidized.registry.BlockEntityRegistry;
import safro.oxidized.registry.RecipeRegistry;

public class CopperKilnBlockEntity extends AbstractFurnaceBlockEntity {
    public CopperKilnBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.COPPER_KILN.get(), pos, state, RecipeRegistry.COPPER_KILN_TYPE.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.oxidized.copper_kiln");
    }

    @Override
    protected int getBurnDuration(ItemStack fuel) {
        return super.getBurnDuration(fuel) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new CopperKilnScreenHandler(containerId, playerInventory, this, this.dataAccess);
    }
}
