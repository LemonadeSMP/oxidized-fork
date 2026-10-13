package safro.oxidized.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.oxidized.Oxidized;
import safro.oxidized.entity.CopperGolemEntity;
import safro.oxidized.registry.EntityRegistry;

import java.util.Iterator;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin {
    @Unique
    @Nullable
    private BlockPattern oxidized$copperGolemPattern;

    @Inject(method = "onPlace", at = @At("TAIL"), remap = false)
    private void oxidized$checkForGolem(BlockState state, Level level, BlockPos pos, BlockState oldState,
                                        boolean movedByPiston, CallbackInfo ci) {
        if (!oldState.is(state.getBlock()) && Oxidized.CONFIG.enable_copper_golem) {
            this.oxidized$trySpawnCopperGolem(level, pos);
        }
    }

    @Unique
    private void oxidized$trySpawnCopperGolem(Level level, BlockPos pos) {
        BlockPattern.BlockPatternMatch result = this.oxidized$getCopperGolemPattern().find(level, pos);
        if (result == null) {
            return;
        }

        for (int i = 0; i < this.oxidized$getCopperGolemPattern().getHeight(); ++i) {
            BlockInWorld blockInWorld = result.getBlock(0, i, 0);
            BlockPos blockPos = blockInWorld.getPos();
            BlockState matchedState = blockInWorld.getState();
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
            level.levelEvent(2001, blockPos, Block.getId(matchedState));
        }

        CopperGolemEntity golem = EntityRegistry.COPPER_GOLEM.get().create(level);
        BlockPos spawnPos = result.getBlock(0, 2, 0).getPos();
        golem.moveTo((double) spawnPos.getX() + 0.5D, (double) spawnPos.getY() + 0.05D,
                (double) spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
        level.addFreshEntity(golem);

        Iterator<ServerPlayer> players = level.getEntitiesOfClass(ServerPlayer.class,
                golem.getBoundingBox().inflate(5.0D)).iterator();
        while (players.hasNext()) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(players.next(), golem);
        }

        for (int i = 0; i < this.oxidized$getCopperGolemPattern().getHeight(); ++i) {
            BlockPos blockPos = result.getBlock(0, i, 0).getPos();
            level.updateNeighborsAt(blockPos, Blocks.AIR);
        }
    }

    @Unique
    private BlockPattern oxidized$getCopperGolemPattern() {
        if (this.oxidized$copperGolemPattern == null) {
            this.oxidized$copperGolemPattern = BlockPatternBuilder.start()
                    .aisle("^", "$", "#")
                    .where('^', BlockInWorld.hasState(blockState -> blockState.is(Blocks.LIGHTNING_ROD)))
                    .where('$', BlockInWorld.hasState(blockState -> blockState.is(Blocks.CARVED_PUMPKIN)))
                    .where('#', BlockInWorld.hasState(blockState ->
                            blockState.is(Blocks.COPPER_BLOCK) || blockState.is(Blocks.CUT_COPPER)))
                    .build();
        }
        return this.oxidized$copperGolemPattern;
    }
}
