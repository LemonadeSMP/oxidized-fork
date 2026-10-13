package safro.oxidized.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import safro.oxidized.mixin.ServerWorldAccessor;

public class BarometerItem extends Item {
    public BarometerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            ServerLevelData properties = ((ServerWorldAccessor) serverLevel).getLevelData();
            BlockPos pos = player.blockPosition();
            boolean snow = serverLevel.getBiome(pos).value().coldEnoughToSnow(pos);

            if (serverLevel.isThundering()) {
                player.displayClientMessage(getTime(properties, false, false), true);
            } else if (serverLevel.isRaining()) {
                player.displayClientMessage(getTime(properties, true, snow), true);
            } else {
                player.displayClientMessage(Component.translatable("text.oxidized.clearweather", properties.getClearWeatherTime() / 20).withStyle(ChatFormatting.GOLD), true);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static Component getTime(ServerLevelData properties, boolean rain, boolean snow) {
        int time = (rain ? properties.getRainTime() : properties.getThunderTime()) / 20;
        String key = rain ? "text.oxidized.rain" : "text.oxidized.thunderstorm";
        if (snow) {
            key = "text.oxidized.snow";
        }
        return Component.translatable(key, time).withStyle(ChatFormatting.GOLD);
    }
}
