package safro.oxidized.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.oxidized.block.CopperRailBlock;

@Mixin(AbstractMinecart.class)
public abstract class MinecartRailMixin {
    @Inject(at = @At("HEAD"), method = "moveAlongTrack", cancellable = true, remap = false)
    private void oxidized$moveOnRail(BlockPos pos, BlockState state, CallbackInfo ci) {
        Block block = state.getBlock();
        if (block instanceof CopperRailBlock railBlock) {
            railBlock.onMoveOnRail(pos, state, (AbstractMinecart) (Object) this, ci);
        }
    }
}
