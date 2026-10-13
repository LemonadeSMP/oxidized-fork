package safro.oxidized.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractMinecart.class)
public interface MinecartEntityAccessor {
    @Invoker(value = "isRedstoneConductor", remap = false)
    boolean invokeWillHitBlockAt(BlockPos pos);

    @Invoker(value = "getMaxSpeed", remap = false)
    double invokeGetMaxOffRailSpeed();
}
