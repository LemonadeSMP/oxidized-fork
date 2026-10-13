package safro.oxidized.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import safro.oxidized.Oxidized;
import safro.oxidized.recipe.CopperKilnRecipe;

public final class RecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Oxidized.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Oxidized.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<CopperKilnRecipe>> COPPER_KILN_TYPE = TYPES.register(
            "kiln_smelting", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Oxidized.MOD_ID, "kiln_smelting")));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CopperKilnRecipe>> COPPER_KILN_SERIALIZER = SERIALIZERS.register(
            "kiln_smelting", () -> new SimpleCookingSerializer<>(CopperKilnRecipe::new, 100));

    private RecipeRegistry() {}

    public static void register(net.neoforged.bus.api.IEventBus modBus) {
        TYPES.register(modBus);
        SERIALIZERS.register(modBus);
    }
}
