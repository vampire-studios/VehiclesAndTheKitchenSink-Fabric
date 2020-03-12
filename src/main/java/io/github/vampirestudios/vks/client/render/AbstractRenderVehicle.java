package io.github.vampirestudios.vks.client.render;

import io.github.vampirestudios.vks.entity.VehicleEntity;
import io.github.vampirestudios.vks.utils.RenderUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractRenderVehicle<T extends VehicleEntity>
{
    /*public ISpecialModel getKeyHoleModel()
    {
        return SpecialModels.KEY_HOLE;
    }

    public ISpecialModel getTowBarModel()
    {
        return SpecialModels.TOW_BAR;
    }*/

    public abstract void render(T entity, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, float partialTicks, int light);

    public void applyPlayerModel(T entity, PlayerEntity player, PlayerEntityModel<AbstractClientPlayerEntity> model, float partialTicks) {}

    public void applyPlayerRender(T entity, PlayerEntity player, float partialTicks, MatrixStack matrixStack, VertexConsumer builder) {}

    protected boolean shouldRenderFuelLid()
    {
        return true;
    }

    protected void renderDamagedPart(VehicleEntity vehicle, ItemStack part, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, int light)
    {
        this.renderDamagedPart(vehicle, RenderUtil.getModel(part), matrixStack, renderTypeBuffer, light);
    }

    protected void renderDamagedPart(VehicleEntity vehicle, BakedModel model, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, int light)
    {
        this.renderDamagedPart(vehicle, model, matrixStack, renderTypeBuffer, false, light);
        this.renderDamagedPart(vehicle, model, matrixStack, renderTypeBuffer, true, light);
    }

    private void renderDamagedPart(VehicleEntity vehicle, BakedModel model, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, boolean renderDamage, int light) {
        if(renderDamage) {
            int stage = vehicle.getDestroyedStage();
            if(stage <= 0)
                return;
            RenderUtil.renderDamagedVehicleModel(model, ModelTransformation.Mode.NONE, false, matrixStack, stage, vehicle.getColor(), light, OverlayTexture.DEFAULT_UV);
        } else {
            RenderUtil.renderColoredModel(model, ModelTransformation.Mode.NONE, false, matrixStack, renderTypeBuffer, vehicle.getColor(), light, OverlayTexture.DEFAULT_UV);
        }
    }
}