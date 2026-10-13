package safro.oxidized.client.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WeatheringCopper;
import safro.oxidized.client.OxidizedClient;
import safro.oxidized.entity.CopperGolemEntity;

public class CopperGolemEntityRenderer extends MobRenderer<CopperGolemEntity, CopperGolemEntityModel> {
    private static final ResourceLocation UNAFFECTED = ResourceLocation.fromNamespaceAndPath("oxidized", "textures/entity/copper_golem.png");
    private static final ResourceLocation EXPOSED = ResourceLocation.fromNamespaceAndPath("oxidized", "textures/entity/exposed_copper_golem.png");
    private static final ResourceLocation WEATHERED = ResourceLocation.fromNamespaceAndPath("oxidized", "textures/entity/weathered_copper_golem.png");
    private static final ResourceLocation OXIDIZED = ResourceLocation.fromNamespaceAndPath("oxidized", "textures/entity/oxidized_copper_golem.png");

    public CopperGolemEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperGolemEntityModel(context.bakeLayer(OxidizedClient.COPPER_GOLEM_LAYER)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(CopperGolemEntity entity) {
        if (entity.getOxidizationLevel() == WeatheringCopper.WeatherState.EXPOSED) {
            return EXPOSED;
        } else if (entity.getOxidizationLevel() == WeatheringCopper.WeatherState.WEATHERED) {
            return WEATHERED;
        } else if (entity.getOxidizationLevel() == WeatheringCopper.WeatherState.OXIDIZED) {
            return OXIDIZED;
        }
        return UNAFFECTED;
    }
}
