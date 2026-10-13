package safro.oxidized.neoforge;

import java.nio.file.Path;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import safro.oxidized.Oxidized;
import safro.oxidized.config.OxidizedConfig;
import safro.oxidized.registry.BlockEntityRegistry;
import safro.oxidized.registry.BlockRegistry;
import safro.oxidized.registry.CreativeTabRegistry;
import safro.oxidized.registry.EntityRegistry;
import safro.oxidized.registry.ItemRegistry;
import safro.oxidized.registry.MenuRegistry;
import safro.oxidized.registry.RecipeRegistry;

@Mod(Oxidized.MOD_ID)
public final class OxidizedNeoForge {
    public static final String MOD_ID = Oxidized.MOD_ID;

    public OxidizedNeoForge(IEventBus modEventBus) {
        Path configPath = FMLPaths.CONFIGDIR.get().resolve("oxidized_config.json");
        Oxidized.CONFIG = OxidizedConfig.loadConfig(configPath.toFile());

        BlockRegistry.register(modEventBus);
        BlockEntityRegistry.register(modEventBus);
        RecipeRegistry.register(modEventBus);
        MenuRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        CopperGear.register(modEventBus);
        EntityRegistry.register(modEventBus);
        CreativeTabRegistry.register(modEventBus);
    }
}
