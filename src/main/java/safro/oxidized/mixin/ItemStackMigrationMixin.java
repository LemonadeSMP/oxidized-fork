package safro.oxidized.mixin;

import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.oxidized.util.CopperToolMigration;

@Mixin(ItemStack.class)
public abstract class ItemStackMigrationMixin {
    @Shadow(remap = false) @Final @Mutable
    private Item item;

    @Shadow(remap = false) @Final @Mutable
    private PatchedDataComponentMap components;

    // All nonempty vanilla stack construction paths converge here, including
    // save codecs, nested container stacks, network decoding, crafting and copies.
    @Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V",
            at = @At("RETURN"), remap = false)
    private void oxidized$migrateCopperTool(ItemLike original, int count,
            PatchedDataComponentMap originalComponents, CallbackInfo ci) {
        Item replacement = CopperToolMigration.replacementFor(this.item);
        if (replacement != this.item) {
            this.components = CopperToolMigration.preserveComponents(this.components, replacement);
            this.item = replacement;
            replacement.verifyComponentsAfterLoad((ItemStack) (Object) this);
        }
    }
}
