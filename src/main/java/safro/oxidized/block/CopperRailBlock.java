package safro.oxidized.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
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

    @Override
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
    protected BlockState rotate(BlockState state, Rotation rotation) {
        RailShape shape = state.getValue(SHAPE);
        return switch (rotation) {
            case CLOCKWISE_180 -> switch (shape) {
                case ASCENDING_EAST -> state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                case ASCENDING_WEST -> state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                case ASCENDING_NORTH -> state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                case ASCENDING_SOUTH -> state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                case SOUTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
                case SOUTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
                case NORTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
                case NORTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_WEST);
                default -> state;
            };
            case COUNTERCLOCKWISE_90 -> switch (shape) {
                case NORTH_SOUTH -> state.setValue(SHAPE, RailShape.EAST_WEST);
                case EAST_WEST -> state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                case ASCENDING_EAST -> state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                case ASCENDING_WEST -> state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                case ASCENDING_NORTH -> state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                case ASCENDING_SOUTH -> state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                case SOUTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
                case SOUTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
                case NORTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_WEST);
                case NORTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
            };
            case CLOCKWISE_90 -> switch (shape) {
                case NORTH_SOUTH -> state.setValue(SHAPE, RailShape.EAST_WEST);
                case EAST_WEST -> state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                case ASCENDING_EAST -> state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                case ASCENDING_WEST -> state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                case ASCENDING_NORTH -> state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                case ASCENDING_SOUTH -> state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                case SOUTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_WEST);
                case SOUTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
                case NORTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
                case NORTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
            };
            default -> state;
        };
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        RailShape shape = state.getValue(SHAPE);
        return switch (mirror) {
            case LEFT_RIGHT -> switch (shape) {
                case ASCENDING_NORTH -> state.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                case ASCENDING_SOUTH -> state.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                case SOUTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
                case SOUTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
                case NORTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_WEST);
                case NORTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
                default -> super.mirror(state, mirror);
            };
            case FRONT_BACK -> switch (shape) {
                case ASCENDING_EAST -> state.setValue(SHAPE, RailShape.ASCENDING_WEST);
                case ASCENDING_WEST -> state.setValue(SHAPE, RailShape.ASCENDING_EAST);
                case SOUTH_EAST -> state.setValue(SHAPE, RailShape.SOUTH_WEST);
                case SOUTH_WEST -> state.setValue(SHAPE, RailShape.SOUTH_EAST);
                case NORTH_WEST -> state.setValue(SHAPE, RailShape.NORTH_EAST);
                case NORTH_EAST -> state.setValue(SHAPE, RailShape.NORTH_WEST);
                default -> super.mirror(state, mirror);
            };
            default -> super.mirror(state, mirror);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, BlockStateProperties.WATERLOGGED);
    }
}
