package safro.oxidized.entity.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import safro.oxidized.Oxidized;
import safro.oxidized.entity.CopperGolemEntity;
import safro.oxidized.registry.BlockRegistry;

public class UseButtonGoal extends MoveToBlockGoal {
    private final CopperGolemEntity golem;

    public UseButtonGoal(CopperGolemEntity entity, double speed) {
        super(entity, speed, 8);
        this.golem = entity;
    }

    @Override
    public void stop() {
        super.stop();
        this.golem.setPressingButtons(false);
    }

    @Override
    public void tick() {
        if (this.isReachedTarget()) {
            this.golem.setPressingButtons(true);
            if (Mth.nextInt(this.golem.getRandom(), 1, Oxidized.CONFIG.copper_golem_button_chance) == 2) {
                BlockState state = this.golem.level().getBlockState(this.blockPos);
                if (!state.getValue(BlockStateProperties.POWERED)) {
                    ((ButtonBlock) state.getBlock()).press(state, this.golem.level(), this.blockPos, null);
                }
            }
        }
        super.tick();
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos).is(BlockRegistry.COPPER_BUTTON.get());
    }

    @Override
    public double acceptedDistance() {
        return 0.75D;
    }
}
