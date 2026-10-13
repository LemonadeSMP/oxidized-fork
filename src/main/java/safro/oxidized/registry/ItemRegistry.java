package safro.oxidized.registry;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import safro.oxidized.Oxidized;
import safro.oxidized.item.BarometerItem;
import safro.oxidized.item.CopperPulsarItem;
import safro.oxidized.item.RoseGoldMaterial;
import safro.oxidized.registry.EntityRegistry;

public final class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Oxidized.MOD_ID);

    private static final net.minecraft.world.item.Tier ROSE_GOLD = RoseGoldMaterial.ROSE_GOLD;

    public static final DeferredHolder<Item, BlockItem> COPPER_RAIL = blockItem("copper_rail", BlockRegistry.COPPER_RAIL);
    public static final DeferredHolder<Item, BlockItem> VERTICAL_CUT_COPPER = blockItem("vertical_cut_copper", BlockRegistry.VERTICAL_CUT_COPPER);
    public static final DeferredHolder<Item, BlockItem> VERTICAL_EXPOSED_CUT_COPPER = blockItem("vertical_exposed_cut_copper", BlockRegistry.VERTICAL_EXPOSED_CUT_COPPER);
    public static final DeferredHolder<Item, BlockItem> VERTICAL_WEATHERED_CUT_COPPER = blockItem("vertical_weathered_cut_copper", BlockRegistry.VERTICAL_WEATHERED_CUT_COPPER);
    public static final DeferredHolder<Item, BlockItem> VERTICAL_OXIDIZED_CUT_COPPER = blockItem("vertical_oxidized_cut_copper", BlockRegistry.VERTICAL_OXIDIZED_CUT_COPPER);
    public static final DeferredHolder<Item, BlockItem> WAXED_VERTICAL_CUT_COPPER = blockItem("waxed_vertical_cut_copper", BlockRegistry.WAXED_VERTICAL_CUT_COPPER);
    public static final DeferredHolder<Item, BlockItem> WAXED_VERTICAL_EXPOSED_CUT_COPPER = blockItem("waxed_vertical_exposed_cut_copper", BlockRegistry.WAXED_VERTICAL_EXPOSED_CUT_COPPER);
    public static final DeferredHolder<Item, BlockItem> WAXED_VERTICAL_WEATHERED_CUT_COPPER = blockItem("waxed_vertical_weathered_cut_copper", BlockRegistry.WAXED_VERTICAL_WEATHERED_CUT_COPPER);
    public static final DeferredHolder<Item, BlockItem> WAXED_VERTICAL_OXIDIZED_CUT_COPPER = blockItem("waxed_vertical_oxidized_cut_copper", BlockRegistry.WAXED_VERTICAL_OXIDIZED_CUT_COPPER);
    public static final DeferredHolder<Item, BlockItem> COPPER_LANTERN = blockItem("copper_lantern", BlockRegistry.COPPER_LANTERN);
    public static final DeferredHolder<Item, BlockItem> COPPER_PAN = blockItem("copper_pan", BlockRegistry.COPPER_PAN);
    public static final DeferredHolder<Item, BlockItem> COPPER_BUTTON = blockItem("copper_button", BlockRegistry.COPPER_BUTTON);
    public static final DeferredHolder<Item, BlockItem> COPPER_KILN = blockItem("copper_kiln", BlockRegistry.COPPER_KILN);
    public static final DeferredHolder<Item, BlockItem> COPPER_TRAP = blockItem("copper_trap", BlockRegistry.COPPER_TRAP);

    public static final DeferredHolder<Item, Item> COPPER_PULSAR = Oxidized.CONFIG.enable_copper_pulsar
            ? ITEMS.register("copper_pulsar", () -> new CopperPulsarItem(new Item.Properties().stacksTo(1))) : null;
    public static final DeferredHolder<Item, Item> COPPER_BAROMETER = ITEMS.register("copper_barometer", () -> new BarometerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, SmithingTemplateItem> ROSE_GOLD_UPGRADE_TEMPLATE = Oxidized.CONFIG.enable_rose_gold_tools
            ? ITEMS.register("rose_gold_upgrade_template", ItemRegistry::createRoseGoldTemplate) : null;
    public static final DeferredHolder<Item, AxeItem> ROSE_GOLD_AXE = Oxidized.CONFIG.enable_rose_gold_tools
            ? ITEMS.register("rose_gold_axe", () -> new AxeItem(ROSE_GOLD, new Item.Properties().attributes(AxeItem.createAttributes(ROSE_GOLD, 6.0F, -3.0F)))) : null;
    public static final DeferredHolder<Item, HoeItem> ROSE_GOLD_HOE = Oxidized.CONFIG.enable_rose_gold_tools
            ? ITEMS.register("rose_gold_hoe", () -> new HoeItem(ROSE_GOLD, new Item.Properties().attributes(HoeItem.createAttributes(ROSE_GOLD, 0.0F, -3.0F)))) : null;
    public static final DeferredHolder<Item, PickaxeItem> ROSE_GOLD_PICKAXE = Oxidized.CONFIG.enable_rose_gold_tools
            ? ITEMS.register("rose_gold_pickaxe", () -> new PickaxeItem(ROSE_GOLD, new Item.Properties().attributes(PickaxeItem.createAttributes(ROSE_GOLD, 1.0F, -2.8F)))) : null;
    public static final DeferredHolder<Item, ShovelItem> ROSE_GOLD_SHOVEL = Oxidized.CONFIG.enable_rose_gold_tools
            ? ITEMS.register("rose_gold_shovel", () -> new ShovelItem(ROSE_GOLD, new Item.Properties().attributes(ShovelItem.createAttributes(ROSE_GOLD, 1.5F, -3.0F)))) : null;
    public static final DeferredHolder<Item, SwordItem> ROSE_GOLD_SWORD = Oxidized.CONFIG.enable_rose_gold_tools
            ? ITEMS.register("rose_gold_sword", () -> new SwordItem(ROSE_GOLD, new Item.Properties().attributes(SwordItem.createAttributes(ROSE_GOLD, 4.0F, -2.4F)))) : null;
    public static final DeferredHolder<Item, SpawnEggItem> COPPER_GOLEM_SPAWN_EGG = Oxidized.CONFIG.enable_copper_golem
            ? ITEMS.register("copper_golem_spawn_egg", () -> new SpawnEggItem(EntityRegistry.COPPER_GOLEM.get(), 0x8A4129, 0xC15B36, new Item.Properties())) : null;

    private ItemRegistry() {}

    public static void register(net.neoforged.bus.api.IEventBus modBus) {
        ITEMS.register(modBus);
    }

    private static DeferredHolder<Item, BlockItem> blockItem(String id, net.neoforged.neoforge.registries.DeferredBlock<? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static SmithingTemplateItem createRoseGoldTemplate() {
        return new SmithingTemplateItem(
                Component.translatable("text.oxidized.rose_gold_template_applies").withStyle(ChatFormatting.BLUE),
                Component.translatable("text.oxidized.rose_gold_template_ingredient").withStyle(ChatFormatting.BLUE),
                Component.translatable("text.oxidized.rose_gold_template").withStyle(ChatFormatting.GRAY),
                Component.translatable("text.oxidized.rose_gold_template.base_desc"),
                Component.translatable("text.oxidized.rose_gold_template.ing_desc"),
                List.of(ResourceLocation.withDefaultNamespace("item/empty_slot_ingot")),
                List.of(
                        ResourceLocation.withDefaultNamespace("item/empty_slot_hoe"),
                        ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
                        ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
                        ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
                        ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe")
                )
        );
    }
}
