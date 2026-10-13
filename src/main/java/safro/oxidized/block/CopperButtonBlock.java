package safro.oxidized.block;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class CopperButtonBlock extends ButtonBlock {
    public CopperButtonBlock(BlockBehaviour.Properties properties) {
        super(BlockSetType.IRON, 20, properties);
    }
}
