package safro.oxidized.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import safro.oxidized.Oxidized;
import safro.oxidized.entity.CopperGolemEntity;

public final class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, Oxidized.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<CopperGolemEntity>> COPPER_GOLEM = ENTITY_TYPES.register(
            "copper_golem", () -> EntityType.Builder.of(CopperGolemEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.9F)
                    .fireImmune()
                    .clientTrackingRange(10)
                    .build("copper_golem"));

    private EntityRegistry() {}

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(EntityRegistry::registerAttributes);
    }

    private static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(COPPER_GOLEM.get(), CopperGolemEntity.createCopperGolemAttributes().build());
    }
}
