package safro.oxidized.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.oxidized.block.CopperTrapBlock;
import safro.oxidized.registry.BlockRegistry;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    private void oxidized$trapEntity(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.getBlockStateOn().is(BlockRegistry.COPPER_TRAP.get())
                && CopperTrapBlock.isClosed(entity.getBlockStateOn())) {
            Vec3 position = entity.position();
            entity.teleportTo(position.x, position.y, position.z);
        }
    }
}
