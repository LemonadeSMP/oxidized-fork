package safro.oxidized.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.server.level.ServerLevel;
import safro.oxidized.Oxidized;

public class CopperPanBlock extends Block implements SimpleWaterloggedBlock {
    public static final IntegerProperty PANNED = BlockStateProperties.HATCH;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D);

    public CopperPanBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(PANNED, 0).setValue(WATERLOGGED, false));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int panned = state.getValue(PANNED);
        if (state.getFluidState().is(FluidTags.WATER) && isSpecialBlockBelow(level, pos)) {
            if (panned < 2) {
                level.setBlock(pos, state.setValue(PANNED, panned + 1), 2);
            } else {
                if (Oxidized.CONFIG.enable_copper_pan_sound) {
                    level.playSound(null, pos, SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.BLOCKS, 0.4F, 0.9F + random.nextFloat() * 0.2F);
                }
                float range = 0.55F;
                double x = (double) (level.random.nextFloat() * range) + (1.0F - range) / 2.0F;
                double z = (double) (level.random.nextFloat() * range) + (1.0F - range) / 2.0F;
                ItemEntity item = new ItemEntity(level, pos.getX() + x, pos.getY() + 0.66000000238418579D, pos.getZ() + z, new ItemStack(getPannedItem(random)));
                item.setDefaultPickUpDelay();
                level.addFreshEntity(item);
            }
        }
    }

    public Item getPannedItem(RandomSource random) {
        if (random.nextFloat() <= Oxidized.CONFIG.iron_nugget_chance) {
            return Items.IRON_NUGGET;
        } else if (random.nextFloat() <= Oxidized.CONFIG.gold_nugget_chance) {
            return Items.GOLD_NUGGET;
        } else if (random.nextFloat() <= Oxidized.CONFIG.sand_chance) {
            return Items.SAND;
        } else if (random.nextFloat() <= Oxidized.CONFIG.emerald_chance) {
            return Items.EMERALD;
        } else if (random.nextFloat() <= Oxidized.CONFIG.gravel_chance) {
            return Items.GRAVEL;
        }
        return Items.CLAY_BALL;
    }

    private static boolean isSpecialBlockBelow(BlockGetter level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(BlockTags.SAND) || below.is(Blocks.GRAVEL);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState().setValue(WATERLOGGED, fluid.is(FluidTags.WATER));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PANNED, WATERLOGGED);
    }
}
