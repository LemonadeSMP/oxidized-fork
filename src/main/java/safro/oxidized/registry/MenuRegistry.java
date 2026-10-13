package safro.oxidized.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.flag.FeatureFlags;
import safro.oxidized.Oxidized;
import safro.oxidized.block.screen.CopperKilnScreenHandler;

public final class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Oxidized.MOD_ID);
    public static final DeferredHolder<MenuType<?>, MenuType<CopperKilnScreenHandler>> COPPER_KILN = MENUS.register(
            "copper_kiln_screen_handler", () -> new MenuType<>(CopperKilnScreenHandler::new, FeatureFlags.VANILLA_SET));

    private MenuRegistry() {}

    public static void register(net.neoforged.bus.api.IEventBus modBus) {
        MENUS.register(modBus);
    }
}
