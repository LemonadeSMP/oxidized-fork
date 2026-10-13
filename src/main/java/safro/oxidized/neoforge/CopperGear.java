package safro.oxidized.neoforge;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import safro.oxidized.Oxidized;

public final class CopperGear {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Oxidized.MOD_ID);
    private static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, Oxidized.MOD_ID);

    private static final SimpleTier COPPER_TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            190,
            5.0F,
            1.0F,
            13,
            () -> Ingredient.of(Items.COPPER_INGOT)
    );

    private static final DeferredHolder<ArmorMaterial, ArmorMaterial> COPPER_ARMOR_MATERIAL =
            ARMOR_MATERIALS.register("copper", () -> new ArmorMaterial(
                    armorDefense(),
                    8,
                    SoundEvents.ARMOR_EQUIP_IRON,
                    () -> Ingredient.of(Items.COPPER_INGOT),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Oxidized.MOD_ID, "copper"))),
                    0.0F,
                    0.0F
            ));

    public static final DeferredHolder<Item, SwordItem> COPPER_SWORD = ITEMS.register("copper_sword", () ->
            new SwordItem(COPPER_TIER, new Item.Properties()
                    .attributes(SwordItem.createAttributes(COPPER_TIER, 3, -2.4F))));
    public static final DeferredHolder<Item, PickaxeItem> COPPER_PICKAXE = ITEMS.register("copper_pickaxe", () ->
            new PickaxeItem(COPPER_TIER, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(COPPER_TIER, 1.0F, -2.8F))));
    public static final DeferredHolder<Item, AxeItem> COPPER_AXE = ITEMS.register("copper_axe", () ->
            new AxeItem(COPPER_TIER, new Item.Properties()
                    .attributes(AxeItem.createAttributes(COPPER_TIER, 7.0F, -3.2F))));
    public static final DeferredHolder<Item, ShovelItem> COPPER_SHOVEL = ITEMS.register("copper_shovel", () ->
            new ShovelItem(COPPER_TIER, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(COPPER_TIER, 1.5F, -3.0F))));
    public static final DeferredHolder<Item, HoeItem> COPPER_HOE = ITEMS.register("copper_hoe", () ->
            new HoeItem(COPPER_TIER, new Item.Properties()
                    .attributes(HoeItem.createAttributes(COPPER_TIER, -1.0F, -2.0F))));

    public static final DeferredHolder<Item, ArmorItem> COPPER_HELMET = armor("copper_helmet", ArmorItem.Type.HELMET);
    public static final DeferredHolder<Item, ArmorItem> COPPER_CHESTPLATE = armor("copper_chestplate", ArmorItem.Type.CHESTPLATE);
    public static final DeferredHolder<Item, ArmorItem> COPPER_LEGGINGS = armor("copper_leggings", ArmorItem.Type.LEGGINGS);
    public static final DeferredHolder<Item, ArmorItem> COPPER_BOOTS = armor("copper_boots", ArmorItem.Type.BOOTS);

    private CopperGear() {}

    public static void register(IEventBus modEventBus) {
        ARMOR_MATERIALS.register(modEventBus);
        ITEMS.register(modEventBus);
        modEventBus.addListener(CopperGear::addToCreativeTabs);
    }

    private static DeferredHolder<Item, ArmorItem> armor(String name, ArmorItem.Type type) {
        return ITEMS.register(name, () -> new ArmorItem(
                COPPER_ARMOR_MATERIAL,
                type,
                new Item.Properties().durability(type.getDurability(11))
        ));
    }

    private static Map<ArmorItem.Type, Integer> armorDefense() {
        Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        defense.put(ArmorItem.Type.HELMET, 2);
        defense.put(ArmorItem.Type.CHESTPLATE, 4);
        defense.put(ArmorItem.Type.LEGGINGS, 3);
        defense.put(ArmorItem.Type.BOOTS, 1);
        defense.put(ArmorItem.Type.BODY, 0);
        return defense;
    }

    private static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(COPPER_PICKAXE.get());
            event.accept(COPPER_AXE.get());
            event.accept(COPPER_SHOVEL.get());
            event.accept(COPPER_HOE.get());
        } else if (event.getTabKey().equals(CreativeModeTabs.COMBAT)) {
            event.accept(COPPER_SWORD.get());
            event.accept(COPPER_HELMET.get());
            event.accept(COPPER_CHESTPLATE.get());
            event.accept(COPPER_LEGGINGS.get());
            event.accept(COPPER_BOOTS.get());
        }
    }
}
