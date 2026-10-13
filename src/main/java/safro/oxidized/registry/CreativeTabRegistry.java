package safro.oxidized.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import safro.oxidized.Oxidized;
import safro.oxidized.neoforge.CopperGear;

public final class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Oxidized.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> OXIDIZED = TABS.register("oxidized", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.oxidized.oxidized"))
            .icon(() -> ItemRegistry.COPPER_KILN.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ItemRegistry.COPPER_RAIL.get());
                output.accept(ItemRegistry.VERTICAL_CUT_COPPER.get());
                output.accept(ItemRegistry.VERTICAL_EXPOSED_CUT_COPPER.get());
                output.accept(ItemRegistry.VERTICAL_WEATHERED_CUT_COPPER.get());
                output.accept(ItemRegistry.VERTICAL_OXIDIZED_CUT_COPPER.get());
                output.accept(ItemRegistry.WAXED_VERTICAL_CUT_COPPER.get());
                output.accept(ItemRegistry.WAXED_VERTICAL_EXPOSED_CUT_COPPER.get());
                output.accept(ItemRegistry.WAXED_VERTICAL_WEATHERED_CUT_COPPER.get());
                output.accept(ItemRegistry.WAXED_VERTICAL_OXIDIZED_CUT_COPPER.get());
                output.accept(ItemRegistry.COPPER_LANTERN.get());
                output.accept(ItemRegistry.COPPER_PAN.get());
                output.accept(ItemRegistry.COPPER_BUTTON.get());
                output.accept(ItemRegistry.COPPER_KILN.get());
                output.accept(ItemRegistry.COPPER_TRAP.get());
                if (ItemRegistry.COPPER_PULSAR != null) output.accept(ItemRegistry.COPPER_PULSAR.get());
                output.accept(ItemRegistry.COPPER_BAROMETER.get());
                output.accept(ItemRegistry.COPPER_NUGGET.get());
                output.accept(CopperGear.COPPER_SWORD.get());
                output.accept(CopperGear.COPPER_PICKAXE.get());
                output.accept(CopperGear.COPPER_AXE.get());
                output.accept(CopperGear.COPPER_SHOVEL.get());
                output.accept(CopperGear.COPPER_HOE.get());
                output.accept(CopperGear.COPPER_HELMET.get());
                output.accept(CopperGear.COPPER_CHESTPLATE.get());
                output.accept(CopperGear.COPPER_LEGGINGS.get());
                output.accept(CopperGear.COPPER_BOOTS.get());
                if (ItemRegistry.ROSE_GOLD_UPGRADE_TEMPLATE != null) {
                    output.accept(ItemRegistry.ROSE_GOLD_UPGRADE_TEMPLATE.get());
                    output.accept(ItemRegistry.ROSE_GOLD_AXE.get());
                    output.accept(ItemRegistry.ROSE_GOLD_HOE.get());
                    output.accept(ItemRegistry.ROSE_GOLD_PICKAXE.get());
                    output.accept(ItemRegistry.ROSE_GOLD_SHOVEL.get());
                    output.accept(ItemRegistry.ROSE_GOLD_SWORD.get());
                }
                if (ItemRegistry.COPPER_GOLEM_SPAWN_EGG != null) output.accept(ItemRegistry.COPPER_GOLEM_SPAWN_EGG.get());
            }).build());

    private CreativeTabRegistry() {}

    public static void register(net.neoforged.bus.api.IEventBus modBus) {
        TABS.register(modBus);
    }
}
