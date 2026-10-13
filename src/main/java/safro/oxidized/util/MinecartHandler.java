package safro.oxidized.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.properties.RailShape;
import safro.oxidized.mixin.MinecartEntityAccessor;

public class MinecartHandler {

    public static void handleBlockHit(BlockPos pos, AbstractMinecart minecart, RailShape railShape) {
        double x = minecart.getDeltaMovement().x;
        double z = minecart.getDeltaMovement().z;
        if (railShape == RailShape.EAST_WEST) {
            if (((MinecartEntityAccessor) minecart).invokeWillHitBlockAt(pos.west())) {
                x = 0.02D;
            } else if (((MinecartEntityAccessor) minecart).invokeWillHitBlockAt(pos.east())) {
                x = -0.02D;
            }
        } else if (railShape == RailShape.NORTH_SOUTH) {
            if (((MinecartEntityAccessor) minecart).invokeWillHitBlockAt(pos.north())) {
                z = 0.02D;
            } else if (((MinecartEntityAccessor) minecart).invokeWillHitBlockAt(pos.south())) {
                z = -0.02D;
            }
        }

        minecart.setDeltaMovement(x, minecart.getDeltaMovement().y, z);
    }
}
