package safro.oxidized.item;

import java.util.List;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import safro.oxidized.Oxidized;

public class CopperPulsarItem extends Item {
    private static final String POWERED = "Powered";

    public CopperPulsarItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide && isActive(stack)) {
            ServerPlayer player = (ServerPlayer) entity;
            AABB box = player.getBoundingBox().inflate(Oxidized.CONFIG.pulsar_reach);
            List<ItemEntity> items = player.serverLevel().getEntitiesOfClass(ItemEntity.class, box, EntitySelector.ENTITY_STILL_ALIVE);
            for (ItemEntity item : items) {
                item.playerTouch(player);
            }
            List<ExperienceOrb> experience = player.serverLevel().getEntitiesOfClass(ExperienceOrb.class, box, EntitySelector.ENTITY_STILL_ALIVE);
            for (ExperienceOrb orb : experience) {
                orb.playerTouch(player);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack pulsar = player.getItemInHand(hand);
        if (!level.isClientSide && !player.isShiftKeyDown()) {
            toggle(pulsar);
            String status = isActive(pulsar) ? "Active" : "Inactive";
            player.displayClientMessage(Component.literal("§6Pulsar is now: " + status), false);
        }
        return InteractionResultHolder.sidedSuccess(pulsar, level.isClientSide);
    }

    public boolean isActive(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            CompoundTag tag = data.copyTag();
            if (tag.contains(POWERED)) {
                return tag.getBoolean(POWERED);
            }
        }
        return true;
    }

    public void toggle(ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(POWERED, !isActive(stack));
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}
