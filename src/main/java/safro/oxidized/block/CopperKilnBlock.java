package safro.oxidized.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import safro.oxidized.block.entity.CopperKilnBlockEntity;
import safro.oxidized.registry.BlockEntityRegistry;

public class CopperKilnBlock extends AbstractFurnaceBlock {
    public static final MapCodec<CopperKilnBlock> CODEC = simpleCodec(CopperKilnBlock::new);

    public CopperKilnBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopperKilnBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, BlockEntityRegistry.COPPER_KILN.get(), AbstractFurnaceBlockEntity::serverTick);
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CopperKilnBlockEntity menuProvider) {
            player.openMenu(menuProvider);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(BlockStateProperties.LIT)) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY();
            double z = pos.getZ() + 0.5D;
            if (random.nextDouble() < 0.1D) {
                level.playLocalSound(x, y, z, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
            level.addParticle(ParticleTypes.SMOKE, x, y + 1.1D, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
