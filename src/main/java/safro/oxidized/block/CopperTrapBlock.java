package safro.oxidized.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import safro.oxidized.util.TrapDamageSource;

public class CopperTrapBlock extends Block {
    public static final BooleanProperty CLOSED = BooleanProperty.create("closed");
    private static final VoxelShape SHAPE = Block.box(2, 0, 1, 14, 2, 15);

    public CopperTrapBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(CLOSED, false));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(CLOSED, !state.getValue(CLOSED)), 3);
        }
        level.playSound(player, pos, SoundEvents.COPPER_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity living && !living.isIgnoringBlockTriggers() && !state.getValue(CLOSED) && !level.isClientSide) {
            double movedX = Math.abs(entity.getX() - entity.xo);
            double movedZ = Math.abs(entity.getZ() - entity.zo);
            if (movedX >= 0.003000000026077032D || movedZ >= 0.003000000026077032D) {
                entity.hurt(TrapDamageSource.create(level), 5.0F);
            }
            schedule(state, level, pos);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CLOSED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public void schedule(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(CLOSED, true), 3);
        level.scheduleTick(pos, this, 60);
        level.updateNeighborsAt(pos, this);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(CLOSED)) {
            level.setBlock(pos, state.setValue(CLOSED, false), 3);
            level.updateNeighborsAt(pos, this);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(CLOSED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbor, BlockPos fromPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(CLOSED, level.hasNeighborSignal(pos)), 3);
        }
    }

    public static boolean isClosed(BlockState state) {
        return state.getValue(CLOSED);
    }
}
