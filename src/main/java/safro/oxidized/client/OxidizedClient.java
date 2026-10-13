package safro.oxidized.client;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;
import safro.oxidized.Oxidized;
import safro.oxidized.client.entity.CopperGolemEntityModel;
import safro.oxidized.client.entity.CopperGolemEntityRenderer;
import safro.oxidized.client.screen.CopperKilnScreen;
import safro.oxidized.neoforge.OxidizedNeoForge;
import safro.oxidized.registry.BlockRegistry;
import safro.oxidized.registry.EntityRegistry;
import safro.oxidized.registry.RecipeRegistry;

@Mod(value = OxidizedNeoForge.MOD_ID, dist = Dist.CLIENT)
public final class OxidizedClient {
    public static final ModelLayerLocation COPPER_GOLEM_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(OxidizedNeoForge.MOD_ID, "copper_golem_layer"),
            "copper_golem_layer"
    );

    public OxidizedClient(IEventBus modEventBus) {
        modEventBus.register(OxidizedClient.class);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        if (Oxidized.CONFIG.enable_copper_golem) {
            event.registerLayerDefinition(COPPER_GOLEM_LAYER, CopperGolemEntityModel::createBodyLayer);
        }
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (Oxidized.CONFIG.enable_copper_golem) {
            event.registerEntityRenderer(EntityRegistry.COPPER_GOLEM.get(), CopperGolemEntityRenderer::new);
        }
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(BlockRegistry.COPPER_KILN_SCREEN_HANDLER.get(), CopperKilnScreen::new);
    }

    @SubscribeEvent
    public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        // Vanilla classifies built-in cooking types only; map the custom type into the furnace tabs.
        event.registerRecipeCategoryFinder(RecipeRegistry.COPPER_KILN_TYPE.get(), recipeHolder -> {
            if (recipeHolder.value() instanceof AbstractCookingRecipe recipe) {
                return switch (recipe.category()) {
                    case BLOCKS -> RecipeBookCategories.FURNACE_BLOCKS;
                    case FOOD -> RecipeBookCategories.FURNACE_FOOD;
                    case MISC -> RecipeBookCategories.FURNACE_MISC;
                };
            }
            return RecipeBookCategories.UNKNOWN;
        });
    }
}
