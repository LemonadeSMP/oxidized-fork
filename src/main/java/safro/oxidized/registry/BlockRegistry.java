package safro.oxidized.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperFullBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import safro.oxidized.block.entity.CopperKilnBlockEntity;
import safro.oxidized.block.screen.CopperKilnScreenHandler;
import safro.oxidized.recipe.CopperKilnRecipe;
import safro.oxidized.Oxidized;
import safro.oxidized.block.CopperButtonBlock;
import safro.oxidized.block.CopperKilnBlock;
import safro.oxidized.block.CopperPanBlock;
import safro.oxidized.block.CopperRailBlock;
import safro.oxidized.block.CopperTrapBlock;

public final class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Oxidized.MOD_ID);

    public static final DeferredBlock<Block> COPPER_RAIL = BLOCKS.register("copper_rail", () -> new CopperRailBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.NONE).noOcclusion().pushReaction(PushReaction.DESTROY)
                    .noCollission().strength(0.7F).sound(SoundType.METAL)));

    public static final DeferredBlock<Block> VERTICAL_CUT_COPPER = weathering("vertical_cut_copper", WeatheringCopper.WeatherState.UNAFFECTED);
    public static final DeferredBlock<Block> VERTICAL_EXPOSED_CUT_COPPER = weathering("vertical_exposed_cut_copper", WeatheringCopper.WeatherState.EXPOSED);
    public static final DeferredBlock<Block> VERTICAL_WEATHERED_CUT_COPPER = weathering("vertical_weathered_cut_copper", WeatheringCopper.WeatherState.WEATHERED);
    public static final DeferredBlock<Block> VERTICAL_OXIDIZED_CUT_COPPER = weathering("vertical_oxidized_cut_copper", WeatheringCopper.WeatherState.OXIDIZED);

    public static final DeferredBlock<Block> WAXED_VERTICAL_CUT_COPPER = waxed("waxed_vertical_cut_copper");
    public static final DeferredBlock<Block> WAXED_VERTICAL_EXPOSED_CUT_COPPER = waxed("waxed_vertical_exposed_cut_copper");
    public static final DeferredBlock<Block> WAXED_VERTICAL_WEATHERED_CUT_COPPER = waxed("waxed_vertical_weathered_cut_copper");
    public static final DeferredBlock<Block> WAXED_VERTICAL_OXIDIZED_CUT_COPPER = waxed("waxed_vertical_oxidized_cut_copper");

    public static final DeferredBlock<LanternBlock> COPPER_LANTERN = BLOCKS.register("copper_lantern", () -> new LanternBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.5F)
                    .sound(SoundType.LANTERN).lightLevel(state -> 15).noOcclusion()));
    public static final DeferredBlock<Block> COPPER_PAN = BLOCKS.register("copper_pan", () -> new CopperPanBlock(copperBlockProperties().randomTicks()));
    public static final DeferredBlock<Block> COPPER_BUTTON = BLOCKS.register("copper_button", () -> new CopperButtonBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.NONE).noOcclusion().pushReaction(PushReaction.DESTROY)
                    .noCollission().strength(0.5F).sound(SoundType.COPPER)));
    public static final DeferredBlock<CopperKilnBlock> COPPER_KILN = BLOCKS.register("copper_kiln", () -> new CopperKilnBlock(
            copperBlockProperties().lightLevel(state -> state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT) ? 13 : 0)
                    .noOcclusion()));
    public static final DeferredBlock<Block> COPPER_TRAP = BLOCKS.register("copper_trap", () -> new CopperTrapBlock(
            copperBlockProperties().strength(2.0F, 3.0F).noCollission()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperKilnBlockEntity>> COPPER_KILN_BLOCK_ENTITY = BlockEntityRegistry.COPPER_KILN;
    public static final DeferredHolder<MenuType<?>, MenuType<CopperKilnScreenHandler>> COPPER_KILN_SCREEN_HANDLER = MenuRegistry.COPPER_KILN;
    public static final DeferredHolder<RecipeType<?>, RecipeType<CopperKilnRecipe>> COPPER_KILN_RECIPE_TYPE = RecipeRegistry.COPPER_KILN_TYPE;
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CopperKilnRecipe>> COPPER_KILN_RECIPE_SERIALIZER = RecipeRegistry.COPPER_KILN_SERIALIZER;

    private BlockRegistry() {}

    public static void register(net.neoforged.bus.api.IEventBus modBus) {
        BLOCKS.register(modBus);
    }

    private static DeferredBlock<Block> weathering(String name, WeatheringCopper.WeatherState age) {
        return BLOCKS.register(name, () -> new WeatheringCopperFullBlock(age, copperBlockProperties()));
    }

    private static DeferredBlock<Block> waxed(String name) {
        return BLOCKS.register(name, () -> new Block(copperBlockProperties()));
    }

    private static BlockBehaviour.Properties copperBlockProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER)
                .requiresCorrectToolForDrops().strength(3.0F, 6.0F);
    }
}
