package safro.oxidized.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.oxidized.Oxidized;
import safro.oxidized.mixin.MinecartEntityAccessor;
import safro.oxidized.util.MinecartHandler;

public class CopperRailBlock extends BaseRailBlock {
    public static final MapCodec<CopperRailBlock> CODEC = simpleCodec(CopperRailBlock::new);
    public static final Property<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public CopperRailBlock(BlockBehaviour.Properties properties) {
        super(true, properties);
        registerDefaultState(defaultBlockState().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(POWERED, false).setValue(BlockStateProperties.WATERLOGGED, false));
    }

    public void onMoveOnRail(BlockPos pos, BlockState state, AbstractMinecart minecart, CallbackInfo info) {
        if (!state.getValue(POWERED)) {
            Vec3 velocity = minecart.getDeltaMovement();
            minecart.setDeltaMovement(velocity.x * 0.25D, velocity.y, velocity.z * 0.25D);
            return;
        }

        RailShape railShape = state.getValue(getShapeProperty());
        Vec3 velocity = minecart.getDeltaMovement();
        double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        if (speed > 0.01D) {
            minecart.setDeltaMovement(velocity.normalize().scale(((MinecartEntityAccessor) minecart).invokeGetMaxOffRailSpeed() * 10D));
        } else {
            MinecartHandler.handleBlockHit(pos, minecart, railShape);
        }
    }

    public boolean isPoweredByOtherRails(Level level, BlockPos pos, BlockState state, boolean forward, int distance) {
        if (distance >= Oxidized.CONFIG.copper_rail_range) {
            return false;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        boolean checkLower = true;
        RailShape shape = state.getValue(SHAPE);
        switch (shape) {
            case NORTH_SOUTH -> z += forward ? 1 : -1;
            case EAST_WEST -> x += forward ? -1 : 1;
            case ASCENDING_EAST -> {
                if (forward) x--;
                else { x++; y++; checkLower = false; }
                shape = RailShape.EAST_WEST;
            }
            case ASCENDING_WEST -> {
                if (forward) { x--; y++; checkLower = false; }
                else x++;
                shape = RailShape.EAST_WEST;
            }
            case ASCENDING_NORTH -> {
                if (forward) z++;
                else { z--; y++; checkLower = false; }
                shape = RailShape.NORTH_SOUTH;
            }
            case ASCENDING_SOUTH -> {
                if (forward) { z++; y++; checkLower = false; }
                else z--;
                shape = RailShape.NORTH_SOUTH;
            }
            default -> { }
        }

        if (isPoweredByOtherRails(level, new BlockPos(x, y, z), forward, distance, shape)) return true;
        return checkLower && isPoweredByOtherRails(level, new BlockPos(x, y - 1, z), forward, distance, shape);
    }

    public boolean isPoweredByOtherRails(Level level, BlockPos pos, boolean forward, int distance, RailShape shape) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(this)) return false;
        RailShape nextShape = state.getValue(SHAPE);
        if (shape == RailShape.EAST_WEST && (nextShape == RailShape.NORTH_SOUTH || nextShape == RailShape.ASCENDING_NORTH || nextShape == RailShape.ASCENDING_SOUTH)) return false;
        if (shape == RailShape.NORTH_SOUTH && (nextShape == RailShape.EAST_WEST || nextShape == RailShape.ASCENDING_EAST || nextShape == RailShape.ASCENDING_WEST)) return false;
        return state.getValue(POWERED) && (level.hasNeighborSignal(pos) || isPoweredByOtherRails(level, pos, state, forward, distance + 1));
    }

    @Override
    protected MapCodec<? extends BaseRailBlock> codec() {
        return CODEC;
    }

    protected void updateState(BlockState state, Level level, BlockPos pos, Block neighbor) {
        boolean wasPowered = state.getValue(POWERED);
        boolean powered = level.hasNeighborSignal(pos)
                || isPoweredByOtherRails(level, pos, state, true, 0)
                || isPoweredByOtherRails(level, pos, state, false, 0);
        if (powered != wasPowered) {
            level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_ALL);
            level.updateNeighborsAt(pos.below(), this);
            if (state.getValue(SHAPE).isAscending()) level.updateNeighborsAt(pos.above(), this);
        }
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, BlockStateProperties.WATERLOGGED);
    }
}
