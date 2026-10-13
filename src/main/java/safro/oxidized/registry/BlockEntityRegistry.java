package safro.oxidized.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import safro.oxidized.Oxidized;
import safro.oxidized.block.entity.CopperKilnBlockEntity;

public final class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Oxidized.MOD_ID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperKilnBlockEntity>> COPPER_KILN =
            TYPES.register("copper_kiln", () -> BlockEntityType.Builder.of(CopperKilnBlockEntity::new, BlockRegistry.COPPER_KILN.get()).build(null));

    private BlockEntityRegistry() {}

    public static void register(net.neoforged.bus.api.IEventBus modBus) {
        TYPES.register(modBus);
    }
}
