package io.github.vampirestudios.vks.client.render.vehicle;

import io.github.vampirestudios.vks.client.render.AbstractRenderVehicle;
import io.github.vampirestudios.vks.client.render.SpecialModels;
import io.github.vampirestudios.vks.entity.vehicles.MiniBusEntity;
import io.github.vampirestudios.vks.utils.RenderUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Author: MrCrayfish
 */
public class RenderMiniBus extends AbstractRenderVehicle<MiniBusEntity> {

    /*@Override
    public ISpecialModel getTowBarModel()
    {
        return SpecialModels.BIG_TOW_BAR;
    }*/

    @Override
    public void render(MiniBusEntity entity, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, float partialTicks, int light) {
        this.renderDamagedPart(entity, SpecialModels.MINI_BUS_BODY.getModel(), matrixStack, renderTypeBuffer, light);

        matrixStack.push();

        // Positions the steering wheel in the correct position
        matrixStack.translate(-0.2825, 0.225, 1.0625);
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-67.5F));
        matrixStack.translate(0, -0.02, 0);
        matrixStack.scale(0.75F, 0.75F, 0.75F);

        // Rotates the steering wheel based on the wheel angle
        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 25F;
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(turnRotation));

        RenderUtil.renderColoredModel(SpecialModels.GO_KART_STEERING_WHEEL.getModel(), ModelTransformation.Mode.NONE, false, matrixStack, renderTypeBuffer, -1, light, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
    }

    @Override
    public void applyPlayerModel(MiniBusEntity entity, PlayerEntity player, PlayerEntityModel<AbstractClientPlayerEntity> model, float partialTicks) {
        if(entity.getControllingPassenger() == player) {
            float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
            float wheelAngleNormal = wheelAngle / 45F;
            float turnRotation = wheelAngleNormal * 6F;
            model.rightArm.pivotX = (float) Math.toRadians(-65F - turnRotation);
            model.rightArm.pivotY = (float) Math.toRadians(-7F);
            model.leftArm.pivotX = (float) Math.toRadians(-65F + turnRotation);
            model.leftArm.pivotY = (float) Math.toRadians(7F);
        }
    }
}