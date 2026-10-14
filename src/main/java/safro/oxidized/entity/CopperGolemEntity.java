package safro.oxidized.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import safro.oxidized.entity.goal.MoveToLightningRodGoal;
import safro.oxidized.entity.goal.UseButtonGoal;

public class CopperGolemEntity extends AbstractGolem {
    private static final EntityDataAccessor<Boolean> IS_PRESSING_BUTTONS =
            SynchedEntityData.defineId(CopperGolemEntity.class, EntityDataSerializers.BOOLEAN);

    private WeatheringCopper.WeatherState oxidizationLevel = WeatheringCopper.WeatherState.UNAFFECTED;
    private int tickOxidization = 0;

    public CopperGolemEntity(EntityType<? extends CopperGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MoveToLightningRodGoal(this, 0.6D));
        this.goalSelector.addGoal(2, new UseButtonGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.4D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createCopperGolemAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.27D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D);
    }

    public void setPressingButtons(boolean pressing) {
        this.entityData.set(IS_PRESSING_BUTTONS, pressing);
    }

    public boolean isPressingButtons() {
        return this.entityData.get(IS_PRESSING_BUTTONS);
    }

    public WeatheringCopper.WeatherState getOxidizationLevel() {
        return oxidizationLevel;
    }

    public boolean isStatue() {
        return oxidizationLevel == WeatheringCopper.WeatherState.OXIDIZED;
    }

    @Override
    public void tick() {
        super.tick();
        this.oxidizationTick();
        if (this.oxidizationLevel != WeatheringCopper.WeatherState.OXIDIZED) {
            ++tickOxidization;
        }
        this.setNoAi(this.isStatue());
    }

    protected void oxidizationTick() {
        if (tickOxidization == 1200000) {
            this.oxidizationLevel = WeatheringCopper.WeatherState.OXIDIZED;
        } else if (tickOxidization == 800000) {
            this.oxidizationLevel = WeatheringCopper.WeatherState.WEATHERED;
        } else if (tickOxidization == 400000) {
            this.oxidizationLevel = WeatheringCopper.WeatherState.EXPOSED;
        } else if (this.tickOxidization < 400000) {
            this.oxidizationLevel = WeatheringCopper.WeatherState.UNAFFECTED;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_PRESSING_BUTTONS, false);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof AxeItem && this.oxidizationLevel != WeatheringCopper.WeatherState.UNAFFECTED) {
            this.degradeLevel();
            this.level().playSound(player, player.blockPosition(), SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.level().levelEvent(player, 3005, player.blockPosition(), 0);
            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void degradeLevel() {
        if (this.oxidizationLevel == WeatheringCopper.WeatherState.OXIDIZED) {
            this.oxidizationLevel = WeatheringCopper.WeatherState.WEATHERED;
        } else if (this.oxidizationLevel == WeatheringCopper.WeatherState.WEATHERED) {
            this.oxidizationLevel = WeatheringCopper.WeatherState.EXPOSED;
        } else if (this.oxidizationLevel == WeatheringCopper.WeatherState.EXPOSED) {
            this.oxidizationLevel = WeatheringCopper.WeatherState.UNAFFECTED;
        }
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        super.thunderHit(level, lightning);
        this.tickOxidization = 0;
        this.oxidizationLevel = WeatheringCopper.WeatherState.UNAFFECTED;
        ParticleUtils.spawnParticlesAlongAxis(this.getMotionDirection().getAxis(), level, this.blockPosition(), 0.125D,
                ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean damaged = super.hurt(source, amount);
        if (source.getEntity() instanceof LightningBolt) {
            return false;
        }
        return damaged;
    }

    @Override
    protected void doPush(Entity entity) {
        if (entity instanceof Player) {
            super.doPush(entity);
        }
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }
}
